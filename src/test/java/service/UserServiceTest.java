package service;

import TaskManager.controller.BoardController;
import TaskManager.controller.UserController;
import TaskManager.entities.Board;
import TaskManager.entities.TaskStatus;
import TaskManager.entities.User;
import TaskManager.entities.entitiesUtils.NotificationTypes;
import TaskManager.entities.entitiesUtils.Ways;
import TaskManager.entities.requests.BoardRequest;
import TaskManager.entities.requests.NotificationRequest;
import TaskManager.entities.responseEntities.BoardDetailsDTO;
import TaskManager.entities.responseEntities.BoardToReturn;
import TaskManager.repository.UserRepository;
import TaskManager.service.BoardService;
import TaskManager.service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testNotificationSetting() {
        // Create a mock of the UserRepository class
        //UserRepository userRepository = mock(UserRepository.class);
        // Set the behavior of the findById method
        when(userRepository.findById(1)).thenReturn(Optional.of(new User()));
        // Set the behavior of the save method
        //doNothing().when(userRepository).save(any(User.class));
        // Create an instance of the NotificationRequest class
        NotificationRequest notificationRequest = new NotificationRequest(new HashSet<>(Arrays.asList(Ways.EMAIL)), new HashSet<>(Arrays.asList(NotificationTypes.ITEM_DELETED)));
        // Call the notificationSetting method
        UserService userService = new UserService(userRepository);
        userService.notificationSetting(1, notificationRequest);
        // Verify that the save method was called
        verify(userRepository).save(any(User.class));
    }


    @Test
    void testNotificationSetting_userNotFound() {
        // Create a mock of the UserRepository class
        //UserRepository userRepository = mock(UserRepository.class);

        // Set the behavior of the findById method to throw an IllegalArgumentException
        when(userRepository.findById(1)).thenThrow(new IllegalArgumentException("user not found"));

        // Create an instance of the NotificationRequest class
        NotificationRequest notificationRequest = new NotificationRequest(new HashSet<>(Arrays.asList(Ways.EMAIL)), new HashSet<>(Arrays.asList(NotificationTypes.ITEM_DELETED)));

        // Call the notificationSetting method
        UserService userService = new UserService(userRepository);
        assertThrows(IllegalArgumentException.class, () -> userService.notificationSetting(1, notificationRequest));

        // Verify that the findById method was called with the correct argument
        verify(userRepository).findById(1);
    }


}