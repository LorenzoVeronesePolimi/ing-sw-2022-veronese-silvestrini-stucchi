package it.polimi.ingsw.Model;

import java.util.List;

public class School {
    private Integer numberTowers;
    private Integer numberStudentsHall;
    private List<Student> studentsHall;
    private List<Student> studentsDiningRoom;

    public School(Integer numberPlayers){}

    public Player getPlayer(){}

    public Professor[] getProfessors(){}

    public void addTower(Tower tower){}
    public Tower removeTower(){}

    public void addProfessor(Professor professor){}
    public void removeProfessor (SPColour colour){}

    public void addStudentHall(Student student){}
    public Student removeStudentHall(SPColour colour){}

    public void moveStudentHallToDiningRoom(Player player, SPColour colour){}
}