package TaskManager.service;

import TaskManager.email.EmailNotification;
import TaskManager.entities.Board;
import TaskManager.entities.Item;
import TaskManager.entities.User;
import TaskManager.entities.entitiesUtils.NotificationTypes;
import TaskManager.repository.BoardRepository;
import TaskManager.repository.ItemRepository;
import TaskManager.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

import static TaskManager.entities.entitiesUtils.NotificationTypes.ITEM_DELETED;

@Service
@AllArgsConstructor
public class NotificationService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final ItemRepository itemRepository;

    public void ItemNotification(int boardId, NotificationTypes notificationTypes) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("board not found"));
        Set<User> result = board.getUsersOnBoard();
        sendMailMassage(result, notificationTypes);
    }


    private void sendMailMassage(Set<User> userSet, NotificationTypes notificationTypes) {
        for(User user : userSet){
            if (user.getNotificationTypes().contains(notificationTypes) && (user.isEmailNotification())) {
                EmailNotification.sendEmailNotification(user.getEmail(), notificationTypes.toString());
            } else
                System.out.println("user don't get email on this notification type");
        }
    }


}
