package TaskManager.entities.responseEntities;

import TaskManager.entities.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardToReturn {
    private int id ;
    private String title;

    public BoardToReturn(Board board){
        if(board != null){
            this.id=board.getId();
            this.title=board.getTitle();
        }
    }
}
