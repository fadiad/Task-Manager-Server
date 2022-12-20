package TaskManager.controller;

import TaskManager.entities.Board;
import TaskManager.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
//@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {


    private final UserService userService;

    @GetMapping
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')"+"|| hasAuthority('ROLE_USER')")
    public void onlyAdmin(){
        System.out.println("hi admin");
    }

    @PostMapping(value = "/board-create", consumes = "application/json", produces = "application/json")
    public void createBoard(@RequestBody Board board){
        Board board1 = userService.addNewBoard(board);
        System.out.println(board);

    }
}
