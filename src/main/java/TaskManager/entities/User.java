package TaskManager.entities;

import TaskManager.entities.entitiesUtils.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.transaction.Transactional;


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

    public User(String email ,String password){
        this.email =email;
        this.password= password;
    }


}