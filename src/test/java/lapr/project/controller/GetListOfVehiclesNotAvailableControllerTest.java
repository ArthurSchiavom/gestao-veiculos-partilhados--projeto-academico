package lapr.project.controller;

import lapr.project.data.DataHandler;
import lapr.project.data.registers.Company;
import lapr.project.model.Trip;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class GetListOfVehiclesNotAvailableControllerTest {
    private static DataHandler dh;
    private static PreparedStatement preparedStatement;
    private static Company company;
    private static GetListOfVehiclesNotAvailableController controller;

    @BeforeAll
    static void prepare() {
        dh = mock(DataHandler.class);
        preparedStatement = mock(PreparedStatement.class);
        Company.reset();
        company = Company.createCompany(dh);
        controller = new GetListOfVehiclesNotAvailableController(company);

        try {
            // When prepareCall is called, whatever the String is, return callableStatement, which is our other mock object
            when(dh.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Test
    void getListOfVehiclesNotAvailable () {
        try {
            LocalDateTime startTime = LocalDateTime.of(2019,10,01,10,50);
            LocalDateTime endTime = LocalDateTime.of(2019,12,3,13,00);
           controller.getListOfVehiclesNotAvailable(startTime,endTime);

            verify(preparedStatement).setTimestamp(1, Timestamp.valueOf(startTime));
            verify(preparedStatement).setTimestamp(2, Timestamp.valueOf(startTime));
            verify(preparedStatement).setTimestamp(3,Timestamp.valueOf(endTime));
            verify(preparedStatement).setTimestamp(4,Timestamp.valueOf(endTime));
            verify(preparedStatement).setTimestamp(5,Timestamp.valueOf(endTime));
            verify(preparedStatement).setTimestamp(6, Timestamp.valueOf(startTime));

        } catch (SQLException e) {
            fail();
        }
    }

}