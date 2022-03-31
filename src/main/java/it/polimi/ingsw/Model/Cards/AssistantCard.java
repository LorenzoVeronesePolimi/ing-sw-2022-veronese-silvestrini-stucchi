package it.polimi.ingsw.Model.Cards;

public class AssistantCard {
    private final int motherNatureMovement;
    private final int turnPriority;

    public AssistantCard(int MNMovement, int turnPriority) {
        this.motherNatureMovement = MNMovement;
        this.turnPriority = turnPriority;
    }

    public int getMotherNatureMovement() {
        return motherNatureMovement;
    }

    public int getTurnPriority() {
        return turnPriority;
    }
}
