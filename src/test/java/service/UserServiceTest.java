package service;

import TaskManager.entities.User;
import TaskManager.entities.entitiesUtils.NotificationTypes;
import TaskManager.entities.entitiesUtils.Ways;
import TaskManager.entities.requests.NotificationRequest;
import TaskManager.repository.UserRepository;
import TaskManager.service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Arrays;
import java.util.HashSet;
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
        when(userRepository.findById(1)).thenReturn(Optional.of(new User()));
        NotificationRequest notificationRequest = new NotificationRequest(new HashSet<>(Arrays.asList(Ways.EMAIL)), new HashSet<>(Arrays.asList(NotificationTypes.ITEM_DELETED)));
        userService.notificationSetting(1, notificationRequest);

        verify(userRepository).save(any(User.class));
    }


    @Test
    void testNotificationSetting_userNotFound() {
        when(userRepository.findById(1)).thenThrow(new IllegalArgumentException("user not found"));
        NotificationRequest notificationRequest = new NotificationRequest(new HashSet<>(Arrays.asList(Ways.EMAIL)), new HashSet<>(Arrays.asList(NotificationTypes.ITEM_DELETED)));
        UserService userService = new UserService(userRepository);

        assertThrows(IllegalArgumentException.class, () -> userService.notificationSetting(1, notificationRequest));
    }
}