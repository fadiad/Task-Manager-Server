package TaskManager.entities;

import TaskManager.entities.entitiesUtils.ItemTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Table(name = "item")
@Transactional
@Getter
@Setter
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int boardId;

    private String title;

    private String Description;

    private int statusId;

    @Enumerated(EnumType.STRING)
    private ItemTypes itemType;

    private LocalDate dueDate;
    private int parentItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assign_to_id")
    private User assignTo;

    private int importance;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Comment> statues=new HashSet<>(); //THE LIST OG THE COMMENT

    public void setItem(Item newItem){
        this.importance =newItem.getImportance();
        this.itemType=newItem.getItemType();
//        this.parentItem=newItem.getParentItem();
        this.Description=newItem.getDescription();
        this.title =newItem.getTitle();
        this.dueDate = newItem.getDueDate();
    }

    public void setAssignTo(User assignTo) {
        this.assignTo = assignTo;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public void setItemType(ItemTypes itemType) {
        this.itemType = itemType;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", boardId=" + boardId +
                ", Title='" + title + '\'' +
                ", Description='" + Description + '\'' +
                ", StatusId=" + statusId +
                ", itemType=" + itemType +
                ", dueDate=" + dueDate +
                ", parentItem=" + parentItem +
                ", creator=" + creator +
                ", assignTo=" + assignTo +
                ", Importance=" + importance +
                '}';
    }
}
