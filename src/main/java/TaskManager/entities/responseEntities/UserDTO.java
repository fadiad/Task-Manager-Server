package TaskManager.entities.responseEntities;

import TaskManager.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class UserDTO {

    private final int id;
    private final String username;
    private final String email;

    public UserDTO(User user){
        this.id= user.getId();
        this.username= user.getUsername();
        this.email=user.getEmail();
    }
}
