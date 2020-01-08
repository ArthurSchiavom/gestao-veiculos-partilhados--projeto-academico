package lapr.project.model;

import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceTest {

    private static final Date LOCAL_DATE = new Date(123);
    private static final String EMAIL = "email@";
    private static final double AMOUNT_LEFT_TO_PAY = 15;
    private static final double USAGE_COST = 25;
    private static final double PENALIZATION_COST = 35;
    private static final int POINTS_USED = 10;
    private static final Invoice INVOICE = new Invoice(EMAIL, LOCAL_DATE, AMOUNT_LEFT_TO_PAY, USAGE_COST, PENALIZATION_COST, POINTS_USED, 10, 10);

    @Test
    void testConstructor() {
        testConstructorExceptionCase(null, LOCAL_DATE, AMOUNT_LEFT_TO_PAY, USAGE_COST, PENALIZATION_COST, POINTS_USED, 10, 10, IllegalArgumentException.class);
        testConstructorExceptionCase("", LOCAL_DATE, AMOUNT_LEFT_TO_PAY, USAGE_COST, PENALIZATION_COST, POINTS_USED, 10, 10, IllegalArgumentException.class);
        testConstructorExceptionCase(EMAIL, null, AMOUNT_LEFT_TO_PAY, USAGE_COST, PENALIZATION_COST, POINTS_USED, 10, 10, IllegalArgumentException.class);
    }

    private <T extends Exception> void testConstructorExceptionCase(String clientEmail, Date paymentStartDate, double amountLeftToPay, double usageCost, double penalizationCost, int pointsUsed, int previousPoints, int earnedPoints, Class<T> exceptionClass) {
        try {
            new Invoice(clientEmail, paymentStartDate, amountLeftToPay, usageCost, penalizationCost, pointsUsed, 10, 10);
            fail();
        } catch (Exception e) {
            if (e.getClass() != exceptionClass)
                fail();
        }
    }

    @Test
    void gettersTest() {
        assertEquals(LOCAL_DATE, INVOICE.getPaymentStartDate());
        assertEquals(EMAIL, INVOICE.getClientEmail());
        assertEquals(AMOUNT_LEFT_TO_PAY, INVOICE.getAmountLeftToPay());
        assertEquals(PENALIZATION_COST, INVOICE.getPenalizationCost());
        assertEquals(POINTS_USED, INVOICE.getPointsUsed());
        assertEquals(USAGE_COST, INVOICE.getUsageCost());
        assertEquals(USAGE_COST+PENALIZATION_COST, INVOICE.getTotalAmountToPay());
        assertEquals(10, INVOICE.getPreviousPoints());
        assertEquals(10, INVOICE.getEarnedPoints());
    }

    @Test
    void testEquals() {
        Date date1 = new Date(1);
        Date date2 = new Date(1);
        Date dateDif = new Date(2);
        Object inv1 = new Invoice("a", date1, 10, 10, 10, 10, 10, 10);
        assertEquals(inv1, inv1);

        Invoice inv2 = new Invoice("a", date2, 10, 10, 10, 10, 10, 10);
        assertEquals(inv1, inv2);

        inv2 = new Invoice("a", date2, 15, 15, 15, 15, 10, 10);
        assertEquals(inv1, inv2);

        inv2 = new Invoice("b", date2, 10, 10, 10, 10, 10, 10);
        assertNotEquals(inv1, inv2);

        inv2 = new Invoice("a", dateDif, 10, 10, 10, 10, 10, 10);
        assertNotEquals(inv1, inv2);

        inv2 = null;
        assertNotEquals(inv2, inv1);

        assertNotEquals(inv1, "str");
    }

    @Test
    void testHashCode() {
        Date date1 = new Date(1);
        Date date2 = new Date(1);
        Date dateDif = new Date(2);
        Object inv1 = new Invoice("a", date1, 10, 10, 10, 10, 10, 10);
        assertEquals(inv1.hashCode(), inv1.hashCode());

        Invoice inv2 = new Invoice("a", date2, 10, 10, 10, 10, 10, 10);
        assertEquals(inv1.hashCode(), inv2.hashCode());

        inv2 = new Invoice("a", date2, 15, 15, 15, 15, 10, 10);
        assertEquals(inv1.hashCode(), inv2.hashCode());

        inv2 = new Invoice("b", date2, 10, 10, 10, 10, 10, 10);
        assertNotEquals(inv1.hashCode(), inv2.hashCode());

        inv2 = new Invoice("a", dateDif, 10, 10, 10, 10, 10, 10);
        assertNotEquals(inv1.hashCode(), inv2.hashCode());
    }

    @Test
    void calculatePointsToDiscountTest() {
        Invoice inv1 = new Invoice("a", new Date(1), 10, 10, 10,
                10, 10, 10);
        assertEquals(200, inv1.calculatePointsToDiscount(200));
        assertEquals(200, inv1.calculatePointsToDiscount(500));
        assertEquals(100, inv1.calculatePointsToDiscount(100));
        assertEquals(100, inv1.calculatePointsToDiscount(109));
    }
}