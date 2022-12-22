package TaskManager.entities.responseEntities;

import TaskManager.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Getter
@NoArgsConstructor
public class UserDTO {

    private int id;
    private String username;
    private String email;

    public UserDTO(User user){
        if(user != null) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
        }
    }
}
