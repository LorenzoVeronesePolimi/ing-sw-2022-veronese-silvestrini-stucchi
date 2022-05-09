package it.polimi.ingsw.View;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Controller.Enumerations.State;
import it.polimi.ingsw.Messages.OUTMessages.OUTMessage;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;
import it.polimi.ingsw.Model.Board.SerializedBoardAdvanced;
import it.polimi.ingsw.Model.Cards.AbstractCharacterCard;
import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.View.ClientView;

import java.rmi.ConnectIOException;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static it.polimi.ingsw.View.CLIColours.ANSI_RED;
import static it.polimi.ingsw.View.CLIColours.ANSI_RESET;

public class CLIView extends ClientView {

    private String playerNick;

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
        System.out.println("\n----------------------------------------");
        System.out.println(err);
        System.out.println("----------------------------------------\n");
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
    public void askNickName(List<PlayerColour> colourList, int numPlayer) {
        String nickname;
        String colour = "";

        do {
            System.out.println("Write your nickname:");
            nickname = input.nextLine();
            this.playerNick = nickname;
        } while (nickname.equals(""));

        if(numPlayer==2){
            colour= colourList.get(0).equals(PlayerColour.WHITE) ? "BLACK" : "WHITE";
            System.out.println("your colour is " + colour);
        }
        if(numPlayer == 3 ){
            if(colourList.size()==1){
                if(colourList.get(0).equals(PlayerColour.WHITE)) {
                    do {
                        System.out.println("What colour would you like [Black/Gray]:");
                        colour = input.nextLine();
                    } while (!colour.equalsIgnoreCase("black") && !colour.equalsIgnoreCase("gray"));
                }
                if(colourList.get(0).equals(PlayerColour.BLACK)) {
                    do {
                        System.out.println("What colour would you like [White/Gray]:");
                        colour = input.nextLine();
                    } while (!colour.equalsIgnoreCase("white") && !colour.equalsIgnoreCase("gray"));
                }
                if(colourList.get(0).equals(PlayerColour.GRAY)) {
                    do {
                        System.out.println("What colour would you like [Black/White]:");
                        colour = input.nextLine();
                    } while (!colour.equalsIgnoreCase("black") && !colour.equalsIgnoreCase("white"));
                }
            }
            if(colourList.size()==2){
                if(colourList.contains(PlayerColour.WHITE) && colourList.contains(PlayerColour.BLACK)) {
                    colour = "GRAY";
                }
                if(colourList.contains(PlayerColour.WHITE) && colourList.contains(PlayerColour.GRAY)) {
                    colour= "BLACK";
                }
                if(colourList.contains(PlayerColour.GRAY) && colourList.contains(PlayerColour.BLACK)) {
                    colour = "WHITE";
                }
                System.out.println("Your colour is " + colour);
            }
        }
        if (numPlayer == 4){
            long black=colourList.stream().filter(x -> x.equals(PlayerColour.BLACK)).count();
            long white=colourList.stream().filter(x -> x.equals(PlayerColour.WHITE)).count();

            if(colourList.size()==3){
                colour=(black==2)? "WHITE" : "BLACK";
                System.out.println("Your colour is " + colour);
            }
            if(colourList.size()==2){
                if(black==1 || white==1){
                    do {
                        System.out.println("What colour would you like [Black/White]:");
                        colour = input.nextLine();
                    } while (!colour.equalsIgnoreCase("BLACK") && !colour.equalsIgnoreCase("WHITE"));
                }
                else{
                    colour=(black==2)? "WHITE" : "BLACK";
                    System.out.println("Your colour is " + colour);
                }
            }
            if(colourList.size()==1){
                do {
                    System.out.println("What colour would you like [Black/White]:");
                    colour = input.nextLine();
                } while (!colour.equalsIgnoreCase("BLACK") && !colour.equalsIgnoreCase("WHITE"));
            }

        }

        client.asyncWriteToSocket("addPlayer " + nickname + " " + colour);
    }

    @Override
    public void askFirstPlayerInfo() {
        String nickname;
        String colour = null;
        String numPlayers;
        String gameMode;

        do {
            System.out.println("Write your nickname:");
            nickname = input.nextLine();
            this.playerNick = nickname;
        } while (nickname.equals(""));

        do {
            System.out.println("Select number of players [2 - 3 - 4]:");
            numPlayers = input.nextLine();
        } while (!numPlayers.equals("2") && !numPlayers.equals("3") && !numPlayers.equals("4"));

        if(Integer.parseInt(numPlayers)==2 || Integer.parseInt(numPlayers)==4){
            do {
                System.out.println("What colour would you like [Black/White]:");
                colour = input.nextLine();
            } while (!colour.equalsIgnoreCase("black") && !colour.equalsIgnoreCase("white"));
        }
        if(Integer.parseInt(numPlayers)==3){
            do {

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

            printSchool(serializedBoardAbstract);

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

            printSchool(serializedBoardAdvanced);
            printExtractedCC(serializedBoardAdvanced);
        }

        //TODO: Ask for next move (if it's the correct turn, maybe)

        if(this.playerNick.equals(serializedBoardAbstract.getCurrentPlayer().getNickname())) {
            System.out.println("\nIT'S YOUR TURN! MAKE A MOVE!");
            manageNextMove(serializedBoardAbstract);
        } else {
            System.out.println("\nIT'S " + serializedBoardAbstract.getCurrentPlayer().getNickname() + "'s TURN! WAIT...");
        }
    }

    private void manageNextMove(SerializedBoardAbstract serializedBoardAbstract) {
        System.out.println(serializedBoardAbstract.getCurrentState());
        switch(serializedBoardAbstract.getCurrentState()){
            case PLANNING2:
                askAssistantCard();
                break;
            case ACTION1:
                askAction1();
                break;
            case ACTION2:
                //askAction2();
                break;
            case ACTION3:
                //askAction3();
                break;

        }
    }

    // This asks the player what AssistantCard does he want to use
    public void askAssistantCard(){
        int[] cardsMotherNatureMoves= {1, 1, 2, 2, 3, 3, 4, 4, 5, 5};
        int turnPriority;

        do {
            System.out.println("Choose the turn priority of the card you want to use: ");
            turnPriority = Integer.parseInt(input.nextLine());
        } while (turnPriority < 1 || turnPriority > 10);

        // turnPriority is enough to identify the card: assign motherNatureMovement automatically
        client.asyncWriteToSocket("assistantCard " + cardsMotherNatureMoves[turnPriority - 1] + " " + turnPriority);
    }

    // This asks the player to move a student during the ACTION1 state
    public void askAction1(){
        Set<String> possibleColours = new HashSet<String>();
        possibleColours.add("pink"); possibleColours.add("red"); possibleColours.add("yellow"); possibleColours.add("blue"); possibleColours.add("green");
        String command;
        String colour;

        do {
            System.out.println("Is ACTION1. What do you want to do? [studentHallToDiningRoom/studentToArchipelago] ");
            command = input.nextLine();
        } while (!command.equals("studentHallToDiningRoom") && !command.equals("studentToArchipelago"));

        do {
            System.out.println("Choose the colour's student to move from hall: ");
            colour = input.nextLine();
        } while (!possibleColours.contains(colour.toLowerCase()));

        if(command.equals("studentHallToDiningRoom")){ //studentHallToDiningRoom
            client.asyncWriteToSocket(command + " " + colour);
        }
        else{ //studentToArchipelago
            int destArchipelagoIndex;

            do {
                System.out.println("Choose the index of the destination archipelago: ");
                destArchipelagoIndex = Integer.parseInt(input.nextLine());
            } while (destArchipelagoIndex < 0 || destArchipelagoIndex > 11);

            client.asyncWriteToSocket(command + " " + colour + " " + destArchipelagoIndex);
        }
    }

    // This asks the player how much does he want to move Mother Nature
    public void askMoveMotherNature(){
        int moves;

        do {
            System.out.println("Is Action2: how much do you want to move Mother Nature? ");
            moves = Integer.parseInt(input.nextLine());
        } while (moves < 0 || moves > 7);

        client.asyncWriteToSocket("moveMotherNature " + moves);
    }


    private void printSchool(SerializedBoardAbstract serializedBoardAbstract) {
        System.out.println("Schools: ");
        for (int i = 0; i < serializedBoardAbstract.getSchools().size(); i++) {
            System.out.println(serializedBoardAbstract.getSchools().get(i).toString());
            printCards(serializedBoardAbstract, i);
        }
    }

    private void printCards(SerializedBoardAbstract serializedBoardAbstract, int i) {
        if(this.playerNick.equals(serializedBoardAbstract.getSchools().get(i).getPlayer().getNickname())) {
            if(serializedBoardAbstract.getSchools().get(i).getPlayer().getPlayerHand().size() != 0) {
                int size = serializedBoardAbstract.getSchools().get(i).getPlayer().getPlayerHand().size();

                System.out.print("Your assistant cards (Turn priority, Mother nature movements): [");
                for (int j = 0; j < size; j++) {
                    AssistantCard card = serializedBoardAbstract.getSchools().get(i).getPlayer().getPlayerHand().get(j);
                    System.out.print(card.toString());

                    if (j == size - 1) {
                        System.out.println("]");
                    } else {
                        System.out.print("; ");
                    }
                }
            } else {
                System.out.println("There are not assistant cards anymore!");
            }

        } else {
            if (serializedBoardAbstract.getSchools().get(i).getPlayer().getLastCard() != null) {
                System.out.print("Last card played (Turn priority, Mother nature movements): [");
                System.out.print(serializedBoardAbstract.getSchools().get(i).getPlayer().getLastCard().toString());
                System.out.println("]");
            } else {
                System.out.println("There is no last card!");
            }

        }
    }

    private void printExtractedCC(SerializedBoardAdvanced serializedBoardAdvanced) {
        int size = serializedBoardAdvanced.getExtractedCards().size();

        System.out.print("Extracted character cards: [");
        for(int i = 0; i < serializedBoardAdvanced.getExtractedCards().size(); i++) {
            System.out.print(serializedBoardAdvanced.getExtractedCards().get(i).toString());

            if(i == size - 1) {
                System.out.println("]");
            } else {
                System.out.print(", ");
            }
        }
    }
}
