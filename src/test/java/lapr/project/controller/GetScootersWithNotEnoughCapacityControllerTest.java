/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import lapr.project.model.vehicles.ElectricScooter;
import lapr.project.model.vehicles.ElectricScooterType;
import lapr.project.model.vehicles.ElectricScooterTypeTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 *
 */
public class GetScootersWithNotEnoughCapacityControllerTest {

    private static DataHandler dataHandler;
    private static Company company;
    private static GetScootersWithNotEnoughCapacityController controller;

    @BeforeEach
    void prepare() {
        dataHandler = mock(DataHandler.class);
        Company.reset();
        company = Company.createCompany(dataHandler);
        controller = new GetScootersWithNotEnoughCapacityController(company);
    }

    @Test
    public void testWriteOutputFile() throws Exception {
        System.out.println("writeOutputFile");
        int km = 23;
        String filePath = "testFiles/temp/GetSootersWithNotEnoughCapacityControllerTest.writeOutputFileTest.output";

        PreparedStatement preparedStatement1 = mock(PreparedStatement.class);
        ResultSet resultSet1 = mock(ResultSet.class);

        when(dataHandler.prepareStatement(anyString())).thenReturn(preparedStatement1);
        when(dataHandler.executeQuery(preparedStatement1)).thenReturn(resultSet1);
        when(resultSet1.next()).thenReturn(true).thenReturn(false);
        when(resultSet1.getString("description")).thenReturn("desc");
        when(resultSet1.getInt("unique_number")).thenReturn(1);
        when(resultSet1.getString("electric_scooter_type_name")).thenReturn("urban");
        when(resultSet1.getInt("actual_battery_capacity")).thenReturn(35);
        when(resultSet1.getFloat("max_battery_capacity")).thenReturn(1.0f);
        when(resultSet1.getInt("engine_power")).thenReturn(250);
        when(resultSet1.getFloat("frontal_area")).thenReturn(0.3f);
        when(resultSet1.getBoolean("available")).thenReturn(true);
        when(resultSet1.getInt("weight")).thenReturn(30);
        when(resultSet1.getFloat("aerodynamic_coefficient")).thenReturn(1.2f);
        try {
            controller.writeOutputFile(km, filePath);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        
         List<String> expectedResult = new ArrayList<>();
          expectedResult.add("Scooters with not enough autonomy to make " + km + "km:");
          expectedResult.add("scooter " + resultSet1.getInt("unique_number"));
          verifyOutputFile(expectedResult, filePath);
    }

    void verifyOutputFile(List<String> expectedContent, String filePath) {
        try (Scanner scanner = new Scanner(new FileReader(filePath))) {
            int count = 0;
            while (scanner.hasNext()) {
                assertEquals(expectedContent.get(count), scanner.nextLine());
                count++;
            }
            if (count != expectedContent.size()) {
                fail();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            fail();
        }

    }
}
