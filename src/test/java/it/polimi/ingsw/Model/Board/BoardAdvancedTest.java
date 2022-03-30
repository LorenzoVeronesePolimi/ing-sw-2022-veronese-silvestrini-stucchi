package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.ExceededMaxStudentsHallException;
import it.polimi.ingsw.Model.Exceptions.StudentNotFoundException;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class BoardAdvancedTest {
    @Test
    void boardAdvancedTest(){
        List<Player> playerList = new ArrayList<>();
        Player p1 = new Player("player one", PlayerColour.BLACK);
        Player p2 = new Player("player two", PlayerColour.WHITE);
        Player p3 = new Player("player three", PlayerColour.GRAY);
        playerList.add(p1);
        playerList.add(p2);
        playerList.add(p3);

        BoardAbstract board = new BoardThree(playerList);
        BoardAdvanced boardAdvanced = new BoardAdvanced(board);

        Assertions.assertEquals(board.bag , boardAdvanced.getBag());

        Assertions.assertEquals(board.archipelagos, boardAdvanced.getArchiList());

        Assertions.assertEquals(board.schools, boardAdvanced.getSchools());
        Assertions.assertEquals(3, boardAdvanced.getSchools().size());

        /*for(int i=0; i<12; i++) {
            Assertions.assertEquals(board.archipelagos.get(i), boardAdvanced.getArchipelago(i));
        }
        Assertions.assertEquals(9, boardAdvanced.getSchools().get(0).getStudentsHall().size());
        Student s = new Student(SPColour.BLUE);
        try {
            for(int i=0; i<9; i++) {
                boardAdvanced.getSchools().get(0).removeStudentHall(boardAdvanced.getSchools().get(0).getStudentsHall().get(0).getColour());
            }
        } catch (StudentNotFoundException e) {
        e.printStackTrace();
        }
        Assertions.assertFalse(boardAdvanced.isStudentInSchoolHall(p1,SPColour.BLUE));
        try {
            boardAdvanced.getSchools().get(0).addStudentHall(s);
        } catch (ExceededMaxStudentsHallException e) {
            e.printStackTrace();
        }*/
        Assertions.assertTrue(boardAdvanced.isProfessorInSchool(SPColour.BLUE));
    }
}
