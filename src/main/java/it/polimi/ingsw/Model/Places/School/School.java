package it.polimi.ingsw.Model.Places.School;

import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Pawns.Coin;
import it.polimi.ingsw.Model.Pawns.Professor;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Pawns.Tower;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class School {
    protected Player player;

    protected final int numMaxStudentsHall;
    protected final int numMaxTowers;
    protected List<Student> studentsHall;
    protected List<Student> studentsDiningRed;
    protected List<Student> studentsDiningPink;
    protected List<Student> studentsDiningGreen;
    protected List<Student> studentsDiningYellow;
    protected List<Student> studentsDiningBlue;
    protected List<Professor> professors;
    protected List<Tower> towers;

    public School(Player player, int numMaxStudentsHall, int numTowers){
        this.player = player;
        this.numMaxStudentsHall = numMaxStudentsHall;
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

    public List<Student> getStudentsHall() {
        List<Student> newStudentsHall = new ArrayList<>();

        for(Student s : this.studentsHall){
            newStudentsHall.add(s);
        }
        return newStudentsHall;
    }

    public int getNumTowers(){
        return towers.size();
    }

    public List<Tower> getTowers(){
        return towers;
    }

    public void addTower(Tower tower) throws ExceededMaxTowersException{
        try {
            if(this.getNumTowers()< numMaxTowers){
                towers.add(tower);
            }
            else{
             throw new ExceededMaxTowersException();
            }
        } catch (ExceededMaxTowersException e) {
            e.printStackTrace();
        }
    }

    public void addNumTower(List<Tower> toAdd){
        for(Tower t : toAdd){
            try{
                this.addTower(t);
            } catch(ExceededMaxTowersException ex){ex.printStackTrace();}
        }
    }

    public Tower removeTower(){
         return towers.remove(0);
    }

    public List<Tower> removeNumTowers(int num){
        List<Tower> removed = new ArrayList<Tower>();
        for(int i = 0; i < num; i++){
            removed.add(removeTower());
        }

        return removed;
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
        boolean found = false;
        Student removed = null;

        for(Student st : studentsHall) {
            if(st.getColour().equals(colour) && !found) {
                removed = st;
                found = true;
            }
        }

        if(found) {
            studentsHall.remove(removed);
            return removed;
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

    public Student removeStudentDiningRoom(SPColour colour) throws StudentNotFoundException{
        if(colour.equals(SPColour.RED)) {
            if(studentsDiningRed.size() >0) {
                return studentsDiningRed.remove(studentsDiningRed.size()-1);
            } else {
                throw new StudentNotFoundException();
            }
        }
        if(colour.equals(SPColour.PINK)) {
            if(studentsDiningPink.size() > 0) {
                return studentsDiningPink.remove(studentsDiningPink.size()-1);
            } else {
                throw new StudentNotFoundException();
            }
        }
        if(colour.equals(SPColour.GREEN)) {
            if(studentsDiningGreen.size() > 0) {
                return studentsDiningGreen.remove(studentsDiningGreen.size()-1);
            } else {
                throw new StudentNotFoundException();
            }
        }
        if(colour.equals(SPColour.BLUE)) {
            if(studentsDiningBlue.size() > 0) {
                return studentsDiningBlue.remove(studentsDiningBlue.size()-1);
            } else {
                throw new StudentNotFoundException();
            }
        }
        if(colour.equals(SPColour.YELLOW)) {
            if(studentsDiningYellow.size() > 0) {
                return studentsDiningYellow.remove(studentsDiningYellow.size()-1);
            } else {
                throw new StudentNotFoundException();
            }
        }

        throw new StudentNotFoundException();
    }

    public void moveStudentHallToDiningRoom(SPColour colour) throws StudentNotFoundException, ExceededMaxStudentsDiningRoomException {
        Student student = null;

        student = removeStudentHall(colour);
        addStudentDiningRoom(student);
    }

    public int getNumStudentColour(SPColour colour) throws WrongColourException {
        if(colour == SPColour.RED) {
           return studentsDiningRed.size();
        }
        if(colour == SPColour.BLUE) {
            return studentsDiningBlue.size();
        }
        if(colour == SPColour.GREEN) {
            return studentsDiningGreen.size();
        }
        if(colour == SPColour.PINK) {
            return studentsDiningPink.size();
        }
        if(colour == SPColour.YELLOW) {
            return studentsDiningYellow.size();
        }

        throw new WrongColourException();
    }

    public List<Student> getListStudentColour(SPColour colour) throws WrongColourException {
        if(colour == SPColour.RED) {
            return studentsDiningRed;
        }
        if(colour == SPColour.BLUE) {
            return studentsDiningBlue;
        }
        if(colour == SPColour.GREEN) {
            return studentsDiningGreen;
        }
        if(colour == SPColour.PINK) {
            return studentsDiningPink;
        }
        if(colour == SPColour.YELLOW) {
            return studentsDiningYellow;
        }

        throw new WrongColourException();
    }

    public int getNumMaxTowers(){
        return numMaxTowers;
    }

    public int getNumMaxStudentsHall(){
        return numMaxStudentsHall;
    }

    @Override
    public String toString() {
        return "School{" +
                "player=" + player +
                ", studentsHall=" + studentsHall +
                ", studentsDiningRed=" + studentsDiningRed +
                ", studentsDiningPink=" + studentsDiningPink +
                ", studentsDiningGreen=" + studentsDiningGreen +
                ", studentsDiningYellow=" + studentsDiningYellow +
                ", studentsDiningBlue=" + studentsDiningBlue +
                ", professors=" + professors +
                ", towers=" + towers +
                '}';
    }
}