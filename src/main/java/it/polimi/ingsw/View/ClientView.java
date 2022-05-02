package it.polimi.ingsw.View;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Messages.OUTMessages.OUTMessage;

import java.util.Scanner;

public abstract class ClientView {
    protected Client client;
    protected Scanner input;
    private boolean errorStatus = false;
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

    public abstract void printErrorMessage();

    public abstract void askCLIorGUI();
    public abstract void askNickName();
    public abstract void askFirstPlayerInfo();

}
