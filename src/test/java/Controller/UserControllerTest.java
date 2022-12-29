package Controller;

import TaskManager.controller.BoardController;
import TaskManager.controller.UserController;
import TaskManager.entities.Board;
import TaskManager.entities.TaskStatus;
import TaskManager.entities.entitiesUtils.NotificationTypes;
import TaskManager.entities.entitiesUtils.Ways;
import TaskManager.entities.requests.BoardRequest;
import TaskManager.entities.requests.NotificationRequest;
import TaskManager.entities.responseEntities.BoardDetailsDTO;
import TaskManager.entities.responseEntities.BoardToReturn;
import TaskManager.service.BoardService;
import TaskManager.service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;


    @Test
    void testNotificationSetting() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("userId")).thenReturn(1);
        NotificationRequest notificationRequest = new NotificationRequest(new HashSet<>(Arrays.asList(Ways.EMAIL)), new HashSet<>(Arrays.asList(NotificationTypes.ITEM_DELETED)));
        ResponseEntity<Void> response = userController.notificationSetting(request, notificationRequest);

        verify(userService).notificationSetting(1, notificationRequest);
    }


    @Test
    void testGetUserByToken1() {
        BoardService boardService = mock(BoardService.class);
        when(boardService.getUserBoards(1)).thenReturn(Arrays.asList(new BoardToReturn(1, "Board 1"), new BoardToReturn(2, "Board 2")));
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("userId")).thenReturn(1);
        UserController controller = new UserController(userService, boardService);
        List<BoardToReturn> boards = controller.getUserByToken1(request);

        assertEquals(2, boards.size());
        assertEquals(1, boards.get(0).getId());
        assertEquals(2, boards.get(1).getId());
    }
}