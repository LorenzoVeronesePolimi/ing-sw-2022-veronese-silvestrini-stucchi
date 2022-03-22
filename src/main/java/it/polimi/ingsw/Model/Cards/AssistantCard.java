package it.polimi.ingsw.Model.Cards;

public class AssistantCard {
    private int MNMovement = 0;
    private int turnPriority = 0;

    public AssistantCard(int MNMovement, int turnPriority) {
        this.MNMovement = MNMovement;
        this.turnPriority = turnPriority;
    }

    public int getMNMovement() {
        return MNMovement;
    }

    public int getTurnPriority() {
        return turnPriority;
    }
}
