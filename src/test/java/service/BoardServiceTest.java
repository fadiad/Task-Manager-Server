package service;

import TaskManager.entities.Board;
import TaskManager.entities.TaskStatus;
import TaskManager.entities.User;
import TaskManager.entities.responseEntities.BoardToReturn;
import TaskManager.repository.BoardRepository;
import TaskManager.repository.ItemRepository;
import TaskManager.repository.UserRepository;
import TaskManager.service.BoardService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class BoardServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private BoardService boardService;
    private User user;
    private Board goodBoard;
    private Board badBoard;

    @BeforeEach
    @DisplayName("Make sure all the correct parameters are refreshed after each operation")
    void setUp() {
        user = new User();
        user.setEmail("forTest@gmail.com");
        user.setUsername("nameTest");

        goodBoard = new Board();
        goodBoard.setTitle("title");
        goodBoard.setId(1);

        badBoard = new Board();
    }

    @Test
    void deleteStatus_Not_successfully() {
        TaskStatus taskStatus = new TaskStatus(1, "taskStatus", goodBoard);
        assertThrows(EntityNotFoundException.class, ()-> boardService.deleteStatus(2, taskStatus), "Board not found" );
    }
    @Test
    public void testAddNewBoard() {


        // Create mock board
        Board board = new Board();
        board.setTitle("Test Board");

        // Configure mock user repository to return the mock user when findById is called
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(user));

        // Configure mock board repository to return the same board that is passed to it when save is called
        when(boardRepository.save(any(Board.class))).thenReturn(goodBoard);

        // Call addNewBoard method
        BoardToReturn result = boardService.addNewBoard(goodBoard, 1);

        // Verify that the result is as expected
        assertEquals(goodBoard.getId(), result.getId());
        assertEquals(goodBoard.getTitle(), result.getTitle());
    }
}
