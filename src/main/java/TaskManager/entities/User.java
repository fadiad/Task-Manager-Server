package TaskManager.entities;

import TaskManager.entities.entitiesUtils.NotificationTypes;
import TaskManager.entities.entitiesUtils.UserRole;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table(name = "user")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_notification", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "notificationType", nullable = false)
    private Set<NotificationTypes> notificationTypes = new HashSet<>();

    private boolean emailNotification;
    private boolean popUpNotification;

    public void setNotificationTypes(Set<NotificationTypes> notificationTypes) {
        this.notificationTypes = notificationTypes;
    }

    public void setEmailNotification(boolean emailNotification) {
        this.emailNotification = emailNotification;
    }

    public void setPopUpNotification(boolean popUpNotification) {
        this.popUpNotification = popUpNotification;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_board",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "board_id"))
    @ToString.Exclude
    private Set<Board> boards = new HashSet<>();

    public void setBoards(Set<Board> boards) {
        this.boards = boards;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", userRole=" + userRole +
                '}';
    }
}