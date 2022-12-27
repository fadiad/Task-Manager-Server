package TaskManager.controller;


import TaskManager.entities.requests.NotificationRequest;
import TaskManager.entities.responseEntities.BoardToReturn;
import TaskManager.service.BoardService;
import TaskManager.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private  final UserService userService;
    private  final BoardService boardService;
    @PostMapping(value = "/notificationSetting", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> notificationSetting(@RequestParam int userId, @RequestBody NotificationRequest notificationRequest) {
        userService.notificationSetting(userId, notificationRequest);
        return null;
    }
    @RequestMapping(value = "/get-boards-by-userId", method = RequestMethod.GET)
    public List<BoardToReturn> getUserByToken1(@RequestParam int userId) {
        return boardService.getUserBoards(userId);
    }
}