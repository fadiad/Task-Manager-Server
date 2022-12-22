package TaskManager.entities.responseEntities;

import TaskManager.entities.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BoardDetailsDTO{
    private Board board;
    private List<ItemByStatusDTO> itemsByStatus;

    @Override
    public String toString() {
        return "BoardDetailsDTO{" +
                "board=" + board +
                ", itemsByStatus=" + itemsByStatus +
                '}';
    }
}
