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
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;


    private String title;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "ItemTypes", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "ItemTypes", nullable = false)
    private Set<ItemTypes> itemTypes = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "board_users_roles", joinColumns = @JoinColumn(name = "board_id"))
    @MapKeyColumn(name = "user_id")
    @Column(name = "user_role")
    private Map<Integer, UserRole> usersRoles = new HashMap<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "boards", fetch = FetchType.LAZY)
    private Set<User> usersOnBoard = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TaskStatus> statues=new HashSet<>();


    @Override
    public String toString() {
        return "Board{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", statues=" + statues +
                '}';
    }

    /**
     * Static Factory Method
     */
    public static Board createNewBoard(String title) {
        Board board = new Board();
        board.setTitle(title);
        return board;
    }

    public static Board createNewBoard(String title, Set<ItemTypes> itemType, User user) {
        Board board = new Board();
        board.setTitle(title);
        board.setItemTypes(itemType);
        board.getUsersOnBoard().add(user);
        board.getUsersRoles().put(user.getId(), UserRole.ROLE_ADMIN);
        return board;
    }
}
