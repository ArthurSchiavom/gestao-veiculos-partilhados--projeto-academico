/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import java.time.LocalDate;
import java.util.*;

/**
 *
 * @author Pedro Ivo
 */
public class Invoice {

    private final String clientEmail;
    private final LocalDate paymentStartDate;
    private final double amountLeftToPay;
    private final double usageCost;
    private final double penalizationCost;
    private final int pointsUsed;

    /**
     * Instantiates an invoice without receipts
     *  @param clientEmail client's email
     * @param paymentStartDate the start date for the payment
     * @param usageCost the normal cost of the vehicle rent
     * @param penalizationCost the penalization cost (incase it's not locked
     * @param pointsUsed
     */
    public Invoice(String clientEmail, LocalDate paymentStartDate, double amountLeftToPay, double usageCost, double penalizationCost, int pointsUsed) {
        if (paymentStartDate == null || clientEmail == null || clientEmail.isEmpty())
            throw new IllegalArgumentException("Null elements are not allowed");
        this.pointsUsed = pointsUsed;
        this.clientEmail = clientEmail;
        this.paymentStartDate = paymentStartDate;
        this.penalizationCost = penalizationCost;
        this.usageCost = usageCost;
        this.amountLeftToPay = amountLeftToPay;
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
    public LocalDate getPaymentStartDate() {
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
