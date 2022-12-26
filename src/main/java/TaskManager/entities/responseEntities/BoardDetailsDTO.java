package TaskManager.entities.responseEntities;

import TaskManager.entities.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BoardDetailsDTO{
    private Board board;
    private Map<Integer,List<ItemDTO>> itemFilteredByStatus;
    public void setBoard(Board board) {
        this.board = board;
    }
}
