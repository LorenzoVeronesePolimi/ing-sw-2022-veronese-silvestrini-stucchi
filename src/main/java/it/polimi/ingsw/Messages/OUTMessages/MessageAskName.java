package it.polimi.ingsw.Messages.OUTMessages;

import it.polimi.ingsw.Messages.Enumerations.OUTMessageType;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.View.ClientView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MessageAskName extends OUTMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<PlayerColour> playerColourList;
    private int numPlayer;
    public MessageAskName(List<PlayerColour> list, int numPlayer) {
        super(OUTMessageType.ASK_NICKNAME);
        playerColourList = new ArrayList<>(list);
        this.numPlayer = numPlayer;
    }

    @Override
    public void manageMessage(ClientView view) {
        view.askNickName(playerColourList, numPlayer);
    }

    public List<PlayerColour> getPlayerColourList() {
        return playerColourList;
    }

    public int getNumPlayer() {
        return numPlayer;
    }
}
