package it.polimi.ingsw.View;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Messages.OUTMessages.OUTMessage;

import java.util.Scanner;

public abstract class ClientView {
    protected Client client;
    protected Scanner input;
    public ClientView(Client client) {
        this.client = client;
        this.input = new Scanner(System.in);
    }

    public abstract void askCLIorGUI();
    public abstract void askNickName();
    public abstract void askFirstPlayerInfo();
}
