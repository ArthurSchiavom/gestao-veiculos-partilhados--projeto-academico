package lapr.project.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpdateableTest {
    @Test
    void getValueTest() {
        Updateable<Boolean> up = new Updateable<>(true);
        assertEquals(true, up.getValue());
    }

    @Test
    void setValueTest() {
        Updateable<Boolean> up = new Updateable<>(true);
        up.setValue(false);
        assertEquals(false, up.getValue());
    }
}
