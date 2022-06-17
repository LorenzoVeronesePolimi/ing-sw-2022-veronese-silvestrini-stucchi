package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Places.Archipelago;
import it.polimi.ingsw.Model.Places.Cloud;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Observer.Observable;

import java.io.Serializable;
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
     * method that assign an owner (if there is one) to the archipelago where mother nature is currently on
     * @param currentPlayer player that is trying to conquer the archipelago
     * @throws InvalidTowerNumberException if you try to put more than one tower per island that is part of an archipelago
     * @throws AnotherTowerException
     * @throws ExceededMaxTowersException if you try to place in a school more towers than the maximum number it can hold
     * @throws TowerNotFoundException if you try to grab a tower from a school, but it has none of them
     */
    void tryToConquer(Player currentPlayer) throws InvalidTowerNumberException, AnotherTowerException, ExceededMaxTowersException, TowerNotFoundException;

    /**
     * method that verifies if the archipelago where mother nature currently is placed can be conquered by a given player
     * @param currentPlayer player that should conquer the archipelago
     * @return true if the archipelago can be conquered by the current player, else otherwise
     */
    boolean checkIfConquerable(Player currentPlayer);

    /**
     * method that computes who dominates the archipelago, between the previous owner and the challenger player
     * @param owner player that owned the archipelago
     * @param challenger player that wants to conquer the archipelago
     * @param archipelago archipelago on which I want to compute the winner
     * @return the player that dominate the archipelago
     */
    Player computeWinner(Player owner, Player challenger, Archipelago archipelago);

    /**
     * method that compute the influence of a given player on a given archipelago
     * @param player of whom I want to compute the influence on an archipelago
     * @param archipelago on which I want to compute the influence of the player
     * @return the sum of the number of students of the colours of which the player owns the professor, and the number of towers the player
     * has on the archipelago
     */
    int computeInfluenceOfPlayer(Player player, Archipelago archipelago);

    /**
     * method to use an assistant card
     * @param usedCards list of assistant cards already used
     * @param player player that wants to use the assistant card
     * @param turnPriority number of turn priority that the player has chosen
     * @throws AssistantCardAlreadyPlayedTurnException if that assistant card has already been used
     * @throws NoAssistantCardException if all the assistant cards have already been used
     */
    void useAssistantCard(List<Integer> usedCards, Player player, int turnPriority) throws AssistantCardAlreadyPlayedTurnException, NoAssistantCardException;

    /**
     * method that notifies every client of the changes of the board
     */
    void notifyPlayers();
}
