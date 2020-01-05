package lapr.project.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceTest {

    private static final LocalDate LOCAL_DATE = LocalDate.now();
    private static final String EMAIL = "email@";
    private static final double AMOUNT_LEFT_TO_PAY = 15;
    private static final double USAGE_COST = 25;
    private static final double PENALIZATION_COST = 35;
    private static final int POINTS_USED = 10;
    private static final Invoice INVOICE = new Invoice(EMAIL, LOCAL_DATE, AMOUNT_LEFT_TO_PAY, USAGE_COST, PENALIZATION_COST, POINTS_USED);

    @Test
    void testConstructor() {
        testConstructorExceptionCase(null, LOCAL_DATE, AMOUNT_LEFT_TO_PAY, USAGE_COST, PENALIZATION_COST, POINTS_USED, IllegalArgumentException.class);
        testConstructorExceptionCase("", LOCAL_DATE, AMOUNT_LEFT_TO_PAY, USAGE_COST, PENALIZATION_COST, POINTS_USED, IllegalArgumentException.class);
        testConstructorExceptionCase(EMAIL, null, AMOUNT_LEFT_TO_PAY, USAGE_COST, PENALIZATION_COST, POINTS_USED, IllegalArgumentException.class);
    }

    private <T extends Exception> void testConstructorExceptionCase(String clientEmail, LocalDate paymentStartDate, double amountLeftToPay, double usageCost, double penalizationCost, int pointsUsed, Class<T> exceptionClass) {
        try {
            new Invoice(clientEmail, paymentStartDate, amountLeftToPay, usageCost, penalizationCost, pointsUsed);
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
    }

    @Test
    void testEquals() {
        LocalDate now1 = LocalDate.now();
        LocalDate now2 = LocalDate.now();
        LocalDate yesterday = now1.minusDays(1);
        Object inv1 = new Invoice("a", now1, 10, 10, 10, 10);
        assertEquals(inv1, inv1);

        Invoice inv2 = new Invoice("a", now2, 10, 10, 10, 10);
        assertEquals(inv1, inv2);

        inv2 = new Invoice("a", now2, 15, 15, 15, 15);
        assertEquals(inv1, inv2);

        inv2 = new Invoice("b", now2, 10, 10, 10, 10);
        assertNotEquals(inv1, inv2);

        inv2 = new Invoice("a", yesterday, 10, 10, 10, 10);
        assertNotEquals(inv1, inv2);

        inv2 = null;
        assertNotEquals(inv2, inv1);

        assertNotEquals(inv1, "str");
    }

    @Test
    void testHashCode() {
        LocalDate now1 = LocalDate.now();
        LocalDate now2 = LocalDate.now();
        LocalDate yesterday = now1.minusDays(1);
        Object inv1 = new Invoice("a", now1, 10, 10, 10, 10);
        assertEquals(inv1.hashCode(), inv1.hashCode());

        Invoice inv2 = new Invoice("a", now2, 10, 10, 10, 10);
        assertEquals(inv1.hashCode(), inv2.hashCode());

        inv2 = new Invoice("a", now2, 15, 15, 15, 15);
        assertEquals(inv1.hashCode(), inv2.hashCode());

        inv2 = new Invoice("b", now2, 10, 10, 10, 10);
        assertNotEquals(inv1.hashCode(), inv2.hashCode());

        inv2 = new Invoice("a", yesterday, 10, 10, 10, 10);
        assertNotEquals(inv1.hashCode(), inv2.hashCode());
    }
}