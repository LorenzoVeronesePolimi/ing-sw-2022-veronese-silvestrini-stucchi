package it.polimi.ingsw.Model.Places;

import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Pawns.Professor;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Pawns.Tower;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class School {
    private Player player;
    private final int numMaxStudentsHall;
    private final int numMaxTowers;
    private List<Student> studentsHall;
    private List<Student> studentsDiningRed;
    private List<Student> studentsDiningPink;
    private List<Student> studentsDiningGreen;
    private List<Student> studentsDiningYellow;
    private List<Student> studentsDiningBlue;
    private List<Professor> professors;     //TODO: Set is better
    private List<Tower> towers;

    public School(Player player, int numStudentsHall, int numTowers){
        this.player = player;
        this.numMaxStudentsHall = numStudentsHall;
        this.numMaxTowers = numTowers;
        this.studentsHall = new ArrayList<>();
        this.studentsDiningRed = new LinkedList<>();
        this.studentsDiningPink = new LinkedList<>();
        this.studentsDiningGreen = new LinkedList<>();
        this.studentsDiningYellow = new LinkedList<>();
        this.studentsDiningBlue = new LinkedList<>();
        this.professors = new ArrayList<>();
        this.towers = new ArrayList<>();

        for (int i = 0; i < numMaxTowers; i++) {
            Tower t = new Tower(player);
            towers.add(t);
        }
    }

    public Player getPlayer(){ return this.player; }

    public List<Professor> getProfessors(){ return professors; }

    public void addTower(Tower tower) throws ExceededMaxTowersException{
        try {
            if(towers.size()< numMaxTowers){
                towers.add(tower);
            }
            else{
             throw new ExceededMaxTowersException();
            }
        } catch (ExceededMaxTowersException e) {
            e.printStackTrace();
        }
    }

    public Tower removeTower(){
         return towers.remove(towers.indexOf(towers.size() - 1));
    }

    public void addProfessor(Professor professor){
        professors.add(professor);
    }

    public Professor removeProfessor (SPColour colour) throws ProfessorNotFoundException {
        for(Professor pr : professors) {
            if(pr.getColour().equals(colour)) {
                return professors.remove(professors.indexOf(pr));
            }
        }

        throw new ProfessorNotFoundException();
    }

    public void addStudentHall(Student student) throws ExceededMaxStudentsHallException{
        if(studentsHall.size() < numMaxStudentsHall) {
            studentsHall.add(student);
        } else {
            throw new ExceededMaxStudentsHallException();
        }
    }

    public Student removeStudentHall(SPColour colour) throws StudentNotFoundException{
        for(Student st : studentsHall) {
            if(st.getColour().equals(colour)) {
                return studentsHall.remove(studentsHall.indexOf(st));
            }
        }

        throw new StudentNotFoundException();
    }

    public void addStudentDiningRoom(Student student) throws ExceededMaxStudentsDiningRoomException{
        if(student.getColour() == SPColour.RED) {
            if(studentsDiningRed.size() < 10) {
                studentsDiningRed.add(student);
            } else {
                throw new ExceededMaxStudentsDiningRoomException();
            }
        }
        if(student.getColour() == SPColour.PINK) {
            if(studentsDiningPink.size() < 10) {
                studentsDiningPink.add(student);
            } else {
                throw new ExceededMaxStudentsDiningRoomException();
            }
        }
        if(student.getColour() == SPColour.GREEN) {
            if(studentsDiningGreen.size() < 10) {
                studentsDiningGreen.add(student);
            } else {
                throw new ExceededMaxStudentsDiningRoomException();
            }
        }
        if(student.getColour() == SPColour.BLUE) {
            if(studentsDiningBlue.size() < 10) {
                studentsDiningBlue.add(student);
            } else {
                throw new ExceededMaxStudentsDiningRoomException();
            }
        }
        if(student.getColour() == SPColour.YELLOW) {
            if(studentsDiningYellow.size() < 10) {
                studentsDiningYellow.add(student);
            } else {
                throw new ExceededMaxStudentsDiningRoomException();
            }
        }
    }

    public void moveStudentHallToDiningRoom(SPColour colour) {
        Student student = null;
        try {
            student = removeStudentHall(colour);
            addStudentDiningRoom(student);
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        } catch (ExceededMaxStudentsDiningRoomException e) {
            e.printStackTrace();
        }
    }
}