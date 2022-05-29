package it.polimi.ingsw.View;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;
import it.polimi.ingsw.Model.Board.SerializedBoardAdvanced;
import it.polimi.ingsw.Model.Cards.*;
import it.polimi.ingsw.Model.Enumerations.CharacterCardEnumeration;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Places.Archipelago;
import it.polimi.ingsw.Model.Places.School.School;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static it.polimi.ingsw.View.CLIColours.*;

public class CLIView extends ClientView {

    private String playerNick;

    public CLIView(Client client) {
        super(client);
    }

    @Override
    public void printErrorMessage(String err) {
        System.out.print(ANSI_RED);
        printLines(err);
        System.out.print(ANSI_RESET);

        System.out.flush();
    }

    public void printCustom(String msg) {
        printLines(msg);

        System.out.flush();
    }

    private void printLines(String msg) {
        for(int i = 0; i < msg.length(); i++)
            System.out.print("-");
        System.out.println();

        System.out.println(msg);

        for(int i = 0; i < msg.length(); i++)
            System.out.print("-");
        System.out.println();
    }

    public void endView() {
        System.out.println();
        printCustom("Closing connection...");
        System.out.println();
        System.out.flush();
    }

    @Override
    public void clientDisconnectionEnd() {
        System.out.print(ANSI_RED);
        printCustom("An error occurred on one of the other players. The match is ended. :(");
        System.out.print(ANSI_RESET);
    }

    public void askReconnect() {
        String response;

        System.out.println();
        do {
            System.out.println("> Do you want to be play another match? [Y/N]");
            System.out.print("> ");
            response = input.nextLine();
        } while(!response.equalsIgnoreCase("Y") && !response.equalsIgnoreCase("N"));

        if(response.equalsIgnoreCase("Y"))
            this.client.setClientReconnect(true);
        else
            this.client.setClientReconnect(false);
    }

    @Override
    public void askCLIorGUI() {
        String response;

        do {
            System.out.println("> Select between CLI[0] or GUI[1]:");
            System.out.print("> ");
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
            System.out.println("> Write your nickname:");
            System.out.print("> ");
            System.out.flush();
            nickname = input.nextLine();
            this.playerNick = nickname;
        } while (nickname.equals(""));

        if(numPlayer==2){
            colour= colourList.get(0).equals(PlayerColour.WHITE) ? "BLACK" : "WHITE";
            System.out.println("> your colour is " + colour);
        }
        if(numPlayer == 3 ){
            if(colourList.size()==1){
                if(colourList.get(0).equals(PlayerColour.WHITE)) {
                    do {
                        System.out.println("> What colour would you like [Black/Gray]:");
                        System.out.print("> ");
                        System.out.flush();
                        colour = input.nextLine();
                    } while (!colour.equalsIgnoreCase("black") && !colour.equalsIgnoreCase("gray"));
                }
                if(colourList.get(0).equals(PlayerColour.BLACK)) {
                    do {
                        System.out.println("> What colour would you like [White/Gray]:");
                        System.out.print("> ");
                        System.out.flush();
                        colour = input.nextLine();
                    } while (!colour.equalsIgnoreCase("white") && !colour.equalsIgnoreCase("gray"));
                }
                if(colourList.get(0).equals(PlayerColour.GRAY)) {
                    do {
                        System.out.println("> What colour would you like [Black/White]:");
                        System.out.print("> ");
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
                System.out.println("> Your colour is " + colour);
            }
        }
        if (numPlayer == 4){
            long black=colourList.stream().filter(x -> x.equals(PlayerColour.BLACK)).count();
            long white=colourList.stream().filter(x -> x.equals(PlayerColour.WHITE)).count();

            if(colourList.size()==3){
                colour=(black==2)? "WHITE" : "BLACK";
                System.out.println("> Your colour is " + colour);
            }
            if(colourList.size()==2){
                if(black==1 || white==1){
                    do {
                        System.out.println("> What colour would you like [Black/White]:");
                        System.out.print("> ");
                        System.out.flush();
                        colour = input.nextLine();
                    } while (!colour.equalsIgnoreCase("BLACK") && !colour.equalsIgnoreCase("WHITE"));
                }
                else{
                    colour=(black==2)? "WHITE" : "BLACK";
                    System.out.println("> Your colour is " + colour);
                }
            }
            if(colourList.size()==1){
                do {
                    System.out.println("> What colour would you like [Black/White]:");
                    System.out.print("> ");
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
            System.out.println("> Write your nickname:");
            System.out.print("> ");
            System.out.flush();
            nickname = input.nextLine();
            this.playerNick = nickname;
        } while (nickname.equals(""));

        do {
            System.out.println("> Select number of players [2 - 3 - 4]:");
            System.out.print("> ");
            System.out.flush();
            numPlayers = input.nextLine();
        } while (!numPlayers.equals("2") && !numPlayers.equals("3") && !numPlayers.equals("4"));

        if(Integer.parseInt(numPlayers)==2 || Integer.parseInt(numPlayers)==4){
            do {
                System.out.println("> What colour would you like [Black/White]:");
                System.out.print("> ");
                System.out.flush();
                colour = input.nextLine();
            } while (!colour.equalsIgnoreCase("black") && !colour.equalsIgnoreCase("white"));
        }
        if(Integer.parseInt(numPlayers)==3){
            do {

                System.out.println("> What colour would you like [Black/White/Gray]:");
                System.out.print("> ");
                System.out.flush();
                colour = input.nextLine();
            } while (!colour.equalsIgnoreCase("black") && !colour.equalsIgnoreCase("white") &&
                    !colour.equalsIgnoreCase("gray"));
        }

        do {
            System.out.println("> Do you want to play in ADVANCED mode? [Y/N]:");
            System.out.print("> ");
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
            if(serializedBoardAbstract.getClouds().get(i).getStudents().size() > 0) {
                System.out.print(i + ": ");
                System.out.println(serializedBoardAbstract.getClouds().get(i).toString());
            }
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
    }

    private void printWaitTurn(SerializedBoardAbstract serializedBoardAbstract) {
        System.out.print(ANSI_GREEN);
        if(this.playerNick.equals(serializedBoardAbstract.getCurrentPlayer().getNickname())) {
            System.out.println("\n> IT'S YOUR TURN! MAKE A MOVE!");
            System.out.print(ANSI_RESET);

            manageNextMove(serializedBoardAbstract);
        } else {
            System.out.println("\n> IT'S " + serializedBoardAbstract.getCurrentPlayer().getNickname() + "'s TURN! WAIT...");
        }
        System.out.print(ANSI_RESET);
        System.out.flush();
    }

    private void manageNextMove(SerializedBoardAbstract serializedBoardAbstract) {
        //System.out.println("State: " + serializedBoardAbstract.getCurrentState());
        switch(serializedBoardAbstract.getCurrentState()){
            case PLANNING2:
                askAssistantCard();
                break;
            case ACTION1:
                askMoveStudents(serializedBoardAbstract);
                break;
            case ACTION2:
                askMoveMotherNature(serializedBoardAbstract);
                break;
            case ACTION3:
                askCloudChoice(serializedBoardAbstract);
                break;
            case END:
                showWinner(serializedBoardAbstract);
                this.client.setEndGame(true);
                break;
        }
    }

    // This asks the player what AssistantCard does he want to use
    private void askAssistantCard(){
        int[] cardsMotherNatureMoves= {1, 1, 2, 2, 3, 3, 4, 4, 5, 5};
        String response;
        int turnPriority = 0;

        do {
            System.out.println("> Choose the turn priority of the card you want to use: ");
            System.out.print("> ");
            System.out.flush();
            response = input.nextLine();

            if(checkInt(response)) {
                turnPriority = Integer.parseInt(response);
            }
        } while ((turnPriority < 1 || turnPriority > 10) && !checkInt(response));

        // turnPriority is enough to identify the card: assign motherNatureMovement automatically
        client.asyncWriteToSocket("assistantCard " + cardsMotherNatureMoves[turnPriority - 1] + " " + turnPriority);
    }

    // This asks the player to move a student during the ACTION1 state
    private void askMoveStudents(SerializedBoardAbstract serializedBoardAbstract){
        Set<String> possibleColours = new HashSet<String>();
        possibleColours.add("pink"); possibleColours.add("red"); possibleColours.add("yellow"); possibleColours.add("blue"); possibleColours.add("green");
        String command;
        String colour;
        String action=null;
        do {
            do {
                if (serializedBoardAbstract.getType().equals("advanced")) {
                    System.out.println("> Move a student [DiningRoom/Archipelago] or buy a card [Card]:");
                } else {
                    System.out.println("> Move a student [DiningRoom/Archipelago]:");
                }
                System.out.print("> ");
                System.out.flush();
                command = input.nextLine();
            } while (checkStudentInput(serializedBoardAbstract, command));

            if (!command.equalsIgnoreCase("Card")) {
                action="ok";
                if (command.equalsIgnoreCase("DiningRoom")) {
                    command = "studentHallToDiningRoom";
                } else if (command.equalsIgnoreCase("Archipelago")) {
                    command = "studentToArchipelago";
                }

                do {
                    System.out.println("> Choose the colour's student to move from hall: ");
                    System.out.print("> ");
                    System.out.flush();
                    colour = input.nextLine();
                } while (!possibleColours.contains(colour.toLowerCase()));

                if (command.equals("studentHallToDiningRoom")) { //studentHallToDiningRoom
                    client.asyncWriteToSocket(command + " " + colour);
                } else { //studentToArchipelago
                    String response;
                    int destArchipelagoIndex = 0;

                    do {
                        System.out.println("> Choose the index of the destination archipelago: ");
                        System.out.print("> ");
                        System.out.flush();
                        response = input.nextLine();

                        if(checkInt(response)) {
                            destArchipelagoIndex = Integer.parseInt(response);
                        }
                    } while ((destArchipelagoIndex < 0 || destArchipelagoIndex > serializedBoardAbstract.getArchipelagos().size()) && !checkInt(response));


                    client.asyncWriteToSocket(command + " " + colour + " " + destArchipelagoIndex);
                }
            } else {
                action=askCharacterCard((SerializedBoardAdvanced) serializedBoardAbstract);
            }
        }while(action.equalsIgnoreCase("Back"));
    }

    private boolean checkStudentInput(SerializedBoardAbstract serializedBoardAbstract, String command) {
        if(serializedBoardAbstract.getType().equals("advanced")) {
            return !command.equalsIgnoreCase("DiningRoom") && !command.equalsIgnoreCase("Archipelago")
                    && !command.equalsIgnoreCase("Card");
        } else {
            return !command.equalsIgnoreCase("DiningRoom") && !command.equalsIgnoreCase("Archipelago");
        }
    }

    private String askCharacterCard(SerializedBoardAdvanced serializedBoardAdvanced) {
        String card;

        do {
            System.out.println("> Choose a card from the extracted ones or go back [Back]:");
            System.out.print("> ");
            System.out.flush();
            card = input.nextLine();
        } while(!checkCharacterChoice(serializedBoardAdvanced, card) && !card.equalsIgnoreCase("back"));

        if(!card.equalsIgnoreCase("back")) {
            manageCard(serializedBoardAdvanced, card);
            return "ok";
        }
        else{
            return card;
        }
    }

    private boolean checkCharacterChoice(SerializedBoardAdvanced serializedBoardAdvanced, String card) {
        List<AbstractCharacterCard> extracted = serializedBoardAdvanced.getExtractedCards();

        if(card.equalsIgnoreCase("exchangethreestudents") || card.equalsIgnoreCase("exchangethree")
                || card.equalsIgnoreCase("ex3")) {

            if(extracted.get(0).getType().equals(CharacterCardEnumeration.EXCHANGE_THREE_STUDENTS)
                    || extracted.get(1).getType().equals(CharacterCardEnumeration.EXCHANGE_THREE_STUDENTS)
                    || extracted.get(2).getType().equals(CharacterCardEnumeration.EXCHANGE_THREE_STUDENTS)) {
                return true;
            }
        }

        if(card.equalsIgnoreCase("exchangetwohalldining") || card.equalsIgnoreCase("ex2")
                || card.equalsIgnoreCase("exchangetwo")) {
            if (extracted.get(0).getType().equals(CharacterCardEnumeration.EXCHANGE_TWO_HALL_DINING)
                    || extracted.get(1).getType().equals(CharacterCardEnumeration.EXCHANGE_TWO_HALL_DINING)
                    || extracted.get(2).getType().equals(CharacterCardEnumeration.EXCHANGE_TWO_HALL_DINING)) {
                return true;
            }
        }

        if(card.equalsIgnoreCase("excludecolourfromcounting") || card.equalsIgnoreCase("excludecolour")
                || card.equalsIgnoreCase("excludec") ||  card.equalsIgnoreCase("exclude")
                || card.equalsIgnoreCase("excl")) {
            if (extracted.get(0).getType().equals(CharacterCardEnumeration.EXCLUDE_COLOUR_FROM_COUNTING)
                    || extracted.get(1).getType().equals(CharacterCardEnumeration.EXCLUDE_COLOUR_FROM_COUNTING)
                    || extracted.get(2).getType().equals(CharacterCardEnumeration.EXCLUDE_COLOUR_FROM_COUNTING)) {
                return true;
            }
        }

        if(card.equalsIgnoreCase("extrastudentindining") || card.equalsIgnoreCase("extrastudent")
                || card.equalsIgnoreCase("extra")) {
            if (extracted.get(0).getType().equals(CharacterCardEnumeration.EXTRA_STUDENT_IN_DINING)
                    || extracted.get(1).getType().equals(CharacterCardEnumeration.EXTRA_STUDENT_IN_DINING)
                    || extracted.get(2).getType().equals(CharacterCardEnumeration.EXTRA_STUDENT_IN_DINING)) {
                return true;
            }
        }

        if(card.equalsIgnoreCase("fakemnmovement") || card.equalsIgnoreCase("fakemn")
                || card.equalsIgnoreCase("fake")) {
            if (extracted.get(0).getType().equals(CharacterCardEnumeration.FAKE_MN_MOVEMENT)
                    || extracted.get(1).getType().equals(CharacterCardEnumeration.FAKE_MN_MOVEMENT)
                    || extracted.get(2).getType().equals(CharacterCardEnumeration.FAKE_MN_MOVEMENT)) {
                return true;
            }
        }

        if(card.equalsIgnoreCase("forbidisland") || card.equalsIgnoreCase("forbid")) {
            if (extracted.get(0).getType().equals(CharacterCardEnumeration.FORBID_ISLAND)
                    || extracted.get(1).getType().equals(CharacterCardEnumeration.FORBID_ISLAND)
                    || extracted.get(2).getType().equals(CharacterCardEnumeration.FORBID_ISLAND)) {
                return true;
            }
        }

        if(card.equalsIgnoreCase("placeonestudent") || card.equalsIgnoreCase("placestudent")
                || card.equalsIgnoreCase("place")) {
            if (extracted.get(0).getType().equals(CharacterCardEnumeration.PLACE_ONE_STUDENT)
                    || extracted.get(1).getType().equals(CharacterCardEnumeration.PLACE_ONE_STUDENT)
                    || extracted.get(2).getType().equals(CharacterCardEnumeration.PLACE_ONE_STUDENT)) {
                return true;
            }
        }

        if(card.equalsIgnoreCase("reducecolourindining") || card.equalsIgnoreCase("reducecolour")
                || card.equalsIgnoreCase("reduce")) {
            if (extracted.get(0).getType().equals(CharacterCardEnumeration.REDUCE_COLOUR_IN_DINING)
                    || extracted.get(1).getType().equals(CharacterCardEnumeration.REDUCE_COLOUR_IN_DINING)
                    || extracted.get(2).getType().equals(CharacterCardEnumeration.REDUCE_COLOUR_IN_DINING)) {
                return true;
            }
        }

        if(card.equalsIgnoreCase("takeprofessoronequity") || card.equalsIgnoreCase("takeprofessor")
                || card.equalsIgnoreCase("take")) {
            if (extracted.get(0).getType().equals(CharacterCardEnumeration.TAKE_PROFESSOR_ON_EQUITY)
                    || extracted.get(1).getType().equals(CharacterCardEnumeration.TAKE_PROFESSOR_ON_EQUITY)
                    || extracted.get(2).getType().equals(CharacterCardEnumeration.TAKE_PROFESSOR_ON_EQUITY)) {
                return true;
            }
        }

        if(card.equalsIgnoreCase("towernovalue") || card.equalsIgnoreCase("tower")) {
            if (extracted.get(0).getType().equals(CharacterCardEnumeration.TOWER_NO_VALUE)
                    || extracted.get(1).getType().equals(CharacterCardEnumeration.TOWER_NO_VALUE)
                    || extracted.get(2).getType().equals(CharacterCardEnumeration.TOWER_NO_VALUE)) {
                return true;
            }
        }

        if(card.equalsIgnoreCase("twoextraislands") || card.equalsIgnoreCase("twoextrai")
                || card.equalsIgnoreCase("2extrai")) {
            if (extracted.get(0).getType().equals(CharacterCardEnumeration.TWO_EXTRA_ISLANDS)
                    || extracted.get(1).getType().equals(CharacterCardEnumeration.TWO_EXTRA_ISLANDS)
                    || extracted.get(2).getType().equals(CharacterCardEnumeration.TWO_EXTRA_ISLANDS)) {
                return true;
            }
        }

        if(card.equalsIgnoreCase("twoextrapoints") || card.equalsIgnoreCase("twoextrap")
                || card.equalsIgnoreCase("2extrap")) {
            if (extracted.get(0).getType().equals(CharacterCardEnumeration.TWO_EXTRA_POINTS)
                    || extracted.get(1).getType().equals(CharacterCardEnumeration.TWO_EXTRA_POINTS)
                    || extracted.get(2).getType().equals(CharacterCardEnumeration.TWO_EXTRA_POINTS)) {
                return true;
            }
        }

        return false;
    }

    private void manageCard(SerializedBoardAdvanced serializedBoardAdvanced, String card) {
        List<AbstractCharacterCard> extracted = serializedBoardAdvanced.getExtractedCards();

        School school = null;
        for(School sc : serializedBoardAdvanced.getSchools()) {
            if(sc.getPlayer().getNickname().equals(this.playerNick)) {
                school = sc;
            }
        }

        if(card.equalsIgnoreCase("exchangethreestudents") || card.equalsIgnoreCase("exchangethree")
                || card.equalsIgnoreCase("ex3")) {
            ExchangeThreeStudents ex3s = null;

            if(extracted.get(0).getType().equals(CharacterCardEnumeration.EXCHANGE_THREE_STUDENTS))
                ex3s = (ExchangeThreeStudents) extracted.get(0);
            else if(extracted.get(1).getType().equals(CharacterCardEnumeration.EXCHANGE_THREE_STUDENTS))
                ex3s = (ExchangeThreeStudents) extracted.get(1);
            else if(extracted.get(2).getType().equals(CharacterCardEnumeration.EXCHANGE_THREE_STUDENTS))
                ex3s = (ExchangeThreeStudents) extracted.get(2);


            askExchangeThreeStudents(ex3s, school);
        }

        if(card.equalsIgnoreCase("exchangetwohalldining") || card.equalsIgnoreCase("ex2")
                || card.equalsIgnoreCase("exchangetwo")) {
            askExchangeTwoHallDining(school);
        }

        if(card.equalsIgnoreCase("excludecolourfromcounting") || card.equalsIgnoreCase("excludecolour")
                || card.equalsIgnoreCase("excludec") ||  card.equalsIgnoreCase("exclude")
                || card.equalsIgnoreCase("excl")) {
            askExcludeColourFromCounting();
        }

        if(card.equalsIgnoreCase("extrastudentindining") || card.equalsIgnoreCase("extrastudent")
                || card.equalsIgnoreCase("extra")) {
            ExtraStudentInDining exst = null;

            if(extracted.get(0).getType().equals(CharacterCardEnumeration.EXTRA_STUDENT_IN_DINING))
                exst = (ExtraStudentInDining) extracted.get(0);
            else if(extracted.get(1).getType().equals(CharacterCardEnumeration.EXCHANGE_TWO_HALL_DINING))
                exst = (ExtraStudentInDining) extracted.get(1);
            else if(extracted.get(2).getType().equals(CharacterCardEnumeration.EXCHANGE_TWO_HALL_DINING))
                exst = (ExtraStudentInDining) extracted.get(2);

            askExtraStudentInDining(exst, school);
        }

        if(card.equalsIgnoreCase("fakemnmovement") || card.equalsIgnoreCase("fakemn")
                || card.equalsIgnoreCase("fake")) {
            askFakeMNMovement(serializedBoardAdvanced);
        }

        if(card.equalsIgnoreCase("forbidisland") || card.equalsIgnoreCase("forbid")) {
            askForbidIsland(serializedBoardAdvanced);
        }

        if(card.equalsIgnoreCase("placeonestudent") || card.equalsIgnoreCase("placestudent")
                || card.equalsIgnoreCase("place")) {
            PlaceOneStudent place = null;

            if(extracted.get(0).getType().equals(CharacterCardEnumeration.PLACE_ONE_STUDENT))
                place = (PlaceOneStudent) extracted.get(0);
            else if(extracted.get(1).getType().equals(CharacterCardEnumeration.PLACE_ONE_STUDENT))
                place = (PlaceOneStudent) extracted.get(1);
            else if(extracted.get(2).getType().equals(CharacterCardEnumeration.PLACE_ONE_STUDENT))
                place = (PlaceOneStudent) extracted.get(2);

            askPlaceOneStudent(serializedBoardAdvanced, place);
        }

        if(card.equalsIgnoreCase("reducecolourindining") || card.equalsIgnoreCase("reducecolour")
                || card.equalsIgnoreCase("reduce")) {
            askReduceColourInDining();
        }

        if(card.equalsIgnoreCase("takeprofessoronequity") || card.equalsIgnoreCase("takeprofessor")
                || card.equalsIgnoreCase("take")) {
            this.client.asyncWriteToSocket("takeProfessorOnEquity");

        }

        if(card.equalsIgnoreCase("towernovalue") || card.equalsIgnoreCase("tower")) {
            this.client.asyncWriteToSocket("towerNoValue");
        }

        if(card.equalsIgnoreCase("twoextraislands") || card.equalsIgnoreCase("twoextrai")
                || card.equalsIgnoreCase("2extrai")) {
            this.client.asyncWriteToSocket("twoExtraIslands");

        }

        if(card.equalsIgnoreCase("twoextrapoints") || card.equalsIgnoreCase("twoextrap")
                || card.equalsIgnoreCase("2extrap")) {
            this.client.asyncWriteToSocket("twoExtraPoints");

        }
    }

    //TODO: for some cards add the colour check
    private void askExchangeThreeStudents(ExchangeThreeStudents ex3s, School school) {
        List<String> cardStudents = new ArrayList<>();
        List<String> hallStudents = new ArrayList<>();

        System.out.println("Students on the card: " + ex3s.printStudents());
        System.out.println("Student in the hall: " + school.getStudentsHall().toString());

        for(int i = 0; i < 3; i++) {
            System.out.println("> Select the colour of the [#" + (i+1) + "] student from the card or none [-]: ");
            System.out.print("> ");
            System.out.flush();
            cardStudents.add(input.nextLine());
        }

        for(int i = 0; i < 3; i++) {
            System.out.println("> Select the colour of the student [" + (i+1) + "] from the hall or none [-]: ");
            System.out.print("> ");
            System.out.flush();
            hallStudents.add(input.nextLine());
        }

        //The space after the command is missing because it is already present in cardStudents
        this.client.asyncWriteToSocket("exchangeThreeStudents " + cardStudents.get(0) + " " +
                cardStudents.get(1) + " " + cardStudents.get(2) + " " +
                hallStudents.get(0) + " " + hallStudents.get(1) + " " + hallStudents.get(2));
    }

    private void askExchangeTwoHallDining(School school) {
        List<String> diningStudents = new ArrayList<>();
        List<String> hallStudents = new ArrayList<>();

        System.out.println("Students in the hall: " + school.getStudentsHall().toString());
        System.out.print("Students in dining room: ");
        System.out.print("[BLUE: " + school.getNumStudentColour(SPColour.BLUE) + ", ");
        System.out.print("PINK: " + school.getNumStudentColour(SPColour.PINK) + ", ");
        System.out.print("RED: " + school.getNumStudentColour(SPColour.RED) + ", ");
        System.out.print("GREEN: " + school.getNumStudentColour(SPColour.GREEN) + ", ");
        System.out.print("YELLOW: " + school.getNumStudentColour(SPColour.YELLOW) + "]\n");

        for(int i = 0; i < 2; i++) {
            if(i == 0)
                System.out.println("> Select one student from the hall: ");
            else
                System.out.println("> Select another student from the hall or none [-]: ");

            System.out.print("> ");
            System.out.flush();
            hallStudents.add(input.nextLine());
        }

        for(int i = 0; i < 2; i++) {
            if(i == 0)
                System.out.println("> Select one student from the dining room: ");
            else
                System.out.println("> Select another student from the dining room or none [-]: ");

            System.out.print("> ");
            System.out.flush();
            diningStudents.add(input.nextLine());
        }

        //The space after the command is missing because it is already present in cardStudents
        this.client.asyncWriteToSocket("exchangeTwoHallDining " +
                hallStudents.get(0) + " " + hallStudents.get(1) + " " +
                diningStudents.get(0) + " " + diningStudents.get(1));
    }

    private void askExcludeColourFromCounting() {
        Set<String> possibleColours = new HashSet<>();
        possibleColours.add("pink"); possibleColours.add("red"); possibleColours.add("yellow"); possibleColours.add("blue"); possibleColours.add("green");
        String colour = null;

        do {
            System.out.println("> Select a colour to exclude from the next conquer attempt: ");
            System.out.print("> ");
            System.out.flush();
            colour = input.nextLine();
        } while(!possibleColours.contains(colour.toLowerCase()));

        this.client.asyncWriteToSocket("excludeColourFromCounting " +  colour);
    }

    private void askExtraStudentInDining(ExtraStudentInDining exst, School school) {
        Set<String> possibleColours = new HashSet<>();
        possibleColours.add("pink"); possibleColours.add("red"); possibleColours.add("yellow"); possibleColours.add("blue"); possibleColours.add("green");
        String cardStudent = null;

        System.out.println("Students on the card: " + exst.printStudents());

        do {
            System.out.println("> Select one student from the card: ");
            System.out.print("> ");
            System.out.flush();
            cardStudent = input.nextLine();
        }while(!possibleColours.contains(cardStudent.toLowerCase()));

        //The space after the command is missing because it is already present in cardStudents
        this.client.asyncWriteToSocket("extraStudentInDining " + cardStudent);
    }

    private void askFakeMNMovement(SerializedBoardAdvanced serializedBoardAdvanced) {
        String response;
        int move = 0;

        do{
            System.out.println("> Insert the archipelago index on which you want to move to (and come back after): ");
            System.out.print("> ");
            System.out.flush();
            response = input.nextLine();

            if(checkInt(response)) {
                move = Integer.parseInt(response);
            }
        } while((move < 0 || move > serializedBoardAdvanced.getArchipelagos().size()) && !checkInt(response) );

        this.client.asyncWriteToSocket("fakeMNMovement " + move);
    }

    private void askForbidIsland(SerializedBoardAdvanced serializedBoardAdvanced) {
        String response;
        int index = 0;

        do{
            System.out.println("> Insert the archipelago index on which you want put a forbid tile: ");
            System.out.print("> ");
            System.out.flush();
            response = input.nextLine();

            if(checkInt(response)) {
                index = Integer.parseInt(response);
            }
        } while((index < 0 || index > serializedBoardAdvanced.getArchipelagos().size()) && !checkInt(response));

        this.client.asyncWriteToSocket("forbidIsland " + index);
    }

    private void askPlaceOneStudent(SerializedBoardAdvanced serializedBoardAdvanced, PlaceOneStudent place) {
        Set<String> possibleColours = new HashSet<String>();
        possibleColours.add("pink"); possibleColours.add("red"); possibleColours.add("yellow"); possibleColours.add("blue"); possibleColours.add("green");
        String cardStudent;
        String response;
        int move = 0;

        System.out.println("Students on the card: " + place.printStudents());

        do {
            System.out.println("> Select one student from the card: ");
            System.out.print("> ");
            System.out.flush();
            cardStudent = input.nextLine();
        }while(!possibleColours.contains(cardStudent.toLowerCase()));

        do{
            System.out.println("> Insert the archipelago index on which you want put the student: ");
            System.out.print("> ");
            System.out.flush();
            response = input.nextLine();

            if(checkInt(response)) {
                move = Integer.parseInt(response);
            }
        } while((move < 0 || move > serializedBoardAdvanced.getArchipelagos().size()) && !checkInt(response));

        this.client.asyncWriteToSocket("placeOneStudent " + cardStudent + " " + move);
    }
    private void askReduceColourInDining(){
        Set<String> possibleColours = new HashSet<String>();
        possibleColours.add("pink"); possibleColours.add("red"); possibleColours.add("yellow"); possibleColours.add("blue"); possibleColours.add("green");
        String colour = null;

        do {
            System.out.println("> Select a colour of students that you want to remove from dining rooms: ");
            System.out.print("> ");
            System.out.flush();
            colour = input.nextLine();
        }while(!possibleColours.contains(colour.toLowerCase()));

        this.client.asyncWriteToSocket("reduceColourInDining " + colour);

    }

    // This asks the player how much does he want to move Mother Nature
    private void askMoveMotherNature(SerializedBoardAbstract serializedBoardAbstract){
        String response;
        int moves = 0;
        String action = null;

        do{
            do{
                if (serializedBoardAbstract.getType().equals("advanced")) {
                    System.out.println("> Is Action2: Move mother nature [Move] or buy a card [Card] ");
                    System.out.print("> ");
                    System.out.flush();
                    response = input.nextLine();
                } else {
                    response = "move";
                }

            }while(!response.equalsIgnoreCase("move") && !response.equalsIgnoreCase("card"));

            if(response.equalsIgnoreCase("move")) {
                action = "ok";
                do {
                    System.out.println("> Is Action2: How much do you want to move Mother Nature? ");
                    System.out.print("> ");
                    System.out.flush();
                    response = input.nextLine();

                    if (checkInt(response)) {
                        moves = Integer.parseInt(response);
                    }
                } while ((moves < 0 || moves > 7) && !checkInt(response));

                client.asyncWriteToSocket("moveMotherNature " + moves);
            }
            else{
                action = askCharacterCard((SerializedBoardAdvanced) serializedBoardAbstract);
            }
        }while(action.equalsIgnoreCase("back"));
    }

    private void askCloudChoice(SerializedBoardAbstract serializedBoardAbstract) {
        String response;
        int cloudIndex = 0;
        String action = null;

        do {
            do {
                if(serializedBoardAbstract.getType().equals("advanced")) {
                    System.out.println("> Is Action2: Choose a cloud [Cloud] or buy a card [Card] ");
                    System.out.print("> ");
                    System.out.flush();
                    response = input.nextLine();
                } else {
                    response = "cloud";
                }
            } while (!response.equalsIgnoreCase("cloud") && !response.equalsIgnoreCase("card"));

            if(response.equalsIgnoreCase("cloud")) {
                action = "ok";
                if (serializedBoardAbstract.getClouds().stream().filter(x -> x.getStudents().size() > 0).count() > 1) {
                    do {
                        System.out.println("> Is Action3: Which cloud do you choose? ");
                        System.out.print("> ");
                        System.out.flush();
                        response = input.nextLine();

                        if (checkInt(response)) {
                            cloudIndex = Integer.parseInt(response);
                        }
                    } while ((cloudIndex < 0 || cloudIndex > serializedBoardAbstract.getClouds().size()) && !checkInt(response));
                } else {
                    for (int i = 0; i < serializedBoardAbstract.getClouds().size(); i++) {
                        if (serializedBoardAbstract.getClouds().get(i).getStudents().size() > 0) {
                            cloudIndex = i;
                        }
                    }
                }

                client.asyncWriteToSocket("studentCloudToSchool " + cloudIndex);
            }
            else{
                action = askCharacterCard((SerializedBoardAdvanced) serializedBoardAbstract);
            }
        }while (response.equalsIgnoreCase("back"));
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

    private void showWinner(SerializedBoardAbstract serializedBoardAbstract) {
        if(serializedBoardAbstract.getNicknameWinner().equals(this.playerNick)) {
            colourWinner();
        } else {
            colourLooser(serializedBoardAbstract);
        }
    }

    private boolean checkInt(String response) {
        try {
            int conversion = Integer.parseInt(response);
        }catch(NumberFormatException nfe) {
            return false;
        }

        return true;
    }

    private void colourWinner() {
        System.out.println(ANSI_GREEN);
        System.out.println("\n----------------------------------------");
        System.out.println("YOU ARE THE WINNER!");
        System.out.println("----------------------------------------\n");
        System.out.println(ANSI_RESET);
        System.out.flush();
    }

    private void colourLooser(SerializedBoardAbstract se) {
        System.out.println(ANSI_RED);
        System.out.println("\n----------------------------------------");
        System.out.println("YOU LOST! THE WINNER IS " + se.getNicknameWinner());
        System.out.println("----------------------------------------\n");
        System.out.println(ANSI_RESET);
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
