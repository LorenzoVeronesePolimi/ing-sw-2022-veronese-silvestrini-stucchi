package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Enumerations.ControllerErrorType;
import it.polimi.ingsw.Controller.Enumerations.State;
import it.polimi.ingsw.Controller.Exceptions.ControllerException;
import it.polimi.ingsw.Controller.Exceptions.NoPlayerException;
import it.polimi.ingsw.Messages.Enumerations.INMessageType;
import it.polimi.ingsw.Messages.INMessages.*;
import it.polimi.ingsw.Model.Board.*;
import it.polimi.ingsw.Model.Cards.*;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Observer.ObserverController;
import it.polimi.ingsw.Server.Server;
import it.polimi.ingsw.View.Exceptions.NoCharacterCardException;
import it.polimi.ingsw.View.ServerView;

import java.util.*;
import java.util.stream.Collectors;


// This is the main Controller: it coordinates all the others
public class Controller implements ObserverController<Message> {
    private final Server server;
    private int numPlayers;
    private boolean advanced = false;
    private BoardAbstract board;
    private BoardAdvanced boardAdvanced; //null if advanced=0
    private List<Player> sitPlayers; //contains initial order
    private List<Player> players; //ordered
    private Player precomputedPlayer;
    private int iteratorAC = 0; //this takes into account the number of AC played
    private State precomputedState = State.PLANNING1;
    private List<Integer> usedCards;
    private boolean gameEnded = false; //true when the game is going to end at the end of the round
    private String nicknameWinner = null; //nickname of the winner

    private int currentPlayerIndex = 0;
    private final List<ServerView> serverViews;

    private final ControllerInput controllerInput;
    private final ControllerState controllerState;
    private final ControllerIntegrity controllerIntegrity;

    private int numStudentsToMove;
    private int numStudentsToMoveCurrent;
    private final int NUM_STUDENTS_TO_MOVE_TWO_PLAYERS = 3;
    private final int NUM_STUDENTS_TO_MOVE_THREE_PLAYERS = 4;

    boolean just_started = true; //true until the very first PLANNING1 has still not been reached

    private boolean characterCardUsed = false; //true when a CC has been used

    public Controller(Server server){
        this.server = server;
        this.players = new ArrayList<>();
        this.sitPlayers = new ArrayList<>();
        this.usedCards = new ArrayList<>();
        this.controllerInput = new ControllerInput();
        this.controllerState = new ControllerState();
        this.controllerIntegrity = new ControllerIntegrity();
        this.serverViews = new ArrayList<>();
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public Player getPrecomputedPlayer() {
        return precomputedPlayer;
    }

    public State getPrecomputedState() {
        return precomputedState;
    }

    public Player getCurrentPlayer(){
        return this.players.get(this.currentPlayerIndex);
    }

    public Player getCurrentSitPlayer(){
        return this.sitPlayers.get(this.currentPlayerIndex);
    }

    public int getCurrentPlayerIndex(){
        return this.currentPlayerIndex;
    }

    public List<Player> getPlayers(){
        return new ArrayList<>(this.players);
    }

    public int getNumStudentsToMoveCurrent(){
        return this.numStudentsToMoveCurrent;
    }

    public BoardAbstract getBoard(){return this.board;}

    public BoardAdvanced getBoardAdvanced(){return this.boardAdvanced;}

    public boolean isAdvanced(){
        return this.advanced;
    }

    public ControllerState getControllerState(){
        return this.controllerState;
    }

    public ControllerIntegrity getControllerIntegrity(){
        return this.controllerIntegrity;
    }

    public ControllerInput getControllerInput(){
        return this.controllerInput;
    }

    public boolean isCharacterCardUsed(){
        return this.characterCardUsed;
    }

    public void setCharacterCardUsed(boolean newValue){
        this.characterCardUsed = newValue;
    }

    /* To be used if we want the client to write only "characterCard" when he uses one CC
    public List<AbstractCharacterCard> getExtractedCharacterCards() throws NoCharacterCardException{
        if(this.boardAdvanced == null){
            throw new NoCharacterCardException();
        }

        return this.boardAdvanced.getExtractedCards();
    }*/

    /*I RECEIVED A MESSAGE => I need to:
     * Know its format: is it a STUDENT_TO_ARCHIPELAGO or something else?
     *   if not a valid format: resend
     * Now I know what to do
     * Is it the right time to receive this message? Is it the right part of the game/turn?
     *   if no: resend
     * Does this message respect the rules (ex. I can't move MotherNature of 6 plates)?
     *   if no: resend
     * Call the Model and apply the move requested
     * */
    /**
     * Controller is observer of ServerView, so it's awaken by its notify
     * @param message is INMessage coming from the ServerView. It contains the user action
     * @throws ControllerException if the action made is not possible.
     */
    public void update(Message message) throws ControllerException {
        if(!controllerInput.checkFormat(message)){
            System.out.println("Invalid format");
            throw new ControllerException(ControllerErrorType.FORMAT_ERROR);
        }

        if(!controllerState.checkState(message.getType())){
            System.out.println("You can't do that now");
            throw new ControllerException(ControllerErrorType.STATE_ERROR);
        }

        if(!message.manageMessage(this)){
            System.out.println("Integrity error");
            throw new ControllerException(ControllerErrorType.INTEGRITY_ERROR);
        }

        //check if I have to make some automatic action (=>PIANIFICATION1)
        if(controllerState.getState() == State.PLANNING1){
            if(this.just_started){
                controllerState.setState(State.PLANNING2);
                this.precomputedState = State.PLANNING2;
                this.just_started = false;

                if(this.isAdvanced())
                    this.boardAdvanced.notifyPlayers();
                else
                    this.board.notifyPlayers();
                System.out.println("notify");
            }
            else{
                controllerState.setState(State.PLANNING2);
                this.precomputedState = State.PLANNING2;
                try {
                    if(isAdvanced())
                        this.boardAdvanced.moveStudentBagToCloud();
                    else
                        this.board.moveStudentBagToCloud();
                } catch (ExceededMaxStudentsCloudException e) {
                    return; //case of first turn, in which clouds are filled immediately
                } catch (StudentNotFoundException e){ //CASE 2.1 of end of the game
                    gameEndedBag(this.board.getNumStudentsInBag());
                }
            }
        }
    }

    //associate the String to its SPColour. Note that I'm sure this association exists, since I made a control
    // in ControllerInput (checkStudentColour())
    /**
     * Convert String -> SPColour
     * @param s is the string to be mapped.
     * @return mapped SPColour or null of s="-"
     */
    private SPColour mapStringToSPColour(String s){
        switch(s.toLowerCase()){
            case "red":
                return SPColour.RED;
            case "pink":
                return SPColour.PINK;
            case "blue":
                return SPColour.BLUE;
            case "yellow":
                return SPColour.YELLOW;
            case "green":
                return SPColour.GREEN;
        }
        return null; //possible only in case of "-"
    }

    /**
     * Convert List<String> -> List<SPColour>
     * @param colours are the Strings to convert
     * @return mapped strings. It eliminates all "-" strings, so the returned List might be smaller
     *      than the input one
     */
    private List<SPColour> mapListStringToColour(List<String> colours){
        List<SPColour> converted = new ArrayList<>();

        for(String s : colours){
            if(!s.equals("-")){
                converted.add(this.mapStringToSPColour(s));
            }
        }

        return converted;
    }

    /**
     * Convert String -> PlayerColour
     * @param s is the string to be mapped
     * @return mapped PlayerColour
     */
    private PlayerColour mapStringToPlayerColour(String s){
        switch(s.toLowerCase()){
            case "white":
                return PlayerColour.WHITE;
            case "black":
                return PlayerColour.BLACK;
            case "gray":
                return PlayerColour.GRAY;
        }
        return null; //impossible
    }

    /**
     * Convert String -> Player
     * @param s is the string to be mapped
     * @return mapped Player
     */
    private Player mapStringToPlayer(String s) throws NoPlayerException{
        for(Player p : this.players){
            if(p.getNickname().equals(s)){
                return p;
            }
        }

        throw new NoPlayerException();
    }

    /**
     * Check if nickname is the current Player
     * @param nickname is the string to be checked
     * @return true if it's the current player, false otherwise
     */
    private boolean isCurrentPlayer(String nickname){
        try{
            Player player = this.mapStringToPlayer(nickname);
            if(getCurrentPlayer() == player){
                return true;
            }
        } catch(NoPlayerException ex){
            return false;
        }
        return false;
    }

    private boolean isCurrentSitPlayer(String nickname) {
        try{
            Player player = this.mapStringToPlayer(nickname);
            if(getCurrentSitPlayer() == player){
                return true;
            }
        } catch(NoPlayerException ex){
            return false;
        }
        return false;
    }

    /**
     * Initialize the Board and BoardAdvances (eventually) and set observers
     */
    private void initMatch(){
        this.sitPlayers.addAll(this.players);
        this.precomputedPlayer = this.sitPlayers.get(0);
        
        if(numPlayers==4){
            if(!players.get(0).getColour().equals(players.get(1).getColour())){
                Player park;
                if(!players.get(0).getColour().equals(players.get(2).getColour())){
                    park = players.remove(3);
                    players.add(players.size()-1,players.remove(1));
                }
                else{
                    park = players.remove(2);
                    players.add(players.size()-2,players.remove(1));
                }
                players.add(1, park);
            }
            /*
            this.sitPlayers.addAll(this.players);
            this.precomputedPlayer = this.sitPlayers.get(0);
             */
        }

        BoardFactory factory = new BoardFactory(this.players);
        this.board = factory.createBoard();

        if(this.advanced){
            //TODO: surrounded with try catch just to remove errors. It needs to be checked before leaving it like that
            try {
                this.boardAdvanced = new BoardAdvanced(this.board);
            } catch (ExceededMaxStudentsHallException | StudentNotFoundException | TowerNotFoundException | EmptyCaveauException e) {
                e.printStackTrace();
            }
        }

        controllerIntegrity.setBoard(this.board);
        controllerIntegrity.setBoardAdvanced(this.boardAdvanced);
        controllerIntegrity.setAdvanced(this.advanced);

        //set number of Students to move at each ACTION1
        if(this.players.size() == 3){
            this.numStudentsToMove = NUM_STUDENTS_TO_MOVE_THREE_PLAYERS;
            this.numStudentsToMoveCurrent = NUM_STUDENTS_TO_MOVE_THREE_PLAYERS;
        } else{
            this.numStudentsToMove = NUM_STUDENTS_TO_MOVE_TWO_PLAYERS;
            this.numStudentsToMoveCurrent = NUM_STUDENTS_TO_MOVE_TWO_PLAYERS;
        }

        System.out.println("init match");
        this.addBoardObserver();
        System.out.println("add obs");
    }

    private List<Player> precomputeTurnOrder(List<Player> list, int turnPriority){
        Map<Player, Integer> values = new HashMap<>();

        for(int i = 0; i < list.size() - 1; i++){
            values.put(list.get(i), list.get(i).getLastCard().getTurnPriority());
        }
        values.put(list.get(list.size() - 1), turnPriority);

        List<Player> orderedPlayerList = new ArrayList<>();
        while (values.size() > 0) {
            Map.Entry<Player, Integer> min = null;
            for (Map.Entry<Player, Integer> e : values.entrySet()) {
                if (min == null || min.getValue() > e.getValue()) {
                    min = e;
                }
            }
            Player minPlayer = min.getKey();
            orderedPlayerList.add(minPlayer);
            values.remove(minPlayer);
        }

        return orderedPlayerList;
    }

    private int computeNextACIndex(){
        if(currentPlayerIndex < this.numPlayers - 1) {
            return this.currentPlayerIndex + 1;
        }
        return 0;
    }

    //this creates a list of Players who played their AC according to sitPlayers and iteratorAC
    //Has to be used AFTER incrementing the iteratorAC (after you have precomputed the next player)
    private List<Player> whoPlayedAC(){
        List<Player> result = new ArrayList<>();
        for(int i = this.currentPlayerIndex - this.iteratorAC; i <= this.currentPlayerIndex; i++){
            result.add(this.sitPlayers.get(i));
        }

        return result;
    }

    /**
     * Change order of Players in players according to the AssistantCards they played
     */
    private void changeTurnOrder(){
        Map<Player, Integer> values = new HashMap<>();

        for(Player p : this.players){
            values.put(p, p.getLastCard().getTurnPriority());
        }

        List<Player> orderedPlayerList = new ArrayList<>();
        while (values.size() > 0) {
            Map.Entry<Player, Integer> min = null;
            for (Map.Entry<Player, Integer> e : values.entrySet()) {
                if (min == null || min.getValue() > e.getValue()) {
                    min = e;
                }
            }
            Player minPlayer = min.getKey();
            orderedPlayerList.add(minPlayer);
            values.remove(minPlayer);
        }

        this.players = orderedPlayerList;
    }

    /**
     * @param message incomingMessage
     * @return true if the actions has been done, false other-ways
     */
    public boolean manageCreateMatch(MessageCreateMatch message){
        this.numPlayers = message.getNumPlayers();
        this.advanced = message.isAdvanced();
        ServerView serverView = message.getServerView();
        PlayerColour colourFirstPlayer = mapStringToPlayerColour(message.getColourFirstPlayer());
        // no need to control the boolean "advanced"

        if(!controllerIntegrity.checkCreateMatch(numPlayers)){
            return false;
        }

        Player player = new Player(message.getNickname(), colourFirstPlayer);
        this.players.add(player);
        serverView.setPlayerNickname(message.getNickname());
        this.serverViews.add(serverView);

        controllerState.setState(State.WAITING_PLAYERS);
        this.precomputedState = State.WAITING_PLAYERS;

        server.askPlayerInfo(new ArrayList<>(Collections.singletonList(colourFirstPlayer)), numPlayers);

        return true;
    }

    /**
     * @param message incomingMessage
     * @return true if the actions has been done, false other-ways
     */
    public boolean manageAddPlayer(MessageAddPlayer message){
        String nickname = message.getNickname();
        PlayerColour colour = mapStringToPlayerColour(message.getColour());
        ServerView serverView = message.getServerView();

        // He can't have the name of an existing Player
        for(Player p : this.players){
            if(p.getNickname().equals(nickname)){return false;}
        }

        // Check colour not already chosen (or chosen only one time for GameFour)
        if(this.numPlayers == 4){
            long numSameColour = 0;
            // check for how many player already have the chosen colour
            numSameColour = this.players.stream().filter(x -> x.getColour().equals(colour)).count();

            // if no one has the chosen colour, check if there are not already two players with different colour
            // this can happen if player1->black
            //                    player2->white
            //                    player3->gray --> ERROR
            if(numSameColour == 0) {
                if(this.players.stream().anyMatch(x -> x.getColour().equals(PlayerColour.WHITE))) {
                    if(this.players.stream().anyMatch(x -> x.getColour().equals(PlayerColour.BLACK)) ||
                            this.players.stream().anyMatch(x -> x.getColour().equals(PlayerColour.GRAY))) {
                        return false;
                    }
                } else if(this.players.stream().anyMatch(x -> x.getColour().equals(PlayerColour.BLACK))) {
                    if(this.players.stream().anyMatch(x -> x.getColour().equals(PlayerColour.GRAY))) {
                        return false;
                    }
                }
            }

            // check is the chosen colour has already been chosen by two players
            if(numSameColour >= 2){
                return false;
            }
        }
        else{
            if(this.players.stream().anyMatch(x -> x.getColour().equals(colour)))
                return false;
        }

        // No integrity to check
        Player player = new Player(nickname, colour);
        this.players.add(player);
        serverView.setPlayerNickname(message.getNickname());
        this.serverViews.add(serverView);

        if(this.players.size() == numPlayers){ // The requested number of players has been reached: let's go on
            this.initMatch();
            this.precomputedState = State.PLANNING1;
            controllerState.setState(State.PLANNING1);
        }
        else if(this.players.size() < numPlayers) {
            List<PlayerColour> playerColourList = players.stream().map(Player::getColour).collect(Collectors.toList());
            server.askPlayerInfo(playerColourList, numPlayers);
        }
        return true;
    }

    /**
     * @param message incomingMessage
     * @return true if the actions has been done, false other-ways
     */
    public boolean manageAssistantCard(MessageAssistantCard message){
        String nicknamePlayer = message.getNickname();
        int turnPriority = message.getTurnPriority();

        // Is him the currentPlayer? Can he use that AssistantCard?
        if(!isCurrentSitPlayer(nicknamePlayer)){
            System.out.println("here");
            return false;
        }

        if(!controllerIntegrity.checkAssistantCard(this.usedCards, getCurrentSitPlayer(), turnPriority)) {
            return false;
        }

        //precompute
        this.precomputeNextPlayer(turnPriority);

        // Remove the card from his hand
        try{
            if(isAdvanced()){
                boardAdvanced.useAssistantCard(this.usedCards, getCurrentSitPlayer(), turnPriority);
            }
            else{
                board.useAssistantCard(this.usedCards, getCurrentSitPlayer(), turnPriority);
            }
        } catch(AssistantCardAlreadyPlayedTurnException | NoAssistantCardException ex){
            this.iteratorAC --;
            System.out.println("catch");
            return false;
        } // card already used or no AssistantCard present

        this.gameEndedAssistantCards(this.getCurrentSitPlayer().getHandLength());

        this.usedCards.add(getCurrentSitPlayer().getLastCard().getTurnPriority());

        // Go on within the turn
        this.currentPlayerIndex = this.sitPlayers.indexOf(this.precomputedPlayer);

        if(this.iteratorAC == this.numPlayers){ //last player: all players has played their AssistantCard. No I can set the order
            this.changeTurnOrder(); // reset the order of the Players according to the values of the AssistantCards
            this.currentPlayerIndex = 0; // the new turn will start
            controllerState.setState(State.ACTION1);
            iteratorAC = 0;
            this.usedCards.clear();
        }
        return true;
    }

    private void precomputeNextPlayer(int turnPriority) {
        if(this.iteratorAC == 0){
            this.currentPlayerIndex = this.sitPlayers.indexOf(this.precomputedPlayer); //precomputedPlayer was set in the last Cloud
        }

        if(this.iteratorAC < this.numPlayers - 1){
            this.precomputedPlayer = this.sitPlayers.get(this.computeNextACIndex());
        }
        else{
            List<Player> precomputedNextPlayersList = precomputeTurnOrder(this.players, turnPriority);
            this.precomputedPlayer = precomputedNextPlayersList.get(0);
            this.precomputedState = State.ACTION1;
        }
        this.iteratorAC++;
    }

    /**
     * @param message incomingMessage
     * @return true if the actions has been done, false other-ways
     */
    public boolean manageStudentHallToDiningRoom(MessageStudentHallToDiningRoom message){
        String nicknamePlayer = message.getNickname();
        SPColour studentColour = mapStringToSPColour(message.getColour());

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(this.numStudentsToMoveCurrent == 1 || // all possible Students are going to be moved
                this.board.getPlayerSchool(getCurrentPlayer()).getStudentsHall().size() == 1){
            this.precomputedState = State.ACTION2;
        }

        if(controllerIntegrity.checkStudentHallToDiningRoom(getCurrentPlayer(), studentColour)){
            if(this.advanced){
                try {
                    boardAdvanced.moveStudentHallToDiningRoom(getCurrentPlayer(), studentColour);
                } catch (StudentNotFoundException | ExceededMaxStudentsDiningRoomException | EmptyCaveauException |
                        ProfessorNotFoundException | NoProfessorBagException e) {
                    return false;
                }
            } else{
                try {
                    board.moveStudentHallToDiningRoom(getCurrentPlayer(), studentColour);
                } catch (StudentNotFoundException | ExceededMaxStudentsDiningRoomException |
                        ProfessorNotFoundException | NoProfessorBagException e) {
                    return false;
                }
            }
            this.numStudentsToMoveCurrent--;
            if(this.numStudentsToMoveCurrent == 0 || // all possible Students moved
                    this.board.getPlayerSchool(getCurrentPlayer()).getStudentsHall().size() == 0){ // no Students remained
                this.numStudentsToMoveCurrent = this.numStudentsToMove;
                controllerState.setState(State.ACTION2);
            }

            return true;
        }
        return false;
    }

    /**
     * @param message incomingMessage
     * @return true if the actions has been done, false other-ways
     */
    public boolean manageStudentToArchipelago(MessageStudentToArchipelago message){
        String nicknamePlayer = message.getNickname();
        SPColour studentColour = mapStringToSPColour(message.getColour());
        int destinationArchipelagoIndex = message.getDestArchipelagoIndex();

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(this.numStudentsToMoveCurrent == 1 || // all possible Students are going to be moved
                this.board.getPlayerSchool(getCurrentPlayer()).getStudentsHall().size() == 1){
            this.precomputedState = State.ACTION2;
        }

        if(controllerIntegrity.checkStudentToArchipelago(getCurrentPlayer(), studentColour, destinationArchipelagoIndex)){
            if(isAdvanced()){
                try {
                    boardAdvanced.moveStudentSchoolToArchipelagos(getCurrentPlayer(), studentColour, destinationArchipelagoIndex);
                } catch (StudentNotFoundException e) {
                    return false;
                }
            } else {
                try {
                    board.moveStudentSchoolToArchipelagos(getCurrentPlayer(), studentColour, destinationArchipelagoIndex);
                } catch (StudentNotFoundException e) {
                    return false;
                }
            }

            this.numStudentsToMoveCurrent--;
            if(this.numStudentsToMoveCurrent == 0 || // all possible Students moved
                    this.board.getPlayerSchool(getCurrentPlayer()).getStudentsHall().size() == 0){ // no Students remained
                this.numStudentsToMoveCurrent = this.numStudentsToMove;
                controllerState.setState(State.ACTION2);
            }
            return true;
        }
        return false;
    }

    /**
     * @param message incomingMessage
     * @return true if the actions has been done, false other-ways
     */
    public boolean manageMoveMotherNature(MessageMoveMotherNature message){
        String nicknamePlayer = message.getNickname();
        int moves = message.getMoves();

        this.precomputedState = State.ACTION3;
        try {
            this.gameEndedArchipelagos(moves);
        } catch (TowerNotFoundException | EmptyCaveauException | ExceededMaxStudentsHallException | StudentNotFoundException | InvalidTowerNumberException | AnotherTowerException | ExceededMaxTowersException e) {
            e.printStackTrace();
            this.precomputedState = State.ACTION2;
            return false;
        }

        if(!isCurrentPlayer(nicknamePlayer)){
            this.precomputedState = State.ACTION2;
            return false;
        }

        if(controllerIntegrity.checkMoveMotherNature(getCurrentPlayer(), moves)){
            if(isAdvanced()){
                boardAdvanced.moveMotherNature(moves);
                try {
                    boardAdvanced.tryToConquer(getCurrentPlayer());
                } catch (InvalidTowerNumberException | AnotherTowerException | ExceededMaxTowersException | TowerNotFoundException e) {
                    this.precomputedState = State.ACTION2;
                    return false;
                }
            }else {
                board.moveMotherNature(moves);
                try {
                    board.tryToConquer(getCurrentPlayer());
                } catch (InvalidTowerNumberException | AnotherTowerException | ExceededMaxTowersException |
                         TowerNotFoundException e) {
                    this.precomputedState = State.ACTION2;
                    return false;
                }
            }
            if(this.precomputedState != State.END){
                controllerState.setState(State.ACTION3);
            }

            return true;
        }
        return false;
    }

    /**
     * @param message incomingMessage
     * @return true if the actions has been done, false other-ways
     */
    public boolean manageStudentCloudToSchool(MessageStudentCloudToSchool message){
        String nicknamePlayer = message.getNickname();
        int indexCloud = message.getIndexCloud();

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(this.currentPlayerIndex == this.players.size() - 1){ //all Players made their move => new turn
            if(this.gameEnded){ //case game ends at the end of the turn
                this.precomputedState = State.END;
            }
            else{
                this.precomputedState = State.PLANNING1;
                this.precomputedPlayer = players.get(0);
            }
        }
        else{
            this.precomputedState = State.ACTION1;
            this.precomputedPlayer = this.players.get(currentPlayerIndex+1);
        }

        if(controllerIntegrity.checkStudentCloudToSchool(getCurrentPlayer(), indexCloud) || this.gameEnded){//TODO: I can choose a void cloud only if the game is going to finish, RIGHT?
            if(isAdvanced()) {
                try{
                    boardAdvanced.moveStudentCloudToSchool(getCurrentPlayer(), indexCloud);
                } catch(ExceededMaxStudentsHallException ex){
                    return false;
                }
            } else {
                try{
                    board.moveStudentCloudToSchool(getCurrentPlayer(), indexCloud);
                } catch(ExceededMaxStudentsHallException ex){
                    ex.printStackTrace();
                    return false;
                }
            }

            // change current Player
            this.currentPlayerIndex++;
            this.characterCardUsed = false;
            if(this.currentPlayerIndex >= this.players.size()){ //all Players made their move => new turn
                if(this.gameEnded){ //case game ends at the end of the turn
                    controllerState.setState(State.END);
                }
                else{
                    controllerState.setState(State.PLANNING1);
                    this.precomputedPlayer = players.get(0); //this will be used in the first iteration of manageAssistantCard
                    this.currentPlayerIndex = this.sitPlayers.indexOf(this.precomputedPlayer);
                }
            }
            else{
                controllerState.setState(State.ACTION1);
            }
            //TODO: remove some card effects (colourtoexclude, towernovalue...)

            return true;
        }
        return false;
    }

    //--------------------------------------------------CHARACTER CARDS
    /**
     * @param message incomingMessage
     * @return true if the actions has been done, false other-ways
     */
    public boolean manageCCExchangeThreeStudents(MessageCCExchangeThreeStudents message){
        int indexCard;
        try {
            indexCard = this.ccTypeToIndex(message.getType());
        } catch (NoCharacterCardException e) {
            return false;
        }
        String nicknamePlayer = message.getNickname();
        List<SPColour> coloursCard = this.mapListStringToColour(message.getColoursCard());
        List<SPColour> coloursHall = this.mapListStringToColour(message.getColoursHall());

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(isCharacterCardUsed()){return false;}

        try{
            if(controllerIntegrity.checkCCExchangeThreeStudents(getCurrentPlayer(), coloursCard, coloursHall,
                    (ExchangeThreeStudents) this.boardAdvanced.getExtractedCards().get(indexCard))) {

                this.boardAdvanced.useExchangeThreeStudents(getCurrentPlayer(), coloursHall, coloursCard, indexCard);

                this.characterCardUsed = true;

                return true;
            }

        } catch(WrongNumberOfStudentsTransferException | StudentNotFoundException | ExceededMaxStudentsHallException | CoinNotFoundException | EmptyCaveauException | ExceededMaxNumCoinException ex){
            return false;
        }

        return false;
    }

    /**
     * @param message incomingMessage
     * @return true if the actions has been done, false other-ways
     */
    public boolean manageCCExchangeTwoHallDining(MessageCCExchangeTwoHallDining message){
        int indexCard;
        try {
            indexCard = this.ccTypeToIndex(message.getType());
        } catch (NoCharacterCardException e) {
            return false;
        }
        String nicknamePlayer = message.getNickname();
        List<SPColour> coloursHall = this.mapListStringToColour(message.getColoursHall());
        List<SPColour> coloursDiningRoom = this.mapListStringToColour(message.getColoursDiningRoom());

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(isCharacterCardUsed()){return false;}

        try {
            if(controllerIntegrity.checkCCExchangeTwoHallDining(getCurrentPlayer(), coloursHall, coloursDiningRoom)){
                this.boardAdvanced.useExchangeTwoHallDining(getCurrentPlayer(), coloursHall, coloursDiningRoom, indexCard);

                this.characterCardUsed = true;

                return true;
            }
        } catch (WrongNumberOfStudentsTransferException | StudentNotFoundException | ExceededMaxStudentsHallException |
                 ExceededMaxStudentsDiningRoomException | EmptyCaveauException | ExceededMaxNumCoinException |
                 CoinNotFoundException | ProfessorNotFoundException | NoProfessorBagException e) {return false;}

        return false;
    }

    /**
     * @param message incomingMessage
     * @return true if the actions has been done, false other-ways
     */
    public boolean manageCCExcludeColourFromCounting(MessageCCExcludeColourFromCounting message){
        int indexCard;
        try {
            indexCard = this.ccTypeToIndex(message.getType());
        } catch (NoCharacterCardException e) {
            return false;
        }
        String nicknamePlayer = message.getNickname();
        SPColour colourToExclude = this.mapStringToSPColour(message.getColourToExclude());

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(isCharacterCardUsed()){return false;}

        if(controllerIntegrity.checkCCGeneric()){
            try {
                this.boardAdvanced.useExcludeColourFromCounting(getCurrentPlayer(), colourToExclude, indexCard);
            } catch (EmptyCaveauException |
                    ExceededMaxNumCoinException |
                    CoinNotFoundException |
                    InvalidTowerNumberException |
                    AnotherTowerException |
                    ExceededMaxTowersException |
                    TowerNotFoundException e) {
                e.printStackTrace();
            }

            this.characterCardUsed = true;

            return true;
        }

        return false;
    }

    /**
     * @param message incomingMessage
     * @return true if the actions has been done, false other-ways
     */
    public boolean manageCCExtraStudentInDining(MessageCCExtraStudentInDining message){
        int indexCard;
        try {
            indexCard = this.ccTypeToIndex(message.getType());
        } catch (NoCharacterCardException e) {
            return false;
        }
        String nicknamePlayer = message.getNickname();
        SPColour colourToMove = mapStringToSPColour(message.getColourToMove());

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(isCharacterCardUsed()){return false;}

        if(controllerIntegrity.checkCCGeneric()){
            try {
                this.boardAdvanced.useExtraStudentInDining(getCurrentPlayer(), colourToMove, indexCard);

                this.characterCardUsed = true;

                return true;
            } catch (ExceededMaxStudentsDiningRoomException |
                    StudentNotFoundException |
                    EmptyCaveauException |
                    ExceededMaxNumCoinException |
                    CoinNotFoundException e) {
                return false;
            }
        }

        return false;
    }

    /**
     * @param message incomingMessage
     * @return true if the actions has been done, false other-ways
     */
    public boolean manageCCFakeMNMovement(MessageCCFakeMNMovement message){
        int indexCard;
        try {
            indexCard = this.ccTypeToIndex(message.getType());
        } catch (NoCharacterCardException e) {
            return false;
        }
        String nicknamePlayer = message.getNickname();
        int fakeMNPosition = message.getFakeMNPosition();

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(isCharacterCardUsed()){return false;}

        if(controllerIntegrity.checkCCFakeMNMovement(fakeMNPosition)){
            try {
                this.boardAdvanced.useFakeMNMovement(getCurrentPlayer(), fakeMNPosition, indexCard);

                this.characterCardUsed = true;

                return true;
            } catch (TowerNotFoundException |
                    InvalidTowerNumberException |
                    AnotherTowerException |
                    ExceededMaxTowersException |
                    EmptyCaveauException |
                    ExceededMaxNumCoinException |
                    CoinNotFoundException e) {
                return false;
            }
        }

        return false;
    }

    /**
     * @param message incomingMessage
     * @return true if the actions has been done, false other-ways
     */
    public boolean manageCCForbidIsland(MessageCCForbidIsland message){
        int indexCard;
        try {
            indexCard = this.ccTypeToIndex(message.getType());
        } catch (NoCharacterCardException e) {
            return false;
        }
        String nicknamePlayer = message.getNickname();
        int archipelagoIndexToForbid = message.getArchipelagoIndexToForbid();

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(isCharacterCardUsed()){return false;}

        if(controllerIntegrity.checkCCForbidIsland(archipelagoIndexToForbid)){
            try {
                this.boardAdvanced.useForbidIsland(getCurrentPlayer(), archipelagoIndexToForbid, indexCard);

                this.characterCardUsed = true;

                return true;
            } catch (ExceededNumberForbidFlagException |
                    EmptyCaveauException |
                    ExceededMaxNumCoinException |
                    CoinNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * @param message incomingMessage
     * @return true if the actions has been done, false other-ways
     */
    public boolean manageCCPlaceOneStudent(MessageCCPlaceOneStudent message){
        int indexCard;
        try {
            indexCard = this.ccTypeToIndex(message.getType());
        } catch (NoCharacterCardException e) {
            return false;
        }
        String nicknamePlayer = message.getNickname();
        SPColour colourToMove = mapStringToSPColour(message.getColourToMove());
        int archipelagoIndexDestination = message.getArchipelagoIndexDest();

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(isCharacterCardUsed()){return false;}

        try {
            PlaceOneStudent chosenCard = (PlaceOneStudent)this.boardAdvanced.getExtractedCards().get(indexCard);
            if(controllerIntegrity.checkCCPlaceOneStudent(colourToMove, archipelagoIndexDestination, chosenCard)){
                this.boardAdvanced.usePlaceOneStudent(getCurrentPlayer(), colourToMove, archipelagoIndexDestination, indexCard);

                this.characterCardUsed = true;

                return true;
            }
        } catch (StudentNotFoundException |
                EmptyCaveauException |
                ExceededMaxNumCoinException |
                CoinNotFoundException e) {
            return false;
        }

        return false;
    }

    /**
     * @param message incomingMessage
     * @return true if the actions has been done, false other-ways
     */
    public boolean manageCCReduceColourInDining(MessageCCReduceColourInDining message){
        int indexCard;
        try {
            indexCard = this.ccTypeToIndex(message.getType());
        } catch (NoCharacterCardException e) {
            return false;
        }
        String nicknamePlayer = message.getNickname();
        SPColour colourToReduce = mapStringToSPColour(message.getColourToReduce());

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(isCharacterCardUsed()){return false;}

        try {
            if(controllerIntegrity.checkCCGeneric()) {
                this.boardAdvanced.useReduceColourInDining(getCurrentPlayer(), colourToReduce, indexCard);

                this.characterCardUsed = true;

                return true;
            }
        } catch (StudentNotFoundException |
                EmptyCaveauException |
                ExceededMaxNumCoinException |
                CoinNotFoundException e) {
            return false;
        }

        return false;
    }

    /**
     * @param message incomingMessage
     * @return true if the actions has been done, false other-ways
     */
    public boolean manageCCTowerNoValue(MessageCCTowerNoValue message){
        int indexCard;
        try {
            indexCard = this.ccTypeToIndex(message.getType());
        } catch (NoCharacterCardException e) {
            return false;
        }
        String nicknamePlayer = message.getNickname();

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(isCharacterCardUsed()){return false;}

        if(controllerIntegrity.checkCCGeneric()){
            try {
                this.boardAdvanced.useTowerNoValue(getCurrentPlayer(), indexCard);
            } catch (EmptyCaveauException |
                    ExceededMaxNumCoinException |
                    CoinNotFoundException e) {
                e.printStackTrace();
            }

            this.characterCardUsed = true;

            return true;
        }

        return false;
    }

    /**
     * @param message incomingMessage
     * @return true if the actions has been done, false other-ways
     */
    public boolean manageCCTwoExtraPoints(MessageCCTwoExtraPoints message){
        int indexCard;
        try {
            indexCard = this.ccTypeToIndex(message.getType());
        } catch (NoCharacterCardException e) {
            return false;
        }
        String nicknamePlayer = message.getNickname();

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(isCharacterCardUsed()){return false;}

        if(controllerIntegrity.checkCCGeneric()){
            try {
                this.boardAdvanced.useTwoExtraPoints(getCurrentPlayer(), indexCard);
            } catch (EmptyCaveauException |
                    ExceededMaxNumCoinException |
                    CoinNotFoundException e) {
                e.printStackTrace();
            }

            this.characterCardUsed = true;

            return true;
        }

        return false;
    }

    /**
     * @param message incomingMessage
     * @return true if the actions has been done, false other-ways
     */
    public boolean manageCCTakeProfessorOnEquity(MessageCCTakeProfessorOnEquity message){
        int indexCard;
        try {
            indexCard = this.ccTypeToIndex(message.getType());
        } catch (NoCharacterCardException e) {
            return false;
        }
        String nicknamePlayer = message.getNickname();

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(isCharacterCardUsed()){return false;}

        try {
            if(controllerIntegrity.checkCCGeneric()){
                this.boardAdvanced.useTakeProfessorOnEquity(getCurrentPlayer(), indexCard);

                this.characterCardUsed = true;

                return true;
            }
        } catch (TowerNotFoundException |
                InvalidTowerNumberException |
                AnotherTowerException |
                ProfessorNotFoundException |
                NoProfessorBagException |
                ExceededMaxTowersException |
                EmptyCaveauException |
                ExceededMaxNumCoinException |
                CoinNotFoundException e) {
            return false;
        }

        return false;
    }

    /**
     * @param message incomingMessage
     * @return true if the actions has been done, false other-ways
     */
    public boolean manageCCTwoExtraIslands(MessageCCTwoExtraIslands message){
        int indexCard;
        try {
            indexCard = this.ccTypeToIndex(message.getType());
        } catch (NoCharacterCardException e) {
            return false;
        }
        String nicknamePlayer = message.getNickname();

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(isCharacterCardUsed()){return false;}

        if(controllerIntegrity.checkCCGeneric()){
            try {
                this.boardAdvanced.useTwoExtraIslands(getCurrentPlayer(), indexCard);
            } catch (EmptyCaveauException |
                    ExceededMaxNumCoinException |
                    CoinNotFoundException e) {
                e.printStackTrace();
            }

            this.characterCardUsed = true;

            return true;
        }

        return false;
    }

    /**
     * Check if the type of CCCard chosen exists among extracted CCCards
     * @param type of the message received
     * @return int of the CCCard among the extracted CCCards
     * @throws NoCharacterCardException if there is no right CCCard
     */
    private int ccTypeToIndex(INMessageType type) throws NoCharacterCardException {
        List<AbstractCharacterCard> extractedCards = this.boardAdvanced.getExtractedCards();
        for(int i = 0; i < extractedCards.size(); i++){
            switch(extractedCards.get(i).getType()){
                case EXCHANGE_THREE_STUDENTS:
                    if(type == INMessageType.CC_EXCHANGE_THREE_STUDENTS){
                        return i;
                    }
                    break;
                case EXCHANGE_TWO_HALL_DINING:
                    if(type == INMessageType.CC_EXCHANGE_TWO_HALL_DINING){
                        return i;
                    }
                    break;
                case EXCLUDE_COLOUR_FROM_COUNTING:
                    if(type == INMessageType.CC_EXCLUDE_COLOUR_FROM_COUNTING){
                        return i;
                    }
                    break;
                case EXTRA_STUDENT_IN_DINING:
                    if(type == INMessageType.CC_EXTRA_STUDENT_IN_DINING){
                        return i;
                    }
                    break;
                case FAKE_MN_MOVEMENT:
                    if(type == INMessageType.CC_FAKE_MN_MOVEMENT){
                        return i;
                    }
                    break;
                case FORBID_ISLAND:
                    if(type == INMessageType.CC_FORBID_ISLAND){
                        return i;
                    }
                    break;
                case PLACE_ONE_STUDENT:
                    if(type == INMessageType.CC_PLACE_ONE_STUDENT){
                        return i;
                    }
                    break;
                case REDUCE_COLOUR_IN_DINING:
                    if(type == INMessageType.CC_REDUCE_COLOUR_IN_DINING){
                        return i;
                    }
                    break;
                case TAKE_PROFESSOR_ON_EQUITY:
                    if(type == INMessageType.CC_TAKE_PROFESSOR_ON_EQUITY){
                        return i;
                    }
                    break;
                case TOWER_NO_VALUE:
                    if(type == INMessageType.CC_TOWER_NO_VALUE){
                        return i;
                    }
                    break;
                case TWO_EXTRA_ISLANDS:
                    if(type == INMessageType.CC_TWO_EXTRA_ISLANDS){
                        return i;
                    }
                    break;
                case TWO_EXTRA_POINTS:
                    if(type == INMessageType.CC_TWO_EXTRA_POINTS){
                        return i;
                    }
                    break;
            }
        }

        throw new NoCharacterCardException();
    }


    /*
    private boolean isRightMapIndexToCharacterCard(INMessageType type, int indexCard){
        AbstractCharacterCard chosenCard = this.boardAdvanced.getExtractedCards().get(indexCard);

        // is this card corresponding to the index chosen?
        switch(type) {
            case CC_EXCHANGE_THREE_STUDENTS:
                return chosenCard.getType() == CharacterCardEnumeration.EXCHANGE_THREE_STUDENTS;
            case CC_EXCHANGE_TWO_HALL_DINING:
                return chosenCard.getType() == CharacterCardEnumeration.EXCHANGE_TWO_HALL_DINING;
            case CC_EXCLUDE_COLOUR_FROM_COUNTING:
                return chosenCard.getType().equals(CharacterCardEnumeration.EXCLUDE_COLOUR_FROM_COUNTING);
            case CC_EXTRA_STUDENT_IN_DINING:
                return chosenCard.getType().equals(CharacterCardEnumeration.EXTRA_STUDENT_IN_DINING);
            case CC_FAKE_MN_MOVEMENT:
                return chosenCard.getType().equals(CharacterCardEnumeration.FAKE_MN_MOVEMENT);
            case CC_FORBID_ISLAND:
                return chosenCard.getType().equals(CharacterCardEnumeration.FORBID_ISLAND);
            case CC_PLACE_ONE_STUDENT:
                return chosenCard.getType().equals(CharacterCardEnumeration.PLACE_ONE_STUDENT);
            case CC_REDUCE_COLOUR_IN_DINING:
                return chosenCard.getType().equals(CharacterCardEnumeration.REDUCE_COLOUR_IN_DINING);
            case CC_TAKE_PROFESSOR_ON_EQUITY:
                return chosenCard.getType().equals(CharacterCardEnumeration.TAKE_PROFESSOR_ON_EQUITY);
            case CC_TOWER_NO_VALUE:
                return chosenCard.getType().equals(CharacterCardEnumeration.TOWER_NO_VALUE);
            case CC_TWO_EXTRA_ISLANDS:
                return chosenCard.getType().equals(CharacterCardEnumeration.TWO_EXTRA_ISLANDS);
            case CC_TWO_EXTRA_POINTS:
                return chosenCard.getType().equals(CharacterCardEnumeration.TWO_EXTRA_POINTS);
        }
        return false;
    }*/

    private void gameEndedArchipelagos(int moves) throws TowerNotFoundException, EmptyCaveauException, ExceededMaxStudentsHallException, StudentNotFoundException, InvalidTowerNumberException, AnotherTowerException, ExceededMaxTowersException {
        BoardAbstract boardCopy;
        if(this.numPlayers == 2){
            boardCopy = new BoardTwo(this.board);
        }
        else if(this.numPlayers == 3){
            boardCopy = new BoardThree(this.board);
        }
        else{
            boardCopy = new BoardFour((BoardFour) this.board);
        }

        //make fake conquer
        if(isAdvanced()){
            BoardAdvanced boardAdvancedCopy = new BoardAdvanced(boardCopy, this.boardAdvanced);

            boardAdvancedCopy.moveMotherNature(moves);
            boardAdvancedCopy.tryToConquer(this.players.get(this.currentPlayerIndex));
        }
        else{
            boardCopy.moveMotherNature(moves);
            boardCopy.tryToConquer(this.players.get(this.currentPlayerIndex));
        }
        //CASE 1: 3 Archipelagos remained
        //this condition has to be checked every time a merge is made
        //game ends immediately
        if(boardCopy.getNumArchipelagos() <= 3){
            this.precomputedState = State.END; //game ends immediately
            controllerState.setState(State.END);

        }

        //CASE 0: a player has put all of his towers
        if(this.players.size() == 4){
            Player p1 = this.players.get(this.currentPlayerIndex);
            Player p2 = ((BoardFour)this.board).getTeammates().get(this.players.get(this.currentPlayerIndex));
            if(this.board.getPlayerSchool(p1).getNumTowers() == 0 && this.board.getPlayerSchool(p2).getNumTowers() == 0){
                this.precomputedState = State.END; //game ends immediately
                controllerState.setState(State.END);
            }
        }
        else{
            if(boardCopy.getPlayerSchool(this.players.get(this.currentPlayerIndex)).getNumTowers() == 0){
                this.precomputedState = State.END; //game ends immediately
                controllerState.setState(State.END);
            }
        }
    }

    private void gameEndedBag(int numStudentsInBag){
        //TODO: test
        //CASE 2.1: no more Students in the Bag
        //game ends at the end of round
        if(numStudentsInBag == 0){
            this.gameEnded = true;
        }
    }

    private void gameEndedAssistantCards(int handSize){
        //CASE 2.2: no more assistant cards
        //game ends at the end of the round (?)
        if(handSize == 0){
            this.gameEnded = true;
        }
    }

    private String computeNicknameWinner(){
        //CASE 1: one player has fewer towers then the others
        Map<Player, Integer> playerTowersLeft = new HashMap<>();
        for(Player p : this.players){
            playerTowersLeft.put(p, this.board.getPlayerSchool(p).getNumTowers());
        }
        //order the map so that at first place there is the player with fewer towers left
        List<Player> orderedPlayerTowerLeft = new ArrayList<>();
        while (playerTowersLeft.size() > 0) {
            Map.Entry<Player, Integer> min = null;
            for (Map.Entry<Player, Integer> e : playerTowersLeft.entrySet()) {
                if (min == null || min.getValue() > e.getValue()) {
                    min = e;
                }
            }
            Player minPlayer = min.getKey();
            orderedPlayerTowerLeft.add(minPlayer);
            playerTowersLeft.remove(minPlayer);
        }
        //if the num of the first player is lower (not equal) he won
        if(this.board.getPlayerSchool(orderedPlayerTowerLeft.get(0)).getNumTowers() < this.board.getPlayerSchool(orderedPlayerTowerLeft.get(1)).getNumTowers()){
            return orderedPlayerTowerLeft.get(0).getNickname();
        }
        else{//CASE 2: between the players with same number of towers left, the one with more professors win
            //remove all Players with too many towers
            int maxNumTowers = this.board.getPlayerSchool(orderedPlayerTowerLeft.get(0)).getNumTowers();
            for(int i = 1; i < this.numPlayers; i++){
                Player p = orderedPlayerTowerLeft.get(i);
                if(this.board.getPlayerSchool(p).getNumTowers() > maxNumTowers){
                    orderedPlayerTowerLeft.remove(p);
                }
            }

            Map<Player, Integer> playerProfessors = new HashMap<>();
            for(Player p : orderedPlayerTowerLeft){
                playerProfessors.put(p, this.board.getPlayerSchool(p).getProfessors().size());
            }
            //order the map so that at first place there is the player with fewer towers left
            List<Player> orderedPlayerProfessors = new ArrayList<>();
            while (playerProfessors.size() > 0) {
                Map.Entry<Player, Integer> min = null;
                for (Map.Entry<Player, Integer> e : playerProfessors.entrySet()) {
                    if (min == null || min.getValue() > e.getValue()) {
                        min = e;
                    }
                }
                Player minPlayer = min.getKey();
                orderedPlayerProfessors.add(minPlayer);
                playerProfessors.remove(minPlayer);
            }

            return orderedPlayerProfessors.get(0).getNickname();
        }
    }

    public void addBoardObserver() {
        for(ServerView s : serverViews) {
            this.board.addObserver(s);
            if(isAdvanced())
                this.boardAdvanced.addObserver(s);
        }
    }
}

