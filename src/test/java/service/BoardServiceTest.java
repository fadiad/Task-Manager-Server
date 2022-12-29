package service;

import TaskManager.entities.Board;
import TaskManager.entities.Item;
import TaskManager.entities.TaskStatus;
import TaskManager.entities.User;

import TaskManager.entities.entitiesUtils.ItemTypes;
import TaskManager.entities.entitiesUtils.UserRole;
import TaskManager.entities.responseEntities.BoardDetailsDTO;
import TaskManager.entities.responseEntities.BoardToReturn;
import TaskManager.repository.BoardRepository;
import TaskManager.repository.ItemRepository;
import TaskManager.repository.UserRepository;
import TaskManager.service.BoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;

import java.util.*;

import static TaskManager.entities.entitiesUtils.ItemTypes.BUG;
import static TaskManager.entities.entitiesUtils.ItemTypes.TASK;
import static TaskManager.entities.entitiesUtils.UserRole.ROLE_USER;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private BoardService boardService;
    @Mock
    private UserRepository userRepository;

    private User user;
    private Board goodBoard;
    private Board badBoard;
    private Item item;

    @BeforeEach
    @DisplayName("Make sure all the correct parameters are refreshed after each operation")
    public void setUp() {
        user = new User();
        user.setEmail("forTest@gmail.com");
        user.setId(1);
        user.setUsername("nameTest");
        goodBoard = new Board();
        goodBoard.setTitle("title");
        goodBoard.setId(1);
        badBoard = new Board();
        item = new Item();
        item.setId(2);
        item.setStatusId(1);
        TaskStatus Status = new TaskStatus(1, "taskStatus", goodBoard);
        Set<ItemTypes> typeSet = new HashSet<>();
    }

    @Test
    @DisplayName("add item to board successfully")
    public void addNewBoard_successfully() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        Board toSave = new Board();
        toSave.setTitle(goodBoard.getTitle());
        toSave.setItemTypes(goodBoard.getItemTypes());
        toSave.getUsersRoles().put(user.getId(), UserRole.ROLE_ADMIN);
        toSave.getUsersOnBoard().add(user);
        given(boardRepository.save(Mockito.any(Board.class))).willReturn(goodBoard);
        BoardToReturn boardToReturn = new BoardToReturn(goodBoard);
        System.out.println(toSave);
        assertEquals(boardService.addNewBoard(goodBoard, user.getId()).getId(), (boardToReturn.getId()));
    }

    @Test
    @DisplayName("get board by id successfully")
    public void getBoardById_successfully() {
        when(boardRepository.findById(1)).thenReturn(Optional.of(goodBoard));
        goodBoard.getUsersRoles().put(1, ROLE_USER);
        when(itemRepository.findByBoardId(1)).thenReturn(Mockito.any(List.class));
        BoardDetailsDTO boardDetailsDTO = new BoardDetailsDTO();
        boardDetailsDTO.setBoard(goodBoard);
        assertEquals(boardService.getBoardById(1, 1).getBoard(), boardDetailsDTO.getBoard());
    }

    @Test
    @DisplayName("getUserBoards successfully")
    public void getUserBoards_successfully() {
        given(userRepository.findById(1)).willReturn(Optional.of(user));
        assertNotNull(boardService.getUserBoards(user.getId()));
    }

    @Test
    @DisplayName("addNewStatusToBoard successfully")
    public void addNewStatusToBoard_successfully() {
        TaskStatus taskStatus = new TaskStatus(11, "taskStatus", goodBoard);
        when(boardRepository.findById(1)).thenReturn(Optional.of(goodBoard));
        given(boardRepository.save(Mockito.any(Board.class))).willReturn(goodBoard);
        assertTrue(boardService.addNewStatusToBoard(1, taskStatus).getTitle() == goodBoard.getTitle());
    }

//    @Test
//    @Disabled
//    @DisplayName("deleteItemTypeOnBoardd successfully")
//    public void deleteItemTypeOnBoard_successfully() {
//        when(boardRepository.findById(1)).thenReturn(Optional.of(goodBoard));
//        when(itemRepository.findByBoardId(1)).thenReturn(Mockito.any(List.class));
//        Set<ItemTypes> typeSet = new HashSet<>();
//        typeSet.add(TASK);
//
//        when(itemRepository.saveAll(Mockito.anyList())).thenReturn(Mockito.anyList());
//        given(boardRepository.save(Mockito.any(Board.class))).willReturn(goodBoard);
//
//        assertNotNull(boardService.deleteItemTypeOnBoard(1, typeSet));
//    }

//    @Test
//    @DisplayName("addItemTypeOnBoard successfully")
//    public void addItemTypeOnBoard_successfully() {
//        when(boardRepository.findById(1)).thenReturn(Optional.of(goodBoard));
//        given(boardRepository.save(Mockito.any(Board.class))).willReturn(goodBoard);
//        Set<ItemTypes> typeSet = new HashSet<>();
//        typeSet.add(BUG);
//
//        assertNotNull(boardService.addItemTypeOnBoard(1, typeSet));
//    }

    @Test
    @DisplayName("updateItemStatusToBoard successfully")
    public void updateItemStatusToBoard_successfully() {
        when(boardRepository.findById(1)).thenReturn(Optional.of(goodBoard));
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        given(itemRepository.save(Mockito.any(Item.class))).willReturn(item);
        given(boardRepository.save(Mockito.any(Board.class))).willReturn(goodBoard);
        TaskStatus taskStatus = new TaskStatus(1, "BUG", goodBoard);
        goodBoard.getStatues().add(taskStatus);
        assertNotNull(boardService.updateItemStatusToBoard(1, 1, taskStatus));

    }

    @Test
    public void testDeleteStatus_Success() {
        // Set up the test data
        int boardId = 1;
        int statusId = 1;
        Board board = new Board();
        board.setId(boardId);
        TaskStatus taskStatus = new TaskStatus();
        taskStatus.setId(statusId);
        board.getStatues().add(taskStatus);
        // Set up the mock behavior
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        doNothing().when(itemRepository).deleteByStatusId(statusId);
        // Invoke the method under test
        boardService.deleteStatus(boardId, statusId);
        // Verify the mock interactions
        verify(boardRepository).findById(boardId);
        verify(itemRepository).deleteByStatusId(statusId);
    }

    @Test
    public void testDeleteStatus_BoardNotFound() {
        int boardId = 1;
        int statusId = 1;
        when(boardRepository.findById(boardId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> boardService.deleteStatus(boardId, statusId));

        verify(boardRepository).findById(boardId);
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    public void testGetBoardById_Success() {
        int boardId = 1;
        int userId = 1;
        User user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
        Board board = new Board();
        board.setId(boardId);
        board.setTitle("Test Board");
        board.getUsersRoles().put(userId, UserRole.ROLE_ADMIN);
        board.getUsersOnBoard().add(user);
        // Set up the mock behavior
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        BoardDetailsDTO result = boardService.getBoardById(boardId, userId);

        assertEquals(boardId, result.getBoard().getId());
        assertEquals("Test Board", result.getBoard().getTitle());
        assertEquals(1, result.getBoard().getUsersRoles().size());
        assertEquals(UserRole.ROLE_ADMIN, result.getBoard().getUsersRoles().get(userId));
        assertEquals(1, result.getUsersOnBoard().size());
        assertEquals("test@example.com", result.getUsersOnBoard().get(0).getEmail());
        verify(boardRepository).findById(boardId);
    }

    @Test
    public void testGetBoardById_NotFound() {
        int boardId = 1;
        int userId = 1;
        when(boardRepository.findById(boardId)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> boardService.getBoardById(boardId, userId));
        verify(boardRepository).findById(boardId);
    }
}