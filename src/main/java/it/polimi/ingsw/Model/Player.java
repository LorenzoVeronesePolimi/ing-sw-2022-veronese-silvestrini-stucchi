package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Exceptions.ExceedingAssistantCardNumberException;
import it.polimi.ingsw.Model.Exceptions.NoAssistantCardException;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the player of the game.
 * A player has a nickname, a hand of Assistant cards and a colour.
 */
public class Player {
    private final String nickname;
    private final List<AssistantCard> playerHand;
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
            if(c.getTurnPriority() == turnPriority){
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
        return "Player{" +
                "nickname='" + nickname + '\'' +
                ", colour=" + colour +
                '}';
    }
}
