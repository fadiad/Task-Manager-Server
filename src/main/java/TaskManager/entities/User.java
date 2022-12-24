package TaskManager.entities;

import TaskManager.entities.entitiesUtils.NotificationTypes;
import TaskManager.entities.entitiesUtils.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table(name = "user")
@Transactional
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotBlank
    @NotNull
    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_notification", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "notificationType", nullable = false)
    private Set<NotificationTypes> notificationTypes = new HashSet<>();

    private boolean emailNotification;
    private boolean popUpNotification;

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

}