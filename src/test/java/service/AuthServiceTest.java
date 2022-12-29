package service;

import TaskManager.entities.User;

import TaskManager.repository.UserRepository;
import TaskManager.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AuthService authService;
    private User user;

    @BeforeEach
    @DisplayName("Make sure all the correct parameters are refreshed after each operation")
    public void setUp() {
        user = new User();
        user.setEmail("forTest@gmail.com");
        user.setId(1);
        user.setUsername("nameTest");
    }

    @Test
    @DisplayName("add user not successfully because the email exist on DB ")
    public void addUser_Not_successfully() {
        when(userRepository.findByEmail("forTest@gmail.com")).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> authService.addUser(user), "this email is already in use");
    }

    @Test
    @DisplayName("load user by username successfully")
    public void loadUserByUsername_successfully() {
        when(userRepository.findByEmail("forTest@gmail.com")).thenReturn(Optional.of(user));

        assertTrue(authService.loadUserByUsername(user.getEmail()).getUsername().length() > 0);
    }
}
