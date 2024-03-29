package TaskManager.controller;

import TaskManager.entities.Comment;
import TaskManager.entities.Item;
import TaskManager.entities.entitiesUtils.FilterItem;
import TaskManager.entities.entitiesUtils.RealTimeActions;
import TaskManager.entities.entitiesUtils.UserRole;
import TaskManager.entities.requests.UpdateStatusRequest;
import TaskManager.entities.responseEntities.ItemDTO;
import TaskManager.service.ItemService;
import TaskManager.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.NoPermissionException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/item")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ItemController {

    private final ItemService itemService;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate simpMessagingTemplate;


    /**
     * Add new item to boardId , it gets a board id and an item title by the object Item ,
     * and adds it to the board items .
     *
     * @param boardId to find the board where we want to add the item
     * @param newItem the new item we want to add
     * @return itemDTO
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_LEADER')")
    @PostMapping(value = "/item-create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ItemDTO> addNewItem(@RequestParam int boardId, @RequestBody Item newItem) {

        ItemDTO itemDTO = itemService.addNewItem(newItem, boardId);
        Map<String, Object> payload = new HashMap<>();
        payload.put("action", RealTimeActions.ADD_ITEM_ON_BOARD);
        payload.put("item", itemDTO);

        simpMessagingTemplate.convertAndSend("/board/" + boardId, payload);

        return new ResponseEntity<>(itemDTO, HttpStatus.CREATED);

    }


    /**
     * Update item on board , it gets an item - copy of an item the already existed in the db ,
     * and it set its new details on the old one .
     *
     * @param request     contain the details about the request in the server.
     * @param itemId      to find the details of the item we want to update
     * @param updatedItem the new details
     * @return the item after changes.
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_LEADER')")
    @PutMapping(value = "/item-update", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ItemDTO> updateItem(HttpServletRequest request, @RequestParam int itemId, @RequestBody Item updatedItem) {
        try {
            UserRole userRole = (UserRole) request.getAttribute("role");
            ItemDTO itemDTO = itemService.updateItem(itemId, updatedItem, userRole);
            Map<String, Object> payload = new HashMap<>();
            payload.put("action", RealTimeActions.UPDATE_ITEM_DATA);
            payload.put("item", itemDTO);

            simpMessagingTemplate.convertAndSend("/board/" + updatedItem.getBoardId(), payload);
            return new ResponseEntity<>(itemDTO, HttpStatus.OK);
        } catch (NoPermissionException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getLocalizedMessage());
        }
    }


    /**
     * Assign Item To User , it gets an item id and a user id ,
     * to make item assignTo equals to used id .
     *
     * @param itemId  the item details
     * @param userId  that we want to assign
     * @param boardId to find the board
     * @return the itemDTO
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_LEADER')")
    @PutMapping(value = "/item-assignTO", produces = "application/json")
    public ResponseEntity<ItemDTO> assignItemTo(@RequestParam int itemId, @RequestParam int userId, @RequestParam int boardId) {

        ItemDTO itemDTO = itemService.assignItemTo(itemId, userId, boardId);
        Map<String, Object> payload = new HashMap<>();
        payload.put("action", RealTimeActions.ITEM_ASSIGNED_TO_ME);
        payload.put("item", itemDTO);

        simpMessagingTemplate.convertAndSend("/board/" + boardId, payload);
        return new ResponseEntity<>(itemService.assignItemTo(itemId, userId, boardId), HttpStatus.OK);
    }


    /**
     * This rout is used to change item status , it deletes the item old status id ,
     * and add a new status id
     *
     * @param boardId
     * @param itemId
     * @param updateStatusRequest
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/updateItemStatus", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ItemDTO> updateItemStatus(@RequestParam int boardId, @RequestParam int itemId, @RequestBody UpdateStatusRequest updateStatusRequest) {
        ItemDTO item = itemService.updateItemStatusToBoard(boardId, itemId, updateStatusRequest.getNewStatus(), updateStatusRequest.getOldStatus());
        Map<String, Object> payload = new HashMap<>();

        payload.put("action", RealTimeActions.UPDATE_ITEM_STATUS);
        payload.put("oldStatus", updateStatusRequest.getOldStatus());
        payload.put("newStatus", updateStatusRequest.getNewStatus());
        payload.put("item", item);
        simpMessagingTemplate.convertAndSend("/board/" + boardId, payload);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }


    /**
     * Delete item by id ,
     * it gets an item id and delete it from board items .
     *
     * @param itemId to find the item in DATA BASE
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/item-delete")
    public ResponseEntity<String> deleteItem(@RequestParam int itemId, @RequestParam int boardId) {
        Item item = itemService.deleteItem(itemId);
        Map<String, Object> payload = new HashMap<>();
        payload.put("action", RealTimeActions.DELETE_ITEM_ON_BOARD);
        payload.put("statusId", item.getStatusId());
        payload.put("itemId", item.getId());
        simpMessagingTemplate.convertAndSend("/board/" + boardId, payload);
        return ResponseEntity.noContent().build();

    }

    /**
     * Add comment to the list of comments of the item , gets an item id and a comment to add .
     *
     * @param request contain the details about the request in the server.
     * @param itemId  to find the item that we want to add comment
     * @param comment the comment details
     */
    @PostMapping(value = "/add-comment")
    public ResponseEntity<ItemDTO> addComment(HttpServletRequest request, @RequestParam int boardId, @RequestParam int itemId, @RequestBody Comment comment) {
        int userId = (int) request.getAttribute("userId");
        System.out.println(comment);
        ItemDTO itemDTO = itemService.addComment(itemId, userId, comment);

        Map<String, Object> payload = new HashMap<>();

        payload.put("action", RealTimeActions.ADD_COMMENT);
        payload.put("item", itemDTO);
        simpMessagingTemplate.convertAndSend("/board/" + boardId, payload);
        return new ResponseEntity<>(itemDTO, HttpStatus.OK);
    }


    /**
     * It gets a different parameters of an item such as importance , itemType , dueDate
     * and returns an items that has these parameters values .
     *
     * @param filterItem
     * @return
     */
    @PostMapping(value = "/filter", produces = "application/json")
    public List<ItemDTO> getBoardById(@RequestBody FilterItem filterItem) {
        return itemService.filter(filterItem);
    }

}
