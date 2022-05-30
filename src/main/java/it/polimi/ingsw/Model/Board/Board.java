package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Places.Archipelago;
import it.polimi.ingsw.Model.Places.Cloud;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Observer.Observable;

import java.util.List;
import java.util.Map;

/**
 * Interface that models the concept of the main board of the game
 */
public interface Board {
    /**
     * method that returns the archipelago of defined index
     * @param archipelagoIndex index of the archipelago that is wanted to be returned
     * @return archipelago with index archipelagoIndex
     */
    Archipelago getArchipelago(int archipelagoIndex);

    /**
     * method that return the list of the clouds of this board
     * @return the list of the clouds of this board
     */
    List<Cloud> getClouds();

    /**
     * method that  returns a map that contains data about students on a defined archipelago
     * @param archipelagoIndex index of the archipelago of which information is required
     * @return a map that links the student's colour and the number of students of that colour placed on the archipelago
     */
    Map<SPColour, Integer> getNumStudentsInArchipelago(int archipelagoIndex);

    /**
     * method that returns true if in a defined player's hall is present a student of a given colour
     * @param player is the owner of the school where the student of c colour is supposed to be
     * @param c colour of student that is being looked for
     * @return true if present, false otherwise
     */
    boolean isStudentInSchoolHall(Player player, SPColour c);

    /**
     * method that moves a student of a giver colour from the hall of a given player to the archipelago of a given index
     * @param player owner of the school from which I want to move the student
     * @param colour of the student that I want to move
     * @param archipelagoIndex index of the archipelago where I want to move the student to
     * @throws StudentNotFoundException if in player's hall there are no students of "colour" colour
     */
    void moveStudentSchoolToArchipelagos(Player player, SPColour colour, int archipelagoIndex) throws StudentNotFoundException;

    void moveStudentCloudToSchool(Player player, int cloudIndex) throws ExceededMaxStudentsHallException;

    /**
     * method that moves a student from a given player's hall to his dining room
     * @param player owner of the school from which I want to move the student
     * @param colour of the student that I want to move to the dining room
     * @throws StudentNotFoundException if in player's hall there are no students of such colour
     * @throws ExceededMaxStudentsDiningRoomException if in player's dining room there are already 10 students of such colour
     * @throws EmptyCaveauException if there are no coins to be removed from the school
     * @throws ProfessorNotFoundException if is not possible to move the professor
     * @throws NoProfessorBagException if is not possible to grab the professor from the bag
     */
    void moveStudentHallToDiningRoom(Player player, SPColour colour) throws StudentNotFoundException, ExceededMaxStudentsDiningRoomException, EmptyCaveauException, ProfessorNotFoundException, NoProfessorBagException;

    /**
     * method that moves a student from the bag to a cloud
     * @throws ExceededMaxStudentsCloudException if is not possible to place that student to a cloud because already full
     * @throws StudentNotFoundException if is not possible to move a student from the bag because it is empty
     */
    void moveStudentBagToCloud() throws ExceededMaxStudentsCloudException, StudentNotFoundException;

    /**
     * method that moves a given number of students from the bag to a school
     * @param numStudents number of student that I want to move
     * @throws StudentNotFoundException if is not possible to move such number of students from the bag because it
     * doesn't have enough of them
     * @throws ExceededMaxStudentsHallException if is not possible to move such number of students to the hall because it
     * would have more than the maximum number of students
     */
    void moveStudentBagToSchool(int numStudents) throws StudentNotFoundException, ExceededMaxStudentsHallException;

    /**
     * method that places mother nature in the archipelago of index 0
     */
    void placeMotherNatureInitialBoard();

    /**
     * method that moves mother nature of a given number of archipelagos
     * @param mnMoves number of archipelagos that I want mother nature to move through
     */
    void moveMotherNature(int mnMoves);

    /**
     * method that moves a professor of a given colour to a given school
     * @param destinationPlayer owner of the school that I want to move mother the professor to
     * @param colour of the professor I want to move
     * @throws NoProfessorBagException if the is no professor of colour colour in the bag
     * @throws ProfessorNotFoundException if is not possible to move the professor
     */
    void moveProfessor(Player destinationPlayer, SPColour colour) throws NoProfessorBagException, ProfessorNotFoundException;

    /**
     * method that returns if the professor of a given colour is placed in any school or not
     * @param colour of the professor
     * @return true if the professor is the one of the school, false if it is the bag
     */
    boolean isProfessorInSchool(SPColour colour);

    /**
     * method that returns the school in which the professor of a given colour is
     * @param colour of the professor
     * @return method that returns the school in which the professor of a given colour is
     */
    School whereIsProfessor(SPColour colour);

    /**
     * method that moves the professor of a given colour from its current position to the current player's school, if the number of
     * students of that colour in his dining room is greater than the number of students of that colour in the previous owner hall dining room
     * @param currentPlayer is the current player
     * @param colour of the professor to conquer
     * @throws NoProfessorBagException if there is not such professor in the bag
     * @throws ProfessorNotFoundException if such professor is not in any school
     */
    void conquerProfessor(Player currentPlayer, SPColour colour) throws NoProfessorBagException, ProfessorNotFoundException;

    /**
     *
     * @return the index of the archipelago where is mother nature at the moment
     */
    int whereIsMotherNature();

    /**
     * method that returns the school of a given player
     * @param player owner of the school that I want to get
     * @return the player 's school
     */
    School getPlayerSchool(Player player);

    /**
     * method that compute
     * @param currentPlayer
     * @throws InvalidTowerNumberException
     * @throws AnotherTowerException
     * @throws ExceededMaxTowersException
     * @throws TowerNotFoundException
     */
    void tryToConquer(Player currentPlayer) throws InvalidTowerNumberException, AnotherTowerException, ExceededMaxTowersException, TowerNotFoundException;
    boolean checkIfConquerable(Player currentPlayer);
    Player computeWinner(Player owner, Player challenger, Archipelago archipelago);
    int computeInfluenceOfPlayer(Player player, Archipelago archipelago);
    void useAssistantCard(List<Integer> usedCards, Player player, int turnPriority) throws AssistantCardAlreadyPlayedTurnException, NoAssistantCardException;
    void notifyPlayers();
}
