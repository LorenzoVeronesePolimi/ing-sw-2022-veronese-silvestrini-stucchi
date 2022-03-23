package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Exceptions.NoAssistantCardException;

import java.util.List;

public class Player {
    private String nickname;
    private List<AssistantCard> playerHand;
    private AssistantCard lastCard;
    private PlayerColour colour;

    public Player(String nickname, PlayerColour colour) {
        this.nickname = nickname;
        this.colour = colour;
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

    public void addAssistantCard(AssistantCard toAdd){
        this.playerHand.add(toAdd);
    }

    // Player uses the AssistantCard. Remove it from the playerHand, and put in lastCard
    public void useAssistantCard(int turnPriority) throws NoAssistantCardException {
        for(AssistantCard c : this.playerHand){
            if(c.getTurnPriority() == turnPriority){
                this.lastCard = c;
                playerHand.remove(c);
                break;
            }
        }

        throw new NoAssistantCardException();
    }


    @Override
    public String toString() {
        return "Player{" +
                "nickname='" + nickname + '\'' +
                ", colour=" + colour +
                '}';
    }
}
