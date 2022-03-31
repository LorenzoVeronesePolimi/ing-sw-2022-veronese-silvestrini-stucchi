package it.polimi.ingsw.Model.Places;

import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.AnotherTowerException;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Pawns.Tower;

import java.util.*;

public class Island {
    private final Set<Student> students;
    private Tower tower;

    public Island(){
        this.students = new HashSet<>();
        this.tower = null;
    }

    // return a Map like: RED -> 3 (Students of colour RED)
    public Map<SPColour, Integer> howManyStudents(){
        Map<SPColour, Integer> data = new HashMap<>();

        SPColour[] availableColours = {SPColour.BLUE, SPColour.PINK, SPColour.RED, SPColour.GREEN, SPColour.YELLOW};
        for(SPColour c : availableColours){
            data.put(c, 0);
        }
        for(Student student : this.students){
            data.replace(student.getColour(), data.get(student.getColour()) + 1); //add 1 for each Student of a certain SPColour you find
        }
        return data;
    }

    public void addStudent(Student studentToAdd) {
        this.students.add(studentToAdd);
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

        this.tower = null;

        return removed;
    }

    public Tower getTower() {
        return tower;
    }
}
