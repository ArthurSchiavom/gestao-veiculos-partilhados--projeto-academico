/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.model.Vehicles;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author kevin
 */
public class BicycleTest {
    
    @Test
    public void testGetSize() {
        System.out.println("getSize");
        Bicycle instance = new Bicycle(1,"IVO",40,160,0,false,VehicleType.BICYCLE,15);
        int expResult = 15;
        int result = instance.getSize();
        assertNotNull(result);
        assertEquals(expResult, result);
    }
    
}
