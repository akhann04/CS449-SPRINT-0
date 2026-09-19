import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class test {

    @Test
    public void testShortPassword() {
        Main checker = new Main();

        assertEquals(
            "Password should be at least 8 characters",
            checker.checkLength("hello")
        );
    }

    @Test
    public void testValidPassword() {
        Main checker = new Main();

        assertEquals(
            "Password length is valid",
            checker.checkLength("password123")
        );
    }
}
