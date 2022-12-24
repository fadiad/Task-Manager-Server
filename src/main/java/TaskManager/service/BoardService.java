package TaskManager.service;

import TaskManager.entities.Board;
import TaskManager.entities.Item;
import TaskManager.entities.TaskStatus;
import TaskManager.entities.User;
import TaskManager.entities.entitiesUtils.ItemTypes;
import TaskManager.entities.entitiesUtils.UserRole;
import TaskManager.entities.responseEntities.BoardDetailsDTO;
import TaskManager.entities.responseEntities.BoardToReturn;
import TaskManager.entities.responseEntities.ItemByStatusDTO;
import TaskManager.repository.BoardRepository;
import TaskManager.repository.ItemRepository;
import TaskManager.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final Set<ItemTypes> itemTypesSet=new HashSet<>(Arrays.asList(ItemTypes.values()));
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public void deleteStatus(int boardId, int statusId) {
        Board board = boardRepository.findById(boardId).orElseThrow(()-> new EntityNotFoundException("Board not found"));
       // board.getStatues().stream().f
    }

    @Transactional
    public BoardToReturn addNewBoard(Board board , int userId){
        User user = userRepository.findById(userId).orElseThrow(()->  new IllegalArgumentException("user not found"));

        Board toSave = new Board();
        toSave.setTitle(board.getTitle());
//        toSave.getStatues().add(board.getStatues()); //TODO
        toSave.getUsersRoles().put(user.getId(), UserRole.ROLE_ADMIN);
        toSave.getUsersOnBoard().add(user);
        user.getBoards().add(toSave);

        return new BoardToReturn(boardRepository.save(toSave));
    }

    public BoardDetailsDTO getBoardById(int boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(()->new IllegalArgumentException("no board found"));
        List<Item> allItemsByBoardId=itemRepository.findByBoardId(boardId);
        List<ItemByStatusDTO>itemFilteredByStatus=board.getStatues().stream().map(taskStatus -> new ItemByStatusDTO(taskStatus,allItemsByBoardId)).collect(Collectors.toList());
        return new BoardDetailsDTO(board, itemFilteredByStatus);
    }

    public List<BoardToReturn> getUserBoards(int userId){
        System.out.println(userId);
        User user = userRepository.findById(userId).orElseThrow(()->  new IllegalArgumentException("user not found"));

        return user.getBoards().stream()
                .map(BoardToReturn::new)
                .collect(Collectors.toList());
    }
    @Transactional
    public Board addNewStatusToBoard(int boardId, TaskStatus taskStatus) {
        Board board = boardRepository.findById(boardId).orElseThrow(()-> new IllegalArgumentException("board not found"));
        board.getStatues().add(taskStatus);
        return boardRepository.save(board);
    }
}
