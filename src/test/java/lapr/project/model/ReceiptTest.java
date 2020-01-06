package lapr.project.model;

import lapr.project.model.point.of.interest.PointOfInterest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptTest {

    private LocalDate cal;

    @BeforeEach
    private void before() {
        cal = LocalDate.now();
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
        LocalDate expResult = cal;
        assertEquals(expResult, receipt.getPaymentDate());
    }

    /**
     * constructor
     */
    @Test
    public void constructor() {
        try{
            Receipt receipt = new Receipt(null, 0, 5);
        }catch(IllegalArgumentException e) {
            if (e.getClass()!= IllegalArgumentException.class)
                fail();
        }
    }
    @Test
    void testEquals() {
        Object receipt1 = new Receipt(cal, 0, 5);
        assertEquals(receipt1, receipt1);

        Receipt receipt2 = new Receipt(cal, 0, 5);
        assertEquals(receipt1, receipt2);

        LocalDate cal1 = LocalDate.of(2019, 1, 1);
        receipt1 = new Receipt(cal1, 0, 5);
        assertNotEquals(receipt1, receipt2);

        receipt1 = null;
        assertNotEquals(receipt2, receipt1);

        receipt1 = "";
        assertNotEquals(receipt1, receipt2);

        Path test = new Path(new PointOfInterest("desc1",new Coordinates(0.0, 0.0, 0)), new PointOfInterest("desc2",new Coordinates(1.0, 0.0, 0)), 5.3, 0, 3.0);

        assertNotEquals(receipt1, test);
    }

    @Test
    void testHashCode() {
        Receipt receipt1 = new Receipt(cal, 0,5);
        Receipt receipt2 = new Receipt(cal, 0,5);
        int expResult = receipt2.hashCode();
        assertEquals(expResult, receipt1.hashCode());

        LocalDate cal1 = LocalDate.of(2019,1,1);
        receipt1 = new Receipt(cal1, 5,5);
        assertNotEquals(expResult, receipt1.hashCode());
    }
}