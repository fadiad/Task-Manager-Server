package Entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import TaskManager.entities.Board;
import TaskManager.entities.TaskStatus;
import TaskManager.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import TaskManager.entities.entitiesUtils.ItemTypes;
import TaskManager.entities.entitiesUtils.UserRole;

@ExtendWith(MockitoExtension.class)
public class BoardTest {
    @Mock
    private Set<ItemTypes> mockItemTypes;
    @Mock
    private Map<Integer, UserRole> mockUsersRoles;
    @Mock
    private Set<User> mockUsersOnBoard;
    @Mock
    private Set<TaskStatus> mockStatues;

    private Board board;

    @BeforeEach
    public void setup() {
        board = new Board();
        board.setItemTypes(mockItemTypes);
        board.setUsersRoles(mockUsersRoles);
        board.setUsersOnBoard(mockUsersOnBoard);
        board.setStatues(mockStatues);
    }

    @Test
    public void testGetId() {
        int expectedId = 123;
        board.setId(expectedId);
        assertEquals(expectedId, board.getId());
    }

    @Test
    public void testGetTitle() {
        String expectedTitle = "Test Board";
        board.setTitle(expectedTitle);
        assertEquals(expectedTitle, board.getTitle());
    }

    @Test
    public void testGetItemTypes() {
        assertEquals(mockItemTypes, board.getItemTypes());
    }

    @Test
    public void testGetUsersRoles() {
        assertEquals(mockUsersRoles, board.getUsersRoles());
    }

    @Test
    public void testGetUsersOnBoard() {
        assertEquals(mockUsersOnBoard, board.getUsersOnBoard());
    }

    @Test
    public void testGetStatues() {
        assertEquals(mockStatues, board.getStatues());
    }
}
