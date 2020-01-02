package lapr.project.data.registers;

import lapr.project.data.DataHandler;
import lapr.project.model.Trip;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;

class TripAPITest {
    private LocalDateTime startCalendar;
    private LocalDateTime endCalendar;
    private static TripAPI tripAPI;
    private static DataHandler dataHandler;
    private static ResultSet resultSet;

    @BeforeAll
    static void beforeAll() {
        dataHandler = Mockito.mock(DataHandler.class);
        resultSet = Mockito.mock(ResultSet.class);
        tripAPI = new TripAPI(dataHandler);
    }

    @BeforeEach
    void beforeEach() {
        startCalendar = LocalDateTime.of(2019, 1, 1, 15, 0);
        endCalendar = LocalDateTime.of(2019, 1, 1, 15, 15);
    }

    @Test
    void testCreateNewTrip() {
        Trip expResult = new Trip(startCalendar, endCalendar, "email@email.com", "0", "0", "0");
        assertEquals(expResult, tripAPI.createNewTrip(startCalendar, endCalendar, "email@email.com", "0", "0", "0"));

        expResult = new Trip(startCalendar, "email@email.com", "0", "1");
        assertEquals(expResult, tripAPI.createNewTrip(startCalendar, "email@email.com", "0", "0"));

        expResult = new Trip(startCalendar, "email@email.com", "0", "0");
        assertEquals(expResult, tripAPI.createNewTrip(startCalendar, "email@email.com", "0", null, "0"));
    }
}