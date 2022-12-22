package TaskManager.service;

import TaskManager.entities.Board;
import TaskManager.entities.Item;
import TaskManager.entities.entitiesUtils.ItemTypes;
import TaskManager.entities.responseEntities.BoardDetailsDTO;
import TaskManager.entities.responseEntities.ItemByStatusDTO;
import TaskManager.repository.BoardRepository;
import TaskManager.repository.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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

    public Board addNewBoard(Board board){
        System.out.println(itemTypesSet);

        return boardRepository.save(board);
    }

    public BoardDetailsDTO getBoardById(int boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(()->new IllegalArgumentException("no board found"));
        List<Item> allItemsByBoardId=itemRepository.findByBoardId(boardId);
        List<ItemByStatusDTO>itemFilteredByStatus=board.getStatues().stream().map(taskStatus -> new ItemByStatusDTO(taskStatus,allItemsByBoardId)).collect(Collectors.toList());
        System.out.println(itemFilteredByStatus);
        return new BoardDetailsDTO(board, itemFilteredByStatus);
    }
}
