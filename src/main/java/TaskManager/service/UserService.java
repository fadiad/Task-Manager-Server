package TaskManager.service;

import TaskManager.entities.Board;
import TaskManager.entities.User;
import TaskManager.entities.entitiesUtils.BoardToReturn;
import TaskManager.entities.entitiesUtils.ItemTypes;
import TaskManager.repository.BoardRepository;
import TaskManager.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class UserService  {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final Set<ItemTypes> itemTypesSet=new HashSet<>(Arrays.asList(ItemTypes.values()));

    public Board addNewBoard(Board board , int userId){
        User user = userRepository.findById(userId).orElseThrow(()->  new IllegalArgumentException("user not found"));
        board.getUsersOnBoard().add(user);
        user.getBoards().add(board);
        return boardRepository.save(board);
    }


    public List<BoardToReturn> getUserBoards(int userId){
        System.out.println(userId);
        User user = userRepository.findById(userId).orElseThrow(()->  new IllegalArgumentException("user not found"));

        Set<Board> boards = user.getBoards();
//        Set<Board> boards = user.getBoards(); boards.stream().map(b -> b.getTitle() )

        List<BoardToReturn> boardsList = boards.stream()
                .map(b -> new BoardToReturn( b.getId(), b.getTitle()) )
                .collect(Collectors.toList());

        return boardsList ;
    }

//    public Board updateBoard(Board update){
//
//        return boardRepository.save(update);
//    }

    public void getAll() {
        //System.out.println(boardRepository.findAllStatuses());
    }

}
