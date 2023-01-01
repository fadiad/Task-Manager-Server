package TaskManager.service;

import TaskManager.entities.*;
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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final ItemRepository itemRepository;

    //------------------------------------

    /**
     * filter the items by specific property he gets.
     *
     * @param filter contain types to the filter.
     * @return list of filter items.
     */
    public List<ItemDTO> filter(FilterItem filter) {
        QueryBuilder queryBuilder = new QueryBuilder(filter);
        return itemRepository.findAll(queryBuilder).stream().map(ItemDTO::createItemDTO).collect(Collectors.toList());
    }

    /**
     * add new item.
     *
     * @param newItem new item
     * @return the item after he added the item into the repository.
     */
    @Transactional
    public ItemDTO addNewItem(Item newItem, int boardId) {
        User createUser = userRepository.findById(newItem.getCreator().getId()).orElseThrow(() -> new IllegalArgumentException("User not found !"));

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("Board is not existed !"));

        boolean setExistedOnBoard = board.getStatues().stream().anyMatch(status -> status.getId() == newItem.getStatusId());

        if (!setExistedOnBoard)
            throw new IllegalArgumentException("status is not existed in board !");

        Item toSave = Item.createItem(createUser,
                newItem.getBoardId(),
                newItem.getStatusId(),
                newItem.getTitle());

        return ItemDTO.createItemDTO(itemRepository.save(toSave));
    }


    /**
     * found the item in the board and set the AssignTo to the user he found.
     *
     * @param itemId  to find item
     * @param userId  to find user
     * @param boardId to find board
     * @return the item after updated.
     */
    @Transactional
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
     *
     * @param itemId      to find item.
     * @param updatedItem the new item.
     * @param userRole    of the user.
     * @return the new item after updated in itemDTO.
     * @throws NoPermissionException if user without permission try to update the item.
     */
    @Transactional
    public ItemDTO updateItem(int itemId, Item updatedItem, UserRole userRole) throws NoPermissionException {
        Item oldItem = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Item not found"));

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
     *
     * @param itemId to find the item.
     */
    @Transactional
    public Item deleteItem(int itemId) {
        Item item= itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("Item not found"));
        itemRepository.deleteById(itemId);
        return item;
    }

    /**
     * add comment to item in board by their id.
     *
     * @param itemId  to find item
     * @param userId  to find the user in comment.
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

        Comment toSave = Comment.createComment(user.getUsername());

        item.getStatues().add(toSave);
        itemRepository.save(item);
        return comment;
    }


    /**
     * @param boardId    to find the board.
     * @param itemId     to found the item.
     * @param taskStatus the new one, with the new name and old id.
     * @return get taskStatus and update him on the board he found.
     */
    @Transactional
    public ItemDTO  updateItemStatusToBoard(int boardId, int itemId, TaskStatus newStatus, TaskStatus oldStatus) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("board not found"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("item not found"));

        Set<TaskStatus> result = board.getStatues();
        if(!result.contains(newStatus) || !result.contains(oldStatus)) {
            throw new IllegalArgumentException("STATUS NOT FOUND");
        }else if (oldStatus.getId() != item.getStatusId()){
            throw new IllegalArgumentException("this item is not part of this status");
        }

        item.setStatusId(newStatus.getId());

        return  ItemDTO.createItemDTO(itemRepository.save(item));
    }
}
