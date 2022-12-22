package TaskManager.controller;

import TaskManager.entities.Board;
import TaskManager.entities.Item;
import TaskManager.entities.TaskStatus;
import TaskManager.entities.requests.BoardRequest;
import TaskManager.entities.responseEntities.BoardDetailsDTO;
import TaskManager.entities.responseEntities.BoardToReturn;
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
    @PostMapping(value = "/board-create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BoardToReturn> createBoard(@RequestBody Board board, @RequestParam int userId){
        return new ResponseEntity<>(boardService.addNewBoard(board,userId),HttpStatus.CREATED);
    }

    @GetMapping(value = "/{boardId}",produces = "application/json")
    public ResponseEntity<BoardDetailsDTO> createBoard(@PathVariable("boardId") int boardId){
        return new ResponseEntity<>(boardService.getBoardById(boardId), HttpStatus.OK);
    }

    @RequestMapping(value = "/get-boards-by-userId", method = RequestMethod.GET)
    public List<BoardToReturn> getUserByToken1(@RequestParam int userId) {
        return boardService.getUserBoards(userId);
    }

    @DeleteMapping(value = "/delete-statues", produces = "application/json")
    public Item removeStatuses(@RequestParam int boardId,@RequestParam int statusId){
        return null;
    }
    @PostMapping(value = "/add-statues/{boardId}", consumes = "application/json", produces = "application/json")
    public Item addNewStatusToBoard(@PathVariable("boardId") int boardId ,@RequestBody TaskStatus taskStatus){
        return null;
    }

    @DeleteMapping(value = "/delete-itemType/{boardId}", consumes = "application/json", produces = "application/json")
    public Item deleteItemType(@RequestBody BoardRequest boardRequest){
        return null;
    }
    @PostMapping(value = "/add-itemType/{boardId}",consumes = "application/json", produces = "application/json")
    public Item addItemType(@RequestBody BoardRequest boardRequest){
        System.out.println(boardRequest);
        return null;
    }
}
