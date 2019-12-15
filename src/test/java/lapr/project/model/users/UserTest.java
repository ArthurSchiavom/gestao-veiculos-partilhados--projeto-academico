package lapr.project.model.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private Client instance;

    @BeforeEach
    void beforeEach() {
        instance = new Client("1180852@isep.ipp.pt","username","password", 22, 180, 60, 'm',2.3F,
                new CreditCard("12341234123412", LocalDate.of(2020,12,31), 321));
    }

    @Test
    void getType() {
        assertEquals(UserType.CLIENT,instance.getType(),"Should be a client");
    }

    @Test
    void getEmail() {
        assertEquals("1180852@isep.ipp.pt",instance.getEmail(),"Wrong email");
    }

    @Test
    void getPassword() {
        assertEquals("password",instance.getPassword(),"Wrong password");
    }

    @Test
    void getUsername() {
        assertEquals("username",instance.getUsername(),"Wrong email");
    }

    @Test
    void testHashCode() {
        assertEquals(new Client("1180852@isep.ipp.pt","username","password", 22, 180, 60, 'm',2.3F,
                new CreditCard("12341234123412", LocalDate.of(2020,12,31), 321)).hashCode(),instance.hashCode(),"Wrong email");
    }

    @Test
    void testEquals() {
        assertEquals(instance.equals(new Client("1180852@isep.ipp.pt","username","password", 22, 180, 60, 'm',2.3F,
                new CreditCard("12341234123412", LocalDate.of(2020,12,31), 321))),true,"The objects should be equal");

    }

    @Test
    void testToString() {
        System.out.println(instance);
        assertEquals("Client{points=0, age=22, height=180, weight=60, gender=m, cyclingAverageSpeed=2.3, cc=CreditCard{ccNumber='12341234123412', ccExpiration=2020-12-31, ccv=321}}",instance.toString(),"ToString");

    }
}