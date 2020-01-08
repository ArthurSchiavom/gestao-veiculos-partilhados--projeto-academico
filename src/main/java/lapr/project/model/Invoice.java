/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import lapr.project.utils.Utils;

import java.sql.Date;
import java.util.Objects;

/**
 * Class that represents an invoice
 */
public class Invoice {
    public static final int HOW_MANY_POINTS_IS_1_EURO = 10;

    private final String clientEmail;
    private final Date paymentStartDate;
    private final double amountLeftToPay;
    private final double usageCost;
    private final double penalizationCost;
    private final int pointsUsed;
    private final int previousPoints;
    private final int earnedPoints;

    /**
     * Instantiates an invoice without receipts
     * @param clientEmail client's email
     * @param paymentStartDate the start date for the payment
     * @param usageCost the normal cost of the vehicle rent
     * @param penalizationCost the penalization cost (incase it's not locked
     * @param pointsUsed
     * @param previousPoints
     * @param earnedPoints
     */
    public Invoice(String clientEmail, Date paymentStartDate, double amountLeftToPay, double usageCost, double penalizationCost, int pointsUsed, int previousPoints, int earnedPoints) {
        this.previousPoints = previousPoints;
        this.earnedPoints = earnedPoints;
        if (paymentStartDate == null || clientEmail == null || clientEmail.isEmpty())
            throw new IllegalArgumentException("Null elements are not allowed");
        this.pointsUsed = pointsUsed;
        this.clientEmail = clientEmail;
        this.paymentStartDate = paymentStartDate;
        this.penalizationCost = penalizationCost;
        this.usageCost = usageCost;
        this.amountLeftToPay = amountLeftToPay;
    }

    public int getPreviousPoints() {
        return previousPoints;
    }

    public int getEarnedPoints() {
        return earnedPoints;
    }

    /**
     * Returns the client ID
     *
     * @return the client ID
     */
    public String getClientEmail() {
        return clientEmail;
    }

    /**
     * Returns the payment start date
     *
     * @return the payment start date
     */
    public Date getPaymentStartDate() {
        return paymentStartDate;
    }

    /**
     * Returns the amount left to pay
     *
     * @return the amount left to pay
     */
    public double getAmountLeftToPay() {
        return amountLeftToPay;
    }

    /**
     * Returns the usage cost
     *
     * @return the usage cost
     */
    public double getUsageCost() {
        return usageCost;
    }

    /**
     * Returns the penalization cost
     *
     * @return the penalization cost
     */
    public double getPenalizationCost() {
        return penalizationCost;
    }

    /**
     * Returns the total cost of the invoice
     *
     * @return the total cost of the invoice
     */
    public double getTotalAmountToPay() {
        return (usageCost + penalizationCost);
    }

    public int getPointsUsed() {
        return pointsUsed;
    }

    /**
     * Calculates the number of points to discount on this invoice.
     *
     * @param maxPointsToUse the maximum number of points to discount
     * @return the number of points to discount on this invoice. 0 if maxPointsToUse is negative
     */
    public int calculatePointsToDiscount(int maxPointsToUse) {
        if (maxPointsToUse < 0)
            return 0;
        maxPointsToUse = (maxPointsToUse / 10) * 10;
        double total = getTotalAmountToPay();
        // The line below must be updated if the points get updated
        int amountToPayInPoints = (Utils.eurosToPoints(total) / 10) * 10;
        if (maxPointsToUse <= amountToPayInPoints)
            return maxPointsToUse;
        else
            return amountToPayInPoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invoice invoice = (Invoice) o;
        return clientEmail == invoice.clientEmail &&
                paymentStartDate.equals(invoice.paymentStartDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientEmail, paymentStartDate);
    }

}
