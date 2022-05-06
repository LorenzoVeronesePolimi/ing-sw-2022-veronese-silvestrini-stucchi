package it.polimi.ingsw.View;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Messages.OUTMessages.OUTMessage;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;
import it.polimi.ingsw.Model.Board.SerializedBoardAdvanced;
import it.polimi.ingsw.View.ClientView;

import java.rmi.ConnectIOException;

public class CLIView extends ClientView {
    public CLIView(Client client) {
        super(client);
    }

    @Override
    public void printErrorMessage() {
        System.err.println("\n----------------------------------------");
        System.err.println("Oh no! There's something wrong: try again!");
        System.err.println("----------------------------------------\n");
    }

    public void printCustom(String err) {
        System.err.println("\n----------------------------------------");
        System.err.println(err);
        System.err.println("----------------------------------------\n");
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
            System.out.println("Select number of players [2 - 3 - 4]:");
            numPlayers = input.nextLine();
        } while (!numPlayers.equals("2") && !numPlayers.equals("3") && !numPlayers.equals("4"));

        if(Integer.parseInt(numPlayers)==2){
            do {
                System.out.println("What colour would you like [Black/White]:");
                colour = input.nextLine();
            } while (!colour.equalsIgnoreCase("black") && !colour.equalsIgnoreCase("white"));
        }
        else{
            do {
                //TODO: manage game 4

                System.out.println("What colour would you like [Black/White/Gray]:");
                colour = input.nextLine();
            } while (!colour.equalsIgnoreCase("black") && !colour.equalsIgnoreCase("white") &&
                    !colour.equalsIgnoreCase("gray"));
        }

        do {
            System.out.println("Do you want to play in ADVANCED mode? [Y/N]:");
            gameMode = input.nextLine();
        } while (!gameMode.equalsIgnoreCase("Y") && !gameMode.equalsIgnoreCase("N"));

        gameMode = gameMode.equalsIgnoreCase("Y") ? "true" : "false";

        client.asyncWriteToSocket("createMatch " + nickname + " " + colour + " " + numPlayers + " " + gameMode);
    }

    @Override
    public void showBoard(SerializedBoardAbstract serializedBoardAbstract) {
        System.out.println("Clouds: ");
        for(int i=0; i<serializedBoardAbstract.getClouds().size(); i++) {
            System.out.print(i + ": ");
            System.out.println(serializedBoardAbstract.getClouds().get(i).toString());
        }

        if(serializedBoardAbstract.getType().equals("standard")) {
            System.out.println("Archipelagos: ");
            for (int i = 0; i < serializedBoardAbstract.getArchipelagos().size(); i++) {
                System.out.print(i + ": ");
                System.out.print(serializedBoardAbstract.getArchipelagos().get(i).toString());
                if (serializedBoardAbstract.getMn().getCurrentPosition().equals(serializedBoardAbstract.getArchipelagos().get(i))) {
                    System.out.println("\tMother nature is here!");
                } else {
                    System.out.println();
                }
            }

            System.out.println("Schools: ");
            for (int i = 0; i < serializedBoardAbstract.getSchools().size(); i++) {
                System.out.println(serializedBoardAbstract.getSchools().get(i).toString());
            }

        }
        else{
            SerializedBoardAdvanced serializedBoardAdvanced = (SerializedBoardAdvanced)serializedBoardAbstract;
            if(serializedBoardAdvanced.getColourToExclude()!=null){
                System.out.println("Colour to exclude: "+ serializedBoardAdvanced.getColourToExclude().toString());
            }
            System.out.println("Archipelagos: ");
            for (int i = 0; i < serializedBoardAdvanced.getArchipelagos().size(); i++) {
                System.out.print(i + ": ");
                System.out.print(serializedBoardAdvanced.getArchipelagos().get(i).toString());
                if (serializedBoardAdvanced.getMn().getCurrentPosition().equals(serializedBoardAdvanced.getArchipelagos().get(i))) {
                    System.out.print("\tMother nature is here!");
                }
                if(serializedBoardAdvanced.getArchipelagos().get(i).getForbidFlag()>0) {
                    System.out.print("Forbidden conquer!");
                }
                System.out.println();
            }

            System.out.println("Schools: ");
            for (int i = 0; i < serializedBoardAdvanced.getSchools().size(); i++) {
                System.out.println(serializedBoardAdvanced.getSchools().get(i).toString());
            }
        }

    }
}
