package lapr.project.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * CalculatorExample Class Unit Testing.
 *
 * @author Nuno Bettencourt <nmb@isep.ipp.pt> on 24/05/16.
 */
public class CalculatorExampleTest {
    /**
     * Ensure second operand can assume a negative value.
     */
    @Test
    public void ensureSecondNegativeOperandWorks() {
        int expected = 5;
        int firstOperand = 10;
        int secondOperand = -5;
        CalculatorExample calculator = new CalculatorExample();
        int result = calculator.sum(firstOperand, secondOperand);
        assertEquals(expected, result);
    }

}
