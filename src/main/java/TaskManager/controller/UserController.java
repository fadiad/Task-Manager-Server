package TaskManager.controller;

import TaskManager.entities.Board;
import TaskManager.entities.entitiesUtils.BoardToReturn;
import TaskManager.service.UserService;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {


    private final UserService userService;

//    @GetMapping
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')"+"|| hasAuthority('ROLE_USER')")

//    public void onlyAdmin(){
//        userService.getAll();
//
//        System.out.println("hi admin");
//    }


    @PostMapping(value = "/board-create", consumes = "application/json", produces = "application/json")
    public void createBoard(@RequestBody Board board, @RequestParam int userId) {
        Board board1 = userService.addNewBoard(board, userId);
    }

    @RequestMapping(value = "/get-boards-by-userId", method = RequestMethod.GET)
    public List<BoardToReturn> getUserByToken1(@RequestParam int userId) {
//        System.out.println("------boards-get------");
//        System.out.println(userId);
//        System.out.println("pring : " + userService.getUserBoards(userId));
        return userService.getUserBoards(userId);
    }

//    @PutMapping(value = "/board-update/{boardId}", consumes = "application/json", produces = "application/json")
//    public void updateBoard(@PathVariable("boardId") int boardId ,@RequestBody Board updatedBoard){
//        Board board1 = userService.updateBoard(updatedBoard);
//    }

}
