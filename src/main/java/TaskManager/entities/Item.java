package TaskManager.entities;

import TaskManager.entities.entitiesUtils.ItemTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int boardId;

    private String Title;
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

    private int Importance;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Comment> statues=new HashSet<>();

    public void setItem(Item newItem){
        this.assignTo=newItem.getAssignTo();
        this.creator=newItem.getCreator();
        this.Importance=newItem.getImportance();
        this.itemType=newItem.getItemType();
        this.parentItem=newItem.getParentItem();
        this.statusId=newItem.getStatusId();
        this.Description=newItem.getDescription();
        this.Title=newItem.getTitle();
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
                ", Title='" + Title + '\'' +
                ", Description='" + Description + '\'' +
                ", StatusId=" + statusId +
                ", itemType=" + itemType +
                ", dueDate=" + dueDate +
                ", parentItem=" + parentItem +
                ", creator=" + creator +
                ", assignTo=" + assignTo +
                ", Importance=" + Importance +
                '}';
    }
}
