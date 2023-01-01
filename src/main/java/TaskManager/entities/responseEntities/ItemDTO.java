package TaskManager.entities.responseEntities;

import TaskManager.entities.Comment;
import TaskManager.entities.Item;
import TaskManager.entities.entitiesUtils.ItemTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {

    private int id;
    private int boardId;
    private String Title;
    private String Description;
    private int StatusId;
    private ItemTypes itemType;
    private LocalDate dueDate;
    private int parentItem;
    private UserDTO creator;
    private UserDTO assignTo;
    private int Importance;

    private Set<Comment> commentSet;

    private ItemDTO(Item item) {
        if (item != null) {
            this.id = item.getId();
            this.Title = item.getTitle();
            this.creator = new UserDTO(item.getCreator());
            this.assignTo = new UserDTO(item.getAssignTo());
            this.boardId = item.getBoardId();
            this.Description = item.getDescription();
            this.StatusId = item.getStatusId();
            this.itemType = item.getItemType();
            this.dueDate = item.getDueDate();
            this.parentItem = item.getParentItem();
            this.Importance = item.getImportance();
            this.commentSet=item.getComments();
        }
    }

    @Override
    public String toString() {
        return "ItemDTO{" +
                "id=" + id +
                ", boardId=" + boardId +
                ", Title='" + Title + '\'' +
                ", Description='" + Description + '\'' +
                ", StatusId=" + StatusId +
                ", itemType=" + itemType +
                ", dueDate=" + dueDate +
                ", parentItem=" + parentItem +
                ", creator=" + creator +
                ", assignTo=" + assignTo +
                ", Importance=" + Importance +
                '}';
    }

    public static ItemDTO createItemDTO(Item item) {
        return new ItemDTO(item);
    }
}