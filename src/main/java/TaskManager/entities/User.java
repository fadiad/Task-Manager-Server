package TaskManager.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Objects;

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

}