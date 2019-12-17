package lapr.project.model.users;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTypeTest {
    @Test
    void getSQLNameTest() {
        UserType userType = UserType.ADMIN;
        assertEquals("administrator", userType.getSQLName());

        userType = UserType.CLIENT;
        assertEquals("client", userType.getSQLName());
    }

    @Test
    void parseUserTypeTest() {
        String sqlName = "administrator";
        UserType userType = UserType.parseUserType(sqlName);
        assertTrue(userType == UserType.ADMIN);

        sqlName = "client";
        userType = UserType.parseUserType(sqlName);
        assertTrue(userType == UserType.CLIENT);

        sqlName = "user";
        userType = UserType.parseUserType(sqlName);
        assertNull(userType);
    }
}
