package TaskManager.controller;

import TaskManager.entities.Comment;
import TaskManager.entities.Item;
import TaskManager.entities.responseEntities.ItemDTO;
import TaskManager.service.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping(value = "/item-create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Item> addNewItem(@RequestBody Item newItem){

        return new ResponseEntity<Item>(itemService.addNewItem(newItem), HttpStatus.CREATED);
    }
    @PutMapping(value = "/item-update", consumes = "application/json", produces = "application/json")
    public Item  updateItem(@RequestBody Item updatedItem){
        return null;
    }

    @PutMapping(value = "/item-assignTO", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ItemDTO>  assignItemTo(@RequestParam int itemId, @RequestParam int userId){
        return new ResponseEntity<>(itemService.aassignItemTo(itemId,userId),HttpStatus.OK);

    }
    @DeleteMapping(value = "/item-delete/{itemId}")
    public void deleteItem(@PathVariable("itemId") int itemId){
        itemService.deleteItem(itemId);
    }

    @PostMapping(value = "/add-comment/{itemId}")
    public void addComment(@RequestBody Comment comment){

    }

}