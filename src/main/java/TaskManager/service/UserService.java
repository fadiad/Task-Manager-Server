package TaskManager.service;
import TaskManager.entities.User;
import TaskManager.entities.entitiesUtils.NotificationTypes;
import TaskManager.entities.requests.NotificationRequest;
import TaskManager.entities.responseEntities.UserDTO;
import TaskManager.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;

import static TaskManager.entities.entitiesUtils.Ways.EMAIL;
import static TaskManager.entities.entitiesUtils.Ways.POP_UP;


@Service
@AllArgsConstructor
public class UserService  {
    private final UserRepository userRepository;

    @Transactional
    public UserDTO notificationSetting(int userId, NotificationRequest notificationRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("user not found"));
        System.out.println(user.getUsername());
        user.setEmailNotification(notificationRequest.getWays().contains(EMAIL));
        user.setPopUpNotification(notificationRequest.getWays().contains(POP_UP));
        Set<NotificationTypes> types = notificationRequest.getOption();
        user.setNotificationTypes(types);
        return new UserDTO(userRepository.save(user));
    }
}
