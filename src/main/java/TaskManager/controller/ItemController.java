package TaskManager.controller;

import TaskManager.entities.Comment;
import TaskManager.entities.Item;
import TaskManager.entities.entitiesUtils.NotificationTypes;
import TaskManager.entities.responseEntities.ItemDTO;
import TaskManager.service.ItemService;
import TaskManager.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final NotificationService notificationService;

    @PostMapping(value = "/item-create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ItemDTO> addNewItem(@RequestBody Item newItem){
        return new ResponseEntity<ItemDTO>(itemService.addNewItem(newItem), HttpStatus.CREATED);
    }
    @PutMapping(value = "/item-update/{itemId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ItemDTO> updateItem(@PathVariable("itemId") int itemId,@RequestBody Item updatedItem){
        ResponseEntity<ItemDTO> result = new ResponseEntity<>(itemService.updateItem(itemId, updatedItem), HttpStatus.OK);
        notificationService.ItemNotification(123 , NotificationTypes.ITEM_DATA_CHANGED );
        return  result;
    }


    @PutMapping(value = "/item-assignTO", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ItemDTO>  assignItemTo(@RequestParam int itemId, @RequestParam int userId){
        return new ResponseEntity<>(itemService.assignItemTo(itemId,userId),HttpStatus.OK);
    }

    @DeleteMapping(value = "/item-delete/{itemId}")
    public void deleteItem(@PathVariable("itemId") int itemId) {
        itemService.deleteItem(itemId);
        //TODO: FOUND BOARD ID
        notificationService.ItemNotification(123, NotificationTypes.ITEM_DELETED); //send message to all users that in this board with the item that deleted
    }

    @PostMapping(value = "/add-comment/{itemId}")
    public void addComment(@RequestBody Comment comment){

    }

}
