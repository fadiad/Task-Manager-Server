package Entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import TaskManager.entities.Board;
import TaskManager.entities.TaskStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TaskStatusTest {

    @Mock
    private Board board;

    @Test
    public void testSettersAndGetters() {
        TaskStatus taskStatus = new TaskStatus();
        String name = "testName";

        taskStatus.setName(name);
        taskStatus.setBoard(board);

        assertEquals(name, taskStatus.getName());
        assertEquals(board, taskStatus.getBoard());
    }

    @Test
    public void testEquals() {
        String name = "testName";
        int id = 1;
        TaskStatus taskStatus1 = new TaskStatus(id, name, board);
        TaskStatus taskStatus2 = new TaskStatus(id, name, board);

        assertEquals(taskStatus1, taskStatus2);
    }
}

