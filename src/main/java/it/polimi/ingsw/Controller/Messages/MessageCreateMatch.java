package it.polimi.ingsw.Controller.Messages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;

import static it.polimi.ingsw.Controller.Enumerations.MessageType.CREATE_MATCH;

public class MessageCreateMatch extends Message{
    private final String nicknameFirstPlayer;
    private final String colourFirstPlayer;
    private final int numPlayers;
    private final boolean advanced; // 1 if advanced, otherwise 0

    public MessageCreateMatch(String nicknameFirstPlayer, String colourFirstPlayer, int numPlayers, boolean advanced){
        super(CREATE_MATCH);
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

    public boolean checkInput(ControllerInput controller){
        return (controller.checkNickname(this.nicknameFirstPlayer) &&
                controller.checkPlayerColour(this.colourFirstPlayer));
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCreateMatch(this);
    }
}
