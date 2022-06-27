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
import org.fusesource.jansi.AnsiConsole;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static it.polimi.ingsw.View.CLIColours.*;

public class CLIView extends ClientView {

    private String playerNick;
    private String TAB = "    ";

    /**
     * constructor of the class
     * @param client owner of this CLI view
     */
    public CLIView(Client client) {
        super(client);
    }

    /**
     * method that prints an error
     * @param err error to be printed
     */
    @Override
    public void printErrorMessage(String err) {
        AnsiConsole.systemInstall();
        System.out.print(ANSI_RED);
        printLines(err);
        System.out.print(ANSI_RESET);
        AnsiConsole.systemUninstall();
        System.out.flush();
    }

    /**
     * method that prints a message
     * @param msg message to be printed
     */
    public void printCustom(String msg) {
        AnsiConsole.systemInstall();
        printLines(msg);
        AnsiConsole.systemUninstall();
        System.out.flush();
    }

    /**
     * message that prints a line of "-" of the same size of a message
     * @param msg
     */
    private void printLines(String msg) {
        for(int i = 0; i < msg.length(); i++)
            System.out.print("-");
        System.out.println();

        System.out.println(msg);

        for(int i = 0; i < msg.length(); i++)
            System.out.print("-");
        System.out.println();
    }

    /**
     * method that ends a client view
     */
    public void endView() {
        AnsiConsole.systemInstall();
        System.out.println();
        printCustom("Closing connection...");
        System.out.println();
        AnsiConsole.systemUninstall();
        System.out.flush();
        System.exit(0);
    }

    /**
     * method that disconnect a client
     */
    @Override
    public void clientDisconnectionEnd() {
        AnsiConsole.systemInstall();
        System.out.print(ANSI_RED);
        printCustom("An error occurred on one of the other players. The match is ended. :(");
        System.out.print(ANSI_RESET);
        AnsiConsole.systemUninstall();
    }

    /**
     * method that asks a client if he wants to reconnect to the server, after the disconnection of another client from the game
     */
    public void askReconnect() {
        String response;

        AnsiConsole.systemInstall();
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

        AnsiConsole.systemUninstall();
    }

    /**
     * method that asks a client if he wants to play through a CLI or GUI view
     */
    @Override
    public void askCLIorGUI() {
        String response;

        AnsiConsole.systemInstall();
        do {
            System.out.println("> Select between CLI[0] or GUI[1]:");
            System.out.print("> ");
            System.out.flush();
            response = input.nextLine();

        } while (!response.equals("0") && !response.equals("1"));
        AnsiConsole.systemUninstall();

        if (response.equals("1")) {
            client.setCLIorGUI(true);
        }
    }

    /**
     * method that asks a client which nickname and colour (among the available one) he wants to use
     * @param chosenColourList list of available colours
     * @param numPlayer number of players of the game
     */
    @Override
    public void askNickName(List<PlayerColour> chosenColourList, int numPlayer) {
        String nickname;
        String colour = "";

        AnsiConsole.systemInstall();
        do {
            System.out.println("> Write your nickname:");
            System.out.print("> ");
            System.out.flush();
            nickname = input.nextLine();
            this.playerNick = nickname;
        } while (nickname.equals(""));

        if(numPlayer==2){
            colour= chosenColourList.get(0).equals(PlayerColour.WHITE) ? "BLACK" : "WHITE";
            System.out.println("> your colour is " + colour);
        }
        if(numPlayer == 3 ){
            if(chosenColourList.size() == 1){
                if(chosenColourList.get(0).equals(PlayerColour.WHITE)) {
                    do {
                        System.out.println("> What colour would you like [Black/Gray]:");
                        System.out.print("> ");
                        System.out.flush();
                        colour = input.nextLine();
                    } while (!colour.equalsIgnoreCase("black") && !colour.equalsIgnoreCase("gray"));
                }
                if(chosenColourList.get(0).equals(PlayerColour.BLACK)) {
                    do {
                        System.out.println("> What colour would you like [White/Gray]:");
                        System.out.print("> ");
                        System.out.flush();
                        colour = input.nextLine();
                    } while (!colour.equalsIgnoreCase("white") && !colour.equalsIgnoreCase("gray"));
                }
                if(chosenColourList.get(0).equals(PlayerColour.GRAY)) {
                    do {
                        System.out.println("> What colour would you like [Black/White]:");
                        System.out.print("> ");
                        System.out.flush();
                        colour = input.nextLine();
                    } while (!colour.equalsIgnoreCase("black") && !colour.equalsIgnoreCase("white"));
                }
            }
            if(chosenColourList.size() == 2){
                if(chosenColourList.contains(PlayerColour.WHITE) && chosenColourList.contains(PlayerColour.BLACK)) {
                    colour = "GRAY";
                }
                if(chosenColourList.contains(PlayerColour.WHITE) && chosenColourList.contains(PlayerColour.GRAY)) {
                    colour= "BLACK";
                }
                if(chosenColourList.contains(PlayerColour.GRAY) && chosenColourList.contains(PlayerColour.BLACK)) {
                    colour = "WHITE";
                }
                System.out.println("> Your colour is " + colour);
            }
        }
        if (numPlayer == 4){
            long black = chosenColourList.stream().filter(x -> x.equals(PlayerColour.BLACK)).count();
            long white = chosenColourList.stream().filter(x -> x.equals(PlayerColour.WHITE)).count();

            if(chosenColourList.size() == 3){
                colour = (black == 2)? "WHITE" : "BLACK";
                System.out.println("> Your colour is " + colour);
            }
            if(chosenColourList.size() == 2){
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
            if(chosenColourList.size() == 1){
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
        AnsiConsole.systemUninstall();

        client.asyncWriteToSocket("addPlayer " + nickname + " " + colour);
        this.client.setNickname(nickname);
    }

    /**
     * method that asks the first player who connects to the server his nickname and colour, how many players we want the game to contain,
     * and the game mode (advanced or standard)
     */
    @Override
    public void askFirstPlayerInfo() {
        String nickname;
        String colour = null;
        String numPlayers;
        String gameMode;

        AnsiConsole.systemInstall();
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
        AnsiConsole.systemUninstall();

        client.asyncWriteToSocket("createMatch " + nickname + " " + colour + " " + numPlayers + " " + gameMode);
        this.client.setNickname(nickname);
    }

    /**
     * method that prints the board information every time something is updated in the model board
     * @param serializedBoardAbstract serialized board that is notified by the model
     */
    @Override
    public void showBoard(SerializedBoardAbstract serializedBoardAbstract) {
        AnsiConsole.systemInstall();
        System.out.println("\n" + TAB + " The Board is this:");
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
                    System.out.println(TAB + "Mother nature is here!");
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
                    System.out.print(TAB + "Mother nature is here!");
                }
                if(serializedBoardAdvanced.getArchipelagos().get(i).getForbidFlag()>0) {
                    System.out.print(TAB + "Forbidden conquer!");
                }
                System.out.println();

                removeColour();
            }

            printSchool(serializedBoardAdvanced);
            printExtractedCC(serializedBoardAdvanced);
        }
        AnsiConsole.systemUninstall();

        printWaitTurn(serializedBoardAbstract);
        System.out.flush();
    }

    /**
     * method that prints whose turn is it at the moment
     * @param serializedBoardAbstract
     */
    private void printWaitTurn(SerializedBoardAbstract serializedBoardAbstract) {
        AnsiConsole.systemInstall();
        System.out.print(ANSI_GREEN);
        if(this.playerNick.equals(serializedBoardAbstract.getCurrentPlayer().getNickname())) {
            System.out.println("\n> IT'S YOUR TURN! MAKE A MOVE!");
            System.out.print(ANSI_RESET);

            manageNextMove(serializedBoardAbstract);
        } else {
            System.out.println("\n> IT'S " + serializedBoardAbstract.getCurrentPlayer().getNickname() + "'s TURN! WAIT...");
        }
        System.out.print(ANSI_RESET);
        AnsiConsole.systemUninstall();

        System.out.flush();
    }

    /**
     * method that changes the phase of the round
     * @param serializedBoardAbstract serialized board notified by the model
     */
    private void manageNextMove(SerializedBoardAbstract serializedBoardAbstract) {
        System.out.println("[CLIView, manageNextMove]: receiving board type " + serializedBoardAbstract.getType());
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

    /**
     * method that asks the player what AssistantCard does he want to use
     */
    private void askAssistantCard(){
        int[] cardsMotherNatureMoves= {1, 1, 2, 2, 3, 3, 4, 4, 5, 5};
        String response;
        int turnPriority = 0;

        AnsiConsole.systemInstall();
        do {
            System.out.println("> Choose the turn priority of the card you want to use: ");
            System.out.print("> ");
            System.out.flush();
            response = input.nextLine();

            if(checkInt(response)) {
                turnPriority = Integer.parseInt(response);
            }
        } while ((turnPriority < 1 || turnPriority > 10) && !checkInt(response));
        AnsiConsole.systemUninstall();

        // turnPriority is enough to identify the card: assign motherNatureMovement automatically
        client.asyncWriteToSocket("assistantCard " + cardsMotherNatureMoves[turnPriority - 1] + " " + turnPriority);
    }

    /**
     * method that asks the player to move a student during the ACTION1 state
     * @param serializedBoardAbstract serialized board notified by the model
     */
    private void askMoveStudents(SerializedBoardAbstract serializedBoardAbstract){
        Set<String> possibleColours = new HashSet<String>();
        possibleColours.add("pink"); possibleColours.add("red"); possibleColours.add("yellow"); possibleColours.add("blue"); possibleColours.add("green");
        String command;
        String colour;
        String action;

        AnsiConsole.systemInstall();
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
                action = askCharacterCard((SerializedBoardAdvanced) serializedBoardAbstract);
            }
        }while(action.equalsIgnoreCase("Back"));
        AnsiConsole.systemUninstall();
    }

    /**
     * method that checks that the input given by the user is admissible
     * @param serializedBoardAbstract serialized board notified by the model
     * @param command input given by the user
     * @return true if the command is admissible, false otherwise
     */
    private boolean checkStudentInput(SerializedBoardAbstract serializedBoardAbstract, String command) {
        if(serializedBoardAbstract.getType().equals("advanced")) {
            return !command.equalsIgnoreCase("DiningRoom") && !command.equalsIgnoreCase("Archipelago")
                    && !command.equalsIgnoreCase("Card");
        } else {
            return !command.equalsIgnoreCase("DiningRoom") && !command.equalsIgnoreCase("Archipelago");
        }
    }

    /**
     * method that asks the player which character card he wants to use, or if he doesn't want to use any of them anymore
     * @param serializedBoardAdvanced serialized board notified by the model
     * @return the name of the chosen card, "ok" is none of the cards is chosen
     */
    private String askCharacterCard(SerializedBoardAdvanced serializedBoardAdvanced) {
        String card;

        AnsiConsole.systemInstall();
        do {
            System.out.println("> Choose a card from the extracted ones or go back [Back]:");
            System.out.print("> ");
            System.out.flush();
            card = input.nextLine();
        } while(!checkCharacterChoice(serializedBoardAdvanced, card) && !card.equalsIgnoreCase("back"));
        AnsiConsole.systemUninstall();

        if(!card.equalsIgnoreCase("back")) {
            manageCard(serializedBoardAdvanced, card);
            return "ok";
        }
        else{
            return card;
        }
    }

    /**
     * method that checks if the chosen card in input is one of the extracted one (are ok also a great number of shortcuts, at least
     * the most intuitive one)
     * @param serializedBoardAdvanced serialized board notified by the model
     * @param card user choice of card given in input
     * @return true if the input is admissible, false otherwise
     */
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

    /**
     * method that manages the usage of the chosen card
     * @param serializedBoardAdvanced serialized board notified by the model
     * @param card user choice of card given in input
     */
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

    /**
     * method that asks information for the exchangeThreeStudents card
     * @param ex3s exchangeThreeStudents card
     * @param school list of students in the hall
     */
    private void askExchangeThreeStudents(ExchangeThreeStudents ex3s, School school) {
        List<String> cardStudents = new ArrayList<>();
        List<String> hallStudents = new ArrayList<>();

        AnsiConsole.systemInstall();
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
        AnsiConsole.systemUninstall();

        //The space after the command is missing because it is already present in cardStudents
        this.client.asyncWriteToSocket("exchangeThreeStudents " + cardStudents.get(0) + " " +
                cardStudents.get(1) + " " + cardStudents.get(2) + " " +
                hallStudents.get(0) + " " + hallStudents.get(1) + " " + hallStudents.get(2));
    }

    /**
     * method that asks information for the exchangeTwoHallDining card
     * @param school school in which the exchange takes place
     */
    private void askExchangeTwoHallDining(School school) {
        List<String> diningStudents = new ArrayList<>();
        List<String> hallStudents = new ArrayList<>();

        AnsiConsole.systemInstall();
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
        AnsiConsole.systemUninstall();

        System.out.println("exchangeTwoHallDining " +
                hallStudents.get(0) + " " + hallStudents.get(1) + " " +
                diningStudents.get(0) + " " + diningStudents.get(1));
        //The space after the command is missing because it is already present in cardStudents
        this.client.asyncWriteToSocket("exchangeTwoHallDining " +
                hallStudents.get(0) + " " + hallStudents.get(1) + " " +
                diningStudents.get(0) + " " + diningStudents.get(1));
    }

    /**
     * method that asks information for the excludeColourFromCounting card
     */
    private void askExcludeColourFromCounting() {
        Set<String> possibleColours = new HashSet<>();
        possibleColours.add("pink"); possibleColours.add("red"); possibleColours.add("yellow"); possibleColours.add("blue"); possibleColours.add("green");
        String colour;

        AnsiConsole.systemInstall();
        do {
            System.out.println("> Select a colour to exclude from the next conquer attempt: ");
            System.out.print("> ");
            System.out.flush();
            colour = input.nextLine();
        } while(!possibleColours.contains(colour.toLowerCase()));
        AnsiConsole.systemUninstall();

        this.client.asyncWriteToSocket("excludeColourFromCounting " +  colour);
    }

    /**
     * method that asks information for the extraStudentInDining card
     * @param exst extraStudentInDining card
     * @param school school in which the exchange takes place
     */
    private void askExtraStudentInDining(ExtraStudentInDining exst, School school) {
        Set<String> possibleColours = new HashSet<>();
        possibleColours.add("pink"); possibleColours.add("red"); possibleColours.add("yellow"); possibleColours.add("blue"); possibleColours.add("green");
        String cardStudent = null;

        AnsiConsole.systemInstall();
        System.out.println("Students on the card: " + exst.printStudents());

        do {
            System.out.println("> Select one student from the card: ");
            System.out.print("> ");
            System.out.flush();
            cardStudent = input.nextLine();
        }while(!possibleColours.contains(cardStudent.toLowerCase()));
        AnsiConsole.systemUninstall();

        //The space after the command is missing because it is already present in cardStudents
        this.client.asyncWriteToSocket("extraStudentInDining " + cardStudent);
    }

    /**
     * method that asks information for the fakeMNMovement card
     * @param serializedBoardAdvanced serialized board notified by the model
     */
    private void askFakeMNMovement(SerializedBoardAdvanced serializedBoardAdvanced) {
        String response;
        int move = 0;

        AnsiConsole.systemInstall();
        do{
            System.out.println("> Insert the archipelago index on which you want to move to (and come back after): ");
            System.out.print("> ");
            System.out.flush();
            response = input.nextLine();

            if(checkInt(response)) {
                move = Integer.parseInt(response);
            }
        } while((move < 0 || move > serializedBoardAdvanced.getArchipelagos().size()) && !checkInt(response) );
        AnsiConsole.systemUninstall();

        this.client.asyncWriteToSocket("fakeMNMovement " + move);
    }

    /**
     * method that asks information for the forbidIsland card
     * @param serializedBoardAdvanced serialized board notified by the model
     */
    private void askForbidIsland(SerializedBoardAdvanced serializedBoardAdvanced) {
        String response;
        int index = 0;

        AnsiConsole.systemInstall();
        do{
            System.out.println("> Insert the archipelago index on which you want put a forbid tile: ");
            System.out.print("> ");
            System.out.flush();
            response = input.nextLine();

            if(checkInt(response)) {
                index = Integer.parseInt(response);
            }
        } while((index < 0 || index > serializedBoardAdvanced.getArchipelagos().size()) && !checkInt(response));
        AnsiConsole.systemUninstall();

        this.client.asyncWriteToSocket("forbidIsland " + index);
    }

    /**
     * method that asks information for the forbidIsland card
     * @param serializedBoardAdvanced serialized board notified by model
     * @param place placeOneStudent card
     */
    private void askPlaceOneStudent(SerializedBoardAdvanced serializedBoardAdvanced, PlaceOneStudent place) {
        Set<String> possibleColours = new HashSet<String>();
        possibleColours.add("pink"); possibleColours.add("red"); possibleColours.add("yellow"); possibleColours.add("blue"); possibleColours.add("green");
        String cardStudent;
        String response;
        int move = 0;

        AnsiConsole.systemInstall();
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
        AnsiConsole.systemUninstall();

        this.client.asyncWriteToSocket("placeOneStudent " + cardStudent + " " + move);
    }

    /**
     * method that asks information for the reduceColourInDining card
     */
    private void askReduceColourInDining(){
        Set<String> possibleColours = new HashSet<String>();
        possibleColours.add("pink"); possibleColours.add("red"); possibleColours.add("yellow"); possibleColours.add("blue"); possibleColours.add("green");
        String colour = null;

        AnsiConsole.systemInstall();
        do {
            System.out.println("> Select a colour of students that you want to remove from dining rooms: ");
            System.out.print("> ");
            System.out.flush();
            colour = input.nextLine();
        }while(!possibleColours.contains(colour.toLowerCase()));
        AnsiConsole.systemUninstall();

        this.client.asyncWriteToSocket("reduceColourInDining " + colour);

    }

    /**
     * method that asks information for the mother nature movement
     * @param serializedBoardAbstract serialized board notified by model
     */
    private void askMoveMotherNature(SerializedBoardAbstract serializedBoardAbstract){
        String response;
        int moves = 0;
        String action = null;

        AnsiConsole.systemInstall();
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
        AnsiConsole.systemUninstall();
    }

    /**
     * method that asks information about the cloud choice
     * @param serializedBoardAbstract serialized board notified by model
     */
    private void askCloudChoice(SerializedBoardAbstract serializedBoardAbstract) {
        String response;
        int cloudIndex = 0;
        String action = null;

        AnsiConsole.systemInstall();
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
        }while (action.equalsIgnoreCase("back"));
        AnsiConsole.systemUninstall();
    }

    /**
     * method that prints all the schools' information
     * @param serializedBoardAbstract serialized board notified by model
     */
    private void printSchool(SerializedBoardAbstract serializedBoardAbstract) {
        AnsiConsole.systemInstall();
        System.out.println("Schools: ");
        for (int i = 0; i < serializedBoardAbstract.getSchools().size(); i++) {
            colourSchool(serializedBoardAbstract, i);

            System.out.println(serializedBoardAbstract.getSchools().get(i).toString());
            printCards(serializedBoardAbstract, i);

            removeColour();
        }
        AnsiConsole.systemUninstall();
        System.out.flush();
    }

    /**
     * method that prints all the available assistant cards for the clients school, and just the last used assistant cards for all
     * the opponents
     * @param serializedBoardAbstract serialized board notified by model
     * @param i index of the client
     */
    private void printCards(SerializedBoardAbstract serializedBoardAbstract, int i) {
        AnsiConsole.systemInstall();
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
        AnsiConsole.systemUninstall();
    }

    /**
     * method that prints the extracted character cards
     * @param serializedBoardAdvanced serialized board notified by model
     */
    private void printExtractedCC(SerializedBoardAdvanced serializedBoardAdvanced) {
        int size = serializedBoardAdvanced.getExtractedCards().size();

        AnsiConsole.systemInstall();
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
        AnsiConsole.systemUninstall();
    }

    /**
     * method that prints the winner
     * @param serializedBoardAbstract serialized board that is notified by the model
     */
    public void showWinner(SerializedBoardAbstract serializedBoardAbstract) {
        AnsiConsole.systemInstall();
        if(serializedBoardAbstract.getNicknameWinner().equals(this.playerNick)) {
            colourWinner();
        } else {
            colourLooser(serializedBoardAbstract);
        }
        AnsiConsole.systemUninstall();
    }

    /**
     * method that checks if a string contains an int
     * @param response string to check
     * @return true if is an int, false otherwise
     */
    private boolean checkInt(String response) {
        try {
            int conversion = Integer.parseInt(response);
        }catch(NumberFormatException nfe) {
            return false;
        }

        return true;
    }

    /**
     * method that prints in green if you are the winner
     */
    private void colourWinner() {
        System.out.println(ANSI_GREEN);
        System.out.println("\n----------------------------------------");
        System.out.println("YOU ARE THE WINNER!");
        System.out.println("----------------------------------------\n");
        System.out.println(ANSI_RESET);
        System.out.flush();
    }

    /**
     *  method that prints in red if you are the looser
     * @param se serialized board notified by the model
     */
    private void colourLooser(SerializedBoardAbstract se) {
        System.out.println(ANSI_RED);
        System.out.println("\n----------------------------------------");
        System.out.println("YOU LOST! THE WINNER IS " + se.getNicknameWinner());
        System.out.println("----------------------------------------\n");
        System.out.println(ANSI_RESET);
        System.out.flush();
    }

    /**
     * method that prints in green the archipelago information if mother nature is placed on it, in yellow if it has been conquered
     * @param serializedBoardAbstract serialized board notified by the model
     * @param i index of the archipelago
     * @param archipelagos list of archipelagos
     * @param serializedBoardAdvanced serialized board advanced notified by the model
     */
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

    /**
     * method that prints blue the school if it is owned by this client
     * @param serializedBoardAbstract
     * @param i
     */
    private void colourSchool(SerializedBoardAbstract serializedBoardAbstract, int i) {
        if(this.playerNick.equals(serializedBoardAbstract.getSchools().get(i).getPlayer().getNickname())) {
            System.out.print(ANSI_CYAN);
        }
        System.out.flush();
    }

    /**
     * method that resets the printing colour
     */
    private void removeColour() {
        System.out.print(ANSI_RESET);
        System.out.flush();
    }
}
