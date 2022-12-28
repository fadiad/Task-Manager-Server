//package service;
//
//import TaskManager.entities.Board;
//import TaskManager.entities.Item;
//import TaskManager.entities.TaskStatus;
//import TaskManager.entities.User;
////
//import TaskManager.entities.entitiesUtils.ItemTypes;
//import TaskManager.entities.entitiesUtils.UserRole;
//import TaskManager.entities.responseEntities.BoardDetailsDTO;
//import TaskManager.entities.responseEntities.BoardToReturn;
//import TaskManager.repository.BoardRepository;
//import TaskManager.repository.ItemRepository;
//import TaskManager.repository.UserRepository;
//import TaskManager.service.BoardService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import javax.persistence.EntityNotFoundException;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//
//import static TaskManager.entities.entitiesUtils.ItemTypes.BUG;
//import static TaskManager.entities.entitiesUtils.ItemTypes.TASK;
//import static TaskManager.entities.entitiesUtils.UserRole.ROLE_USER;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//public class BoardServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private BoardRepository boardRepository;
//    @Mock
//    private ItemRepository itemRepository;
//    @InjectMocks
//    private BoardService boardService;
//    private User user;
//    private Board goodBoard;
//    private Board badBoard;
//    private Item item;
//    private List<Item> itemList;
//    private Set<ItemTypes> typeSet;
//
//    @BeforeEach
//    @DisplayName("Make sure all the correct parameters are refreshed after each operation")
//    public void setUp() {
//        user = new User();
//        user.setEmail("forTest@gmail.com");
//        user.setId(1);
//        user.setUsername("nameTest");
//        goodBoard = new Board();
//        goodBoard.setTitle("title");
//        goodBoard.setId(1);
//        badBoard = new Board();
//        item = new Item();
//        item.setId(2);
//        item.setStatusId(1);
//        TaskStatus Status = new TaskStatus(1, "taskStatus", goodBoard);
//        Set<ItemTypes> typeSet = new HashSet<>();
//    }
//
//    @Test
//    @DisplayName("delete status not successfully")
//    public void deleteStatus_Not_successfully() {
//        TaskStatus taskStatus = new TaskStatus(1, "taskStatus", goodBoard);
//        assertThrows(EntityNotFoundException.class, () -> boardService.deleteStatus(2, taskStatus));
//    }
//
//    @Test
//    @DisplayName("add item to board successfully")
//    public void addNewBoard_successfully() {
//        when(userRepository.findById(1)).thenReturn(Optional.of(user));
//        Board toSave = new Board();
//        toSave.setTitle(goodBoard.getTitle());
//        toSave.setItemTypes(goodBoard.getItemTypes());
//        toSave.getUsersRoles().put(user.getId(), UserRole.ROLE_ADMIN);
//        toSave.getUsersOnBoard().add(user);
//
//        given(boardRepository.save(Mockito.any(Board.class))).willReturn(goodBoard);
//        BoardToReturn boardToReturn = new BoardToReturn(goodBoard);
//        System.out.println(toSave);
//        assertEquals(boardService.addNewBoard(goodBoard, user.getId()).getId(), (boardToReturn.getId()));
//    }
//
//    @Test
//    @DisplayName("get board by id successfully")
//    public void getBoardById_successfully() {
//        when(boardRepository.findById(1)).thenReturn(Optional.of(goodBoard));
//        goodBoard.getUsersRoles().put(1, ROLE_USER);
//        when(itemRepository.findByBoardId(1)).thenReturn(Mockito.any(List.class));
//        BoardDetailsDTO boardDetailsDTO = new BoardDetailsDTO();
//        boardDetailsDTO.setBoard(goodBoard);
//        assertEquals(boardService.getBoardById(1, 1).getBoard(), boardDetailsDTO.getBoard());
//    }
//
//    @Test
//    @DisplayName("getUserBoards successfully")
//    public void getUserBoards_successfully() {
//        given(userRepository.findById(1)).willReturn(Optional.of(user));
//        assertNotNull(boardService.getUserBoards(user.getId()));
//    }
//
//    @Test
//    @DisplayName("addNewStatusToBoard successfully")
//    public void addNewStatusToBoard_successfully() {
//        TaskStatus taskStatus = new TaskStatus(11, "taskStatus", goodBoard);
//        when(boardRepository.findById(1)).thenReturn(Optional.of(goodBoard));
//        given(boardRepository.save(Mockito.any(Board.class))).willReturn(goodBoard);
//        assertTrue(boardService.addNewStatusToBoard(1, taskStatus).getTitle() == goodBoard.getTitle());
//    }
//
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
//
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
//
//    @Test
//    @DisplayName("updateItemStatusToBoard successfully")
//    public void updateItemStatusToBoard_successfully() {
//        when(boardRepository.findById(1)).thenReturn(Optional.of(goodBoard));
//        when(itemRepository.findById(1)).thenReturn(Optional.of(item));
//        given(itemRepository.save(Mockito.any(Item.class))).willReturn(item);
//        given(boardRepository.save(Mockito.any(Board.class))).willReturn(goodBoard);
//        TaskStatus taskStatus = new TaskStatus(1, "BUG", goodBoard);
//        goodBoard.getStatues().add(taskStatus);
//        assertNotNull(boardService.updateItemStatusToBoard(1, 1, taskStatus));
//
//    }
//}
