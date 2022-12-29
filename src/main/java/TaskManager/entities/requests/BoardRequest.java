package TaskManager.entities.requests;

import TaskManager.entities.entitiesUtils.ItemTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BoardRequest {
    private ItemTypes type;
}
