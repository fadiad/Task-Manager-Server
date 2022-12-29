package Entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;

import TaskManager.entities.Comment;
import TaskManager.entities.Item;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CommentTest {

    @Mock
    private Item item;

    @Test
    public void testSettersAndGetters() {
        Comment comment = new Comment();
        int id = 1;
        String username = "testUsername";
        String content = "testContent";
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        comment.setId(id);
        comment.setUsername(username);
        comment.setContent(content);
        comment.setDate(date);
        comment.setTime(time);
        comment.setItem(item);

        assertEquals(id, comment.getId());
        assertEquals(username, comment.getUsername());
        assertEquals(content, comment.getContent());
        assertEquals(date, comment.getDate());
        assertEquals(time, comment.getTime());
        assertEquals(item, comment.getItem());
    }
}
