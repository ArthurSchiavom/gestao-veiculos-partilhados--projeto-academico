package lapr.project.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Calendar;
import static org.junit.jupiter.api.Assertions.*;

class ReceiptTest {

    private Calendar cal;

    @BeforeEach
    private void before() {
        cal = Calendar.getInstance();
    }

    @Test
    void testGetPointsUsed() {
        Receipt receipt = new Receipt(cal, 0, 5);
        int expResult = 0;
        assertEquals(expResult, receipt.getPointsUsed());
    }

    @Test
    void testGetAmountPaidCash() {
        Receipt receipt = new Receipt(cal, 0,5);
        double expResult = 5;
        assertEquals(expResult, receipt.getAmountPaidCash(), 0);

        receipt = new Receipt(cal, 5,0);
        assertEquals(0, receipt.getAmountPaidCash());

        try {
            receipt = new Receipt(cal, 0, -5);
            fail("receipt did not throw an illegal argument exception");
        } catch (IllegalArgumentException ex) {
            assertTrue(true, "receipt threw an illegal argument exception");
        }
    }

    @Test
    void testGetPaymentDate() {
        Receipt receipt = new Receipt(cal, 0,5);
        Calendar expResult = cal;
        assertEquals(expResult, receipt.getPaymentDate());
    }

    @Test
    void testEquals() {
        Object receipt1 = new Receipt(cal, 0, 5);
        assertEquals(receipt1, receipt1);

        Receipt receipt2 = new Receipt(cal, 0, 5);
        assertEquals(receipt1, receipt2);

        Calendar cal1 = Calendar.getInstance();
        cal1.set(2019, Calendar.JANUARY,0);
        receipt1 = new Receipt(cal1, 0, 5);
        assertNotEquals(receipt1, receipt2);

        receipt1 = null;
        assertNotEquals(receipt2, receipt1);

        receipt1 = "";
        assertNotEquals(receipt1, receipt2);
    }

    @Test
    void testHashCode() {
        Receipt receipt1 = new Receipt(cal, 0,5);
        Receipt receipt2 = new Receipt(cal, 0,5);
        int expResult = receipt2.hashCode();
        assertEquals(expResult, receipt1.hashCode());

        Calendar cal1 = cal;
        cal1.set(2019,Calendar.JANUARY,0);
        receipt1 = new Receipt(cal1, 5,5);
        assertNotEquals(expResult, receipt1.hashCode());
    }
}