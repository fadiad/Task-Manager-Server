package Entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Set;

import TaskManager.entities.Comment;
import TaskManager.entities.Item;
import TaskManager.entities.User;
import TaskManager.entities.entitiesUtils.ItemTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ItemTest {

    @Mock
    private User creator;

    @Mock
    private User assignTo;

    @Mock
    private Set<Comment> comments;

    @Test
    public void testSettersAndGetters() {
        Item item = new Item();
        int id = 1;
        int boardId = 2;
        String title = "testTitle";
        String description = "testDescription";
        int statusId = 3;
        ItemTypes itemType = ItemTypes.TESTING;
        LocalDate dueDate = LocalDate.now();
        int parentItem = 4;
        int importance = 5;

        item.setId(id);
        item.setBoardId(boardId);
        item.setTitle(title);
        item.setDescription(description);
        item.setStatusId(statusId);
        item.setItemType(itemType);
        item.setDueDate(dueDate);
        item.setParentItem(parentItem);
        item.setCreator(creator);
        item.setAssignTo(assignTo);
        item.setImportance(importance);
        item.setStatues(comments);

        assertEquals(id, item.getId());
        assertEquals(boardId, item.getBoardId());
        assertEquals(title, item.getTitle());
        assertEquals(description, item.getDescription());
        assertEquals(statusId, item.getStatusId());
        assertEquals(itemType, item.getItemType());
        assertEquals(dueDate, item.getDueDate());
        assertEquals(parentItem, item.getParentItem());
        assertEquals(creator, item.getCreator());
        assertEquals(assignTo, item.getAssignTo());
        assertEquals(importance, item.getImportance());
        assertEquals(comments, item.getStatues());
    }
}



