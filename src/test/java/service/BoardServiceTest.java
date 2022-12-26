package service;

import TaskManager.controller.BoardController;
import TaskManager.entities.Board;
import TaskManager.entities.TaskStatus;
import TaskManager.entities.User;
import TaskManager.entities.entitiesUtils.UserRole;
import TaskManager.entities.responseEntities.BoardDetailsDTO;
import TaskManager.entities.responseEntities.BoardToReturn;
import TaskManager.repository.BoardRepository;
import TaskManager.repository.ItemRepository;
import TaskManager.repository.UserRepository;
import TaskManager.service.BoardService;
import TaskManager.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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
        user.setId(1);
        user.setUsername("nameTest");

        goodBoard = new Board();
        goodBoard.setTitle("title");
        goodBoard.setId(1);

        badBoard = new Board();
    }

    @Test
    @DisplayName("Trying to delete status not successfully, entity not found")
    void deleteStatus_Not_successfully() {
        TaskStatus taskStatus = new TaskStatus(1, "taskStatus", goodBoard);
        assertThrows(EntityNotFoundException.class, ()-> boardService.deleteStatus(2, taskStatus), "Board not found" );
    }
    @Test
    void addNewBoard_successfully() {
        given(userRepository.findById(user.getId())).willReturn(Optional.ofNullable(user));

        Board toSave = new Board();
        toSave.setTitle(goodBoard.getTitle());
        toSave.setItemTypes(goodBoard.getItemTypes());
        toSave.getUsersRoles().put(user.getId(), UserRole.ROLE_ADMIN);
        toSave.getUsersOnBoard().add(user);

        when(user.getBoards().add(toSave));

        assertEquals(boardService.addNewBoard(goodBoard, user.getId()), toSave);
    }
/*    @Transactional
    public BoardToReturn addNewBoard(Board board, int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("user not found"));
        Board toSave = new Board();
        toSave.setTitle(board.getTitle());
        toSave.setItemTypes(board.getItemTypes());
        toSave.getUsersRoles().put(user.getId(), UserRole.ROLE_ADMIN);
        toSave.getUsersOnBoard().add(user);
        user.getBoards().add(toSave);
        return new BoardToReturn(boardRepository.save(toSave));
    }*/
}
