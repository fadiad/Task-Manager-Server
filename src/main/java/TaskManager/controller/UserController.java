package TaskManager.controller;

import TaskManager.entities.Board;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
//@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {


    @GetMapping
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')"+"|| hasAuthority('ROLE_USER')")
    public void onlyAdmin(){
        System.out.println("hi admin");
    }

    @PostMapping(value = "/board-create", consumes = "application/json", produces = "application/json")
    public void createBoard(@RequestBody Board board){
        System.out.println(board);
    }
}
