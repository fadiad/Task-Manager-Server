package TaskManager.entities.responseEntities;

import TaskManager.entities.Item;
import TaskManager.entities.TaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
@Getter
@NoArgsConstructor
public class ItemByStatusDTO {

    private  TaskStatus status;
    private List<ItemDTO> itemFilteredByStatus;

    public ItemByStatusDTO(TaskStatus status, List<Item> items){
        this.status=status;
        itemFilteredByStatus=items.stream().filter(item -> item.getStatusId()==status.getId()).map(ItemDTO::new).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "ItemByStatusDTO{" +
                "status=" + status +
                ", itemFilteredByStatus=" + itemFilteredByStatus +
                '}';
    }
}
