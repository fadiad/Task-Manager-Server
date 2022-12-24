package TaskManager.entities.requests;

import TaskManager.entities.entitiesUtils.ItemTypes;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BoardRequest {
    private ItemTypes type;
}
