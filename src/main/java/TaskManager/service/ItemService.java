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
     * add new item to board, get anew item details and board id, find the user of the new item, and the board from the repo,
     found the status if existed, else throw exception
     * @param newItem new item details (user id, boardId, title and more)
     * @return the itemDTO after he added the item into the repository.
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
     * get itemID, userID, boardID, found the item in the board and set the AssignTo
     to the user he found. if the user/board/item not found, throws exception,
     if success, set the assignItem to the user he gets
     * @param itemId  to find item
     * @param userId  to find user
     * @param boardId to find board
     * @return the itemDTO after updated. with specific details
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
     * get itemId to update  and the new item, and the user role, found the "old" item, check the role
     if ok, update the item in the repository, else, throws exception.
     * @param itemId to find the old item.
     * @param updatedItem the find the new item.
     * @param userRole of the user to check if he can update the item.
     * @return the new itemDTO after updated in item.
     * @throws NoPermissionException if user without permission try to update the item.
     */
    @Transactional
    public ItemDTO updateItem(int itemId, Item updatedItem, UserRole userRole) throws NoPermissionException {
        Item oldItem = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("Item not found"));
        if(oldItem.getId() != updatedItem.getId() || oldItem.getBoardId()!= updatedItem.getBoardId()
                || oldItem.getStatusId() !=updatedItem.getStatusId()){
            throw new IllegalArgumentException("You are trying to update invalid item");
        }
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
     * get id of item, and delete item by the id. if id not found  throws EntityNotFoundException
     keep changes on the repository
     * @param itemId to find the item.
     */
    @Transactional
    public Item  deleteItem(int itemId) {
        Item item= itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("Item not found"));
        itemRepository.deleteById(itemId);
        return item;
    }

    /**
     * add comment to item in board by the id.
     *get item/user id and full comment, fins the item, and if not exist throws exception. and found the user also,
     * if succeeded, add the comment to the item he found.
     * @param itemId  to find the item
     * @param userId  to find the user in comment.
     * @param comment the comment with the details.
     * @return the comment after adding into the item in board.
     */
    @Transactional
    public ItemDTO addComment(int itemId, int userId, Comment comment) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("Item not found"));
        Board board = boardRepository.findById(item.getBoardId()).orElseThrow(() -> new EntityNotFoundException("board not found"));

        User user = board.getUsersOnBoard()
                .stream()
                .filter(u -> u.getId() == userId)
                .findFirst().orElseThrow(() -> new EntityNotFoundException("user not found"));

        Comment toSave = Comment.createComment(user.getUsername(),comment.getContent());
        toSave.setItem(item);
        item.getComments().add(toSave);
        return ItemDTO.createItemDTO(itemRepository.save(item));
    }


    /**
     * get boardId, and itemId, and new and old taskStatus
     found the item on the board, and found the old status, and change the status to the new status he has,
     and keep the new item on the repository
     * @param boardId to find the board, else, throws exception
     * @param itemId to find the item, else, throws exception
     * @param newStatus to find the new status we want to update, else, throws exception
     * @param oldStatus to find the old status we want to update, else, throws exception
     * @return the new item after updated, as a itemDTO
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
