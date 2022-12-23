package TaskManager.service;

import TaskManager.entities.entitiesUtils.NotificationTypes;
import TaskManager.entities.User;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public void emailNotification(User user, NotificationTypes notificationTypes) throws IllegalAccessException {
    if(user.getNotificationTypes().contains(notificationTypes)){
        //EmailNotification.sendEmailNotification(user.getEmail(),notificationTypes.toString());
    }
    else
        throw new IllegalAccessException("user do not get email on this notification type");
    }

}
