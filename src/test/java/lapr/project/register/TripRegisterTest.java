package lapr.project.register;

import lapr.project.model.Trip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class TripRegisterTest {
    private Calendar startCalendar;
    private Calendar endCalendar;
    private TripRegister tripRegister;

    @BeforeEach
    void beforeEach() {
        tripRegister = new TripRegister();

        startCalendar = Calendar.getInstance();
        startCalendar.set(2019, Calendar.JANUARY,1, 15,0);
        endCalendar = Calendar.getInstance();
        endCalendar.set(2019, Calendar.JANUARY, 1, 15, 15);
    }

    @Test
    void testCreateNewTrip() {
        Trip expResult = new Trip(startCalendar, endCalendar, 0, 0, 0, 0);
        assertEquals(expResult, tripRegister.createNewTrip(startCalendar, endCalendar,0,0,0,0));

        expResult = new Trip(startCalendar,0,0,0);
        assertEquals(expResult, tripRegister.createNewTrip(startCalendar,0,0,0));

        expResult = new Trip(startCalendar, 0,0,0,0);
        assertEquals(expResult, tripRegister.createNewTrip(startCalendar,0,0,0,0));
    }
}