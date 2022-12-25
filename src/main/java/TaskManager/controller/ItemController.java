package TaskManager.controller;

import TaskManager.entities.Comment;
import TaskManager.entities.Item;
import TaskManager.entities.entitiesUtils.UserRole;
import TaskManager.entities.responseEntities.ItemDTO;
import TaskManager.service.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.NoPermissionException;
import javax.validation.Valid;

@RestController
///{boardId}/item"
@RequestMapping("/item")
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping(value = "/item-create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ItemDTO> addNewItem(@RequestBody Item newItem) {

        System.out.println(newItem.getStatusId() + " " + newItem.getBoardId());
        return new ResponseEntity<ItemDTO>(itemService.addNewItem(newItem), HttpStatus.CREATED);
    }

    @PutMapping(value = "/item-update/{itemId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ItemDTO> updateItem(@PathVariable("itemId") int itemId,@RequestBody Item updatedItem) {
        UserRole userRole = UserRole.ROLE_ADMIN;

        try {
            return new ResponseEntity<>(itemService.updateItem(itemId, updatedItem, userRole), HttpStatus.OK);
        } catch (NoPermissionException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getLocalizedMessage());
        }
    }


    @PutMapping(value = "/item-assignTO/{boardId}", produces = "application/json")
    public ResponseEntity<ItemDTO> assignItemTo(@RequestParam int itemId, @RequestParam int userId, @PathVariable("boardId") int boardId) {

        System.out.println(boardId);
        return new ResponseEntity<>(itemService.assignItemTo(itemId, userId, boardId), HttpStatus.OK);
    }

    @DeleteMapping(value = "/item-delete/{itemId}")
    public void deleteItem(@PathVariable("itemId") int itemId) {
        itemService.deleteItem(itemId);
    }

    @PostMapping(value = "/add-comment/{itemId}")
    public void addComment(@RequestBody Comment comment) {

    }

}
