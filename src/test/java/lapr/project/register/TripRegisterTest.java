package lapr.project.register;

import jdk.nashorn.internal.ir.annotations.Ignore;
import lapr.project.model.Company;
import lapr.project.model.Trip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TripRegisterTest {
    private LocalDateTime startCalendar;
    private LocalDateTime endCalendar;
    private TripRegister tripRegister;

    @Ignore
    void beforeEach() {
        tripRegister = Company.getInstance().getTripRegister();

        startCalendar = LocalDateTime.of(2019,1,1,15,0);
        endCalendar = LocalDateTime.of(2019,1,1,15,15);
    }

    @Ignore
    void testCreateNewTrip() {
        Trip expResult = new Trip(startCalendar, endCalendar, "email@email.com", 0, 0, 0);
        assertEquals(expResult, tripRegister.createNewTrip(startCalendar, endCalendar,"email@email.com",0,0,0));

        expResult = new Trip(startCalendar,"email@email.com",0,0);
        assertEquals(expResult, tripRegister.createNewTrip(startCalendar,"email@email.com",0,0));

        expResult = new Trip(startCalendar, "email@email.com",0,0,0);
        assertEquals(expResult, tripRegister.createNewTrip(startCalendar,"email@email.com",0,0,0));
    }
}