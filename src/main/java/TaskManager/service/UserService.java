package TaskManager.service;

import TaskManager.entities.Board;
import TaskManager.entities.entitiesUtils.ItemTypes;
import TaskManager.repository.BoardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


@Service
@AllArgsConstructor
public class UserService  {

    private final BoardRepository boardRepository;

    private final Set<ItemTypes> itemTypesSet=new HashSet<>(Arrays.asList(ItemTypes.values()));
    public Board addNewBoard(Board board){
        System.out.println(itemTypesSet);

        return boardRepository.save(board);
    }

    public Board updateBoard(Board update){

        return boardRepository.save(update);
    }

    public void getAll() {
        //System.out.println(boardRepository.findAllStatuses());
    }
}
