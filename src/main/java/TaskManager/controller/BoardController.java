package TaskManager.controller;

import TaskManager.entities.Board;
import TaskManager.entities.TaskStatus;
import TaskManager.entities.entitiesUtils.Validations;
import TaskManager.entities.requests.BoardRequest;
import TaskManager.entities.requests.ShareBoard;
import TaskManager.entities.responseEntities.BoardDetailsDTO;
import TaskManager.entities.responseEntities.BoardToReturn;
import TaskManager.entities.responseEntities.ItemDTO;
import TaskManager.entities.responseEntities.UserDTO;
import TaskManager.service.BoardService;
import TaskManager.service.NotificationService;
import TaskManager.utils.emailUtils.EmailSenderFacade;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/board")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class BoardController {

    private final BoardService boardService;
    private  final NotificationService notificationService;
    private final EmailSenderFacade emailSenderFacade;


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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Board> removeStatuses(@RequestParam int boardId, @RequestParam int statusId) {
        boardService.deleteStatus(boardId, statusId);
        return ResponseEntity.noContent().build();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/add-statues", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Board> addNewStatusToBoard(@RequestParam int boardId, @RequestBody TaskStatus taskStatus) {
        return new ResponseEntity<>(boardService.addNewStatusToBoard(boardId, taskStatus), HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/delete-itemType", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Board> deleteItemType(@RequestParam int boardId, @RequestBody BoardRequest boardRequest) {
        return new ResponseEntity<>(boardService.deleteItemTypeOnBoard(boardId, boardRequest.getType()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/add-itemType", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Board> addItemType(@RequestParam int boardId, @RequestBody BoardRequest boardRequest) {
        System.out.println(boardRequest);
        return new ResponseEntity<>(boardService.addItemTypeOnBoard(boardId, boardRequest.getType()), HttpStatus.OK);
    }
    @PutMapping(value = "/updateItemStatus", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ItemDTO> updateItemStatus(@RequestParam int boardId, @RequestParam int itemId, @RequestBody TaskStatus taskStatus) {
        return new ResponseEntity<>(boardService.updateItemStatusToBoard(boardId, itemId ,taskStatus), HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/shareBoard")
    public ResponseEntity<UserDTO> shareBoardByUserId(@RequestParam int boardId, @RequestBody ShareBoard share) {
        System.out.println(share.getEmail());
        if(!Validations.isEmailRegexValid(share.getEmail())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid email");
        }
        UserDTO userDTO=boardService.shareBoard(boardId, share.getEmail());
        emailSenderFacade.sendEmail(share.getEmail());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
}