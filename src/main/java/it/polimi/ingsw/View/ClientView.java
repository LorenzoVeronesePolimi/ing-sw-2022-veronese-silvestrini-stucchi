package it.polimi.ingsw.View;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Messages.OUTMessages.OUTMessage;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;

import java.util.List;
import java.util.Scanner;

public abstract class ClientView {
    protected Client client;
    protected Scanner input;
    private boolean errorStatus = false;

    public ClientView() {}

    public ClientView(Client client) {
        this.client = client;
        this.input = new Scanner(System.in);
    }

    public boolean isErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(boolean errorStatus) {
        this.errorStatus = errorStatus;
    }

    public abstract void printErrorMessage(String err);

    public abstract void endView();
    public abstract void clientDisconnectionEnd();
    public abstract void askReconnect();
    public abstract void askCLIorGUI();
    public abstract void askNickName(List<PlayerColour> list, int numPlayer);
    public abstract void askFirstPlayerInfo();
    public abstract void showBoard(SerializedBoardAbstract serializedBoardAbstract);
    public abstract void showWinner(SerializedBoardAbstract serializedBoardAbstract);
    public void setClientActive(boolean active) {
        this.client.setActive(active);
    }
    public void setClientError(boolean err) {
        this.client.setClientError(err);
    }

    //TODO: just for test
    public abstract void printCustom(String err);
}
