package TaskManager.service;


import TaskManager.entities.Board;
import TaskManager.entities.entitiesUtils.UserRole;
import TaskManager.repository.BoardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class PermissionService {

    private final BoardRepository boardRepository;
    @Transactional
    public UserRole getUserPermission(int boardId, int userId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("no board found"));
        UserRole userRole = board.getUsersRoles().get(userId);
        if(userRole != null){
            return userRole;
        }
        return null;
    }

}
