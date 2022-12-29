package TaskManager.controller;


import TaskManager.entities.requests.NotificationRequest;
import TaskManager.entities.responseEntities.BoardToReturn;
import TaskManager.entities.responseEntities.UserDTO;
import TaskManager.service.BoardService;
import TaskManager.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/user")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private  final UserService userService;
    private  final BoardService boardService;
    @PostMapping(value = "/notificationSetting", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserDTO> notificationSetting(HttpServletRequest request, @RequestBody NotificationRequest notificationRequest) {
        int userId= (int) request.getAttribute("userId");
        return new ResponseEntity<>(userService.notificationSetting(userId, notificationRequest), HttpStatus.CREATED);
    }
    @GetMapping(value = "/get-boards-by-userId")
    public List<BoardToReturn> getUserByToken1(HttpServletRequest request) {
        int userId= (int) request.getAttribute("userId");
        return boardService.getUserBoards(userId);
    }
}