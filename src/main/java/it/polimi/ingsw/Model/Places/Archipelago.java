package it.polimi.ingsw.Model.Places;

import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.AnotherTowerException;
import it.polimi.ingsw.Model.Exceptions.InvalidTowerNumberException;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Pawns.Tower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* After two islands unified, I put all future students in the first island
*
* MOTHER NATURE POSITIONS ON AN ARCHIPELAGO:
*   Board.checkIfConquerable()
*       Archipelago.howManyStudents()
*       Board.computeWinner()
*   if(currentPlayer is winner and is not the owner):
*       Board.conquerArchipelago()
*           Archipelago.conquerArchipelago()
*           Board.replaceTowers()
*       Board.mergeArchipelagos()
*           recursively:
*               Archipelago.mergeArchipelagos()
* */

//TODO: create Map of SPColour -> numberOfStudentsOfThatColour in an intelligent way: calculate only one time, and
// then update when a change is made
public class Archipelago {
    private List<Island> islands;
    private boolean isTaken; //has this Archipelago been taken by any player?
    // studentsData: is a Map which matches each SPColour with how many Students of that SPColour the Archipelago
    // contains. This is updated using updateStudentsData() each time a Student is added/removed
    private Map<SPColour, Integer> studentsData;


    public Archipelago(){
        this.islands = new ArrayList<Island>();
        this.islands.add(new Island());
        this.isTaken = false;
    }

    public int getNumIslands(){
        return this.islands.size();
    }


    // this return original reference of each island. Useful for mergeArchipelagos()
    private List<Island> getOriginalIslands(){
        return this.islands;
    }


    // Remove the Tower from each of its Islands
    private List<Tower> removeTowers() {
        List<Tower> removed = new ArrayList<Tower>();

        for(Island i : this.islands){
            removed.add(i.removeTower());
        }

        return removed;

    }


    /*
     * Two chances:
     * 1) No one conquered the Archipelago before: I have no Tower to remove => return void List
     * 2) Another player conquered the Archipelago => remove his Towers. Board will put these Towers into
     *      the right School
     * EXCEPTION when the number of input Towers is different from the number of Archipelago's Islands
     *      To let Board know this number, getNumIslands() is needed
     */
    public List<Tower> conquerArchipelago(List<Tower> towersToAdd) throws InvalidTowerNumberException{
        List<Tower> removed = new ArrayList<Tower>();
        if (towersToAdd.size() == this.islands.size()) {
            //1) do nothing
            //2)
            if(this.isTaken == true){
                removed = this.removeTowers();
            }
            for(Island i : this.islands) {
                try {
                    i.addTower(towersToAdd.remove(0)); // Always remove the first one because the List looses length
                } catch (AnotherTowerException ex){ex.printStackTrace();}
            }
        }
        else{
            throw new InvalidTowerNumberException();
        }

        return removed;
    }


    // This make me loose the reference to archipelagoToMerge; no problem because I still have the
    // reference to each island
    public void mergeArchipelagos(Archipelago archipelagoToMerge) {
        PlayerColour c1 = this.islands.get(0).getTower().getPlayer().getColour();
        PlayerColour c2 = archipelagoToMerge.islands.get(0).getTower().getPlayer().getColour();

        if(archipelagoToMerge.isTaken == true && this.isTaken == true) { // I can't merge not taken Archipelagos
            if(c1.equals(c2)) { //check if the Tower color is the same
                this.islands.addAll(archipelagoToMerge.getOriginalIslands());
            }
        }
    }


    // This updates this.studentsData
    private void updateStudentsData(){
        Map<SPColour, Integer> newStudentsData = new HashMap<SPColour, Integer>();
        SPColour[] availableColours = {SPColour.BLUE, SPColour.PINK, SPColour.RED, SPColour.GREEN, SPColour.YELLOW};
        for(SPColour c : availableColours){
            newStudentsData.put(c, 0);
        }

        //TODO: use functional approach
        Map<SPColour, Integer> singleIslandStudentsData = new HashMap<SPColour, Integer>();
        for(Island island : this.islands){
            singleIslandStudentsData = island.howManyStudents();
            for(SPColour c : singleIslandStudentsData.keySet()){
                newStudentsData.replace(c, this.studentsData.get(c) + singleIslandStudentsData.get(c));
            }
        }

        this.studentsData = newStudentsData;
    }


    public void addStudent(Student studentToAdd) {
        this.islands.get(0).addStudent(studentToAdd);
    }
}
