package TaskManager.entities;

import TaskManager.entities.entitiesUtils.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private Set<Board> boards;

    public User(String email ,String password,UserRole userRole){
        this.email =email;
        this.password= password;
        this.userRole=userRole;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}