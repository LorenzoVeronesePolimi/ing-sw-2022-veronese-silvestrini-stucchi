package it.polimi.ingsw.Model;

import java.util.List;

public class Player {
    private String nickname;
    private List<AssistantCard> playerHand;
    private AssistantCard lastCard;
    private PlayerColour colour;

    public Player(String nickname) {
        this.nickname = nickname;
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
}
