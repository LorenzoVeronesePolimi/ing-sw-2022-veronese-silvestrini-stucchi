package it.polimi.ingsw.Model.Places;

import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.AnotherTowerException;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Pawns.Tower;

import java.util.*;

/**
 * This class represents the island in the game. The island can contain only one tower at the time, plus many students.
 */
public class Island {
    private final Set<Student> students;
    private Tower tower;

    /**
     * Creates and empty island, with no students or towers.
     */
    public Island(){
        this.students = new HashSet<>();
        this.tower = null;
    }

    /**
     * @return the number of students, for each colour, placed on the island.
     */
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

    /**
     * This method adds a student on the island.
     * @param studentToAdd A single Student that needs to be placed on the island.
     */
    public void addStudent(Student studentToAdd) {
        this.students.add(studentToAdd);
    }

    /**
     * This method adds a tower on the island, if there isn't another tower.
     * @param toAdd Tower to add on the island.
     * @throws AnotherTowerException when there is already a tower on the island.
     */
    public void addTower(Tower toAdd) throws AnotherTowerException {
        if(this.tower == null){ // No tower has been added, yet
            this.tower = toAdd;
        }
        else{
            throw new AnotherTowerException();
        }
    }

    /**
     * This method removes a tower from the island.
     * @return The tower removed from the island.
     */
    public Tower removeTower(){
        Tower removed;

        removed = this.tower;

        this.tower = null;

        return removed;
    }

    /**
     * This method return a copy of the tower present on the island, without removing it.
     * @return The copy of the tower on the island.
     */
    public Tower getTower() {
        return tower;
    }
}
