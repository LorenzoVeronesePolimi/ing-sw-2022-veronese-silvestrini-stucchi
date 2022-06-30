package it.polimi.ingsw.Messages.INMessages;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.ControllerInput;
import it.polimi.ingsw.Messages.Enumerations.INMessageType;
import it.polimi.ingsw.Server.ServerView;

/**
 * input message that asks to add a player to the already existent game
 */
public class MessageAddPlayer extends Message {
    private final String colour;
    private final ServerView serverView;

    /**
     * constructor of the addPlayer input message
     * @param nickname nick of the player to add
     * @param colour colour of the player to add
     * @param serverView serverView of the player to add
     */
    public MessageAddPlayer(String nickname, String colour, ServerView serverView){
        super(INMessageType.ADD_PLAYER, nickname);
        this.colour = colour;
        this.serverView = serverView;
    }

    /**
     * getter of new player's colour
     * @return new player's colour
     */
    public String getColour() {
        return colour;
    }

    /**
     * getter of new player's server view
     * @return new player's server view
     */
    public ServerView getServerView() {
        return serverView;
    }

    /**
     * method that verifies the input of the message
     * @param controller controller
     * @return true if input is acceptable, else otherwise
     */
    @Override
    public boolean checkInput(ControllerInput controller) {
        return (controller.checkNickname(this.nickname) &&
                controller.checkPlayerColour(this.colour));
    }

    /**
     * method that does what the message requires
     * @param controller controller
     * @return true if the message has been successfully managed, false otherwise
     */
    public boolean manageMessage(Controller controller){
        return controller.manageAddPlayer(this);
    }
}
