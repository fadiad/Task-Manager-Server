package Controller;

import TaskManager.controller.BoardController;
import TaskManager.entities.Board;
import TaskManager.entities.responseEntities.BoardDetailsDTO;
import TaskManager.entities.responseEntities.BoardToReturn;
import TaskManager.service.BoardService;
import TaskManager.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class BoardControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private BoardService boardService;
    @InjectMocks
    private BoardController boardController;

    private Board goodBoard;

    @BeforeEach
    @DisplayName("Make sure all the correct parameters are refreshed after each operation")
    void setUp() {
        goodBoard = new Board();
        goodBoard.setTitle("title");
        goodBoard.setId(1);
    }

    @Test
    @DisplayName("Trying to create board successfully")
    void create_board_successfully() {
        BoardToReturn boardToReturn = new BoardToReturn(1, "title");
        given(boardService.addNewBoard(goodBoard, 1)).willReturn(boardToReturn);
        assertEquals(201, boardController.createBoard(goodBoard, 1).getStatusCodeValue(), "create board successfully");
    }
    @Test
    @DisplayName("Trying to get board by id successfully")
    void get_Board_By_Id_successfully() {
        BoardDetailsDTO boardDetailsDTO = new BoardDetailsDTO();
        boardDetailsDTO.setBoard(goodBoard);
        given(boardService.getBoardById(1, 1)).willReturn(boardDetailsDTO);
        assertEquals(1, boardController.getBoardById(1).getBody().getBoard().getId());
    }
}
