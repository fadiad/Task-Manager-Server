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
    /**
     * filter the items by specific property he gets.
     * @param filter contain types to the filter.
     * @return list of filter items.
     */
    public List<ItemDTO> filter(FilterItem filter) {
        QueryBuilder queryBuilder = new QueryBuilder(filter);
        return itemRepository.findAll(queryBuilder).stream().map(ItemDTO::createItemDTO).collect(Collectors.toList());
    }

    /**
     * add new item.
     * @param newItem new item
     * @return the item after he added the item into the repository.
     */
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
        return ItemDTO.createItemDTO(itemRepository.save(toSave));
    }

    /**
     *  found the item in the board and set the AssignTo to the user he found.
     * @param itemId to find item
     * @param userId to find user
     * @param boardId to find board
     * @return the item after updated.
     */
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
        return ItemDTO.createItemDTO(itemRepository.save(itemToAssign));
    }

    /**
     * update item.
     * @param itemId to find item.
     * @param updatedItem the new item.
     * @param userRole of the user.
     * @return the new item after updated in itemDTO.
     * @throws NoPermissionException if user without permission try to update the item.
     */
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
        return ItemDTO.createItemDTO(itemRepository.save(oldItem));
    }

    /**
     * delete item by the id.
     * @param itemId to find the item.
     */
    @Transactional
    public void deleteItem(int itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("Item not found"));
        itemRepository.deleteById(itemId);
    }

    /**
     * add comment to item in board by their id.
     * @param itemId to find item
     * @param userId to find the user in comment.
     * @param comment the comment with the details.
     * @return the comment after adding into the item in board.
     */
    public Comment addComment(int itemId, int userId, Comment comment) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("Item not found"));
        Board board = boardRepository.findById(item.getBoardId()).orElseThrow(() -> new EntityNotFoundException("board not found"));

        User user = board.getUsersOnBoard()
                .stream()
                .filter(u -> u.getId() == userId)
                .findFirst().orElseThrow(() -> new EntityNotFoundException("user not found"));
        comment.createComment(user.getUsername());
        //comment.setUsername(user.getUsername());
        // comment.setDate(LocalDate.now());
        // comment.setTime(LocalTime.now());
        item.getStatues().add(comment);
        itemRepository.save(item);
        return comment;
    }
}
