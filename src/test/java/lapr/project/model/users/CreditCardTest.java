package lapr.project.model.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lapr.project.model.users.CreditCard;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CreditCardTest {

    private CreditCard cc;
    private LocalDate expirationDate;

    @BeforeEach
    void beforeEach() {
        expirationDate = LocalDate.of(2020,12,31);
        cc = new CreditCard("00000000000000", expirationDate, 000);
    }

    @Test
    void testGetCcv() {
        assertEquals(000, cc.getCcv());
    }

    @Test
    void testGetCcNumber() {
        assertEquals("00000000000000", cc.getCcNumber());
    }

    @Test
    void testGetCcExpiration() {
        assertEquals(LocalDate.of(2020,12,31), cc.getCcExpiration());
    }

    @Test
    void testEquals() {
        Object cc1 = this.cc;
        assertEquals(cc1, cc1);

        CreditCard cc2 = new CreditCard("00000000000000", expirationDate, 000);
        assertEquals(cc1, cc2);

        cc1 = new CreditCard("11111111111111", expirationDate, 000);
        assertNotEquals(cc1, cc2);

        cc1 = null;
        assertNotEquals(cc2, cc1);

        cc1 = "";
        assertNotEquals(cc1, cc2);
    }

    @Test
    void testHashCode() {
        CreditCard cc1 = cc;
        CreditCard cc2 = new CreditCard("00000000000000", expirationDate, 000);
        int expResult = cc2.hashCode();
        assertEquals(expResult, cc1.hashCode());

        cc1 = new CreditCard("11111111111111", expirationDate, 000);
        assertNotEquals(expResult, cc1.hashCode());
    }
}