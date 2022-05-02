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
    public void printErrorMessage() {
        System.out.println("\n--------------------");
        System.out.println("Oh no! There's something wrong: try again!");
        System.out.println("--------------------\n");
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
        String colour;

        do {
            System.out.println("Write your nickname:");
            nickname = input.nextLine();
        } while (nickname.equals(""));

        do {
            //TODO: maybe show only available possibilities
            //TODO: manage game 4 case
            System.out.println("What colour would you like [Black/White/Gray]:");
            colour = input.nextLine();
        } while (colour.equals(""));

        client.asyncWriteToSocket("addPlayer " + nickname + " " + colour);
    }

    @Override
    public void askFirstPlayerInfo() {
        String nickname;
        String colour;
        String numPlayers;
        String gameMode;

        do {
            System.out.println("Write your nickname:");
            nickname = input.nextLine();
        } while (nickname.equals(""));

        do {
            //TODO: manage game 4
            System.out.println("What colour would you like [Black/White/Gray]:");
            colour = input.nextLine();
        } while (!colour.equalsIgnoreCase("black") && !colour.equalsIgnoreCase("white") &&
                !colour.equalsIgnoreCase("gray"));

        do {
            System.out.println("Select number of players [2 - 3 - 4]:");
            numPlayers = input.nextLine();
        } while (!numPlayers.equals("2") && !numPlayers.equals("3") && !numPlayers.equals("4"));

        do {
            System.out.println("Do you want to play in ADVANCED mode? [Y/N]:");
            gameMode = input.nextLine();
        } while (!gameMode.equalsIgnoreCase("Y") && !gameMode.equalsIgnoreCase("N"));

        gameMode = gameMode.equalsIgnoreCase("Y") ? "true" : "false";

        client.asyncWriteToSocket("createMatch " + nickname + " " + colour + " " + numPlayers + " " + gameMode);
    }
}
