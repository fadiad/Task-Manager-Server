package TaskManager.controller;

import TaskManager.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

//    @PutMapping(value = "/board-update/{boardId}", consumes = "application/json", produces = "application/json")
//    public void updateBoard(@PathVariable("boardId") int boardId ,@RequestBody Board updatedBoard){
//        Board board1 = userService.updateBoard(updatedBoard);
//
//    }
}
