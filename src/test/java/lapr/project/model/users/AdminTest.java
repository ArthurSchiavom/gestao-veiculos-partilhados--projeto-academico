package lapr.project.model.users;

import org.junit.jupiter.api.Test;

public class AdminTest {

    @Test
    void adminTest() {
        new Admin("email@host.com", "admin", "pwd");
    }
}
