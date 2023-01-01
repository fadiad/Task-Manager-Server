package TaskManager.entities.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShareBoard {
    private Boolean assignLeader;
    private String email;
}
