package Entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import TaskManager.entities.LoginRequest;
import org.junit.jupiter.api.Test;

public class LoginRequestTest {

    @Test
    public void testSettersAndGetters() {
        LoginRequest loginRequest = new LoginRequest();
        String email = "testEmail";
        String password = "testPassword";

        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        assertEquals(email, loginRequest.getEmail());
        assertEquals(password, loginRequest.getPassword());
    }
}

