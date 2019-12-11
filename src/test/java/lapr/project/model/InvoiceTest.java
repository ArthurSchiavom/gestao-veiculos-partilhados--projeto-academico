package lapr.project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceTest {

    private Calendar cal;

    @BeforeEach
    private void beforeEach() {
        cal = Calendar.getInstance();
    }

    @Test
    void testGetClientId() {
        Invoice invoice = new Invoice(0, cal, 0, 0);
        assertEquals(0, invoice.getClientId());

        try {
            invoice = new Invoice(-5, cal, 0, 0);
            fail("did not throw illegal argument exception");
        } catch (IllegalArgumentException ex) {
        }
    }

    @Test
    void testGetPaymentStartDate() {
        Invoice invoice = new Invoice(0, cal, 0, 0);
        assertEquals(cal, invoice.getPaymentStartDate());
    }

    @Test
    void testGetAmountLeftToPay() {
        Invoice invoice = new Invoice(0, cal, 5, 3);
        assertEquals(8, invoice.getAmountLeftToPay());
    }

    @Test
    void testGetUsageCost() {
        Invoice invoice = new Invoice(0, cal, 0, 0);
        assertEquals(0, invoice.getUsageCost());
    }

    @Test
    void testGetPenalizationCost() {
        Invoice invoice = new Invoice(0, cal, 0, 0);
        assertEquals(0, invoice.getPenalizationCost());
    }

    @Test
    void testGetTotalAmount() {
        Invoice invoice = new Invoice(0, cal, 5, 5);
        assertEquals(10, invoice.getTotalAmount());
    }

    @Test
    void testGetReceipts() {
        Invoice invoice = new Invoice(0, cal, 0, 0);
        assertNull(invoice.getReceipts());
    }

    @Test
    void testEquals() {
        Object inv1 = new Invoice(0, cal, 0, 5);
        assertEquals(inv1, inv1);

        Invoice inv2 = new Invoice(0, cal, 0, 5);
        assertEquals(inv1, inv2);

        Calendar cal1 = Calendar.getInstance();
        cal1.set(2019, Calendar.JANUARY, 0);
        inv1 = new Invoice(0, cal1, 0, 5);
        assertNotEquals(inv1, inv2);

        inv1 = null;
        assertNotEquals(inv2, inv1);

        inv1 = "";
        assertNotEquals(inv1, inv2);
    }

    @Test
    void testHashCode() {
        Invoice inv1 = new Invoice(0, cal, 0, 5);
        Invoice inv2 = new Invoice(0, cal, 0, 5);
        int expResult = inv2.hashCode();
        assertEquals(expResult, inv1.hashCode());

        Calendar cal1 = cal;
        cal1.set(2019, Calendar.JANUARY, 0);
        inv1 = new Invoice(0, cal1, 5, 5);
        assertNotEquals(expResult, inv1.hashCode());
    }
}