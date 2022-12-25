package TaskManager.controller;

import TaskManager.entities.Board;
import TaskManager.entities.Item;
import TaskManager.entities.TaskStatus;
import TaskManager.entities.requests.BoardRequest;
import TaskManager.entities.responseEntities.BoardDetailsDTO;
import TaskManager.entities.responseEntities.BoardToReturn;
import TaskManager.repository.BoardRepository;
import TaskManager.service.BoardService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board")
@AllArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardRepository boardRepository;

    @PostMapping(value = "/board-create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BoardToReturn> createBoard(@RequestBody Board board, @RequestParam int userId){
        return new ResponseEntity<>(boardService.addNewBoard(board,userId),HttpStatus.CREATED);
    }

    @GetMapping(value = "/{boardId}",produces = "application/json")
    public ResponseEntity<BoardDetailsDTO> getBoardById(@PathVariable("boardId") int boardId){
        int userId =1;
        return new ResponseEntity<>(boardService.getBoardById(boardId,userId), HttpStatus.OK);
    }

    @RequestMapping(value = "/get-boards-by-userId", method = RequestMethod.GET)
    public List<BoardToReturn> getUserByToken1(@RequestParam int userId) {
        return boardService.getUserBoards(userId);
    }

    @DeleteMapping(value = "/delete-statues/{boardId}", produces = "application/json")
    public ResponseEntity<Board> removeStatuses(@PathVariable("boardId") int boardId, @RequestBody TaskStatus status){
        System.out.println(status);
        boardService.deleteStatus(boardId,status);
        return ResponseEntity.noContent().build();
    }
    @PostMapping(value = "/add-statues/{boardId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Board>  addNewStatusToBoard(@PathVariable("boardId") int boardId ,@RequestBody TaskStatus taskStatus){
        return new ResponseEntity<>(boardService.addNewStatusToBoard(boardId, taskStatus),HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete-itemType/{boardId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Board> deleteItemType(@PathVariable("boardId")int boardId,@RequestBody BoardRequest boardRequest){
        System.out.println(boardRequest.getType());
        return new ResponseEntity<>(boardService.deleteItemTypeOnBoard(boardId,boardRequest.getType()),HttpStatus.OK);
    }
    @PostMapping(value = "/add-itemType/{boardId}",consumes = "application/json", produces = "application/json")
    public  ResponseEntity<Board> addItemType(@PathVariable("boardId")int boardId, @RequestBody BoardRequest boardRequest){
        System.out.println(boardRequest);
        return new ResponseEntity<>(boardService.addItemTypeOnBoard(boardId,boardRequest.getType()),HttpStatus.OK);
    }
}