package TaskManager.entities;

import TaskManager.entities.entitiesUtils.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.Set;


@Entity
@NoArgsConstructor
@Table(name = "user")
@Transactional
@Getter

public class User{
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


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_board",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "board_id"))
    @ToString.Exclude
    private Set<Board> boards;

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