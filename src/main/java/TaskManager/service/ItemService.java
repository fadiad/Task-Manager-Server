package TaskManager.service;

import TaskManager.entities.Board;
import TaskManager.entities.Comment;
import TaskManager.entities.Item;
import TaskManager.entities.User;
import TaskManager.entities.entitiesUtils.FilterItem;
import TaskManager.entities.entitiesUtils.UserRole;
import TaskManager.entities.responseEntities.ItemDTO;
import TaskManager.repository.BoardRepository;
import TaskManager.repository.ItemRepository;
import TaskManager.repository.UserRepository;
import TaskManager.utils.filter.QueryBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import javax.naming.NoPermissionException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
@AllArgsConstructor
public class ItemService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final ItemRepository itemRepository;

    //------------------------------------
    public List<ItemDTO> filter(FilterItem filter) {
        QueryBuilder queryBuilder = new QueryBuilder(filter);
        return itemRepository.findAll(queryBuilder).stream().map(ItemDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public ItemDTO addNewItem(Item newItem,int boardId) {
        System.out.println("new item : " + newItem);

        User createUser = userRepository.findById(newItem.getCreator().getId()).orElseThrow(() -> new IllegalArgumentException("User not found !"));

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("Board is not existed !"));

        boolean setExistedOnBoard = board.getStatues().stream().anyMatch(status -> status.getId() == newItem.getStatusId());

        if (!setExistedOnBoard)
            throw new IllegalArgumentException("status is not existed in board !");

        Item toSave = new Item();

        toSave.setCreator(createUser);
        toSave.setBoardId(newItem.getBoardId());
        toSave.setTitle(newItem.getTitle());
        toSave.setStatusId(newItem.getStatusId());
        System.out.println("toSave : " + toSave);
        return new ItemDTO(itemRepository.save(toSave));
    }

    //TODO DONE
    public ItemDTO assignItemTo(int itemId, int userId, int boardId) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("Board not found"));

        if (board.getUsersRoles().get(userId) == null) {
            throw new IllegalArgumentException("User is not in the board users list");
        }

        User userToAssign = board.getUsersOnBoard().stream()
                .filter(u -> u.getId() == userId)
                .findAny().orElseThrow(() -> new IllegalArgumentException("user not found "));

        Item itemToAssign = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Item not found"));

        itemToAssign.setAssignTo(userToAssign);
        return new ItemDTO(itemRepository.save(itemToAssign));
    }

    //TODO DONE
    public ItemDTO updateItem(int itemId, Item updatedItem, UserRole userRole) throws NoPermissionException {
        Item oldItem = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Item not found"));

        System.out.println("this is the item  : " + updatedItem);

        if (userRole == UserRole.ROLE_LEADER) {
            if (!oldItem.getDueDate().equals(updatedItem.getDueDate()) ||
                    oldItem.getImportance() != updatedItem.getImportance()) {
                throw new NoPermissionException("You have no permission !");
            }
        }

        oldItem.setItem(updatedItem);
        return new ItemDTO(itemRepository.save(oldItem));
    }

    // TODO DONE
    @Transactional
    public void deleteItem(int itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("Item not found"));
        itemRepository.deleteById(itemId);
    }

    public Comment addComment(int itemId, int userId, Comment comment) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("Item not found"));
        Board board = boardRepository.findById(item.getBoardId()).orElseThrow(() -> new EntityNotFoundException("board not found"));

        User user = board.getUsersOnBoard()
                .stream()
                .filter(u -> u.getId() == userId)
                .findFirst().orElseThrow(() -> new EntityNotFoundException("user not found"));

        comment.setUsername(user.getUsername());
        comment.setDate(LocalDate.now());
        comment.setTime(LocalTime.now());

        item.getStatues().add(comment);
        itemRepository.save(item);
        return comment;
    }
}
