package TaskManager.service;

import TaskManager.email.EmailNotification;
import TaskManager.entities.Board;
import TaskManager.entities.Item;
import TaskManager.entities.User;
import TaskManager.entities.entitiesUtils.NotificationTypes;
import TaskManager.entities.requests.NotificationRequest;
import TaskManager.repository.BoardRepository;
import TaskManager.repository.ItemRepository;
import TaskManager.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

import static TaskManager.entities.entitiesUtils.NotificationTypes.ITEM_ASSIGNED_TO_ME;
import static TaskManager.entities.entitiesUtils.NotificationTypes.ITEM_DELETED;
import static TaskManager.entities.entitiesUtils.Ways.EMAIL;
import static TaskManager.entities.entitiesUtils.Ways.POP_UP;

@Service
@AllArgsConstructor
public class NotificationService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public void itemAssignedToMe(int itemId, int userId, int boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("Board not found"));
        User userToAssign = board.getUsersOnBoard().stream()
                .filter(u -> u.getId() == userId)
                .findAny().orElseThrow(() -> new IllegalArgumentException("user not found "));
        String massage = "Item Assigned To Me";
        sendMailMassage(userToAssign, ITEM_ASSIGNED_TO_ME, massage);
    }
    public void itemDeleted(int itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("item not found"));
        Board board = boardRepository.findById(item.getBoardId()).orElseThrow(() -> new IllegalArgumentException("board not found"));
        Set<User> result = board.getUsersOnBoard();
        String massage = "itemDeleted";
        sendMailMassage(result, ITEM_DELETED, massage);
    }
    public void statusChange() {

    }

    public void commentAdded() {

    }
    public void dataChange() {

    }

    public void userAddedToSystem() {

    }

    private void sendMailMassage(Set<User> userSet, NotificationTypes notificationTypes, String massage) {
        for (User user : userSet) {
            if (user.getNotificationTypes().contains(notificationTypes) && (user.isEmailNotification())) {
                EmailNotification.sendEmailNotification(user.getEmail(), notificationTypes.toString());
            } else
                System.out.println("user don't get email on this notification type");
        }
    }

    private void sendMailMassage(User user, NotificationTypes notificationTypes, String massage) {

        if (user.getNotificationTypes().contains(notificationTypes) && (user.isEmailNotification())) {
            EmailNotification.sendEmailNotification(user.getEmail(), massage);
        } else
            System.out.println("user don't get email on this notification type");

    }

    public void notificationSetting(int userId, NotificationRequest notificationRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("user not found"));
        System.out.println(user.getUsername());

        if (notificationRequest.getWays().contains(EMAIL)) {
            user.setEmailNotification(true);
            userRepository.save(user);
        } else {
            user.setEmailNotification(false);
            userRepository.save(user);
        }

        if (notificationRequest.getWays().contains(POP_UP)) {
            user.setPopUpNotification(true);
            userRepository.save(user);
        } else {
            user.setPopUpNotification(false);
            userRepository.save(user);
        }

        Set<NotificationTypes> types = notificationRequest.getOption();
        user.getNotificationTypes().clear();
        for (NotificationTypes ty : types) {
            user.getNotificationTypes().add(ty);
        }
        System.out.println(user.getNotificationTypes().toString());
    }
}
