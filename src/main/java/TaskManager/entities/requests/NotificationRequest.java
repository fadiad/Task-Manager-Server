package TaskManager.entities.requests;

import TaskManager.entities.entitiesUtils.NotificationTypes;
import TaskManager.entities.entitiesUtils.Ways;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class NotificationRequest {

    private Set<Ways> ways;
    private Set<NotificationTypes> option;

}
