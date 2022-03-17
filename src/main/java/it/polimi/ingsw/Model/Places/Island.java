package it.polimi.ingsw.Model.Places;

import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.AnotherTowerException;
import it.polimi.ingsw.Model.Exceptions.NoStudentMatchColourException;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Pawns.Tower;

import java.util.*;

public class Island {
    private Set<Student> students;
    private Tower tower;

    public Island(){
        this.students = new HashSet<Student>();
        this.tower = null;
    }

    // return a Map like: RED -> 3 (Students of colour RED)
    public Map<SPColour, Integer> howManyStudents(){
        Map<SPColour, Integer> data = new HashMap<SPColour, Integer>();

        data.put(SPColour.BLUE, 0);
        data.put(SPColour.RED, 0);
        data.put(SPColour.PINK, 0);
        data.put(SPColour.GREEN, 0);
        data.put(SPColour.YELLOW, 0);

        for(Student student : this.students){
            data.replace(student.getColour(), data.get(student.getColour()) + 1); //add 1 for each Student of a certain SPColour you find
        }

        return data;
    }

    public void addStudent(Student studentToAdd) {
        this.students.add(studentToAdd);
    }

    public Student removeStudent(SPColour colourToRemove) throws NoStudentMatchColourException {
        for(Student student : this.students){
            if (student.getColour().equals(colourToRemove)){
                Student removed;
                removed = student;
                this.students.remove(student);

                return removed;
            }
        }
        throw new NoStudentMatchColourException();
    }

    public void addTower(Tower toAdd) throws AnotherTowerException {
        if(this.tower == null){ // No tower has been added, yet
            this.tower = toAdd;
        }
        else{
            throw new AnotherTowerException();
        }
    }

    public Tower removeTower(){
        Tower removed;

        removed = this.tower;

        return removed;
    }

}
