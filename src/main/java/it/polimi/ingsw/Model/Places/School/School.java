package it.polimi.ingsw.Model.Places.School;

import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Pawns.Professor;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Pawns.Tower;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a school in the game.
 * A school contains a certain amount of students and towers, specified by the type of match played.
 * Also, a school can contain professors in relation to the status of the match and accordingly to the rules of the game.
 * It is divided in a hall, in a dining room -with places for students and professors- and in a place to put towers.
 */
public class School implements Serializable {
    private static final long serialVersionUID = 1L;
    protected Player player;

    protected transient int numMaxStudentsHall;
    protected transient int numMaxTowers;
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
     * This constructor clones the School and calls for constructor of its attributes to clone them.
     * @param school School that needs to be cloned.
     */
    public School(School school) {
        this.player = new Player(school.player);
        this.studentsHall = school.studentsHall;
        this.studentsDiningBlue = school.studentsDiningBlue;
        this.studentsDiningPink = school.studentsDiningPink;
        this.studentsDiningRed = school.studentsDiningRed;
        this.studentsDiningGreen = school.studentsDiningGreen;
        this.studentsDiningYellow = school.studentsDiningYellow;
        this.professors = school.professors;
        this.towers = school.towers;
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
     * This method removes a number of towers from the school.
     * @param num number of towers to remove.
     * @return the list of towers removed from the school.
     * @throws TowerNotFoundException when there aren't towers left in the school.
     */
    public List<Tower> removeNumTowers(int num) throws TowerNotFoundException {
        List<Tower> removed = new ArrayList<>();
        for(int i = 0; i < num; i++){
            removed.add(removeTower());
        }

        return removed;
    }

    /**
     * This method add a professor to the school.
     * @param professor Professor to add to the school.
     */
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

    /**
     * This method adds a student to the hall of the school.
     * @param student Student to add in the hall of the school.
     * @throws ExceededMaxStudentsHallException when there is no space for the student in the hall, that is already full,
     *                                          accordingly to the numMaxStudentsHall constant.
     */
    public void addStudentHall(Student student) throws ExceededMaxStudentsHallException{
        if(studentsHall.size() < numMaxStudentsHall) {
            studentsHall.add(student);
        } else {
            throw new ExceededMaxStudentsHallException();
        }
    }

    /**
     * This method return a student, removing it from the hall of the school.
     * @param colour Colour of the student to remove.
     * @return The student removed.
     * @throws StudentNotFoundException when there are no students with the specified colours in the hall, or when the hall
     *                                  is empty.
     */
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

    /**
     * This method adds a student to the dining room of the school.
     * @param student Student to add in the dining room, in relation with the colour of it.
     * @throws ExceededMaxStudentsDiningRoomException when there are already 10 student in the dining room
     *                                                of the corresponding colour.
     */
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

    /**
     * This method return and removes a student from the dining room on the school, based on the colour of the student.
     * @param colour Colour of the student to remove from the dining room.
     * @return The student removed from the dining room.
     * @throws StudentNotFoundException when there are no students left in the dining room of the student colour.
     */
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

    /**
     * This method transfers a student from the hall to the dining room, based on the student colour.
     * @param colour Colour of the student that needs to be removed from the hall and placed in the dining room.
     * @throws StudentNotFoundException where there are no students of the specified colour in the hall.
     * @throws ExceededMaxStudentsDiningRoomException when there are already 10 students of the specified colour in the
     *                                                dining room.
     */
    public void moveStudentHallToDiningRoom(SPColour colour) throws StudentNotFoundException, ExceededMaxStudentsDiningRoomException {
        Student student;

        student = removeStudentHall(colour);
        addStudentDiningRoom(student);
    }

    /**
     * @param colour color of students of which we want to know the number in the dining room.
     * @return the number of students, in the dining room, of the specified colour.
     */
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

    /**
     *
     * @param colour color of students of which we want to know the number in the dining room.
     * @return List of the students in the dining room having the specified colour.
     */
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

    /**
     * @return The maximum number of towers in the school.
     */
    public int getNumMaxTowers(){
        return numMaxTowers;
    }

    /**
     * @return The maximum number of students in the school hall.
     */
    public int getNumMaxStudentsHall(){
        return numMaxStudentsHall;
    }

    /**
     * Method toString of the structure of the class.
     * @return The description of the class.
     */
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
