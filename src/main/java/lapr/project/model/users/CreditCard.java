package lapr.project.model.users;

import java.util.Objects;

public class CreditCard {
    private String ccNumber;

    /**
     * Instantiates a credit card
     *
     * @param ccNumber the number on the credit card
     */
    public CreditCard(String ccNumber) {
        this.ccNumber = ccNumber;
    }

    /**
     * Returns the credit card number
     * @return the credit card number
     */
    public String getCcNumber() {
        return ccNumber;
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

    @Override
    public String toString() {
        return "ccNumber='" + ccNumber+"'";
    }
}
