package TaskManager.entities;

import TaskManager.entities.entitiesUtils.ItemTypes;
import TaskManager.entities.entitiesUtils.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Map;
import java.util.Set;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "ItemTypes", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "ItemTypes", nullable = false)
    private Set<ItemTypes> itemTypes;


    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TaskStatus> statuses;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "board_users_roles", joinColumns = @JoinColumn(name = "board_id"))
    @MapKeyColumn(name = "user_id")
    @Column(name = "user_role")
    private Map<Integer,UserRole> usersRoles;



    @ManyToMany(mappedBy = "boards", fetch = FetchType.LAZY)
    private Set<User> usersOnBoard;


    @Override
    public String toString() {
        return "Board{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", itemTypes=" + itemTypes +
                ", statuses=" + statuses +
                ", usersRoles=" + usersRoles +
                ", usersOnBoard=" + usersOnBoard +
                '}';
    }
}
