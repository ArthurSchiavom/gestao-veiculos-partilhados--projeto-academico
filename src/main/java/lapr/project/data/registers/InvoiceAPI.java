package lapr.project.data.registers;

import lapr.project.data.AutoCloseableManager;
import lapr.project.data.DataHandler;
import lapr.project.data.Emailer;
import lapr.project.model.Invoice;
import lapr.project.model.users.Client;
import lapr.project.utils.Utils;

import javax.mail.MessagingException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class InvoiceAPI {
    private final DataHandler dataHandler;

    public InvoiceAPI(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    public List<Invoice> fetchUnpaidInvoicesFor(String userEmail) throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        AutoCloseableManager autoCloseableManager = new AutoCloseableManager();
        try {
            PreparedStatement preparedStatement = dataHandler.prepareStatement("SELECT * FROM INVOICES WHERE USER_EMAIL = ? AND AMOUNT_LEFT_TO_PAY != 0");
            preparedStatement.setString(1, userEmail);
            autoCloseableManager.addAutoCloseable(preparedStatement);
            ResultSet resultSet = dataHandler.executeQuery(preparedStatement);
            autoCloseableManager.addAutoCloseable(resultSet);

            while (resultSet.next()) {
                invoices.add(new Invoice(resultSet.getString("user_email"),
                        resultSet.getDate("payment_start_date"),
                        resultSet.getDouble("amount_left_to_pay"),
                        resultSet.getDouble("usage_cost"),
                        resultSet.getDouble("penalisation_cost"),
                        resultSet.getInt("points_used"),
                        resultSet.getInt("previous_points"),
                        resultSet.getInt("earned_points")));
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to access the database when fetching invoices.", e.getSQLState(), e.getErrorCode());
        } finally {
            autoCloseableManager.closeAutoCloseables();
        }
        return invoices;
    }

    public Invoice fetchInvoice(String userEmail, Date startDate) throws SQLException {
        AutoCloseableManager autoCloseableManager = new AutoCloseableManager();
        try {
            PreparedStatement preparedStatement = dataHandler.prepareStatement("SELECT * FROM INVOICES WHERE USER_EMAIL = ? AND PAYMENT_START_DATE = ?");
            preparedStatement.setString(1, userEmail);
            preparedStatement.setDate(2, startDate);
            autoCloseableManager.addAutoCloseable(preparedStatement);
            ResultSet resultSet = dataHandler.executeQuery(preparedStatement);
            autoCloseableManager.addAutoCloseable(resultSet);

            if (!resultSet.next())
                return null;

            return new Invoice(resultSet.getString("user_email"),
                        resultSet.getDate("payment_start_date"),
                        resultSet.getDouble("amount_left_to_pay"),
                        resultSet.getDouble("usage_cost"),
                        resultSet.getDouble("penalisation_cost"),
                        resultSet.getInt("points_used"),
                        resultSet.getInt("previous_points"),
                        resultSet.getInt("earned_points"));
        } catch (SQLException e) {
            throw new SQLException("Failed to access the database when fetching invoices.", e.getSQLState(), e.getErrorCode());
        } finally {
            autoCloseableManager.closeAutoCloseables();
        }
    }

    public Invoice fetchLastInvoiceFor(String userEmail) throws SQLException {
        AutoCloseableManager autoCloseableManager = new AutoCloseableManager();
        try {
            PreparedStatement preparedStatement = dataHandler.prepareStatement("SELECT MAX(PAYMENT_START_DATE) FROM INVOICES WHERE USER_EMAIL = ?");
            autoCloseableManager.addAutoCloseable(preparedStatement);
            preparedStatement.setString(1, userEmail);
            ResultSet resultSet = dataHandler.executeQuery(preparedStatement);
            autoCloseableManager.addAutoCloseable(resultSet);
            if (!resultSet.next())
                return null;
            Date latestDate = resultSet.getDate(1);


            preparedStatement = dataHandler.prepareStatement("SELECT * FROM INVOICES WHERE USER_EMAIL = ? AND payment_start_date = ?");
            preparedStatement.setString(1, userEmail);
            preparedStatement.setDate(2, latestDate);
            autoCloseableManager.addAutoCloseable(preparedStatement);
            resultSet = dataHandler.executeQuery(preparedStatement);
            autoCloseableManager.addAutoCloseable(resultSet);

            if (!resultSet.next())
                return null;

            return new Invoice(resultSet.getString("user_email"),
                    resultSet.getDate("payment_start_date"),
                    resultSet.getDouble("amount_left_to_pay"),
                    resultSet.getDouble("usage_cost"),
                    resultSet.getDouble("penalisation_cost"),
                    resultSet.getInt("points_used"),
                    resultSet.getInt("previous_points"),
                    resultSet.getInt("earned_points"));
        } catch (SQLException e) {
            throw new SQLException("Failed to access the database when fetching invoices.", e.getSQLState(), e.getErrorCode());
        } finally {
            autoCloseableManager.closeAutoCloseables();
        }
    }

    /**
     * Issues an invoice to the user:
     * <br>discounts points from the invoice amount to pay
     * <br>updates invoice used points field
     * <br>updates client current amount of points
     * <br>emails the invoice to the client
     *
     * @param month invoice's month
     * @param userEmail user's email
     * @return the issued invoice
     * @throws SQLException if a database access error occurs
     * @throws MessagingException if a problem happens when sending an email
     */
    public Invoice issueInvoice(int month, String userEmail) throws SQLException, MessagingException {
        // using the invoice start date, which is in the month before the issue date
        if (month == 1)
            month = 12;
        else
            month--;
        AutoCloseableManager autoCloseableManager = new AutoCloseableManager();
        try {
            PreparedStatement preparedStatement = dataHandler.prepareStatement("SELECT * FROM INVOICES WHERE USER_EMAIL = ? AND extract(month from PAYMENT_START_DATE) = ? order by PAYMENT_START_DATE desc");
            autoCloseableManager.addAutoCloseable(preparedStatement);
            preparedStatement.setString(1, userEmail);
            preparedStatement.setInt(2, month);
            ResultSet resultSet = dataHandler.executeQuery(preparedStatement);
            autoCloseableManager.addAutoCloseable(resultSet);
            if (!resultSet.next())
                return null;

            Invoice invoiceToIssue = new Invoice(userEmail, resultSet.getDate("payment_start_date"),
                    resultSet.getDouble("amount_left_to_pay"),
                    resultSet.getDouble("usage_cost"),
                    resultSet.getDouble("penalisation_cost"),
                    resultSet.getInt("points_used"),
                    resultSet.getInt("previous_points"),
                    resultSet.getInt("earned_points"));

            UserAPI userAPI = Company.getInstance().getUserAPI();
            Client client = userAPI.fetchClientByEmail(userEmail);
            int pointsToDiscount = invoiceToIssue.calculatePointsToDiscount(invoiceToIssue.getPreviousPoints() + invoiceToIssue.getEarnedPoints());
            double newAmountToPay = invoiceToIssue.getTotalAmountToPay() - Utils.pointsToEuros(pointsToDiscount);
            userAPI.updateClientSubtractPointsNoCommit(client.getUsername(), pointsToDiscount);

            preparedStatement = dataHandler.prepareStatement("UPDATE INVOICES SET AMOUNT_LEFT_TO_PAY = ?, POINTS_USED = ? " +
                    "WHERE USER_EMAIL = ? AND PAYMENT_START_DATE = ?");
            autoCloseableManager.addAutoCloseable(preparedStatement);
            preparedStatement.setDouble(1, newAmountToPay);
            preparedStatement.setInt(2, pointsToDiscount);
            preparedStatement.setString(3, client.getEmail());
            preparedStatement.setDate(4, invoiceToIssue.getPaymentStartDate());
            dataHandler.executeUpdate(preparedStatement);

            dataHandler.commitTransaction();
            Invoice invoiceIssued = fetchInvoice(invoiceToIssue.getClientEmail(), invoiceToIssue.getPaymentStartDate());
            String invoiceDateString = invoiceToIssue.getPaymentStartDate().toLocalDate().plusMonths(1).format(DateTimeFormatter.ofPattern("MMM uuuu"));
                    Emailer.sendEmailCurrentThread(invoiceToIssue.getClientEmail(),
                            "Invoice " + invoiceDateString,
                            String.format("Date: %s" +
                                    "\nAmount to pay: %.2f" +
                                    "\nUsage cost: %.2f" +
                                    "\nPenalization cost: %.2f" +
                                    "\nPrevious points: %d" +
                                    "\nEarned points: %d" +
                                    "\nPoints used for this invoice: %d",
                                    invoiceDateString,
                                    invoiceIssued.getAmountLeftToPay(),
                                    invoiceIssued.getUsageCost(),
                                    invoiceIssued.getPenalizationCost(),
                                    invoiceIssued.getPreviousPoints(),
                                    invoiceIssued.getEarnedPoints(),
                                    invoiceIssued.getPointsUsed()));
            return invoiceIssued;
        } catch (SQLException e) {
            throw new SQLException("Failed to access the database", e.getSQLState(), e.getErrorCode());
        } finally {
            try {dataHandler.rollbackTransaction();} catch (Exception e) {}
            autoCloseableManager.closeAutoCloseables();
        }
    }
}
