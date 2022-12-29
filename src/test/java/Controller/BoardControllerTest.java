package Controller;

import TaskManager.controller.BoardController;
import TaskManager.entities.Board;
import TaskManager.entities.TaskStatus;
import TaskManager.entities.requests.BoardRequest;
import TaskManager.entities.responseEntities.BoardDetailsDTO;
import TaskManager.entities.responseEntities.BoardToReturn;
import TaskManager.entities.responseEntities.UserDTO;
import TaskManager.service.BoardService;
import TaskManager.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

import java.util.Collections;

import static TaskManager.entities.entitiesUtils.ItemTypes.BUG;
import static org.junit.jupiter.api.Assertions.*;
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
    }

    @Test
    public void testCreateBoard() {
        Board board = new Board();
        board.createNewBoard("Test Board");
        int userId = 1;
        when(request.getAttribute("userId")).thenReturn(userId);
        when(boardService.addNewBoard(board, userId)).thenReturn(boardToReturn);
        ResponseEntity<BoardToReturn> response = boardController.createBoard(request, board);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(boardToReturn, response.getBody());
    }

    @Test
    public void testGetBoardById_Success() {
        int boardId = 1;
        int userId = 1;
        BoardDetailsDTO boardDetailsDTO = new BoardDetailsDTO();
        when(request.getAttribute("userId")).thenReturn(userId);
        when(boardService.getBoardById(boardId, userId)).thenReturn(boardDetailsDTO);
        ResponseEntity<BoardDetailsDTO> response = boardController.getBoardById(request, boardId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(boardDetailsDTO, response.getBody());
    }

    @Test
    public void testGetBoardById_BoardNotFound() {
        int boardId = 1;
        int userId = 1;
        when(request.getAttribute("userId")).thenReturn(userId);
        when(boardService.getBoardById(boardId, userId)).thenThrow(new IllegalArgumentException("Board not found"));

        assertThrows(IllegalArgumentException.class, () -> boardController.getBoardById(request, boardId));
    }

    @Test
    public void testRemoveStatuses() {
        int boardId = 1;
        int statusId = 2;
        boardController.removeStatuses(boardId, statusId);

        verify(boardService).deleteStatus(boardId, statusId);
    }

    @Test
    public void testRemoveStatuses_BoardNotFound() {
        int boardId = 1;
        int statusId = 2;
        doThrow(new IllegalArgumentException("Board not found")).when(boardService).deleteStatus(boardId, statusId);

        assertThrows(IllegalArgumentException.class, () -> boardController.removeStatuses(boardId, statusId));
    }

    @Test
    public void testAddNewStatusToBoard_Success() {
        int boardId = 1;
        TaskStatus taskStatus = new TaskStatus();
        Board board = new Board();
        when(boardService.addNewStatusToBoard(boardId, taskStatus)).thenReturn(board);
        ResponseEntity<Board> response = boardController.addNewStatusToBoard(boardId, taskStatus);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertSame(board, response.getBody());
    }

    @Test
    public void testAddNewStatusToBoard_BoardNotFound() {
        int boardId = 1;
        TaskStatus taskStatus = new TaskStatus();
        doThrow(new IllegalArgumentException("Board not found")).when(boardService).addNewStatusToBoard(boardId, taskStatus);

        assertThrows(IllegalArgumentException.class, () -> boardController.addNewStatusToBoard(boardId, taskStatus));
    }

    @Test
    public void testDeleteItemType_Success() {
        int boardId = 1;
        BoardRequest boardRequest = new BoardRequest();
        boardRequest.setType(Collections.singleton(BUG));
        Board board = new Board();
        when(boardService.deleteItemTypeOnBoard(boardId, Collections.singleton(BUG))).thenReturn(board);
        ResponseEntity<Board> response = boardController.deleteItemType(boardId, boardRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(board, response.getBody());
    }

    @Test
    public void testDeleteItemType_BoardNotFound() {
        int boardId = 1;
        BoardRequest boardRequest = new BoardRequest();
        boardRequest.setType(Collections.singleton(BUG));
        doThrow(new IllegalArgumentException("Board not found")).when(boardService).deleteItemTypeOnBoard(boardId, Collections.singleton(BUG));

        assertThrows(IllegalArgumentException.class, () -> boardController.deleteItemType(boardId, boardRequest));
    }

    @Test
    public void testAddItemType_Success() {
        int boardId = 1;
        BoardRequest boardRequest = new BoardRequest();
        boardRequest.setType(Collections.singleton(BUG));
        Board board = new Board();
        when(boardService.addItemTypeOnBoard(boardId, Collections.singleton(BUG))).thenReturn(board);
        ResponseEntity<Board> response = boardController.addItemType(boardId, boardRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(board, response.getBody());
    }

    @Test
    public void testAddItemType_BoardNotFound() {
        int boardId = 1;
        BoardRequest boardRequest = new BoardRequest();
        boardRequest.setType(Collections.singleton(BUG));
        doThrow(new IllegalArgumentException("Board not found")).when(boardService).addItemTypeOnBoard(boardId, Collections.singleton(BUG));

        assertThrows(IllegalArgumentException.class, () -> boardController.addItemType(boardId, boardRequest));
    }

    @Test
    public void testUpdateItemStatus_Success() {
        int boardId = 1;
        int itemId = 1;
        TaskStatus taskStatus = new TaskStatus();
        Board board = new Board();
        when(boardService.updateItemStatusToBoard(boardId, itemId, taskStatus)).thenReturn(board);
        ResponseEntity<Board> response = boardController.updateItemStatus(boardId, itemId, taskStatus);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(board, response.getBody());
    }

    @Test
    public void testUpdateItemStatus_BoardNotFound() {
        int boardId = 1;
        int itemId = 1;
        TaskStatus taskStatus = new TaskStatus();
        doThrow(new IllegalArgumentException("Board not found")).when(boardService).updateItemStatusToBoard(boardId, itemId, taskStatus);

        assertThrows(IllegalArgumentException.class, () -> boardController.updateItemStatus(boardId, itemId, taskStatus));
    }

    @Test
    public void testShareBoardByUserId_Success() {
        int boardId = 1;
        String email = "test@example.com";
        UserDTO userDTO = new UserDTO();
        when(boardService.shareBoard(boardId, email)).thenReturn(userDTO);
        ResponseEntity<UserDTO> response = boardController.shareBoardByUserId(boardId, email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(userDTO, response.getBody());
    }

    @Test
    public void testShareBoardByUserId_BoardNotFound() {
        int boardId = 1;
        String email = "test@example.com";
        doThrow(new IllegalArgumentException("Board not found")).when(boardService).shareBoard(boardId, email);

        assertThrows(IllegalArgumentException.class, () -> boardController.shareBoardByUserId(boardId, email));
    }
}