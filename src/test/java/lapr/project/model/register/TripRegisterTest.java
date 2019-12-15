package lapr.project.model.register;

import lapr.project.data.DataHandler;
import lapr.project.model.Trip;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;

class TripRegisterTest {
    private LocalDateTime startCalendar;
    private LocalDateTime endCalendar;
    private static TripRegister tripRegister;
    private static DataHandler dataHandler;
    private static ResultSet resultSet;

    @BeforeAll
    static void beforeAll() {
        dataHandler = Mockito.mock(DataHandler.class);
        resultSet = Mockito.mock(ResultSet.class);
        tripRegister = new TripRegister(dataHandler);
    }

    @BeforeEach
    void beforeEach() {
        startCalendar = LocalDateTime.of(2019, 1, 1, 15, 0);
        endCalendar = LocalDateTime.of(2019, 1, 1, 15, 15);
    }

    @Test
    void testCreateNewTrip() {
        Trip expResult = new Trip(startCalendar, endCalendar, "email@email.com", 0, 0, 0);
        assertEquals(expResult, tripRegister.createNewTrip(startCalendar, endCalendar, "email@email.com", 0, 0, 0));

        expResult = new Trip(startCalendar, "email@email.com", 0, 0);
        assertEquals(expResult, tripRegister.createNewTrip(startCalendar, "email@email.com", 0, 0));

        expResult = new Trip(startCalendar, "email@email.com", 0, 0);
        assertEquals(expResult, tripRegister.createNewTrip(startCalendar, "email@email.com", 0, 0, 0));
    }

    @Test
    void testFetchTrip() {
//        try {
//            Mockito.when(dataHandler.executeQuery(any(PreparedStatement.class))).thenReturn(resultSet);
//            Mockito.when(resultSet.getInt( 3)).thenReturn(0);
//            Mockito.when(resultSet.getInt( 4)).thenReturn(0);
//            Mockito.when(resultSet.getInt( 5)).thenReturn(0);
//            Mockito.when(resultSet.getTimestamp( 6)).thenReturn(
//                    Timestamp.valueOf(endCalendar));
//            Mockito.when(!resultSet.next()).thenReturn(true);
//
//            Trip expResult = new Trip(startCalendar, endCalendar, "email@email.com", 0, 0, 0);
//            Trip result = tripRegister.fetchTrip("email@email.com", startCalendar);
//            System.out.println(result);
//            assertEquals(expResult, result);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
}