package TaskManager.controller;

import TaskManager.entities.Item;
import TaskManager.entities.TaskStatus;
import TaskManager.entities.requests.BoardRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
@AllArgsConstructor
public class BoardController {

    @DeleteMapping(value = "/delete-statues", produces = "application/json")
    public Item removeStatuses(@RequestParam int boardId,@RequestParam int statusId){
        return null;
    }
    @PostMapping(value = "/add-statues/{boardId}", consumes = "application/json", produces = "application/json")
    public Item addNewItem(@PathVariable("boardId") int boardId ,@RequestBody TaskStatus taskStatus){
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
