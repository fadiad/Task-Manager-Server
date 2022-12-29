package TaskManager.controller;


import TaskManager.entities.requests.NotificationRequest;
import TaskManager.entities.responseEntities.BoardToReturn;
import TaskManager.service.BoardService;
import TaskManager.service.UserService;
import lombok.AllArgsConstructor;
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

    /**
     *notification setting
     * @param request object that contain data about the request that arrive to the server
     * @param notificationRequest contain 2 set of the notification settings
     * @return response/void
     */
    @PostMapping(value = "/notificationSetting", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> notificationSetting(HttpServletRequest request, @RequestBody NotificationRequest notificationRequest) {

        int userId= (int) request.getAttribute("userId");
        userService.notificationSetting(userId, notificationRequest);
        return null;
    }

    /**
     * get user by his token.
     * @param request object that contain data about the request that arrive to the server.
     * @return all boards of user by the userId in the request.
     */
    @GetMapping(value = "/get-boards-by-userId")
    public List<BoardToReturn> getUserByToken1(HttpServletRequest request) {
        int userId= (int) request.getAttribute("userId");
        return boardService.getUserBoards(userId);
    }
}