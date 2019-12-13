/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Pedro Ivo
 */
public class Invoice {

    private int clientId;
    private LocalDate paymentStartDate;
    private double amountLeftToPay;
    private double usageCost;
    private double penalizationCost;
    private Set<Receipt> receipts;

    /**
     * Instantiates an invoice without receipts
     *
     * @param clientId the id of the client
     * @param paymentStartDate the start date for the payment
     * @param usageCost the normal cost of the vehicle rent
     * @param penalizationCost the penalization cost (incase it's not locked
     * properly, etc)
     */
    public Invoice(int clientId, LocalDate paymentStartDate, double usageCost, double penalizationCost) {
        if(clientId<0) {
            throw new IllegalArgumentException("clientId can't be less than 0");
        }
        this.clientId = clientId;
        this.paymentStartDate = paymentStartDate;
        this.penalizationCost = penalizationCost;
        this.usageCost = usageCost;
        this.amountLeftToPay = usageCost + penalizationCost;
        this.receipts = new HashSet<>();
    }

    /**
     * Returns the client ID
     *
     * @return the client ID
     */
    public int getClientId() {
        return clientId;
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
    public double getTotalAmount() {
        return (usageCost + penalizationCost);
    }

    /**
     * Returns a copy of all the receipts associated with this invoice
     *
     * @return a copy of all the receipts associated with this invoice
     */
    public Set<Receipt> getReceipts() {
        if(receipts.isEmpty()) {
            return null;
        }
        return new HashSet<>(receipts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invoice invoice = (Invoice) o;
        return clientId == invoice.clientId &&
                paymentStartDate.equals(invoice.paymentStartDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, paymentStartDate);
    }
}
