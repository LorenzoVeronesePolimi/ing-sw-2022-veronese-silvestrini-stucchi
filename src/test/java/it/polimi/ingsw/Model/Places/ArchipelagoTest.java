package it.polimi.ingsw.Model.Places;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Board.BoardTwo;
import it.polimi.ingsw.Model.Cards.ForbidIsland;
import it.polimi.ingsw.Model.Cards.TowerNoValue;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.InvalidTowerNumberException;
import it.polimi.ingsw.Model.Exceptions.MergeDifferentOwnersException;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Pawns.Tower;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Test;

import java.util.*;

public class ArchipelagoTest {
    @Test
    void getNumIslands(){
        Archipelago tested = new Archipelago();
        assertEquals(1, tested.getNumIslands());
        Archipelago toMerge = new Archipelago();
        try {
            tested.mergeArchipelagos(toMerge);
        } catch (MergeDifferentOwnersException e) {
            e.printStackTrace();
        }
        assertEquals(2, tested.getNumIslands());
    }
    @Test
    void getOwner(){
        Archipelago tested = new Archipelago();
        assertNull(tested.getOwner());

        Player p = new Player("conqueror", PlayerColour.WHITE);
        List<Tower> toAdd = new ArrayList<>();
        Tower t = new Tower(p);
        toAdd.add(t);
        try {
            tested.conquerArchipelago(toAdd);
        } catch (InvalidTowerNumberException e) {
            e.printStackTrace();
        }
        assertEquals(p, tested.getOwner());
    }
    @Test
    void getForbidFlag(){
        Archipelago tested = new Archipelago();
        assertFalse(tested.getForbidFlag());

        Player p1 = new Player("p1", PlayerColour.WHITE);
        Player p2 = new Player("p2", PlayerColour.BLACK);
        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        BoardTwo board =  new BoardTwo(players);
        BoardAdvanced boardAdvanced = new BoardAdvanced(board);
        ForbidIsland card= new ForbidIsland(boardAdvanced);
        card.useEffect(6);
        assertTrue(boardAdvanced.getArchiList().get(6).getForbidFlag());
    }
    @Test
    void getTowerNoValueFlag(){
        Archipelago tested = new Archipelago();
        assertFalse(tested.getTowerNoValueFlag());

        Player p1 = new Player("p1", PlayerColour.WHITE);
        Player p2 = new Player("p2", PlayerColour.BLACK);
        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        BoardTwo board =  new BoardTwo(players);
        BoardAdvanced boardAdvanced = new BoardAdvanced(board);
        TowerNoValue card= new TowerNoValue(boardAdvanced);
        board.moveMotherNature(4);
        card.useEffect(p1);
        assertTrue(boardAdvanced.getArchiList().get(4).getTowerNoValueFlag());
    }
    @Test
    void setForbidFlag(){
        Archipelago tested = new Archipelago();
        tested.setForbidFlag(false);
        assertFalse(tested.getForbidFlag());
        tested.setForbidFlag(true);
        assertTrue(tested.getForbidFlag());
    }
    @Test
    void setTowerNoValueFlag(){
        Archipelago tested = new Archipelago();
        tested.setTowerNoValueFlag(false);
        assertFalse(tested.getTowerNoValueFlag());
        tested.setTowerNoValueFlag(true);
        assertTrue(tested.getTowerNoValueFlag());
    }
    @Test
    void howManyStudents(){
        Archipelago tested = new Archipelago();
        Map<SPColour, Integer> studentsDataCopy = new HashMap<SPColour, Integer>();
        SPColour[] availableColours = {SPColour.BLUE, SPColour.PINK, SPColour.RED, SPColour.GREEN, SPColour.YELLOW};
        for(SPColour c : availableColours){
            studentsDataCopy.put(c, 0);
        }
        assertEquals(studentsDataCopy, tested.howManyStudents());
        Student s = new Student(SPColour.BLUE);
        tested.addStudent(s);
        studentsDataCopy.put(SPColour.BLUE,1);
        s = new Student(SPColour.RED);
        tested.addStudent(s);
        studentsDataCopy.put(SPColour.RED,1);
        assertEquals(studentsDataCopy,tested.howManyStudents());
    }
    @Test
    void conquerArchipelago(){
        Archipelago tested = new Archipelago();
        Player p = new Player("conqueror", PlayerColour.WHITE);
        Tower towerToAdd = new Tower(p);
        List<Tower> towersToAdd = new ArrayList<>();
        towersToAdd.add(towerToAdd);
        try {
            assertTrue(tested.conquerArchipelago(towersToAdd).isEmpty());
            assertEquals(p,tested.getOwner());
        } catch (InvalidTowerNumberException e) {
            e.printStackTrace();
        }
    }
    @Test
    void mergeArchipelagos(){
        Archipelago tested = new Archipelago();
        Archipelago toMerge = new Archipelago();
        try {
            tested.mergeArchipelagos(toMerge);
            assertEquals(2, tested.getNumIslands());
        } catch (MergeDifferentOwnersException e) {
            e.printStackTrace();
        }
        toMerge = new Archipelago();
        try {
            tested.mergeArchipelagos(toMerge);
            assertEquals(3, tested.getNumIslands());
        } catch (MergeDifferentOwnersException e) {
            e.printStackTrace();
        }
    }
    @Test
    void addStudent(){
        Archipelago tested = new Archipelago();
        Student s = new Student(SPColour.BLUE);
        tested.addStudent(s);
        assertEquals("1", tested.howManyStudents().get(SPColour.BLUE).toString());
        s = new Student(SPColour.BLUE);
        tested.addStudent(s);
        s = new Student(SPColour.BLUE);
        tested.addStudent(s);
        assertEquals("3", tested.howManyStudents().get(SPColour.BLUE).toString());
    }
    @Test
    public void toStringTest() {
        Archipelago tested = new Archipelago();

        assertEquals("Archipelago{" +
                "studentsData=" + tested.howManyStudents() +
                ", owner=" + tested.getOwner() +
                ", numTowers=" + 0 +
                '}', tested.toString());
        Player p = new Player("conqueror", PlayerColour.WHITE);
        Tower tower = new Tower(p);
        List<Tower> towers = new ArrayList<>();
        towers.add(tower);
        try {
            tested.conquerArchipelago(towers);
        } catch (InvalidTowerNumberException e) {
            e.printStackTrace();
        }
        assertEquals("Archipelago{" +
                "studentsData=" + tested.howManyStudents() +
                ", owner=" + tested.getOwner() +
                ", numTowers=" + tested.getNumIslands() +
                '}', tested.toString());
    }
}

