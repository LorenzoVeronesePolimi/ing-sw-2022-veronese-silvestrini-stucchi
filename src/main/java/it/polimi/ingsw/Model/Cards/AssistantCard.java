package it.polimi.ingsw.Model.Cards;

public class AssistantCard {
    private int motherNatureMovement = 0;
    private int turnPriority = 0;

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
