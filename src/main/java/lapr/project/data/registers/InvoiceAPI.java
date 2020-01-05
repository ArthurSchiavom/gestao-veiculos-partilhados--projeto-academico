package lapr.project.data.registers;

import lapr.project.data.AutoCloseableManager;
import lapr.project.data.DataHandler;
import lapr.project.model.Invoice;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                        resultSet.getDate("payment_start_date").toLocalDate(),
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
}
