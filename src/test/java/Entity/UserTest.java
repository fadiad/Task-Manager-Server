package Entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Set;

import TaskManager.entities.Board;
import TaskManager.entities.User;
import TaskManager.entities.entitiesUtils.NotificationTypes;
import TaskManager.entities.entitiesUtils.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserTest {

    @Mock
    private Set<Board> boards;

    @Mock
    private Set<NotificationTypes> notificationTypes;

    @Test
    public void testSettersAndGetters() {
        User user = new User();
        String username = "testUsername";
        String email = "testEmail";
        String password = "testPassword";
        UserRole userRole = UserRole.ROLE_USER;
        boolean emailNotification = true;
        boolean popUpNotification = false;
        int id = 1;

        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setUserRole(userRole);
        user.setNotificationTypes(notificationTypes);
        user.setEmailNotification(emailNotification);
        user.setPopUpNotification(popUpNotification);
        user.setBoards(boards);
        user.setId(id);

        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(userRole, user.getUserRole());
        assertEquals(notificationTypes, user.getNotificationTypes());
        assertEquals(emailNotification, user.isEmailNotification());
        assertEquals(popUpNotification, user.isPopUpNotification());
        assertEquals(boards, user.getBoards());
        assertEquals(id, user.getId());
    }
}
