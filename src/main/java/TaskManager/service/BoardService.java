package TaskManager.service;

import TaskManager.entities.Board;
import TaskManager.entities.Item;
import TaskManager.entities.TaskStatus;
import TaskManager.entities.User;
import TaskManager.entities.entitiesUtils.ItemTypes;
import TaskManager.entities.responseEntities.BoardDetailsDTO;
import TaskManager.entities.responseEntities.BoardToReturn;
import TaskManager.entities.responseEntities.ItemByStatusDTO;
import TaskManager.repository.BoardRepository;
import TaskManager.repository.ItemRepository;
import TaskManager.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final Set<ItemTypes> itemTypesSet=new HashSet<>(Arrays.asList(ItemTypes.values()));
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;


    public BoardToReturn addNewBoard(Board board , int userId){
        User user = userRepository.findById(userId).orElseThrow(()->  new IllegalArgumentException("user not found"));
        board.getUsersOnBoard().add(user);
        user.getBoards().add(board);
        return new BoardToReturn(boardRepository.save(board));
    }

    public BoardDetailsDTO getBoardById(int boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(()->new IllegalArgumentException("no board found"));
        List<Item> allItemsByBoardId=itemRepository.findByBoardId(boardId);
        List<ItemByStatusDTO>itemFilteredByStatus=board.getStatues().stream().map(taskStatus -> new ItemByStatusDTO(taskStatus,allItemsByBoardId)).collect(Collectors.toList());
        System.out.println(itemFilteredByStatus);
        return new BoardDetailsDTO(board, itemFilteredByStatus);
    }

    public List<BoardToReturn> getUserBoards(int userId){
        System.out.println(userId);
        User user = userRepository.findById(userId).orElseThrow(()->  new IllegalArgumentException("user not found"));

        return user.getBoards().stream()
                .map(BoardToReturn::new)
                .collect(Collectors.toList());
    }
    public void addNewStatusToBoard(int boardId, TaskStatus taskStatus) {
        Board board = boardRepository.findById(boardId).orElseThrow(()-> new IllegalArgumentException("board not found"));
        board.getStatues().add(taskStatus);
        boardRepository.save(board);
    }
}
