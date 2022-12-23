//package TaskManager.entities;
//
//import TaskManager.entities.entitiesUtils.UserRole;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.ToString;
//
//import javax.persistence.*;
//import javax.transaction.Transactional;
//import java.time.LocalDate;
//import java.util.HashSet;
//import java.util.Set;
//
//@AllArgsConstructor
//@Entity
//@NoArgsConstructor
//@Table(name = "Notification")
//@Transactional
//@Getter
//public class Notification {
//
//    @Id
//    @GeneratedValue
//    private int id; //id of the notification
//
//    @ManyToOne (fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user; //the user id that create the notification
//
//    @ManyToOne (fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "destination_id", nullable = false)
//    private User destination; //the user destination of the notification
//
//    @ManyToOne (fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "board_id", nullable = false)
//    private Board board; //the bord id notification
//
//    private LocalDate Date; //the date of the notification
//
//    @Column(nullable = false)
//    private NotificationTypes notificationTypes; //the notification type
//
//}
