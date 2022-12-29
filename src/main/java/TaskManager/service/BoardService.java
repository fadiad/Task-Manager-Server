package TaskManager.service;

import TaskManager.entities.Board;
import TaskManager.entities.Item;
import TaskManager.entities.TaskStatus;
import TaskManager.entities.User;
import TaskManager.entities.entitiesUtils.ItemTypes;
import TaskManager.entities.entitiesUtils.UserRole;
import TaskManager.entities.responseEntities.BoardDetailsDTO;
import TaskManager.entities.responseEntities.BoardToReturn;
import TaskManager.entities.responseEntities.ItemDTO;
import TaskManager.entities.responseEntities.UserDTO;
import TaskManager.repository.BoardRepository;
import TaskManager.repository.ItemRepository;
import TaskManager.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;


    /**
     * (void).
     * the function found the board by id and delete the status from the board.
     *
     * @param boardId  board id.
     * @param statusId to find task status.
     */
    @Transactional
    public void deleteStatus(int boardId, int statusId) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("Board not found"));
        Optional<TaskStatus> deletedStatus = board.getStatues().stream().filter(taskStatus -> taskStatus.getId() == statusId).findFirst();
        if (!deletedStatus.isPresent()) {
            throw new IllegalArgumentException("This status is not on this board");
        }
        board.getStatues().remove(deletedStatus.get());
        itemRepository.deleteByStatusId(statusId);
    }

    /**
     * (BoardToReturn).
     * add new board to user by board and userId.
     *
     * @param board  board.
     * @param userId user id. if user not fount throw exception.
     * @return BoardToReturn that contain the board.
     */
    @Transactional
    public BoardToReturn addNewBoard(Board board, int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("user not found"));
        Board toSave = new Board();
        toSave.setTitle(board.getTitle());
        toSave.setItemTypes(board.getItemTypes());
        toSave.getUsersRoles().put(user.getId(), UserRole.ROLE_ADMIN);
        toSave.getUsersOnBoard().add(user);
        user.getBoards().add(toSave);
        return new BoardToReturn(boardRepository.save(toSave));
    }

    /**
     * get board by id.
     *
     * @param boardId board id
     * @param userId  user id
     * @return the board, found the board by the userId and board by id
     */
    @Transactional
    public BoardDetailsDTO getBoardById(int boardId, int userId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("no board found"));
        if (!board.getUsersRoles().containsKey(userId)) {
            throw new IllegalArgumentException("this user not on board");
        }

        List<UserDTO> useresOnBoard = board.getUsersOnBoard().stream().map(UserDTO::new).collect(Collectors.toList());

        List<Item> allItemsByBoardId = itemRepository.findByBoardId(boardId);

        Map<Integer, List<ItemDTO>> itemFilteredByStatus = board.getStatues().stream().collect(Collectors.toMap(TaskStatus::getId, taskStatus -> filterItemsByStatus(taskStatus.getId(), allItemsByBoardId)));

        return new BoardDetailsDTO(board, itemFilteredByStatus, useresOnBoard);
    }

    /**
     * filter the items by the statuses un the board.
     *
     * @param statusId status id in a board id.
     * @param allItems list of all items.
     * @return list of items in status x.
     */
    private List<ItemDTO> filterItemsByStatus(int statusId, List<Item> allItems) {
        return allItems.stream().filter(item -> item.getStatusId() == statusId).map(ItemDTO::createItemDTO).collect(Collectors.toList());
    }

    /***
     * get user boards.
     * @param userId user id.
     * @return all boards of the user bt user id.
     */
    @Transactional
    public List<BoardToReturn> getUserBoards(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("user not found"));

        return user.getBoards().stream()
                .map(BoardToReturn::new)
                .collect(Collectors.toList());
    }

    /**
     * add status to board.
     *
     * @param boardId    board id.
     * @param taskStatus contain id and name of the status
     * @return the new board after adding the status
     */
    @Transactional
    public Board addNewStatusToBoard(int boardId, TaskStatus taskStatus) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("board not found"));
        taskStatus.setBoard(board);
        board.getStatues().add(taskStatus);
        System.out.println(boardRepository.save(board));
        return boardRepository.save(board);
    }

    /**
     * delete item types from the board.
     *
     * @param boardId  board id.
     * @param itemType item type he wants to delete.
     * @return the new board after changes.
     */
    @Transactional
    public Board deleteItemTypeOnBoard(int boardId, ItemTypes itemType) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("Board not found"));
        Set<ItemTypes> types = board.getItemTypes();
        List<Item> items = itemRepository.findByBoardId(boardId);

        if (!types.remove(itemType))
            throw new IllegalArgumentException("No such type on this board");

        items.forEach(item -> {
            if (item.getItemType() == itemType) {
                item.setItemType(null);
            }
        });

        itemRepository.saveAll(items);
        return boardRepository.save(board);
    }

    /**
     * add item types to the board.
     *
     * @param boardId board id.
     * @param itemType set of types.
     * @return the new board after changes.
     */
    @Transactional
    public Board addItemTypeOnBoard(int boardId, ItemTypes itemType) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("Board not found"));

        System.out.println(board.getItemTypes().add(itemType));
        return boardRepository.save(board);
    }


    /**
     * @param boardId    to find the board.
     * @param itemId     to found the item.
     * @param taskStatus the new one, with the new name and old id.
     * @return get taskStatus and update him on the board he found.
     */
    @Transactional
    public Board updateItemStatusToBoard(int boardId, int itemId, TaskStatus taskStatus) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("board not found"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("item not found"));
        int statusId = item.getStatusId();
        Set<TaskStatus> result = board.getStatues();

        for (TaskStatus task : result) {
            if (task.getId() == statusId) {
                task.setName(taskStatus.getName());
                itemRepository.save(item);
                boardRepository.save(board);
                return board;
            }
        }
        throw new IllegalArgumentException("STATUS NOT FOUND");
    }


    /**
     * to share board.
     *
     * @param boardId to find the board.
     * @param email   the user to share with.
     * @return the user after he adds to him the board to share.
     */
    @Transactional
    public UserDTO shareBoard(int boardId, String email) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("board not found"));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("user not found"));
        if (board.getUsersRoles().containsKey(user.getId())) {
            throw new IllegalArgumentException("user already exist on the board");
        }
        board.getUsersOnBoard().add(user);
        user.getBoards().add(board);
        return new UserDTO(userRepository.save(user));
    }
}
