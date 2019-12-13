package lapr.project.model.users;

import java.time.LocalDate;
import java.util.Objects;

public class CreditCard {
    private String ccNumber;
    private LocalDate ccExpiration;
    private int ccv;

    /**
     * Instantiates a credit card
     *
     * @param ccNumber the number on the credit card
     * @param ccExpiration the expiration date of the credit card, must follow the "MM/YY" format"
     * @param ccVerification the security verification
     */
    public CreditCard(String ccNumber, LocalDate ccExpiration, int ccVerification) {
        this.ccv = ccVerification;
        this.ccNumber = ccNumber;
        this.ccExpiration = LocalDate.of(ccExpiration.getYear(), ccExpiration.getMonth(), 31);
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
    public LocalDate getCcExpiration() {
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
