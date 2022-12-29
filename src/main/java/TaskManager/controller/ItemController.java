package TaskManager.controller;

import TaskManager.entities.Comment;
import TaskManager.entities.Item;
import TaskManager.entities.entitiesUtils.FilterItem;
import TaskManager.entities.entitiesUtils.UserRole;
import TaskManager.entities.responseEntities.ItemDTO;
import TaskManager.service.ItemService;
import TaskManager.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.NoPermissionException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/item")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ItemController {

    private final ItemService itemService;
    private final NotificationService notificationService;

    /**
     * add new item to boardId
     * @param boardId to find the board where we want to add the item
     * @param newItem the new item we want to add
     * @return itemDTO
     */
    @PostMapping(value = "/item-create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ItemDTO> addNewItem(@RequestParam int boardId,@RequestBody Item newItem) {
        return new ResponseEntity<ItemDTO>(itemService.addNewItem(newItem,boardId), HttpStatus.CREATED);
    }

    /**
     * update item on board
     * @param request contain the details about the request in the server.
     * @param itemId to find the details of the item we want to update
     * @param updatedItem the new details
     * @return the item after changes.
     */
    @PutMapping(value = "/item-update", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ItemDTO> updateItem(HttpServletRequest request, @RequestParam int itemId, @RequestBody Item updatedItem) {
        UserRole userRole = (UserRole) request.getAttribute("role");
        try {
            return new ResponseEntity<>(itemService.updateItem(itemId, updatedItem, userRole), HttpStatus.OK);
        } catch (NoPermissionException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getLocalizedMessage());
        }
    }


    /**
     * assign Item To User
     * @param itemId the item details
     * @param userId that we want to assign
     * @param boardId to find the board
     * @return the itemDTO
     */
    @PutMapping(value = "/item-assignTO", produces = "application/json")
    public ResponseEntity<ItemDTO> assignItemTo(@RequestParam int itemId, @RequestParam int userId, @RequestParam int boardId) {
//        ResponseEntity<ItemDTO> result = new ResponseEntity<>(itemService.assignItemTo(itemId, userId, boardId), HttpStatus.OK);
//        notificationService.itemAssignedToMe(itemId, userId, boardId); //send notification
        return  new ResponseEntity<>(itemService.assignItemTo(itemId, userId, boardId), HttpStatus.OK);
    }

    /**
     * delete item by id
     * @param itemId to find the item in DATA BASE
     */
    @DeleteMapping(value = "/item-delete")
    public void deleteItem(@RequestParam  int itemId) {
//        notificationService.itemDeleted(itemId);
        itemService.deleteItem(itemId);
    }

    /**
     * @param request contain the details about the request in the server.
     * @param itemId to find the item that we want to add comment
     * @param comment the comment details
     */
    @PostMapping(value = "/add-comment")
    public void addComment(HttpServletRequest request,@RequestParam int itemId, @RequestBody Comment comment) {
        int userId= (int) request.getAttribute("userId");
        Comment comment1 = itemService.addComment(itemId, userId , comment);
        notificationService.commentAdded(userId);
    }


    /**
     * get board by id
     * @param filterItem
     * @return the item details
     */
    @PostMapping(value = "/filter", produces = "application/json")
    public List<ItemDTO> getBoardById(@RequestBody FilterItem filterItem) {
        return itemService.filter(filterItem);
    }

}
