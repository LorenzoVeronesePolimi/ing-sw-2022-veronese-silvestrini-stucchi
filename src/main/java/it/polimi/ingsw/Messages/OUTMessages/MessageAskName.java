package it.polimi.ingsw.Messages.OUTMessages;

import it.polimi.ingsw.Messages.Enumerations.OUTMessageType;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.View.ClientView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Message to the client that asks for client name and colour, based on previous player choice.
 */
public class MessageAskName extends OUTMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<PlayerColour> playerColourList;
    private int numPlayer;

    /**
     * Constructor of the class that initializes the type of the message, the list of colour chosen from other players
     * and the number of players that have already logged in.
     * @param colourList list of the colours chosen from the other players.
     * @param numPlayer number of players logged in.
     */
    public MessageAskName(List<PlayerColour> colourList, int numPlayer) {
        super(OUTMessageType.ASK_NICKNAME);
        playerColourList = new ArrayList<>(colourList);
        this.numPlayer = numPlayer;
    }

    /**
     * Method that calls the function askNickName to execute the message.
     * @param view type of view of the client. (it can be CLIView of GUIView)
     */
    @Override
    public void manageMessage(ClientView view) {
        view.askNickName(playerColourList, numPlayer);
    }
}
