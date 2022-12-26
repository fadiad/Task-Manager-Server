package TaskManager.service;

import TaskManager.entities.*;
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

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    //TODO done
    @Transactional
    public void deleteStatus(int boardId, TaskStatus status) {
        System.out.println("status : " + status);
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("Board not found"));
        boolean removedStatus = board.getStatues().remove(status);
        if (!removedStatus) {
            throw new IllegalArgumentException("This status is not on this board");
        }
        itemRepository.deleteByStatusId(status.getId());
    }

    //TODO  this is done
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

    //TODO  this is done
    public BoardDetailsDTO getBoardById(int boardId, int userId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("no board found"));
        if (!board.getUsersRoles().containsKey(userId)) {
            throw new IllegalArgumentException("this user not on board");
        }
//        System.out.println(board.getUsersOnBoard());

        List<UserDTO> useresOnBoard = board.getUsersOnBoard().stream().map(UserDTO::new).collect(Collectors.toList());

        List<Item> allItemsByBoardId = itemRepository.findByBoardId(boardId);

        Map<Integer, List<ItemDTO>> itemFilteredByStatus = board.getStatues().stream().collect(Collectors.toMap(TaskStatus::getId, taskStatus -> filterItemsByStatus(taskStatus.getId(), allItemsByBoardId)));

        return new BoardDetailsDTO(board, itemFilteredByStatus , useresOnBoard);
    }

    private List<ItemDTO> filterItemsByStatus(int statusId, List<Item> allItems) {
        return allItems.stream().filter(item -> item.getStatusId() == statusId).map(ItemDTO::new).collect(Collectors.toList());
    }

    //TODO  this is done
    public List<BoardToReturn> getUserBoards(int userId) {
        System.out.println(userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("user not found"));

        return user.getBoards().stream()
                .map(BoardToReturn::new)
                .collect(Collectors.toList());
    }

    @Transactional//TODO  this is done
    public Board addNewStatusToBoard(int boardId, TaskStatus taskStatus) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("board not found"));
        taskStatus.setBoard(board);
        board.getStatues().add(taskStatus);
        return boardRepository.save(board);
    }

    //TODO done
    @Transactional
    public Board deleteItemTypeOnBoard(int boardId, Set<ItemTypes> typeSet) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("Board not found"));
        Set<ItemTypes> types = board.getItemTypes();
        List<Item> items = itemRepository.findByBoardId(boardId);

        for (ItemTypes itemTypes : typeSet) {
            if (!types.remove(itemTypes))
                throw new IllegalArgumentException("No such type on this board");

              items.forEach(item -> {
                if (item.getItemType() == itemTypes) {
                    item.setItemType(null);
                }
            });
        }
        itemRepository.saveAll(items);
        return boardRepository.save(board);
    }

    @Transactional
    public Board addItemTypeOnBoard(int boardId, Set<ItemTypes> typeSet) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("Board not found"));
        for (ItemTypes itemTypes : typeSet) {
            board.getItemTypes().add(itemTypes);
        }
        return boardRepository.save(board);
    }
    @Transactional
    public Board updateItemStatusToBoard(int boardId, int itemId, TaskStatus taskStatus) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("board not found"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("item not found"));
        int statusId = item.getStatusId();
        Set<TaskStatus> result = board.getStatues();

        for (TaskStatus task: result) {
            if(task.getId()==statusId){
                task.setName(taskStatus.getName());
                itemRepository.save(item);
                boardRepository.save(board);
                return board;
            }
        }
        throw new IllegalArgumentException("STATUS NOT FOUND");
    }
}
