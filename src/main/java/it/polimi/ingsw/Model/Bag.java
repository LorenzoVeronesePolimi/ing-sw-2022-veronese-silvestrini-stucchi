package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.NoProfessorBagException;
import it.polimi.ingsw.Model.Pawns.Professor;
import it.polimi.ingsw.Model.Pawns.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Bag {
    private List<Student> initialStudents; //Students we have at the start, to distribute among all Islands
    private List<Student> students; //remaining students
    private List<Professor> professors; //At the start of the game, all Professors are in the Bag

    private static Bag instance;

    public static Bag instance() {
        if(instance == null) {
            instance = new Bag();
        }

        return instance;
    }
    private Bag(){
        SPColour[] availableColours = {SPColour.BLUE, SPColour.PINK, SPColour.RED, SPColour.GREEN, SPColour.YELLOW};
        this.initialStudents = new ArrayList<Student>();
        this.students = new ArrayList<Student>();

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
        this.professors = new ArrayList<Professor>();
        for(SPColour c : availableColours){
            this.professors.add(new Professor(c));
        }
    }

    // ---------------STUDENTS
    public List<Student> getInitialStudents(){
        return this.initialStudents; // I want to modify it directly: no clone needed
    }

    public List<Student> extractStudents(int num){
        List<Student> extracted = new ArrayList<Student>();

        for(int i = 0; i < num; i++){
            Student removed = this.students.get(0);
            this.students.remove(removed);
            extracted.add(removed);
        }

        return extracted;
    }

    //---------------PROFESSORS
    public Professor takeProfessor(SPColour colourToTake) throws NoProfessorBagException{
        for(Professor p : this.professors){
            if(p.getColour() == colourToTake){
                this.professors.remove(p);
                return p;
            }
        }

        throw new NoProfessorBagException();
    }

    private void shuffleInitial(){
        Collections.shuffle(this.initialStudents, new Random());
    }

    private void shuffle(){
        Collections.shuffle(this.students, new Random());
    }
}