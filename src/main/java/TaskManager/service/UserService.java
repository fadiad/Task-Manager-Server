package TaskManager.service;
import TaskManager.entities.User;
import TaskManager.entities.entitiesUtils.NotificationTypes;
import TaskManager.entities.requests.NotificationRequest;
import TaskManager.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

import static TaskManager.entities.entitiesUtils.Ways.EMAIL;
import static TaskManager.entities.entitiesUtils.Ways.POP_UP;


@Service
@AllArgsConstructor
public class UserService  {
    private final UserRepository userRepository;


    /**
     * notification setting of the user, update and change them.
     * @param userId to find the user
     * @param notificationRequest contain 2 set of enum's. (ways, options)
     */
    public void notificationSetting(int userId, NotificationRequest notificationRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("user not found"));
        System.out.println(user.getUsername());
        user.setEmailNotification(notificationRequest.getWays().contains(EMAIL));
        user.setPopUpNotification(notificationRequest.getWays().contains(POP_UP));
        Set<NotificationTypes> types = notificationRequest.getOption();
        user.setNotificationTypes(types);
        userRepository.save(user);
    }
}
