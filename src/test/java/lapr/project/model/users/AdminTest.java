package lapr.project.model.users;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdminTest {

    @Test
    void adminTest() {
        Admin admin = new Admin("email@host.com", "admin", "pwd");
        assertEquals(admin.getEmail(), "email@host.com");
        assertEquals(admin.getUsername(), "admin");
        assertEquals(admin.getPassword(), "pwd");
    }
}
