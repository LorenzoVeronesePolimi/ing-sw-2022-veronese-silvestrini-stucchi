package it.polimi.ingsw.View;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Messages.OUTMessages.OUTMessage;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;

public class GUIView extends ClientView {
    public GUIView(Client client) {
        super(client);
    }

    @Override
    public void printErrorMessage() {

    }

    @Override
    public void askCLIorGUI() {

    }

    @Override
    public void askNickName() {

    }

    @Override
    public void askFirstPlayerInfo() {

    }

    @Override
    public void showBoard(SerializedBoardAbstract serializedBoardAbstract) {

    }

    //TODO: just for test
    @Override
    public void printCustom(String err) {

    }
}
