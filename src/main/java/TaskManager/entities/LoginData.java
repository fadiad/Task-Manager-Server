package TaskManager.entities;

import TaskManager.entities.responseEntities.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class LoginData {
    private UserDTO userDTO;
    private String token;
}

