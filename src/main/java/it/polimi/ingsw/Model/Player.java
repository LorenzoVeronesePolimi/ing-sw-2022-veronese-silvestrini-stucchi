package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;

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


    @Override
    public String toString() {
        return "Player{" +
                "nickname='" + nickname + '\'' +
                ", colour=" + colour +
                '}';
    }
}
