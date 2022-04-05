package it.polimi.ingsw.Model.Places.School;

import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Pawns.Professor;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Pawns.Tower;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a school in the game.
 * A school contains a certain amount of students and towers, specified by the type of match played.
 * Also, a school can contain professors in relation to the status of the match and accordingly to the rules of the game.
 * It is divided in a hall, in a dining room -with places for students and professors- and in a place to put towers.
 */
public class School {
    protected final Player player;

    protected final int numMaxStudentsHall;
    protected final int numMaxTowers;
    protected final List<Student> studentsHall;
    protected final List<Student> studentsDiningRed;
    protected final List<Student> studentsDiningPink;
    protected final List<Student> studentsDiningGreen;
    protected final List<Student> studentsDiningYellow;
    protected final List<Student> studentsDiningBlue;
    protected final List<Professor> professors;
    protected final List<Tower> towers;

    /**
     * This method create a school and creates a certain amount of towers accordingly to the rules of the specific match.
     * A match with two players needs 8 towers for each school.
     * A match with three players needs 6 towers for each school.
     * A match with four players needs 8 towers placed in only one school of the team (two players are a team).
     * @param player owner of the school.
     * @param numMaxStudentsHall maximum number of students that can be placed in the hall of the school.
     * @param numTowers maximum number of towers that can be placed in the school.
     */
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

    /**
     * @return the owner of the school.
     */
    public Player getPlayer(){ return this.player; }

    /**
     * @return the list of professors in the school.
     */
    public List<Professor> getProfessors(){ return this.professors; }

    /**
     * @return the list of students in the school hall.
     */
    public List<Student> getStudentsHall() {
        return new ArrayList<>(this.studentsHall);
    }

    /**
     * @return the dimension of the towers remaining in the school.
     */
    public int getNumTowers(){
        return towers.size();
    }

    /**
     * @return a copy of the towers in the school.
     */
    public List<Tower> getTowers(){
        return towers;
    }

    /**
     * This method adds a tower in the school, only if there is space for it. (the number of maximum towers has not
     * been exceeded).
     * @param tower Tower to add in the school.
     * @throws ExceededMaxTowersException when there are already enough tower in the school, without exceeding the
     *                                    numMaxTowers value.
     */
    public void addTower(Tower tower) throws ExceededMaxTowersException{
        if(this.getNumTowers() < numMaxTowers){
            towers.add(tower);
        } else {
         throw new ExceededMaxTowersException();
        }
    }

    /**
     * This method adds a list of towers in the school.
     * It is called when two island are merged into one archipelago that, if conquered, needs to be cleared from all the
     * towers.
     * @param toAdd List of towers to add.
     * @throws ExceededMaxTowersException when there are already enough tower in the school, without exceeding the
     *                                    numMaxTowers value.
     */
    public void addNumTower(List<Tower> toAdd) throws ExceededMaxTowersException {
        for(Tower t : toAdd){
            this.addTower(t);
        }
    }

    /**
     * This method removes a tower from the school and returns it.
     * @return a tower from the school, removing it.
     * @throws TowerNotFoundException when there are no towers left in the school.
     */
    public Tower removeTower() throws TowerNotFoundException {
        if(towers.size() > 0)
            return towers.remove(0);

        throw new TowerNotFoundException();
    }

    /**
     *
     * @param num
     * @return
     * @throws TowerNotFoundException
     */
    public List<Tower> removeNumTowers(int num) throws TowerNotFoundException {
        List<Tower> removed = new ArrayList<>();
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
        Student student;

        student = removeStudentHall(colour);
        addStudentDiningRoom(student);
    }

    public int getNumStudentColour(SPColour colour) {
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

        return 0;
    }

    public List<Student> getListStudentColour(SPColour colour) {
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

        return null;
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
