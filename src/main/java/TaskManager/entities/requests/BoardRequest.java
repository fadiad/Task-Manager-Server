package TaskManager.entities.requests;

import TaskManager.entities.entitiesUtils.ItemTypes;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
public class BoardRequest {

    private Set<ItemTypes> type;
}
