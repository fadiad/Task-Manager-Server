package TaskManager.entities.responseEntities;

import TaskManager.entities.User;
import TaskManager.entities.entitiesUtils.NotificationTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;


@AllArgsConstructor
@Getter
@NoArgsConstructor
public class UserDTO {

    private int id;
    private String username;
    private String email;
    private Set<NotificationTypes> notificationTypes;

    public UserDTO(User user){
        if(user != null) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.notificationTypes=user.getNotificationTypes();
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
