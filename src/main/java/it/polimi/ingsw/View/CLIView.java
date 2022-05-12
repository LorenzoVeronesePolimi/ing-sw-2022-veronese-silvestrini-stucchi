package it.polimi.ingsw.View;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Controller.Enumerations.State;
import it.polimi.ingsw.Messages.OUTMessages.OUTMessage;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;
import it.polimi.ingsw.Model.Board.SerializedBoardAdvanced;
import it.polimi.ingsw.Model.Cards.AbstractCharacterCard;
import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Places.Archipelago;
import it.polimi.ingsw.View.ClientView;

import java.awt.*;
import java.rmi.ConnectIOException;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static it.polimi.ingsw.View.CLIColours.*;

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
        System.out.flush();
    }

    public void printCustom(String err) {
        System.out.println("\n----------------------------------------");
        System.out.println(err);
        System.out.println("----------------------------------------\n");
        System.out.flush();
    }

    @Override
    public void askCLIorGUI() {
        String response;

        do {
            System.out.println("Select between CLI[0] or GUI[1]:");
            System.out.flush();
            response = input.nextLine();

        } while (!response.equals("0") && !response.equals("1"));

        if (response.equals("1")) {
            client.setCLIorGUI(true);
        }
    }

    @Override
    public void askNickName(List<PlayerColour> colourList, int numPlayer) {
        String nickname;
        String colour = "";

        do {
            System.out.println("Write your nickname:");
            System.out.flush();
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
                        System.out.flush();
                        colour = input.nextLine();
                    } while (!colour.equalsIgnoreCase("black") && !colour.equalsIgnoreCase("gray"));
                }
                if(colourList.get(0).equals(PlayerColour.BLACK)) {
                    do {
                        System.out.println("What colour would you like [White/Gray]:");
                        System.out.flush();
                        colour = input.nextLine();
                    } while (!colour.equalsIgnoreCase("white") && !colour.equalsIgnoreCase("gray"));
                }
                if(colourList.get(0).equals(PlayerColour.GRAY)) {
                    do {
                        System.out.println("What colour would you like [Black/White]:");
                        System.out.flush();
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
                        System.out.flush();
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
                    System.out.flush();
                    colour = input.nextLine();
                } while (!colour.equalsIgnoreCase("BLACK") && !colour.equalsIgnoreCase("WHITE"));
            }

        }
        System.out.println(ANSI_GREEN + "Please, wait for other players to connect!" + ANSI_RESET);
        System.out.flush();

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
            System.out.flush();
            nickname = input.nextLine();
            this.playerNick = nickname;
        } while (nickname.equals(""));

        do {
            System.out.println("Select number of players [2 - 3 - 4]:");
            System.out.flush();
            numPlayers = input.nextLine();
        } while (!numPlayers.equals("2") && !numPlayers.equals("3") && !numPlayers.equals("4"));

        if(Integer.parseInt(numPlayers)==2 || Integer.parseInt(numPlayers)==4){
            do {
                System.out.println("What colour would you like [Black/White]:");
                System.out.flush();
                colour = input.nextLine();
            } while (!colour.equalsIgnoreCase("black") && !colour.equalsIgnoreCase("white"));
        }
        if(Integer.parseInt(numPlayers)==3){
            do {

                System.out.println("What colour would you like [Black/White/Gray]:");
                System.out.flush();
                colour = input.nextLine();
            } while (!colour.equalsIgnoreCase("black") && !colour.equalsIgnoreCase("white") &&
                    !colour.equalsIgnoreCase("gray"));
        }

        do {
            System.out.println("Do you want to play in ADVANCED mode? [Y/N]:");
            System.out.flush();
            gameMode = input.nextLine();
        } while (!gameMode.equalsIgnoreCase("Y") && !gameMode.equalsIgnoreCase("N"));

        gameMode = gameMode.equalsIgnoreCase("Y") ? "true" : "false";

        System.out.println(ANSI_GREEN + "Please, wait for other players to connect!" + ANSI_RESET + "\n");
        System.out.flush();

        client.asyncWriteToSocket("createMatch " + nickname + " " + colour + " " + numPlayers + " " + gameMode);
    }

    @Override
    public void showBoard(SerializedBoardAbstract serializedBoardAbstract) {
        System.out.println("\n\t The Board is this:");
        System.out.println("Clouds: ");
        for(int i=0; i<serializedBoardAbstract.getClouds().size(); i++) {
            colourCloud(serializedBoardAbstract, i);
            System.out.print(i + ": ");
            System.out.println(serializedBoardAbstract.getClouds().get(i).toString());
            removeColour();
        }

        if(serializedBoardAbstract.getType().equals("standard")) {
            System.out.println("Archipelagos: ");
            for (int i = 0; i < serializedBoardAbstract.getArchipelagos().size(); i++) {
                colourArchipelago(serializedBoardAbstract, i, serializedBoardAbstract.getArchipelagos(), serializedBoardAbstract);
                if (serializedBoardAbstract.getMn().getCurrentPosition().equals(serializedBoardAbstract.getArchipelagos().get(i))) {
                    System.out.println("\tMother nature is here!");
                } else {
                    System.out.println();
                }

               removeColour();
            }

            printSchool(serializedBoardAbstract);

        }
        else{
            SerializedBoardAdvanced serializedBoardAdvanced = (SerializedBoardAdvanced)serializedBoardAbstract;
            if(serializedBoardAdvanced.getColourToExclude()!=null){
                System.out.println(ANSI_RED + "Colour to exclude: "+ serializedBoardAdvanced.getColourToExclude().toString() + ANSI_RESET);
            }
            System.out.println("Archipelagos: ");
            for (int i = 0; i < serializedBoardAdvanced.getArchipelagos().size(); i++) {
                colourArchipelago(serializedBoardAbstract, i, serializedBoardAdvanced.getArchipelagos(), serializedBoardAdvanced);

                if (serializedBoardAdvanced.getMn().getCurrentPosition().equals(serializedBoardAdvanced.getArchipelagos().get(i))) {
                    System.out.print("\tMother nature is here!");
                }
                if(serializedBoardAdvanced.getArchipelagos().get(i).getForbidFlag()>0) {
                    System.out.print("Forbidden conquer!");
                }
                System.out.println();

                removeColour();
            }

            printSchool(serializedBoardAdvanced);
            printExtractedCC(serializedBoardAdvanced);
        }

        printWaitTurn(serializedBoardAbstract);
        System.out.flush();

        //TODO: let buy cards in all ACTION*
        //TODO: manage student movement in an easier way
        //TODO: don't show empty islands and is only one island remaining don't let player choose
    }

    private void printWaitTurn(SerializedBoardAbstract serializedBoardAbstract) {
        System.out.print(ANSI_GREEN);
        if(this.playerNick.equals(serializedBoardAbstract.getCurrentPlayer().getNickname())) {
            System.out.println("\nIT'S YOUR TURN! MAKE A MOVE!");
            System.out.print(ANSI_RESET);

            manageNextMove(serializedBoardAbstract);
        } else {
            System.out.println("\nIT'S " + serializedBoardAbstract.getCurrentPlayer().getNickname() + "'s TURN! WAIT...");
        }
        System.out.print(ANSI_RESET);
        System.out.flush();
    }

    private void manageNextMove(SerializedBoardAbstract serializedBoardAbstract) {
        switch(serializedBoardAbstract.getCurrentState()){
            case PLANNING2:
                askAssistantCard();
                break;
            case ACTION1:
                askMoveStudents();
                break;
            case ACTION2:
                askMoveMotherNature();
                break;
            case ACTION3:
                askCloudChoice(serializedBoardAbstract);
                break;

        }
    }

    // This asks the player what AssistantCard does he want to use
    private void askAssistantCard(){
        int[] cardsMotherNatureMoves= {1, 1, 2, 2, 3, 3, 4, 4, 5, 5};
        int turnPriority;

        do {
            System.out.println("Choose the turn priority of the card you want to use: ");
            System.out.flush();
            turnPriority = Integer.parseInt(input.nextLine());
        } while (turnPriority < 1 || turnPriority > 10);

        // turnPriority is enough to identify the card: assign motherNatureMovement automatically
        client.asyncWriteToSocket("assistantCard " + cardsMotherNatureMoves[turnPriority - 1] + " " + turnPriority);
    }

    // This asks the player to move a student during the ACTION1 state
    private void askMoveStudents(){
        Set<String> possibleColours = new HashSet<String>();
        possibleColours.add("pink"); possibleColours.add("red"); possibleColours.add("yellow"); possibleColours.add("blue"); possibleColours.add("green");
        String command;
        String colour;

        do {
            System.out.println("Is ACTION1. What do you want to do? [studentHallToDiningRoom/studentToArchipelago] ");
            System.out.flush();
            command = input.nextLine();
        } while (!command.equalsIgnoreCase("studentHallToDiningRoom") && !command.equalsIgnoreCase("studentToArchipelago"));

        if(command.equalsIgnoreCase("studentHallToDiningRoom")) {
            command = "studentHallToDiningRoom";
        } else if(command.equalsIgnoreCase("studentToArchipelago")) {
            command = "studentToArchipelago";
        }

        do {
            System.out.println("Choose the colour's student to move from hall: ");
            System.out.flush();
            colour = input.nextLine();
        } while (!possibleColours.contains(colour.toLowerCase()));

        if(command.equals("studentHallToDiningRoom")){ //studentHallToDiningRoom
            client.asyncWriteToSocket(command + " " + colour);
        }
        else{ //studentToArchipelago
            int destArchipelagoIndex;

            do {
                System.out.println("Choose the index of the destination archipelago: ");
                System.out.flush();
                destArchipelagoIndex = Integer.parseInt(input.nextLine());
            } while (destArchipelagoIndex < 0 || destArchipelagoIndex > 11);

            client.asyncWriteToSocket(command + " " + colour + " " + destArchipelagoIndex);
        }
    }

    // This asks the player how much does he want to move Mother Nature
    private void askMoveMotherNature(){
        int moves;

        do {
            System.out.println("Is Action2: How much do you want to move Mother Nature? ");
            System.out.flush();
            moves = Integer.parseInt(input.nextLine());
        } while (moves < 0 || moves > 7);

        client.asyncWriteToSocket("moveMotherNature " + moves);
    }

    private void askCloudChoice(SerializedBoardAbstract serializedBoardAbstract) {
        int cloudIndex;
        do {
            System.out.println("Is Action3: Which cloud do you choose? ");
            System.out.flush();
            cloudIndex = Integer.parseInt(input.nextLine());
        } while (cloudIndex < 0 || cloudIndex > serializedBoardAbstract.getClouds().size());

        client.asyncWriteToSocket("studentCloudToSchool " + cloudIndex);
    }


    private void printSchool(SerializedBoardAbstract serializedBoardAbstract) {
        System.out.println("Schools: ");
        for (int i = 0; i < serializedBoardAbstract.getSchools().size(); i++) {
            colourSchool(serializedBoardAbstract, i);

            System.out.println(serializedBoardAbstract.getSchools().get(i).toString());
            printCards(serializedBoardAbstract, i);

            removeColour();
        }
        System.out.flush();
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

                if(serializedBoardAbstract.getSchools().get(i).getPlayer().getLastCard() != null) {
                    System.out.println("Last card played: [(" +
                            serializedBoardAbstract.getSchools().get(i).getPlayer().getLastCard().getTurnPriority() +
                            ", " + serializedBoardAbstract.getSchools().get(i).getPlayer().getLastCard().getMotherNatureMovement() +
                            ")]");
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
        System.out.flush();
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
        System.out.flush();
    }

    private void colourCloud(SerializedBoardAbstract serializedBoardAbstract, int index){
        if(serializedBoardAbstract.getClouds().get(index).getStudents().size()==0){
            System.out.print(ANSI_BLACK);
        }
        System.out.flush();
    }

    private void colourArchipelago(SerializedBoardAbstract serializedBoardAbstract, int i, List<Archipelago> archipelagos, SerializedBoardAbstract serializedBoardAdvanced) {
        if(serializedBoardAbstract.getMn().getCurrentPosition().equals(serializedBoardAbstract.getArchipelagos().get(i))) {
            System.out.print(ANSI_GREEN);
        }
        if(serializedBoardAbstract.getArchipelagos().get(i).getOwner() != null) {
            System.out.print(ANSI_YELLOW);
        }

        System.out.print(i + ": ");
        System.out.print(archipelagos.get(i).toString());
        System.out.flush();
    }

    private void colourSchool(SerializedBoardAbstract serializedBoardAbstract, int i) {
        if(this.playerNick.equals(serializedBoardAbstract.getSchools().get(i).getPlayer().getNickname())) {
            System.out.print(ANSI_CYAN);
        }
        System.out.flush();
    }

    private void removeColour() {
        System.out.print(ANSI_RESET);
        System.out.flush();
    }
}
