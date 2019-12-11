package lapr.project.model;

import java.util.Calendar;
import java.util.Objects;

/**
 *
 * @author Pedro Ivo
 */
public class Receipt {
    private int pointsUsed;
    private double amountPaidCash;
    private Calendar paymentDate;

    /**
     * Instantiates a receipt for the invoices
     * @param paymentDate the date that the user made a payment
     * @param pointsUsed the amount of points used to make the payment
     * @param amountPaidCash the amount paid in cash
     */
    public Receipt(Calendar paymentDate, int pointsUsed, double amountPaidCash) {
        //TODO: Add checks and tests for points used
        if(amountPaidCash < 0) {
            throw new IllegalArgumentException("Can't pay negative amount of money");
        }
        this.paymentDate = paymentDate;
        this.pointsUsed = pointsUsed;
        this.amountPaidCash = amountPaidCash;
    }

    /**
     * Returns the amount of points used to make a payment
     * 
     * @return the amount of points used to make a payment
     */
    public int getPointsUsed() {
        return pointsUsed;
    }

    /**
     * Returns the amount paid in cash
     * 
     * @return the amount paid in cash
     */
    public double getAmountPaidCash() {
        return amountPaidCash;
    }
    
    /**
     * Returns the date the payment was made
     * 
     * @return the date the payment was made
     */
    public Calendar getPaymentDate() {
        return paymentDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Receipt receipt = (Receipt) o;
        return paymentDate.equals(receipt.paymentDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentDate);
    }
}
