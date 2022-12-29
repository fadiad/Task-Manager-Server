package TaskManager.entities.requests;

import TaskManager.entities.entitiesUtils.ItemTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BoardRequest {
    private ItemTypes type;
}
