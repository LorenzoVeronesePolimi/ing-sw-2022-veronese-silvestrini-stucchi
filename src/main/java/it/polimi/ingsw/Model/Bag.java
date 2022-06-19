package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.NoProfessorBagException;
import it.polimi.ingsw.Model.Exceptions.StudentNotFoundException;
import it.polimi.ingsw.Model.Pawns.Professor;
import it.polimi.ingsw.Model.Pawns.Student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * This class creates every student and professor that can be used in the game.
 */
public class Bag implements Serializable {
    private static final long serialVersionUID = 1L;
    private final List<Student> initialStudents; //Students we have at the start, to distribute among all Islands
    private final List<Student> students; //remaining students
    private final List<Professor> professors; //At the start of the game, all Professors are in the Bag

    /**
     * The constructor generates two different lists of students: one of size 10, that contains two
     * students for each colour (they will be distributed at the construction of the board, one per
     * archipelago); the other of size 120 (24 per colour), that are taken during the course of the game
     * The constructor also creates all five professors (one per colour).
     */
    public Bag(){
        SPColour[] availableColours = {SPColour.BLUE, SPColour.PINK, SPColour.RED, SPColour.GREEN, SPColour.YELLOW};
        this.initialStudents = new ArrayList<>();
        this.students = new ArrayList<>();

        // CREATE STUDENTS
        Student toAdd;
        for(SPColour c : availableColours){

            // Fill this.initialStudents with 2 Students of each SPColour
            for(int i = 0; i < 2; i++){
                toAdd = new Student(c);

                this.initialStudents.add(toAdd);
            }
            // Fill this.students with 24 Students of each SPColour
            for(int i = 0; i < 24; i++){
                toAdd = new Student(c);

                this.students.add(toAdd);
            }
        }

        this.shuffleInitial();
        this.shuffle();

        // CREATE PROFESSORS
        this.professors = new ArrayList<>();
        for(SPColour c : availableColours){
            this.professors.add(new Professor(c));
        }
    }

    // ---------------STUDENTS

    /**
     *
     * @return the list of initial students.
     */
    public List<Student> getInitialStudents(){
        //new list to return
        List<Student> removedStudents = new ArrayList<>(initialStudents);

        //remove all students from the list of initial students
        initialStudents.clear();

        return removedStudents;
    }

    //added for isGameEnded in Controller
    public int getNumStudents(){
        return this.students.size();
    }

    /**
     *
     * @param num is the number of students that the caller wants to extract.
     * @return the list of num random students from the students list.
     * @throws StudentNotFoundException is students list does not contain enough students.
     */
    public List<Student> extractStudents(int num) throws StudentNotFoundException {
        /*if(num > students.size()) {
            throw new StudentNotFoundException();
        }*/

        List<Student> extracted = new ArrayList<>();

        int i;
        for (i = 0; i < num && this.students.size() > 0; i++) {
            Student removed = this.students.get(0);
            this.students.remove(removed);
            extracted.add(removed);
        }

        //TODO: this is a possible solution to the bag problem. Also the first 3 lines of this method would have to be deleted
        /*if(this.students.size() == 0){
            throw new StudentNotFoundException();
        }*/
        if (i < num) {
            throw new StudentNotFoundException();
        }

        return extracted;
    }

    /**
     * this method inserts a student given by the caller into the student list of the bag.
     * @param student is the student that the caller wants to put in the bag.
     */
    //called by reduceColourInDining
    public void putStudent(Student student) {
        students.add(student);
    }

    //---------------PROFESSORS

    /**
     *
     * @param colourToTake is the colour of the requested professor.
     * @return the professor of that colour.
     * @throws NoProfessorBagException is the requested colour is not present in the bag.
     */
    public Professor takeProfessor(SPColour colourToTake) throws NoProfessorBagException{
        for(Professor p : this.professors){
            if(p.getColour() == colourToTake){
                this.professors.remove(p);
                return p;
            }
        }

        throw new NoProfessorBagException();
    }

    /**
     * This method organizes in a random way the students in the initialStudent list
     */
    private void shuffleInitial(){
        Collections.shuffle(this.initialStudents, new Random());
    }

    /**
     * This method organizes in a random way the students in the student list
     */
    private void shuffle(){
        Collections.shuffle(this.students, new Random());
    }
}