package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;
import it.polimi.ingsw.Server.ServerView;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.CREATE_MATCH;

public class MessageCreateMatch extends Message{
    private final String colourFirstPlayer;
    private final int numPlayers;
    private final boolean advanced; // 1 if advanced, otherwise 0
    private final ServerView serverView;

    public MessageCreateMatch(String nicknameFirstPlayer, String colourFirstPlayer, int numPlayers, boolean advanced, ServerView serverView){
        super(CREATE_MATCH, nicknameFirstPlayer);
        this.colourFirstPlayer = colourFirstPlayer;
        this.numPlayers = numPlayers;
        this.advanced = advanced;
        this.serverView = serverView;
    }

    public String getColourFirstPlayer(){
        return this.colourFirstPlayer;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public ServerView getServerView() {
        return serverView;
    }

    public boolean isAdvanced() {
        return advanced;
    }

    public boolean checkInput(ControllerInput controller){
        return (controller.checkNickname(this.nickname) &&
                controller.checkPlayerColour(this.colourFirstPlayer));
    }

    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCreateMatch(this);
    }
}
