package TaskManager.controller;

import TaskManager.entities.Board;
import TaskManager.entities.TaskStatus;
import TaskManager.entities.requests.BoardRequest;
import TaskManager.entities.responseEntities.BoardDetailsDTO;
import TaskManager.entities.responseEntities.BoardToReturn;
import TaskManager.entities.responseEntities.UserDTO;
import TaskManager.repository.BoardRepository;
import TaskManager.service.BoardService;
import TaskManager.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/board")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class BoardController {

    private final BoardService boardService;

    @PostMapping(value = "/board-create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BoardToReturn> createBoard(HttpServletRequest request,@RequestBody Board board) {
        int userId= (int) request.getAttribute("userId");
        return new ResponseEntity<>(boardService.addNewBoard(board,userId), HttpStatus.CREATED);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<BoardDetailsDTO> getBoardById(HttpServletRequest request,@RequestParam int boardId) {
        int userId= (int) request.getAttribute("userId");
        return new ResponseEntity<>(boardService.getBoardById(boardId, userId), HttpStatus.OK);
    }



    @DeleteMapping(value = "/delete-statues")
    public ResponseEntity<Board> removeStatuses(@RequestParam int boardId, @RequestParam int statusId) {
        boardService.deleteStatus(boardId, statusId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/add-statues", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Board> addNewStatusToBoard(@RequestParam int boardId, @RequestBody TaskStatus taskStatus) {
        return new ResponseEntity<>(boardService.addNewStatusToBoard(boardId, taskStatus), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete-itemType", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Board> deleteItemType(@RequestParam int boardId, @RequestBody BoardRequest boardRequest) {
        return new ResponseEntity<>(boardService.deleteItemTypeOnBoard(boardId, boardRequest.getType()), HttpStatus.OK);
    }

    @PostMapping(value = "/add-itemType", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Board> addItemType(@RequestParam int boardId, @RequestBody BoardRequest boardRequest) {
        System.out.println(boardRequest);
        return new ResponseEntity<>(boardService.addItemTypeOnBoard(boardId, boardRequest.getType()), HttpStatus.OK);
    }
    @PutMapping(value = "/updateItemStatus", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Board> updateItemStatus(@RequestParam int boardId,@RequestParam int itemId, @RequestBody TaskStatus taskStatus) {

        return new ResponseEntity<>(boardService.updateItemStatusToBoard(boardId, itemId ,taskStatus), HttpStatus.OK);
    }

    @PostMapping(value = "/shareBoard")
    public ResponseEntity<UserDTO> shareBoardByUserId(@RequestParam int boardId, @RequestParam String email) {
        return new ResponseEntity<>(boardService.shareBoard(boardId, email), HttpStatus.OK);
    }
}