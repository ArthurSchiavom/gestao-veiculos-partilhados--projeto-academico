package lapr.project.model.Users;

import java.util.Objects;

public class CreditCard {
    private String ccNumber, ccExpiration;
    private int ccv;

    /**
     * Instantiates a credit card
     *
     * @param ccNumber the number on the credit card
     * @param ccExpiration the expiration date of the credit card, must follow the "MM/YY" format"
     * @param ccv the security digits
     */
    public CreditCard(String ccNumber, String ccExpiration, int ccv) {
        this.ccv = ccv;
        this.ccNumber = ccNumber;
        this.ccExpiration = ccExpiration;
    }

    /**
     * Returns the credit card security number
     * @return the credit card security number
     */
    public int getCcv() {
        return ccv;
    }

    /**
     * Returns the credit card number
     * @return the credit card number
     */
    public String getCcNumber() {
        return ccNumber;
    }

    /**
     * Returns the credit card expiration date in the format "MM/YY"
     * @return the credit card expiration date in the format "MM/YY"
     */
    public String getCcExpiration() {
        return ccExpiration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreditCard that = (CreditCard) o;
        return ccNumber.equals(that.ccNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ccNumber);
    }
}
