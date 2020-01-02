/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author kevin
 */
public class ClientTest {

    private Client instance;

    @BeforeEach
    void beforeEach() {
        instance = new Client("1180852@isep.ipp.pt","username","password", 22, 180, 60, 'm',2.3F,
                true, new CreditCard("12341234123412"));
    }

    @Test
    void testGetPoints() {
        System.out.println("getPoints");
        int expResult = 22;
        int result = instance.getPoints();
        assertEquals(expResult, result);

        Client client = new Client("email@email.com", "username", "password", 50, 150, 60, 'M',
                10.4f, true, new CreditCard("12341234123412"));
        assertEquals(50, client.getPoints());
    }

    @Test
    void testGetCyclingAvgSpeed() {
        assertEquals(2.3F, instance.getCyclingAverageSpeed());
    }

    @Test
    void testCreditCard() {
        assertEquals(instance.getCreditCard(), new CreditCard("12341234123412"));
    }

    @Test
    void testToString() {
        assertEquals("Client{points=22, height=180, weight=60, gender=m, cyclingAverageSpeed=2.3, cc=12341234123412}", instance.toString());
    }

    @Test
    void testGetHeight() {
        float expResult = 180;
        float result = instance.getHeight();
        assertEquals(expResult, result, 0.0);
    }

    @Test
    void testGetWeight() {
        float expResult = 60F;
        float result = instance.getWeight();
        assertEquals(expResult, result, 0.0);
    }

    @Test
    void testGetGender() {
        char expResult = 'm';
        char result = instance.getGender();
        assertEquals(expResult, result);
    }

    @Test
    void testGetCreditCardNumber() {
        String expResult = "12341234123412";
        String result = instance.getCreditCard().getCcNumber();
        assertEquals(expResult, result);
    }

    @Test
    void testIsRiding() {
        boolean result = instance.isRiding();
        assertEquals(true, result);
    }

}
