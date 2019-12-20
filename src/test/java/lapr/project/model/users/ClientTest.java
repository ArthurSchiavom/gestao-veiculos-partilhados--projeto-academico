/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

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
                new CreditCard("12341234123412"));
    }

    @Test
    void testGetPoints() {
        System.out.println("getPoints");
        int expResult = 0;
        int result = instance.getPoints();
        assertEquals(expResult, result);

        Client client = new Client("email@email.com", "username", "password", 20, 150, 60, 'M',
                10.4f, new CreditCard("12341234123412"));
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
        System.out.println(instance.toString());
        assertEquals("Client{points=0, age=22, height=180, weight=60, gender=m, cyclingAverageSpeed=2.3, cc=ccNumber='12341234123412'}", instance.toString());
    }

    @Test
    void testGetHeight() {
        System.out.println("getHeight");
        float expResult = 180;
        float result = instance.getHeight();
        assertEquals(expResult, result, 0.0);
    }

    @Test
    void testGetWeight() {
        System.out.println("getWeight");
        float expResult = 60F;
        float result = instance.getWeight();
        assertEquals(expResult, result, 0.0);
    }

    @Test
    void testGetGender() {
        System.out.println("getGender");
        char expResult = 'm';
        char result = instance.getGender();
        assertEquals(expResult, result);
    }

    @Test
    void testGetCreditCardNumber() {
        System.out.println("getCreditCardNumber");
        String expResult = "12341234123412";
        String result = instance.getCreditCard().getCcNumber();
        assertEquals(expResult, result);
    }

}
