package lapr.project.model.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CreditCardTest {

    private CreditCard cc;

    @BeforeEach
    void beforeEach() {
        cc = new CreditCard("00000000000000");
    }

    @Test
    void testGetCcNumber() {
        assertEquals("00000000000000", cc.getCcNumber());
    }

    @Test
    void testEquals() {
        Object cc1 = this.cc;
        assertEquals(cc1, cc1);

        CreditCard cc2 = new CreditCard("00000000000000");
        assertEquals(cc1, cc2);

        cc1 = new CreditCard("11111111111111");
        assertNotEquals(cc1, cc2);

        cc1 = null;
        assertNotEquals(cc2, cc1);

        cc1 = "";
        assertNotEquals(cc1, cc2);
    }

    @Test
    void testToString() {
        System.out.println(cc.toString());
        assertEquals("00000000000000",cc.toString(),"toString");
    }

    @Test
    void testHashCode() {
        CreditCard cc1 = cc;
        CreditCard cc2 = new CreditCard("00000000000000");
        int expResult = cc2.hashCode();
        assertEquals(expResult, cc1.hashCode());

        cc1 = new CreditCard("11111111111111");
        assertNotEquals(expResult, cc1.hashCode());
    }
}