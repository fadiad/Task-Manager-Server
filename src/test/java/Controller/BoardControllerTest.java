package Controller;

import TaskManager.controller.BoardController;
import TaskManager.entities.Board;
import TaskManager.entities.TaskStatus;
import TaskManager.entities.entitiesUtils.ItemTypes;
import TaskManager.entities.requests.BoardRequest;
import TaskManager.entities.responseEntities.BoardDetailsDTO;
import TaskManager.entities.responseEntities.BoardToReturn;
import TaskManager.entities.responseEntities.UserDTO;
import TaskManager.service.BoardService;
import TaskManager.service.UserService;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import java.util.Collections;

import static TaskManager.entities.entitiesUtils.ItemTypes.BUG;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private BoardService boardService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private BoardToReturn boardToReturn;
    @InjectMocks
    private BoardController boardController;

    private Board goodBoard;

    @BeforeEach
    @DisplayName("Make sure all the correct parameters are refreshed after each operation")
    void setUp() {
        goodBoard = new Board();
        goodBoard.setTitle("title");
        goodBoard.setId(1);
    }

    @Test
    public void testCreateBoard() {
        // Set up the test data
        Board board = new Board();
        board.setTitle("Test Board");
        int userId = 1;
        // Set up the mock behavior
        when(request.getAttribute("userId")).thenReturn(userId);
        when(boardService.addNewBoard(board, userId)).thenReturn(boardToReturn);
        // Invoke the method under test
        ResponseEntity<BoardToReturn> response = boardController.createBoard(request, board);
        // Verify the results
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(boardToReturn, response.getBody());
    }

    @Test
    public void testGetBoardById_Success() {
        // Set up the test data
        int boardId = 1;
        int userId = 1;
        BoardDetailsDTO boardDetailsDTO = new BoardDetailsDTO();
        // Set up the mock behavior
        when(request.getAttribute("userId")).thenReturn(userId);
        when(boardService.getBoardById(boardId, userId)).thenReturn(boardDetailsDTO);
        // Invoke the method under test
        ResponseEntity<BoardDetailsDTO> response = boardController.getBoardById(request, boardId);
        // Verify the results
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(boardDetailsDTO, response.getBody());
    }

    @Test
    public void testGetBoardById_BoardNotFound() {
        // Set up the test data
        int boardId = 1;
        int userId = 1;
        // Set up the mock behavior
        when(request.getAttribute("userId")).thenReturn(userId);
        when(boardService.getBoardById(boardId, userId)).thenThrow(new IllegalArgumentException("Board not found"));
        // Invoke the method under test
        assertThrows(IllegalArgumentException.class, () -> boardController.getBoardById(request, boardId));
    }

    @Test
    public void testRemoveStatuses() {
        // Set up the test data
        int boardId = 1;
        int statusId = 2;
        // Invoke the method under test
        boardController.removeStatuses(boardId, statusId);
        // Verify the results
        verify(boardService).deleteStatus(boardId, statusId);
    }

    @Test
    public void testRemoveStatuses_BoardNotFound() {
        // Set up the test data
        int boardId = 1;
        int statusId = 2;
        // Set up the mock behavior
        doThrow(new IllegalArgumentException("Board not found")).when(boardService).deleteStatus(boardId, statusId);
        // Invoke the method under test
        assertThrows(IllegalArgumentException.class, () -> boardController.removeStatuses(boardId, statusId));
    }

    @Test
    public void testAddNewStatusToBoard_Success() {
        // Set up the test data
        int boardId = 1;
        TaskStatus taskStatus = new TaskStatus();
        Board board = new Board();
        // Set up the mock behavior
        when(boardService.addNewStatusToBoard(boardId, taskStatus)).thenReturn(board);
        // Invoke the method under test
        ResponseEntity<Board> response = boardController.addNewStatusToBoard(boardId, taskStatus);
        // Verify the results
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(board, response.getBody());
    }

    @Test
    public void testAddNewStatusToBoard_BoardNotFound() {
        // Set up the test data
        int boardId = 1;
        TaskStatus taskStatus = new TaskStatus();
        // Set up the mock behavior
        doThrow(new IllegalArgumentException("Board not found")).when(boardService).addNewStatusToBoard(boardId, taskStatus);
        // Invoke the method under test
        assertThrows(IllegalArgumentException.class, () -> boardController.addNewStatusToBoard(boardId, taskStatus));
    }

    @Test
    public void testDeleteItemType_Success() {
        // Set up the test data
        int boardId = 1;
        BoardRequest boardRequest = new BoardRequest();
        boardRequest.setType(Collections.singleton(BUG));
        Board board = new Board();
        // Set up the mock behavior
        when(boardService.deleteItemTypeOnBoard(boardId, Collections.singleton(BUG))).thenReturn(board);
        // Invoke the method under test
        ResponseEntity<Board> response = boardController.deleteItemType(boardId, boardRequest);
        // Verify the results
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(board, response.getBody());
    }

    @Test
    public void testDeleteItemType_BoardNotFound() {
        // Set up the test data
        int boardId = 1;
        BoardRequest boardRequest = new BoardRequest();
        boardRequest.setType(Collections.singleton(BUG));

        // Set up the mock behavior
        doThrow(new IllegalArgumentException("Board not found")).when(boardService).deleteItemTypeOnBoard(boardId, Collections.singleton(BUG));

        // Invoke the method under test
        assertThrows(IllegalArgumentException.class, () -> boardController.deleteItemType(boardId, boardRequest));
    }

    @Test
    public void testAddItemType_Success() {
        // Set up the test data
        int boardId = 1;
        BoardRequest boardRequest = new BoardRequest();
        boardRequest.setType(Collections.singleton(BUG));
        Board board = new Board();

        // Set up the mock behavior
        when(boardService.addItemTypeOnBoard(boardId, Collections.singleton(BUG))).thenReturn(board);

        // Invoke the method under test
        ResponseEntity<Board> response = boardController.addItemType(boardId, boardRequest);

        // Verify the results
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(board, response.getBody());
    }

    @Test
    public void testAddItemType_BoardNotFound() {
        // Set up the test data
        int boardId = 1;
        BoardRequest boardRequest = new BoardRequest();
        boardRequest.setType(Collections.singleton(BUG));

        // Set up the mock behavior
        doThrow(new IllegalArgumentException("Board not found")).when(boardService).addItemTypeOnBoard(boardId, Collections.singleton(BUG));

        // Invoke the method under test
        assertThrows(IllegalArgumentException.class, () -> boardController.addItemType(boardId, boardRequest));
    }

    @Test
    public void testUpdateItemStatus_Success() {
        // Set up the test data
        int boardId = 1;
        int itemId = 1;
        TaskStatus taskStatus = new TaskStatus();
        Board board = new Board();

        // Set up the mock behavior
        when(boardService.updateItemStatusToBoard(boardId, itemId, taskStatus)).thenReturn(board);

        // Invoke the method under test
        ResponseEntity<Board> response = boardController.updateItemStatus(boardId, itemId, taskStatus);

        // Verify the results
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(board, response.getBody());
    }

    @Test
    public void testUpdateItemStatus_BoardNotFound() {
        // Set up the test data
        int boardId = 1;
        int itemId = 1;
        TaskStatus taskStatus = new TaskStatus();

        // Set up the mock behavior
        doThrow(new IllegalArgumentException("Board not found")).when(boardService).updateItemStatusToBoard(boardId, itemId, taskStatus);

        // Invoke the method under test
        assertThrows(IllegalArgumentException.class, () -> boardController.updateItemStatus(boardId, itemId, taskStatus));
    }

    @Test
    public void testShareBoardByUserId_Success() {
        // Set up the test data
        int boardId = 1;
        String email = "test@example.com";
        UserDTO userDTO = new UserDTO();
        // Set up the mock behavior
        when(boardService.shareBoard(boardId, email)).thenReturn(userDTO);
        // Invoke the method under test
        ResponseEntity<UserDTO> response = boardController.shareBoardByUserId(boardId, email);
        // Verify the results
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(userDTO, response.getBody());
    }

    @Test
    public void testShareBoardByUserId_BoardNotFound() {
        // Set up the test data
        int boardId = 1;
        String email = "test@example.com";
        // Set up the mock behavior
        doThrow(new IllegalArgumentException("Board not found")).when(boardService).shareBoard(boardId, email);
        // Invoke the method under test
        assertThrows(IllegalArgumentException.class, () -> boardController.shareBoardByUserId(boardId, email));
    }
}