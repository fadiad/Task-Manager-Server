package TaskManager.controller;

import TaskManager.entities.Comment;
import TaskManager.entities.Item;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
@AllArgsConstructor
public class ItemController {

    @PostMapping(value = "/item-create", consumes = "application/json", produces = "application/json")
    public Item addNewItem(@RequestBody Item newItem){
        return null;
    }
    @PutMapping(value = "/item-update", consumes = "application/json", produces = "application/json")
    public Item  updateItem(@RequestBody Item updatedItem){
        return null;
    }

    @PutMapping(value = "/item-assignTO", consumes = "application/json", produces = "application/json")
    public Item  assignItemTo(@RequestParam int itemId,@RequestParam int userId){
        return null;
    }
    @DeleteMapping(value = "/item-delete/{itemId}")
    public void deleteItem(@PathVariable("itemId") int itemId){

    }

    @PostMapping(value = "/add-comment/{itemId}")
    public void addComment(@RequestBody Comment comment){

    }


}
