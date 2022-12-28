package service;


import TaskManager.entities.Board;
import TaskManager.entities.Item;
import TaskManager.entities.TaskStatus;
import TaskManager.entities.User;
import TaskManager.entities.entitiesUtils.FilterItem;
import TaskManager.entities.entitiesUtils.ItemTypes;
import TaskManager.entities.entitiesUtils.UserRole;
import TaskManager.entities.responseEntities.ItemDTO;
import TaskManager.repository.BoardRepository;
import TaskManager.repository.ItemRepository;
import TaskManager.repository.UserRepository;
import TaskManager.service.ItemService;
import TaskManager.utils.filter.QueryBuilder;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Example;

import java.time.LocalDate;
import java.util.*;

import static TaskManager.entities.entitiesUtils.ItemTypes.BUG;
import static TaskManager.entities.entitiesUtils.UserRole.ROLE_USER;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private ItemService itemService;
    private User user;
    private Board goodBoard;
    private Board badBoard;
    private Item item;

    private List<Item> itemList;
    private Set<ItemTypes> typeSet;
    private Map<Integer, UserRole> usersRoles;
    private Set<User> UserOnB;

    @BeforeEach
    @DisplayName("Make sure all the correct parameters are refreshed after each operation")
    public void setUp() {
        user = new User();
        user.setEmail("forTest@gmail.com");
        user.setId(1);
        user.setUsername("nameTest");
        user.setUserRole(ROLE_USER);
        goodBoard = new Board();
        goodBoard.setTitle("title");
        goodBoard.setId(1);

        usersRoles = new HashMap<>();
        usersRoles.put(1, ROLE_USER);
        goodBoard.setUsersRoles(usersRoles);
        //UserOnB.add(user);
        //goodBoard.setUsersOnBoard(UserOnB);
        badBoard = new Board();
        item = new Item();
        item.setId(2);
        item.setStatusId(1);
        TaskStatus Status = new TaskStatus(1, "taskStatus", goodBoard);
    }
    @Test
    public void testFilter_noMatchingItems() {
        Item item1 = new Item();
        item1.setBoardId(1);
        item1.setStatusId(1);
        item1.setDueDate(LocalDate.now());
        item1.setImportance(1);

        Item item2 = new Item();
        item2.setBoardId(1);
        item2.setStatusId(2);
        item2.setDueDate(LocalDate.now());
        item2.setImportance(2);

        List<Item> items = Arrays.asList(item1, item2);
        FilterItem filter1 = new FilterItem();

        QueryBuilder queryBuilder = new QueryBuilder(filter1);

        when(itemRepository.findAll(queryBuilder)).thenReturn(items);

        FilterItem filter = new FilterItem();
        filter.setBoardId(1);
        filter.setStatusId(3);
        filter.setDueDate(LocalDate.now().plusDays(1));
        filter.setImportance(3);

        List<ItemDTO> result = itemService.filter(filter);

        assertEquals(0, result.size());
    }
















    @Test
    @Disabled
    @DisplayName("filter successfully")
    public void filter_successfully() {
        FilterItem filterItem = new FilterItem(BUG, 5, LocalDate.now(), 1, 1);
        QueryBuilder queryBuilder = new QueryBuilder(filterItem);
        given(itemRepository.findAll(queryBuilder)).willReturn(Mockito.any(List.class));

        assertNotNull(itemService.filter(filterItem));
    }
    @Test
    @Disabled
    @DisplayName("addNewItem successfully")
    public void addNewItem_successfully() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(boardRepository.findById(1)).thenReturn(Optional.of(goodBoard));

        given(boardRepository.save(Mockito.any(Board.class))).willReturn(Mockito.any(Board.class));
        Item toSave = new Item();
        toSave.setCreator(user);
        toSave.setBoardId(item.getBoardId());
        toSave.setTitle(item.getTitle());
        toSave.setStatusId(item.getStatusId());
        given(itemRepository.save(Mockito.any(Item.class))).willReturn(Mockito.any(Item.class));

        Item item1 = new Item();
        item1.setId(2);
        item1.setTitle("a");
        assertNotNull(itemService.addNewItem(item1));
    }
    @Test
    @Disabled
    @DisplayName("assignItemTo successfully")
    public void assignItemTo_successfully() {
        given(boardRepository.findById(1)).willReturn(Optional.of(goodBoard));
        given(itemRepository.findById(2)).willReturn(Optional.of(item));
        given(itemRepository.save(item)).willReturn(Mockito.any(Item.class));
        user = new User();
        user.setEmail("forTest@gmail.com");
        user.setId(1);
        user.setUsername("nameTest");
        user.setUserRole(ROLE_USER);

        assertNotNull(itemService.assignItemTo(2,1,1));
    }

}
