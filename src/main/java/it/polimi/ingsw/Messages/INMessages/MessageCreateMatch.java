package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;
import it.polimi.ingsw.Server.ServerView;

import static it.polimi.ingsw.Messages.Enumerations.INMessageType.CREATE_MATCH;

/**
 * input message that asks to create a match
 */
public class MessageCreateMatch extends Message{
    private final String colourFirstPlayer;
    private final int numPlayers;
    private final boolean advanced; // 1 if advanced, otherwise 0
    private final ServerView serverView;

    /**
     * constructor of the input message
     * @param nicknameFirstPlayer nickname chosen by the first player who connects
     * @param colourFirstPlayer colour chosen by the first player who connects
     * @param numPlayers number of players of the game, chosen by the first player who connects
     * @param advanced boolean value that says if the first player chose the game to be advanced or not
     * @param serverView server view of the first player who connects
     */
    public MessageCreateMatch(String nicknameFirstPlayer, String colourFirstPlayer, int numPlayers, boolean advanced, ServerView serverView){
        super(CREATE_MATCH, nicknameFirstPlayer);
        this.colourFirstPlayer = colourFirstPlayer;
        this.numPlayers = numPlayers;
        this.advanced = advanced;
        this.serverView = serverView;
    }

    /**
     * getter of the colour of the first player
     * @return colour of the first player
     */
    public String getColourFirstPlayer(){
        return this.colourFirstPlayer;
    }

    /**
     * getter of the number of players of the game
     * @return number of players of the game
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * getter of the server view of the first player
     * @return server view of the first player
     */
    public ServerView getServerView() {
        return serverView;
    }

    /**
     * method that says if the game is advanced
     * @return true if advanced, false otherwise
     */
    public boolean isAdvanced() {
        return advanced;
    }

    /**
     * method that verifies the input of the message
     * @param controller controller
     * @return true if input is acceptable, else otherwise
     */
    public boolean checkInput(ControllerInput controller){
        return (controller.checkNickname(this.nickname) &&
                controller.checkPlayerColour(this.colourFirstPlayer));
    }

    /**
     * method that does what the message requires
     * @param controller controller
     * @return true if the message has been successfully managed, false otherwise
     */
    @Override
    public boolean manageMessage(Controller controller) {
        return controller.manageCreateMatch(this);
    }
}
