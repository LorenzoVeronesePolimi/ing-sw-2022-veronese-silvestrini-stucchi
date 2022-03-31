package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Exceptions.ExceedingAssistantCardNumberException;
import it.polimi.ingsw.Model.Exceptions.NoAssistantCardException;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String nickname;
    private final List<AssistantCard> playerHand;
    private AssistantCard lastCard;
    private final PlayerColour colour;

    public Player(String nickname, PlayerColour colour) {
        this.nickname = nickname;
        this.colour = colour;
        playerHand = new ArrayList<>();
    }

    public String getNickname() {
        return nickname;
    }


    public AssistantCard getLastCard() {
        return lastCard;
    }

    public PlayerColour getColour() {
        return colour;
    }

    public int getHandLength(){
        return this.playerHand.size();
    }

    public void addAssistantCard(AssistantCard toAdd) throws ExceedingAssistantCardNumberException {
        int MAXCARDNUM = 10;
        if(playerHand.size() < MAXCARDNUM)
            this.playerHand.add(toAdd);
        else
            throw new ExceedingAssistantCardNumberException();
    }

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


    @Override
    public String toString() {
        return "Player{" +
                "nickname='" + nickname + '\'' +
                ", colour=" + colour +
                '}';
    }
}
