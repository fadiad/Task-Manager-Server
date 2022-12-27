package TaskManager.entities.entitiesUtils;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class FilterItem {

    private ItemTypes itemType;
    private Integer importance;
    private LocalDate dueDate;
    private Integer statusId;

    private int boardId;
}
