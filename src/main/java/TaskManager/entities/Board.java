package TaskManager.entities;

import TaskManager.entities.entitiesUtils.ItemTypes;
import TaskManager.entities.entitiesUtils.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String title;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "ItemTypes", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "ItemTypes", nullable = false)
    private Set<ItemTypes> itemTypes =new HashSet<>();

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "board_users_roles", joinColumns = @JoinColumn(name = "board_id"))
    @MapKeyColumn(name = "user_id")
    @Column(name = "user_role")
    private Map<Integer,UserRole> usersRoles = new HashMap<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "boards", fetch = FetchType.LAZY)
    private Set<User> usersOnBoard = new HashSet<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<TaskStatus> statues=new HashSet<>();

    @Override
    public String toString() {
        return "Board{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", itemTypes=" + itemTypes +
                ", usersRoles=" + usersRoles +
                ", usersOnBoard=" + usersOnBoard +
                ", statues=" + statues +
                '}';
    }
}
