package it.polimi.ingsw.Model.Places;

import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.AnotherTowerException;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Pawns.Tower;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class IslandTest {
    @Test
    void howManyStudents(){
        Island tested = new Island();
        Student s;
        assertEquals(0,tested.howManyStudents().get(SPColour.BLUE));
        assertEquals(0,tested.howManyStudents().get(SPColour.RED));
        assertEquals(0,tested.howManyStudents().get(SPColour.PINK));
        assertEquals(0,tested.howManyStudents().get(SPColour.GREEN));
        assertEquals(0,tested.howManyStudents().get(SPColour.YELLOW));

        for(int i=0; i<6; i++) {
            s = new Student(SPColour.BLUE);
            tested.addStudent(s);
        }
        s = new Student(SPColour.PINK);
        tested.addStudent(s);
        assertEquals(6,tested.howManyStudents().get(SPColour.BLUE));
        assertEquals(0,tested.howManyStudents().get(SPColour.RED));
        assertEquals(1,tested.howManyStudents().get(SPColour.PINK));
        assertEquals(0,tested.howManyStudents().get(SPColour.GREEN));
        assertEquals(0,tested.howManyStudents().get(SPColour.YELLOW));
    }
    @Test
    void addTower(){
        Island tested = new Island();
        assertNull(tested.getTower());

        Player p = new Player("conqueror", PlayerColour.WHITE);
        Tower t = new Tower(p);
        try {
            tested.addTower(t);
        } catch (AnotherTowerException e) {
            e.printStackTrace();
        }
        assertEquals(p, tested.getTower().getPlayer());

        try {
            tested.addTower(t);
        } catch (AnotherTowerException e) {
            e.printStackTrace();
        }
    }
    @Test
    void removeTower(){
        Island tested = new Island();
        Player p = new Player("owner", PlayerColour.WHITE);
        Tower t = new Tower(p);
        try {
            tested.addTower(t);
        } catch (AnotherTowerException e) {
            e.printStackTrace();
        }
        assertEquals(p,tested.getTower().getPlayer());
        Tower removed = tested.removeTower();
        assertEquals(t,removed);
        assertNull(tested.getTower());

    }
}
