package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Exceptions.ExceedingAssistantCardNumberException;
import it.polimi.ingsw.Model.Exceptions.NoAssistantCardException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the player of the game.
 * A player has a nickname, a hand of Assistant cards and a colour.
 */
public class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String nickname;
    private List<AssistantCard> playerHand;
    private AssistantCard lastCard;
    private final PlayerColour colour;

    /**
     * This method creates a player, assigning him a nickname and a colour.
     * @param nickname It represents the nickname of the player.
     * @param colour It represents the colour of the player. It is derived from the PlayerColour enumeration.
     */
    public Player(String nickname, PlayerColour colour) {
        this.nickname = nickname;
        this.colour = colour;
        playerHand = new ArrayList<>();
    }

    /**
     * Constructor of the class Player that clones the lastCart attribute of the class.
     * @param player Player that needs to be cloned.
     */
    // Constructor called when hiding player information in network communication
    public Player(Player player) {
        this.nickname = player.nickname;
        this.colour = player.colour;
        if(player.lastCard != null)
            this.lastCard = new AssistantCard(player.lastCard);
    }


    /**
     * @return The nickname of the player.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @return The last card played.
     */
    public AssistantCard getLastCard() {
        return lastCard;
    }

    /**
     * @return The colour of the player.
     */
    public PlayerColour getColour() {
        return colour;
    }

    /**
     * @return The length of the player hand. The length changes accordingly to the number of cards played during the game.
     */
    public int getHandLength(){
        return this.playerHand.size();
    }

    /**
     * @return The list of AssistantCard possessed by the player.
     */
    public List<AssistantCard> getPlayerHand() {
        return playerHand;
    }

    /**
     * This method is used to build the player hand, giving him the cards he can play in the game.
     * @param toAdd AssistantCard that needs to be added to the player hand.
     * @throws ExceedingAssistantCardNumberException when the player receives more than 10 cards to be added to his hand.
     */
    public void addAssistantCard(AssistantCard toAdd) throws ExceedingAssistantCardNumberException {
        int MAXCARDNUM = 10;
        if(playerHand.size() < MAXCARDNUM)
            this.playerHand.add(toAdd);
        else
            throw new ExceedingAssistantCardNumberException();
    }

    /**
     * This method is used to call the effect of the AssistantCard in the hand of the player.
     * @param turnPriority  int used to select the correct AssistantCard in the player hand. (All cards have different
     *                      turn priority)
     * @throws NoAssistantCardException when the assistant card selected, specifying the turn priority, has already
     *                                  been played or doesn't exist.
     */
    // Player uses the AssistantCard. Remove it from the playerHand, and put in lastCard
    public void useAssistantCard(int turnPriority) throws NoAssistantCardException {
        boolean cardCorrectlyPlayed = false;
        AssistantCard removed = null;

        for(AssistantCard c : this.playerHand){
            System.out.println("[Player, useAssistantCard]: checking #" + c.getTurnPriority());
            if(c.getTurnPriority() == turnPriority){
                System.out.println("[Player, useAssistantCard]: found #" + c.getTurnPriority());
                this.lastCard = c;
                removed = c;
                cardCorrectlyPlayed = true;
            }
        }

        //if no card was found
        if(!cardCorrectlyPlayed)
            throw new NoAssistantCardException();

        //if it is found and played
        playerHand.remove(removed);
    }

    /**
     * Method toString of the structure of the class.
     * @return The description of the class.
     */
    @Override
    public String toString() {
        return "nickname=" + nickname +
                ", colour=" + colour ;
    }
}
