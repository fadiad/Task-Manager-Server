package TaskManager.controller;

import TaskManager.entities.Board;
import TaskManager.entities.TaskStatus;
import TaskManager.entities.entitiesUtils.RealTimeActions;
import TaskManager.entities.entitiesUtils.Validations;
import TaskManager.entities.requests.BoardRequest;
import TaskManager.entities.requests.ShareBoard;
import TaskManager.entities.responseEntities.BoardDetailsDTO;
import TaskManager.entities.responseEntities.BoardToReturn;
import TaskManager.entities.responseEntities.UserDTO;
import TaskManager.service.BoardService;
import TaskManager.service.NotificationService;
import TaskManager.utils.emailUtils.EmailSenderFacade;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/board")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class BoardController {

    private final BoardService boardService;

    private final NotificationService notificationService;

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final EmailSenderFacade emailSenderFacade;


    /**
     * Create Board rout , when user sends a request to make new board , his request will be treated by this rout ,
     * it gets a board title and item types that he wants to be in the board .
     *
     * @param request contain the details about the request in the server.
     * @param board   details to create
     * @return the board after the creation
     */
    @PostMapping(value = "/board-create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BoardToReturn> createBoard(HttpServletRequest request, @RequestBody Board board) {
        int userId = (int) request.getAttribute("userId");
        return new ResponseEntity<>(boardService.addNewBoard(board, userId), HttpStatus.CREATED);
    }

    /**
     * This rout gets a board id and returns BoardDetailsDTO .
     *
     * @param request contain the details about the request in the server.
     * @param boardId that we want to find
     * @return details
     */
    @GetMapping(produces = "application/json")
    public ResponseEntity<BoardDetailsDTO> getBoardById(HttpServletRequest request, @RequestParam int boardId) {
        int userId = (int) request.getAttribute("userId");
        return new ResponseEntity<>(boardService.getBoardById(boardId, userId), HttpStatus.OK);
    }


    /**
     * This rout is used to delete status - status and its items ,
     * it gets a status id to be deleted and a board to check permissions .
     *
     * @param boardId  that we want to change
     * @param statusId that we want to remove.
     * @return the board after changes.
     */
    @DeleteMapping(value = "/delete-statues")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Board> removeStatuses(@RequestParam int boardId, @RequestParam int statusId) {
        boardService.deleteStatus(boardId, statusId);
        Map<String, Object> payload = new HashMap<>();
        payload.put("action", RealTimeActions.DELETE_STATUS_ON_BOARD);

        payload.put("statusId", statusId);

        simpMessagingTemplate.convertAndSend("/board/" + boardId, payload);

        return ResponseEntity.noContent().build();
    }


    /**
     * This rout is used to add new status to a board ,
     * it gets a board id and a TaskStatus that contains a status title .
     *
     * @param boardId    to find the board we want to change.
     * @param taskStatus the new one
     * @return the board after changes.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/add-statues", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Board> addNewStatusToBoard(@RequestParam int boardId, @RequestBody TaskStatus taskStatus) {
        Board board = boardService.addNewStatusToBoard(boardId, taskStatus);

        Map<String, Object> payload = new HashMap<>();
        payload.put("action", RealTimeActions.ADD_STATUS_ON_BOARD);
        payload.put("board", board);

        simpMessagingTemplate.convertAndSend("/board/" + board.getId(), payload);

        return new ResponseEntity<>(board, HttpStatus.CREATED);
    }


    /**
     * delete item type of a board ,
     * it gets an item type that we want to delete,
     * and it deletes it from the DB .
     *
     * @param boardId      to find the board we want to change.
     * @param boardRequest contain set of types that we want to delete
     * @return the board after changes.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/delete-itemType", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Board> deleteItemType(@RequestParam int boardId, @RequestBody BoardRequest boardRequest) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("action", RealTimeActions.DELETE_ITEM_TYPE_ON_BOARD);
        payload.put("type", boardRequest.getType());

        simpMessagingTemplate.convertAndSend("/board/" + boardId, payload);
        return new ResponseEntity<>(boardService.deleteItemTypeOnBoard(boardId, boardRequest.getType()), HttpStatus.OK);
    }


    /**
     * Add item type to a board , it gets the item type that we want to add ,
     * and it adds it to the set of types in the board .
     *
     * @param boardId      to find the board we want to change.
     * @param boardRequest contain set of types that we want to delete
     * @return the board after changes.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/add-itemType", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Board> addItemType(@RequestParam int boardId, @RequestBody BoardRequest boardRequest) {
        System.out.println(boardRequest);
        return new ResponseEntity<>(boardService.addItemTypeOnBoard(boardId, boardRequest.getType()), HttpStatus.OK);
    }


    /**
     * Share board with other users , it gets a user email in the ShareBoard board ,
     * it adds the user email to the list of users on the board ,
     * and adds the board to the list of boards on the user entity .
     *
     * @param boardId where we want to add someone
     * @return the user details
     */
    @PostMapping(value = "/shareBoard")
    public ResponseEntity<UserDTO> shareBoardByUserId(@RequestParam int boardId, @RequestBody ShareBoard share) {
        System.out.println(share.getEmail());
        if (!Validations.isEmailRegexValid(share.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid email");
        }
        UserDTO userDTO = boardService.shareBoard(boardId, share);
        emailSenderFacade.sendEmail(share.getEmail());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
}