package TaskManager.entities;

import TaskManager.entities.entitiesUtils.ItemTypes;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Table(name = "item")
@Transactional
@Getter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int boardId;

    private String Title;
    private String Description;

    private int StatusId;

    @Enumerated(EnumType.STRING)//TODO must fadi will test this later or  i will kick his butt
    private ItemTypes itemType;

    private LocalDate dueDate;
    private int parentItem;//TODO we will check if we will change it to item object

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assign_to_id")
    private User assignTo;

    private int Importance;

    public void setAssignTo(User assignTo) {
        this.assignTo = assignTo;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }


}
