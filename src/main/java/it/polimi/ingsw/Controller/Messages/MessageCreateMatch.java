package it.polimi.ingsw.Controller.Messages;

import it.polimi.ingsw.Controller.Enumerations.MessageType;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;

public class MessageCreateMatch extends Message{
    private String nicknameFirstPlayer;
    private String colourFirstPlayer;
    private int numPlayers;
    private boolean advanced; // 1 if advanced, otherwise 0

    public MessageCreateMatch(String nicknameFirstPlayer, String colourFirstPlayer, int numPlayers, boolean advanced){
        super(MessageType.CREATE_MATCH);
        this.nicknameFirstPlayer = nicknameFirstPlayer;
        this.colourFirstPlayer = colourFirstPlayer;
        this.numPlayers = numPlayers;
        this.advanced = advanced;
    }

    public String getNicknameFirstPlayer() {
        return nicknameFirstPlayer;
    }

    public String getColourFirstPlayer(){
        return this.colourFirstPlayer;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public boolean isAdvanced() {
        return advanced;
    }
}
