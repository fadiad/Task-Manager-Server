package TaskManager.service;


import TaskManager.entities.Board;
import TaskManager.entities.entitiesUtils.UserRole;
import TaskManager.repository.BoardRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Collections;

@Service
@AllArgsConstructor
public class PermissionService {

    private final BoardRepository boardRepository;
    @Transactional
    public Collection<? extends GrantedAuthority> getUserPermission(int boardId,int userId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("no board found"));
        UserRole userRole = board.getUsersRoles().get(userId);
        if(userRole != null){
           SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userRole.name());
           return Collections.singletonList(authority);
        }
        return null;
    }

}
