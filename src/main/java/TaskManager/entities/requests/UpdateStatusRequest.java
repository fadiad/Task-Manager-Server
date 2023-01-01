package TaskManager.entities.requests;

import TaskManager.entities.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStatusRequest {

    private TaskStatus newStatus;
    private TaskStatus oldStatus;
}
