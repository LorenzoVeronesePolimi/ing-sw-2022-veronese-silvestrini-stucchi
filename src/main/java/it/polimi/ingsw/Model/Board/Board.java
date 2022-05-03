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

/*TODO: Understand what to do with private methods*/
public interface Board {
    Archipelago getArchipelago(int archipelagoIndex);

    List<Cloud> getClouds();

    Map<SPColour, Integer> getNumStudentsInArchipelago(int archipelagoIndex);

    boolean isStudentInSchoolHall(Player player, SPColour c);

    void moveStudentSchoolToArchipelagos(Player player, SPColour colour, int archipelagoIndex) throws StudentNotFoundException;

    void moveStudentCloudToSchool(Player player, int cloudIndex) throws ExceededMaxStudentsHallException;

    void moveStudentHallToDiningRoom(Player player, SPColour colour) throws StudentNotFoundException, ExceededMaxStudentsDiningRoomException, EmptyCaveauException, ProfessorNotFoundException, NoProfessorBagException;

    void moveStudentBagToCloud() throws ExceededMaxStudentsCloudException, StudentNotFoundException;

    void moveStudentBagToSchool(int numStudents) throws StudentNotFoundException, ExceededMaxStudentsHallException;

    void placeMotherNatureInitialBoard();

    void moveMotherNature(int mnMoves);

    void moveProfessor(Player destinationPlayer, SPColour colour) throws NoProfessorBagException, ProfessorNotFoundException;

    boolean isProfessorInSchool(SPColour colour);

    School whereIsProfessor(SPColour colour);

    void conquerProfessor(Player currentPlayer, SPColour colour) throws NoProfessorBagException, ProfessorNotFoundException;

    int whereIsMotherNature();

    School getPlayerSchool(Player player);

    void tryToConquer(Player currentPlayer) throws InvalidTowerNumberException, AnotherTowerException, ExceededMaxTowersException, TowerNotFoundException;
    boolean checkIfConquerable(Player currentPlayer);
    Player computeWinner(Player owner, Player challenger, Archipelago archipelago);
    int computeInfluenceOfPlayer(Player player, Archipelago archipelago);
    void useAssistantCard(Player player, int turnPriority) throws AssistantCardAlreadyPlayedTurnException, NoAssistantCardException;
    void notifyPlayers();
}
