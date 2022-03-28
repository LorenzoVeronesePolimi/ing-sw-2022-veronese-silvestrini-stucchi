package it.polimi.ingsw.Model.Places;

import it.polimi.ingsw.Model.Board.BoardTwo;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Pawns.Professor;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Pawns.Tower;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

//import static org.junit.Assertions.assert.*;


public class SchoolTest {
    @Test
    void getPlayer(){
        Player p = new Player("owner", PlayerColour.WHITE);
        School school = new School(p,7,8);
        Assertions.assertEquals("owner", school.getPlayer().getNickname());
        Assertions.assertEquals(PlayerColour.WHITE, school.getPlayer().getColour());
    }

    @Test
    void getProfessors(){
        Player p = new Player("owner", PlayerColour.WHITE);

        Player p2 = new Player("other", PlayerColour.BLACK);
        List<Player> players = new ArrayList<>();
        players.add(p);
        players.add(p2);
        BoardTwo boardTwo = new BoardTwo(players);
        boardTwo.moveProfessor(p, SPColour.BLUE); //this method calls school method getProfessor and addProfessor
        boardTwo.moveProfessor(p, SPColour.RED);
        Assertions.assertEquals(2, boardTwo.getPlayerSchool(p).getProfessors().size());
        Assertions.assertEquals(SPColour.BLUE, boardTwo.getPlayerSchool(p).getProfessors().get(0).getColour());
        Assertions.assertEquals(SPColour.RED, boardTwo.getPlayerSchool(p).getProfessors().get(1).getColour());

    }

    @Test
    void getNumTowers(){
        Player p = new Player("owner", PlayerColour.WHITE);
        School school = new School(p,7,8);
        Assertions.assertEquals(8, school.getNumTowers());
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
        Assertions.assertEquals(7, school.getNumTowers());

        try {
            school.addTower(tower);
        } catch (ExceededMaxTowersException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(8, school.getNumTowers());
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
        Assertions.assertEquals(6, school.getNumTowers());

        towersToAdd.clear();
        towersToAdd.add(tower1);
        school.addNumTower(towersToAdd);
        Assertions.assertEquals(7, school.getNumTowers());
    }

    @Test
    void removeTower(){
        Player p = new Player("owner", PlayerColour.WHITE);
        School school = new School(p,7,8);
        school.removeTower();
        school.removeTower();
        Assertions.assertEquals(6, school.getNumTowers());
    }

    @Test
    void removeNumTowers(){
        Player p = new Player("owner", PlayerColour.WHITE);
        School school = new School(p,7,8);
        school.removeNumTowers(3);
        Assertions.assertEquals(5, school.getNumTowers());

        school.removeNumTowers(4);
        Assertions.assertEquals(1, school.getNumTowers());
    }

    @Test
    void addProfessor(){
        Player p = new Player("owner", PlayerColour.WHITE);
        School school = new School(p,7,8);
        Assertions.assertTrue(school.getProfessors().isEmpty());
        Professor prof = new Professor(SPColour.BLUE);
        school.addProfessor(prof);
        Assertions.assertEquals(1, school.getProfessors().size());
        Assertions.assertEquals(SPColour.BLUE, school.getProfessors().get(0).getColour());

        prof = new Professor(SPColour.RED);
        school.addProfessor(prof);
        Assertions.assertEquals(2, school.getProfessors().size());
        Assertions.assertEquals(SPColour.BLUE, school.getProfessors().get(0).getColour());
        Assertions.assertEquals(SPColour.RED, school.getProfessors().get(1).getColour());
    }

    @Test
    void removeProfessor(){
        Player p = new Player("owner", PlayerColour.WHITE);
        School school = new School(p,7,8);
        Professor prof = new Professor(SPColour.BLUE);
        school.addProfessor(prof);
        Assertions.assertEquals(1, school.getProfessors().size());
        Assertions.assertEquals(SPColour.BLUE, school.getProfessors().get(0).getColour());
        try {
            Assertions.assertEquals(SPColour.BLUE, school.removeProfessor(SPColour.BLUE).getColour());
        } catch (ProfessorNotFoundException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(school.getProfessors().isEmpty());
        Assertions.assertThrows(ProfessorNotFoundException.class, () -> school.removeProfessor(SPColour.RED));
    }

    @Test
    void addStudentHall(){
        Player p = new Player("owner", PlayerColour.WHITE);
        School school = new School(p,7,8);
        Assertions.assertEquals(0,school.getStudentsHall().size());
        Student s = new Student(SPColour.BLUE);
        try {
            school.addStudentHall(s);
        } catch (ExceededMaxStudentsHallException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(1,school.getStudentsHall().size());

        s = new Student(SPColour.RED);
        try {
            school.addStudentHall(s);
        } catch (ExceededMaxStudentsHallException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(2, school.getStudentsHall().size());
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

        Assertions.assertEquals(1, school.getStudentsHall().size());
        Assertions.assertEquals(SPColour.RED, school.getStudentsHall().get(0).getColour());

        Assertions.assertThrows(StudentNotFoundException.class, () -> school.removeStudentHall(SPColour.YELLOW));
    }

    @Test
    void addStudentDiningRoom(){
        Player p = new Player("owner", PlayerColour.WHITE);
        School school = new School(p,7,8);
        Student s = new Student(SPColour.BLUE);
        try {
            Assertions.assertEquals(0,school.getNumStudentColour(SPColour.BLUE));
        } catch (WrongColourException e) {
            e.printStackTrace();
        }
        try {
            school.addStudentDiningRoom(s);
        } catch (ExceededMaxStudentsDiningRoomException e) {
            e.printStackTrace();
        }
        try {
            Assertions.assertEquals(1,school.getNumStudentColour(SPColour.BLUE));
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
            Assertions.assertEquals(2,school.getNumStudentColour(SPColour.BLUE));
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
            Assertions.assertEquals(2, school.getNumStudentColour(SPColour.BLUE));
        } catch (WrongColourException e) {
            e.printStackTrace();
        }

        try {
            returned = school.removeStudentDiningRoom(SPColour.BLUE);
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }

        try {
            Assertions.assertEquals(1,school.getNumStudentColour(SPColour.BLUE));
            Assertions.assertEquals(SPColour.BLUE, returned.getColour());
        } catch (WrongColourException e) {
            e.printStackTrace();
        }

        try {
            school.removeStudentDiningRoom(SPColour.BLUE);
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }

        try {
            Assertions.assertEquals(0,school.getNumStudentColour(SPColour.BLUE));
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

        try {
            school.moveStudentHallToDiningRoom(SPColour.BLUE);
        } catch (StudentNotFoundException | ExceededMaxStudentsDiningRoomException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(school.getStudentsHall().isEmpty());

        try {
            Assertions.assertEquals(1,school.getNumStudentColour(SPColour.BLUE));
            Assertions.assertThrows(StudentNotFoundException.class, () -> school.moveStudentHallToDiningRoom(SPColour.BLUE));
        } catch (WrongColourException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < 9; i++) {
            s = new Student(SPColour.BLUE);
            try {
                school.addStudentDiningRoom(s);
            } catch (ExceededMaxStudentsDiningRoomException e) {
                e.printStackTrace();
            }
        }

        s = new Student(SPColour.BLUE);

        Student finalS = s;
        Assertions.assertThrows(ExceededMaxStudentsDiningRoomException.class, () -> school.addStudentDiningRoom(finalS));
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
            Assertions.assertEquals(1, school.getNumStudentColour(SPColour.BLUE));
        } catch (WrongColourException e) {
            e.printStackTrace();
        }
        try {
            Assertions.assertEquals(1, school.getNumStudentColour(SPColour.RED));
        } catch (WrongColourException e) {
            e.printStackTrace();
        }
        try {
            Assertions.assertEquals(1, school.getNumStudentColour(SPColour.GREEN));
        } catch (WrongColourException e) {
            e.printStackTrace();
        }
        try {
            Assertions.assertEquals(1, school.getNumStudentColour(SPColour.YELLOW));
        } catch (WrongColourException e) {
            e.printStackTrace();
        }
        try {
            Assertions.assertEquals(1, school.getNumStudentColour(SPColour.PINK));
        } catch (WrongColourException e) {
            e.printStackTrace();
        }
    }

    @Test
    void toStringTest(){

        Player p = new Player("owner", PlayerColour.WHITE);
        School school = new School(p,7,8);
        try {
            Assertions.assertEquals("School{" +
                    "player=" + school.getPlayer() +
                    ", studentsHall=" + school.getStudentsHall() +
                    ", studentsDiningRed=" + school.getListStudentColour(SPColour.RED) +
                    ", studentsDiningPink=" + school.getListStudentColour(SPColour.PINK) +
                    ", studentsDiningGreen=" + school.getListStudentColour(SPColour.GREEN) +
                    ", studentsDiningYellow=" + school.getListStudentColour(SPColour.YELLOW) +
                    ", studentsDiningBlue=" + school.getListStudentColour(SPColour.BLUE) +
                    ", professors=" + school.getProfessors() +
                    ", towers=" + school.getTowers() +
                    '}', school.toString());
        } catch (WrongColourException e) {
            e.printStackTrace();
        }
    }
}
