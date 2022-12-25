package TaskManager.controller;


import TaskManager.entities.requests.NotificationRequest;
import TaskManager.service.NotificationService;
import TaskManager.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import TaskManager.entities.responseEntities.BoardToReturn;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private  final NotificationService notificationService;
    @PostMapping(value = "/notificationSetting", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> notificationSetting(@RequestParam int userId, @RequestBody NotificationRequest notificationRequest) {
        notificationService.notificationSetting(userId, notificationRequest);
        return null;
    }

}