package it.polimi.ingsw.Model.Places;

import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.AnotherTowerException;
import it.polimi.ingsw.Model.Exceptions.InvalidTowerNumberException;
import it.polimi.ingsw.Model.Exceptions.MergeDifferentOwnersException;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Pawns.Tower;
import it.polimi.ingsw.Model.Player;

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
*           Archipelago.mergeArchipelagos()
* */

/**
 * Initially the board creates 12 object of the archipelago class, each one of which contains
 * on object island. During the game, two contiguous archipelagos that have the same owner can be
 * merged is a single archipelago.
 */
//TODO: create Map of SPColour -> numberOfStudentsOfThatColour in an intelligent way: calculate only one time, and
// then update when a change is made
public class Archipelago {
    private final List<Island> islands;
    private Player owner; //has this Archipelago been taken by any player?
    // studentsData: is a Map which matches each SPColour with how many Students of that SPColour the Archipelago
    // contains. This is updated using updateStudentsData() each time a Student is added/removed
    private Map<SPColour, Integer> studentsData;

    private int forbidFlag = 0;
    private boolean towerNoValueFlag = false;

    /**
     * The archipelago is created with one island and no owner. The map of the number of students for
     * each colour on the archipelago is initialized.
     */
    public Archipelago(){
        this.islands = new ArrayList<>();
        this.islands.add(new Island());
        this.owner = null; // null as long as no one owns the Archipelago

        this.studentsData = new HashMap<>();
        SPColour[] availableColours = {SPColour.BLUE, SPColour.PINK, SPColour.RED, SPColour.GREEN, SPColour.YELLOW};
        for(SPColour c : availableColours){
            this.studentsData.put(c, 0);
        }
    }

    /**
     *
     * @return the number of islands that constitute the archipelago.
     */
    public int getNumIslands(){
        return this.islands.size();
    }

    /**
     *
     * @return the player that owns the archipelago.
     */
    public Player getOwner(){
        return this.owner;
    }

    /**
     *
     * @return original reference of each island. Useful for mergeArchipelagos().
     */
    private List<Island> getOriginalIslands(){
        return this.islands;
    }

    /**
     *
     * @return the number of forbid flags that have been placed on the archipelago
     */
    public int getForbidFlag(){
        return this.forbidFlag;
    }

    /**
     * Increments the number of forbid flags on the archipelago.
     */
    public void addForbidFlag(){
         this.forbidFlag++;
    }

    /**
     *
     * @return the value of the flag that says if on this turn (on this archipelago) the towers must
     * be counted for the computation of dominance.
     */
    public boolean getTowerNoValueFlag(){
        return this.towerNoValueFlag;
    }

    /**
     * This method removes a forbid flag from the archipelago.
     */
    public void removeForbidFlag() {
        if(this.forbidFlag>0)
            this.forbidFlag --;
    }

    /**
     * This method sets the towerNoValueFlag to the value of the parameter.
     * @param towerNoValueFlag value to which towerNoValueFlag must be set.
     */
    public void setTowerNoValueFlag(boolean towerNoValueFlag) {
        this.towerNoValueFlag = towerNoValueFlag;
    }

    /**
     *
     * @return for each colour, the number of students that are present on the archipelago.
     */
    public Map<SPColour, Integer> howManyStudents(){
        Map<SPColour, Integer> studentsDataCopy = new HashMap<>();
        SPColour[] availableColours = {SPColour.BLUE, SPColour.PINK, SPColour.RED, SPColour.GREEN, SPColour.YELLOW};
        for(SPColour c : availableColours){
            studentsDataCopy.put(c, this.studentsData.get(c));
        }

        return studentsDataCopy;
    }

    /**
     *
     * @return the list of towers that have been removed from all the islands of the archipelago.
     */
    private List<Tower> removeTowers() {
        List<Tower> removed = new ArrayList<>();

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

    /**
     * this method removes all the towers of the previous owner (and returns the list of them) and places on each
     * island one tower of the conqueror
     * @param towersToAdd is the list of the towers of the conqueror that must be placed
     * @return the list of towers of the previous owner
     * @throws InvalidTowerNumberException if the length of the list of towers of the conqueror is different from the number
     * of islands of the archipelago
     * @throws AnotherTowerException if you're trying to add a tower to an island that is already owned by you
     */
    public List<Tower> conquerArchipelago(List<Tower> towersToAdd) throws InvalidTowerNumberException, AnotherTowerException {
        List<Tower> removed = new ArrayList<>();
        if (towersToAdd.size() == this.islands.size()) {
            //1) do nothing
            //2)
            if(this.owner != null){
                removed = this.removeTowers();
            }
            for(Island i : this.islands) {
                i.addTower(towersToAdd.remove(0)); // Always remove the first one because the List looses length
                this.owner = this.islands.get(0).getTower().getPlayer();
            }
        }
        else{
            throw new InvalidTowerNumberException();
        }

        return removed;
    }


    /**
     * This method unify to the current archipelago the one that receives as a parameter, if they have the same owner.
     * @param archipelagoToMerge is the archipelago that must be merged to the current one.
     * @throws MergeDifferentOwnersException if the two archipelagos have two different owners.
     */
    // This make me loose the reference to archipelagoToMerge; no problem because I still have the
    // reference to each island
    public void mergeArchipelagos(Archipelago archipelagoToMerge) throws MergeDifferentOwnersException{
        if(archipelagoToMerge.getOwner() == this.owner) { // I can't merge not taken Archipelago
            this.islands.addAll(archipelagoToMerge.getOriginalIslands());
        }
        else{
            throw new MergeDifferentOwnersException();
        }
        this.updateStudentsData();
    }

    /**
     * This method updates this.studentsData
     */
    private void updateStudentsData(){
        Map<SPColour, Integer> newStudentsData = new HashMap<>();
        SPColour[] availableColours = {SPColour.BLUE, SPColour.PINK, SPColour.RED, SPColour.GREEN, SPColour.YELLOW};
        for(SPColour c : availableColours){
            newStudentsData.put(c, 0);
        }

        Map<SPColour, Integer> singleIslandStudentsData;
        for(Island island : this.islands){
            singleIslandStudentsData = island.howManyStudents();
            for(SPColour c : singleIslandStudentsData.keySet()){
                newStudentsData.replace(c, newStudentsData.get(c) + singleIslandStudentsData.get(c));
            }
        }
        this.studentsData = newStudentsData;
    }

    /**
     * This method add the student received as parameter to che archipelago.
     * @param studentToAdd is the student that must be added.
     */
    public void addStudent(Student studentToAdd) {
        this.islands.get(0).addStudent(studentToAdd);
        this.updateStudentsData();
    }


    /**
     * this method converts to string an object that is instance of Archipelago.
     */
    @Override
    public String toString() {
        int numTowers;
        if (this.islands.get(0).getTower() == null){
            numTowers = 0;
        } else{ numTowers = this.islands.size();}
        return "Archipelago{" +
                "studentsData=" + studentsData +
                ", owner=" + owner +
                ", numTowers=" + numTowers +
                '}';
    }
}
