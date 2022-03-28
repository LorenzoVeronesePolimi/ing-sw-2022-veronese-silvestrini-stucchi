package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.AssistantCardAlreadyPlayedTurnException;
import it.polimi.ingsw.Model.Places.Archipelago;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Player;

/*TODO: Understand what to do with private methods*/
public interface Board {
    public Archipelago getArchipelago(int archipelagoIndex);

    public boolean isStudentInSchoolHall(Player player, SPColour c);

    public void moveStudentSchoolToArchipelagos(Player player, SPColour colour, int archipelagoIndex);

    public void moveStudentCloudToSchool(Player player, int cloudIndex);

    public void moveStudentHallToDiningRoom(Player player, SPColour colour);

    public void initializeBoard();

    default void moveStudentBagToCloud(){};

    public void moveStudentBagToSchool(int numStudents);

    private void placeMotherNatureInitialBoard(){}

    public void moveMotherNature(int archipelagoIndex);

    public void moveProfessor(Player destinationPlayer, SPColour colour);

    public boolean isProfessorInSchool(SPColour colour);

    public School whereIsProfessor(SPColour colour);

    public void conquerProfessor(SPColour colour);

    public int whereIsMotherNature();

    public School getPlayerSchool(Player player);

    public void makeTurn();
    public void tryToConquer();
    public boolean checkIfConquerable();
    public Player computeWinner(Player owner, Player challenger, Archipelago archipelago);
    public int computeInfluenceOfPlayer(Player player, Archipelago archipelago);

    public void useAssistantCard(Player player, int turnPriority) throws AssistantCardAlreadyPlayedTurnException;
}
