package it.polimi.ingsw.Model.Places;

import it.polimi.ingsw.Model.Board.BoardTwo;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Pawns.Professor;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Pawns.Tower;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;



public class SchoolTest {
    @Test
    void getPlayer(){
        Player p = new Player("owner", PlayerColour.WHITE);
        School school = new School(p,7,8);
        assertEquals("owner", school.getPlayer().getNickname());
        assertEquals(PlayerColour.WHITE, school.getPlayer().getColour());
    }

    @Test
    void getProfessors(){
        Player p = new Player("owner", PlayerColour.WHITE);
        School school = new School(p,7,8);
        assertTrue(school.getProfessors().isEmpty());

        /*Player p2 = new Player("other", PlayerColour.BLACK);
        List<Player> players = new ArrayList<>();
        players.add(p);
        players.add(p2);
        BoardTwo boardTwo = new BoardTwo(players);
        boardTwo.moveProfessor(p, SPColour.BLUE);
        boardTwo.moveProfessor(p, SPColour.RED);
        assertEquals(2, school.getProfessors().size());*/
    }

    @Test
    void getNumTowers(){
        Player p = new Player("owner", PlayerColour.WHITE);
        School school = new School(p,7,8);
        assertEquals(8, school.getNumTowers());
    }

    @Test
    void addTower(){
        Player p = new Player("owner", PlayerColour.WHITE);
        School school = new School(p,7,8);
        Tower tower = new Tower(p);
        school.removeTower();
        school.removeTower();
        try {
            school.addTower(tower);
        } catch (ExceededMaxTowersException e) {
            e.printStackTrace();
        }
        assertEquals(7, school.getNumTowers());

        try {
            school.addTower(tower);
        } catch (ExceededMaxTowersException e) {
            e.printStackTrace();
        }
        assertEquals(8, school.getNumTowers());
    }

    @Test
    void addNumTower(){
        Player p = new Player("owner", PlayerColour.WHITE);
        School school = new School(p,7,8);
        Tower tower1 = new Tower(p);
        Tower tower2 = new Tower(p);
        Tower tower3 = new Tower(p);
        Tower tower4 = new Tower(p);
        List<Tower> towersToAdd = new ArrayList<>();
        towersToAdd.add(tower1);
        towersToAdd.add(tower2);
        towersToAdd.add(tower3);
        towersToAdd.add(tower4);
        school.removeNumTowers(6);
        school.addNumTower(towersToAdd);
        assertEquals(6, school.getNumTowers());

        towersToAdd.clear();
        towersToAdd.add(tower1);
        school.addNumTower(towersToAdd);
        assertEquals(7, school.getNumTowers());
    }

    @Test
    void removeTower(){
        Player p = new Player("owner", PlayerColour.WHITE);
        School school = new School(p,7,8);
        Tower tower = new Tower(p);
        school.removeTower();
        school.removeTower();
        assertEquals(6, school.getNumTowers());
    }

    @Test
    void removeNumTowers(){
        Player p = new Player("owner", PlayerColour.WHITE);
        School school = new School(p,7,8);
        school.removeNumTowers(3);
        assertEquals(5, school.getNumTowers());

        school.removeNumTowers(4);
        assertEquals(1, school.getNumTowers());
    }

    @Test
    void addProfessor(){
        Player p = new Player("owner", PlayerColour.WHITE);
        School school = new School(p,7,8);
        assertTrue(school.getProfessors().isEmpty());
        Professor prof = new Professor(SPColour.BLUE);
        school.addProfessor(prof);
        assertEquals(1, school.getProfessors().size());
        assertEquals(SPColour.BLUE, school.getProfessors().get(0).getColour());

        prof = new Professor(SPColour.RED);
        school.addProfessor(prof);
        assertEquals(2, school.getProfessors().size());
        assertEquals(SPColour.BLUE, school.getProfessors().get(0).getColour());
        assertEquals(SPColour.RED, school.getProfessors().get(1).getColour());
    }

    @Test
    void removeProfessor(){
        Player p = new Player("owner", PlayerColour.WHITE);
        School school = new School(p,7,8);
        Professor prof = new Professor(SPColour.BLUE);
        school.addProfessor(prof);
        assertEquals(1, school.getProfessors().size());
        assertEquals(SPColour.BLUE, school.getProfessors().get(0).getColour());
        try {
            assertEquals(SPColour.BLUE, school.removeProfessor(SPColour.BLUE).getColour());
        } catch (ProfessorNotFoundException e) {
            e.printStackTrace();
        }
        assertTrue(school.getProfessors().isEmpty());
        try {
            school.removeProfessor(SPColour.RED);
        } catch (ProfessorNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void addStudentHall(){
        Player p = new Player("owner", PlayerColour.WHITE);
        School school = new School(p,7,8);
        assertEquals(0,school.getStudentsHall().size());
        Student s = new Student(SPColour.BLUE);
        try {
            school.addStudentHall(s);
        } catch (ExceededMaxStudentsHallException e) {
            e.printStackTrace();
        }
        assertEquals(1,school.getStudentsHall().size());

        s = new Student(SPColour.RED);
        try {
            school.addStudentHall(s);
        } catch (ExceededMaxStudentsHallException e) {
            e.printStackTrace();
        }
        assertEquals(2, school.getStudentsHall().size());
    }

    @Test
    void removeStudentHall(){
        Player p = new Player("owner", PlayerColour.WHITE);
        School school = new School(p,7,8);
        Student s = new Student(SPColour.BLUE);
        try {
            school.addStudentHall(s);
        } catch (ExceededMaxStudentsHallException e) {
            e.printStackTrace();
        }
        s = new Student(SPColour.RED);
        try {
            school.addStudentHall(s);
        } catch (ExceededMaxStudentsHallException e) {
            e.printStackTrace();
        }
        try {
            school.removeStudentHall(SPColour.BLUE);
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(1, school.getStudentsHall().size());
        assertEquals(SPColour.RED, school.getStudentsHall().get(0).getColour());
        try {
            school.removeStudentHall(SPColour.YELLOW);
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void addStudentDiningRoom(){
        Player p = new Player("owner", PlayerColour.WHITE);
        School school = new School(p,7,8);
        Student s = new Student(SPColour.BLUE);
        try {
            assertEquals(0,school.getNumStudentColour(SPColour.BLUE));
        } catch (WrongColourException e) {
            e.printStackTrace();
        }
        try {
            school.addStudentDiningRoom(s);
        } catch (ExceededMaxStudentsDiningRoomException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(1,school.getNumStudentColour(SPColour.BLUE));
        } catch (WrongColourException e) {
            e.printStackTrace();
        }

        s = new Student(SPColour.BLUE);
        try {
            school.addStudentDiningRoom(s);
        } catch (ExceededMaxStudentsDiningRoomException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(2,school.getNumStudentColour(SPColour.BLUE));
        } catch (WrongColourException e) {
            e.printStackTrace();
        }
    }

    @Test
    void removeStudentDiningRoom(){
        Player p = new Player("owner", PlayerColour.WHITE);
        School school = new School(p,7,8);
        Student s = new Student(SPColour.BLUE);
        Student returned = null;
        try {
            school.addStudentDiningRoom(s);
        } catch (ExceededMaxStudentsDiningRoomException e) {
            e.printStackTrace();
        }
        s = new Student(SPColour.BLUE);
        try {
            school.addStudentDiningRoom(s);
        } catch (ExceededMaxStudentsDiningRoomException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(2,school.getNumStudentColour(SPColour.BLUE));
        } catch (WrongColourException e) {
            e.printStackTrace();
        }
        try {
            returned = school.removeStudentDiningRoom(SPColour.BLUE);
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(1,school.getNumStudentColour(SPColour.BLUE));
            assertEquals(SPColour.BLUE, returned.getColour());
        } catch (WrongColourException e) {
            e.printStackTrace();
        }
        try {
            school.removeStudentDiningRoom(SPColour.BLUE);
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(0,school.getNumStudentColour(SPColour.BLUE));
        } catch (WrongColourException e) {
            e.printStackTrace();
        }
    }

    @Test
    void moveStudentHallToDiningRoom(){
        Player p = new Player("owner", PlayerColour.WHITE);
        School school = new School(p,7,8);
        Student s = new Student(SPColour.BLUE);
        try {
            school.addStudentHall(s);
        } catch (ExceededMaxStudentsHallException e) {
            e.printStackTrace();
        }
        school.moveStudentHallToDiningRoom(SPColour.BLUE);
        assertTrue(school.getStudentsHall().isEmpty());
        try {
            assertEquals(1,school.getNumStudentColour(SPColour.BLUE));
        } catch (WrongColourException e) {
            e.printStackTrace();
        }
        school.moveStudentHallToDiningRoom(SPColour.BLUE);
    }

    @Test
    void getNumStudentColour(){
        Player p = new Player("owner", PlayerColour.WHITE);
        School school = new School(p,7,8);
        Student s = new Student(SPColour.BLUE);
        try {
            school.addStudentDiningRoom(s);
        } catch (ExceededMaxStudentsDiningRoomException e) {
            e.printStackTrace();
        }
        s = new Student(SPColour.RED);
        try {
            school.addStudentDiningRoom(s);
        } catch (ExceededMaxStudentsDiningRoomException e) {
            e.printStackTrace();
        }
        s = new Student(SPColour.GREEN);
        try {
            school.addStudentDiningRoom(s);
        } catch (ExceededMaxStudentsDiningRoomException e) {
            e.printStackTrace();
        }s = new Student(SPColour.YELLOW);
        try {
            school.addStudentDiningRoom(s);
        } catch (ExceededMaxStudentsDiningRoomException e) {
            e.printStackTrace();
        }s = new Student(SPColour.PINK);
        try {
            school.addStudentDiningRoom(s);
        } catch (ExceededMaxStudentsDiningRoomException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(1, school.getNumStudentColour(SPColour.BLUE));
        } catch (WrongColourException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(1, school.getNumStudentColour(SPColour.RED));
        } catch (WrongColourException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(1, school.getNumStudentColour(SPColour.GREEN));
        } catch (WrongColourException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(1, school.getNumStudentColour(SPColour.YELLOW));
        } catch (WrongColourException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(1, school.getNumStudentColour(SPColour.PINK));
        } catch (WrongColourException e) {
            e.printStackTrace();
        }
    }

    @Test
    void toStringTest(){
        /*
        Player p = new Player("owner", PlayerColour.WHITE);
        School school = new School(p,7,8);
        assertEquals("School{" +
                "player=" + getPlayer() +
                ", studentsHall=" + school.getStudentsHall() +
                ", studentsDiningRed=" + null +
                ", studentsDiningPink=" + null +
                ", studentsDiningGreen=" + null +
                ", studentsDiningYellow=" + null +
                ", studentsDiningBlue=" + null +
                ", professors=" + school.getProfessors() +
                ", towers=" + school.getTowers() +
                '}', school.toString());*/
    }
}
