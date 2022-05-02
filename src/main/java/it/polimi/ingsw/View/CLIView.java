package it.polimi.ingsw.View;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Messages.OUTMessages.OUTMessage;
import it.polimi.ingsw.View.ClientView;

import java.rmi.ConnectIOException;

public class CLIView extends ClientView {
    public CLIView(Client client) {
        super(client);
    }
    @Override
    public void askCLIorGUI() {
        String response;

        do {
            System.out.println("Select between CLI[0] or GUI[1]:");
            response = input.nextLine();

            if (response.equals("1")) {
                client.setCLIorGUI(true);
            }
        } while (!response.equals("0") && !response.equals("1"));
    }

    @Override
    public void askNickName() {
        String nickname;

        do {
            System.out.println("Write your nickname:");
            nickname = input.nextLine();
        } while (!nickname.equals(""));

        client.asyncWriteToSocket("addPlayer " + nickname);
    }

    @Override
    public void askFirstPlayerInfo() {
        String nickname;
        String numPlayers;
        String gameMode;

        do {
            System.out.println("Write your nickname:");
            nickname = input.nextLine();
        } while (nickname.equals(""));

        do {
            System.out.println("Select number of players [2, 3, 4]:");
            numPlayers = input.nextLine();
        } while (!numPlayers.equals("2") && !numPlayers.equals("3") && !numPlayers.equals("4"));

        do {
            System.out.println("Do you want to play in ADVANCED mode? [Y, N]:");
            gameMode = input.nextLine();
        } while (!gameMode.equalsIgnoreCase("Y") && !gameMode.equalsIgnoreCase("N"));

        gameMode = gameMode.equalsIgnoreCase("Y") ? "true" : "false";

        client.asyncWriteToSocket("createMatch " + nickname + " " + numPlayers + " " + gameMode);
    }
}
