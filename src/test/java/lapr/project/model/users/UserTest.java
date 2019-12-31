package lapr.project.model.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private Client instance;

    @BeforeEach
    void beforeEach() {
        instance = new Client("1180852@isep.ipp.pt","username","password", 22, 180, 60, 'm',2.3F,
                true, new CreditCard("12341234123412"));
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
        Client client1 = new Client("1180852@isep.ipp.pt","username","password",
                22, 180, 60, 'm',2.3F, true, new CreditCard(
                        "12341234123412"));
        Client client2 = new Client("1180852@isep.ipp.pt","lol","password231",
                22, 180, 60, 'm',2.3F, true, new CreditCard(
                "12341234123412"));
        int expResult = client2.hashCode();
        assertEquals(expResult, client1.hashCode());

        client1 = new Client("1170852@isep.ipp.pt","username1","password1",
                22, 180, 60, 'm',2.3F, true, new CreditCard(
                "12341234123412"));
        assertNotEquals(expResult, client1.hashCode());
    }

    @Test
    void testEquals() {
        Object receipt1 = new Client("1170852@isep.ipp.pt","username1","password1",
                22, 180, 60, 'm',2.3F, true, new CreditCard(
                "12341234123412"));
        assertEquals(receipt1, receipt1);

        Client receipt2 = new Client("1170852@isep.ipp.pt","username2","password3",
                22, 180, 60, 'm',2.3F, true, new CreditCard(
                "12341234123412"));
        assertEquals(receipt1, receipt2);

        receipt1 = new Client("1180852@isep.ipp.pt","username3","password4",
                22, 180, 60, 'm',2.3F, true, new CreditCard(
                "12341234123412"));
        assertNotEquals(receipt1, receipt2);

        receipt1 = null;
        assertNotEquals(receipt2, receipt1);

        receipt1 = "";
        assertNotEquals(receipt1, receipt2);
    }
}