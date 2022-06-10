package it.polimi.ingsw.Model.Cards;

import java.io.Serializable;

/**
 * This class models the entity of the assistant card.
 * For each hand, every player chooses one of his assistant cards (he initially has 10 of them): the
 * choice will determine the order of play. During his turn, the player will be allowed to move mother
 * nature up to the number of archipelagos that are written on the card that he chose.
 */
public class AssistantCard implements Serializable {
    private int motherNatureMovement;
    private final int turnPriority;

    /**
     * Constructor of the card
     * @param MNMovement is the number of movements that mother nature can do
     * @param turnPriority is the number that will determine the order of turns
     */
    public AssistantCard(int MNMovement, int turnPriority) {
        this.motherNatureMovement = MNMovement;
        this.turnPriority = turnPriority;
    }

    /**
     * Constructor that clones the assistant card.
     * @param lastCard AssistantCard that needs to be cloned.
     */
    public AssistantCard(AssistantCard lastCard) {
        this.motherNatureMovement = lastCard.motherNatureMovement;
        this.turnPriority = lastCard.turnPriority;
    }

    /**
     *
     * @return the number of movements that mother nature can do choosing this card
     */
    public int getMotherNatureMovement() {
        return motherNatureMovement;
    }

    /**
     *
     * @return the number of priority that player will have choosing this card
     */
    public int getTurnPriority() {
        return turnPriority;
    }

    /**
     * This method will be called only if "TwoExtraIslands" card effect is used: it changes the number of
     * movements that mother nature can do (choosing this card), by adding two.
     */
    public void extendMnMovement(){
        this.motherNatureMovement+=2;
    }

    /**
     * @return Turn priority and mother nature movement of the card.
     */
    public String toString() {
        return "(" + this.turnPriority + ", " + this.motherNatureMovement + ")";
    }
}
