package TaskManager.entities.responseEntities;

import TaskManager.entities.User;
import TaskManager.entities.entitiesUtils.NotificationTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.transaction.Transactional;
import java.util.Set;


@AllArgsConstructor
@Getter
@NoArgsConstructor
@Transactional
public class UserDTO {

    private int id;
    private String username;
    private String email;

    private boolean emailNotification;
    private boolean popUpNotification;
    //private hasEmailnotification
    private Set<NotificationTypes> notificationTypes;

    public UserDTO(User user){
        if(user != null) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.notificationTypes=user.getNotificationTypes();
            this.emailNotification=user.isEmailNotification();
            this.popUpNotification=user.isPopUpNotification();
        }
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
