/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model.Users;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author kevin
 */
public class ClientTest {
    
    public ClientTest() {
    }

    @Test
    public void testGetPoints() {
        System.out.println("getPoints");
         //Client(String email, String password, int creditCardSecret, int age, 
         // float height, float weight, char gender, String creditCardNumber, String creditCardExpiration)
        Client instance = new Client("1180852@isep.ipp.pt","password",1234,22,1.80F,60,'m',"123456","12/12/2019");
        int expResult = 0;
        int result = instance.getPoints();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetCreditCardSecret() {
        System.out.println("getCreditCardSecret");
        Client instance = new Client("1180852@isep.ipp.pt","password",1234,22,1.80F,60,'m',"123456","12/12/2019");
        int expResult = 1234;
        int result = instance.getCreditCardSecret();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetAge() {
        System.out.println("getAge");
        Client instance = new Client("1180852@isep.ipp.pt","password",1234,22,1.80F,60,'m',"123456","12/12/2019");
        int expResult = 22;
        int result = instance.getAge();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetHeight() {
        System.out.println("getHeight");
        Client instance = new Client("1180852@isep.ipp.pt","password",1234,22,1.80F,60,'m',"123456","12/12/2019");
        float expResult = 1.80F;
        float result = instance.getHeight();
        assertEquals(expResult, result, 0.0);
    }

    @Test
    public void testGetWeight() {
        System.out.println("getWeight");
       Client instance = new Client("1180852@isep.ipp.pt","password",1234,22,1.80F,60,'m',"123456","12/12/2019");
        float expResult = 60F;
        float result = instance.getWeight();
        assertEquals(expResult, result, 0.0);
    }

    @Test
    public void testGetGender() {
        System.out.println("getGender");
       Client instance = new Client("1180852@isep.ipp.pt","password",1234,22,1.80F,60,'m',"123456","12/12/2019");
        char expResult = 'm';
        char result = instance.getGender();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetCreditCardNumber() {
        System.out.println("getCreditCardNumber");
        Client instance = new Client("1180852@isep.ipp.pt","password",1234,22,1.80F,60,'m',"123456","12/12/2019");
        String expResult = "123456";
        String result = instance.getCreditCardNumber();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetCreditCardExpiration() {
        System.out.println("getCreditCardExpiration");
        Client instance = new Client("1180852@isep.ipp.pt","password",1234,22,1.80F,60,'m',"123456","12/12/2019");
        String expResult = "12/12/2019";
        String result = instance.getCreditCardExpiration();
        assertEquals(expResult, result);
    }
    
}
