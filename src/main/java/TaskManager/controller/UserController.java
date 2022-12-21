package TaskManager.controller;

import TaskManager.entities.Board;
import TaskManager.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {


    private final UserService userService;

    @GetMapping
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')"+"|| hasAuthority('ROLE_USER')")
    public void onlyAdmin(){
        userService.getAll();
        System.out.println("hi admin");
    }

    @PostMapping(value = "/board-create", consumes = "application/json", produces = "application/json")
    public void createBoard(@RequestBody Board board){
        Board board1 = userService.addNewBoard(board);
        System.out.println(board);

    }

    @PutMapping(value = "/board-update/{boardId}", consumes = "application/json", produces = "application/json")
    public void updateBoard(@PathVariable("boardId") int boardId ,@RequestBody Board updatedBoard){
        Board board1 = userService.updateBoard(updatedBoard);

    }
}
