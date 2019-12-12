/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model.Users;

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
        instance = new Client("1180852@isep.ipp.pt","password", 22, 180, 60f, 'm',
                new CreditCard("12341234123412", "12/20", 321));
    }

    @Test
    void testGetPoints() {
        System.out.println("getPoints");
        int expResult = 0;
        int result = instance.getPoints();
        assertEquals(expResult, result);
    }

    @Test
    void testGetCreditCardSecret() {
        System.out.println("getCreditCardSecret");
        Client instance = new Client("1180852@isep.ipp.pt","password", 22, 180, 60f, 'm',
                new CreditCard("12341234123412", "12/20", 321));
        int expResult = 321;
        int result = instance.getCc().getCcv();
        assertEquals(expResult, result);
    }

    @Test
    void testGetAge() {
        System.out.println("getAge");
        Client instance = new Client("1180852@isep.ipp.pt","password", 22, 180, 60f, 'm',
                new CreditCard("12341234123412", "12/20", 321));
        int expResult = 22;
        int result = instance.getAge();
        assertEquals(expResult, result);
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
        String result = instance.getCc().getCcNumber();
        assertEquals(expResult, result);
    }

    @Test
    void testGetCreditCardExpiration() {
        System.out.println("getCreditCardExpiration");
        String expResult = "12/20";
        String result = instance.getCc().getCcExpiration();
        assertEquals(expResult, result);
    }
}
