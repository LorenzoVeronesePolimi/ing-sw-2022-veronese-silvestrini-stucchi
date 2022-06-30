package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Enumerations.ControllerErrorType;
import it.polimi.ingsw.Controller.Enumerations.State;
import it.polimi.ingsw.Controller.Exceptions.ControllerException;
import it.polimi.ingsw.Controller.Exceptions.NoPlayerException;
import it.polimi.ingsw.Messages.Enumerations.INMessageType;
import it.polimi.ingsw.Messages.INMessages.*;
import it.polimi.ingsw.Model.Board.*;
import it.polimi.ingsw.Model.Cards.*;
import it.polimi.ingsw.Model.Enumerations.CharacterCardEnumeration;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Places.Archipelago;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Observer.ObserverController;
import it.polimi.ingsw.Persistence.PersistenceHandler;
import it.polimi.ingsw.Server.Server;
import it.polimi.ingsw.View.Exceptions.NoCharacterCardException;
import it.polimi.ingsw.Server.ServerView;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


/**
 * This class is the controller of the MVC pattern. It observes the view and plays actions on the model.
 */
public class Controller implements ObserverController<Message>, Serializable {
    private static final long serialVersionUID = 1L;
    private transient final Server server;
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

    private boolean usePersistence = true;
    private int currentPlayerIndex = 0;
    private transient final List<ServerView> serverViews;

    private ControllerInput controllerInput;
    private ControllerState controllerState;
    private ControllerIntegrity controllerIntegrity;

    private int numStudentsToMove;
    private int numStudentsToMoveCurrent;
    private final int NUM_STUDENTS_TO_MOVE_TWO_PLAYERS = 3;
    private final int NUM_STUDENTS_TO_MOVE_THREE_PLAYERS = 4;

    boolean just_started = true; //true until the very first PLANNING1 has still not been reached

    private boolean characterCardUsed = false; //true when a CC has been used

    /**
     * Constructor that initializes the lists and maps of the class.
     * @param server
     */
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

    /**
     * method that says if a game is ended
     * @return true if the game is ended (there is a winner), false otherwise
     */
    public boolean isGameEnded() {
        return gameEnded;
    }

    /**
     * getter of precomputed player (the next player to make his moves)
     * @return precomputed player
     */
    public Player getPrecomputedPlayer() {
        return precomputedPlayer;
    }

    /**
     * getter of precomputed state (state in which the game will be after the current player finishes his moves)
     * @return precomputed state
     */
    public State getPrecomputedState() {
        return precomputedState;
    }

    /**
     * getter of current player
     * @return current player
     */
    public Player getCurrentPlayer(){
        return this.players.get(this.currentPlayerIndex);
    }

    /**
     * getter of the current player from the list of sit players (in order of entrance in the game)
     * @return current player
     */
    public Player getCurrentSitPlayer(){
        return this.sitPlayers.get(this.currentPlayerIndex);
    }

    /**
     * getter of the index of current player
     * @return index of current player
     */
    public int getCurrentPlayerIndex(){
        return this.currentPlayerIndex;
    }

    /**
     * getter of the list of players
     * @return list of players
     */
    public List<Player> getPlayers(){
        return new ArrayList<>(this.players);
    }

    /**
     * getter of the list of sit players(in order of entrance in the game)
     * @return list of sit players
     */
    public List<Player> getSitPlayers() {
        return new ArrayList<>(this.sitPlayers);
    }

    /**
     * getter of the number of students that the current player must move
     * @return number of students that the current player must move
     */
    public int getNumStudentsToMoveCurrent(){
        return this.numStudentsToMoveCurrent;
    }

    /**
     * getter of the board
     * @return board
     */
    public BoardAbstract getBoard(){return this.board;}

    /**
     * getter of advanced board
     * @return board advanced
     */
    public BoardAdvanced getBoardAdvanced(){return this.boardAdvanced;}

    /**
     * getter of winner's nickname
     * @return winner's nickname
     */
    public String getNicknameWinner(){return this.nicknameWinner;}

    /**
     * method that says is a game is advanced or not
     * @return true is advanced game, false otherwise
     */
    public boolean isAdvanced(){
        return this.advanced;
    }

    /**
     * getter of controller state
     * @return controller state
     */
    public ControllerState getControllerState(){
        return this.controllerState;
    }

    /**
     * getter of integrity controller
     * @return integrity controller
     */
    public ControllerIntegrity getControllerIntegrity(){
        return this.controllerIntegrity;
    }

    /**
     * getter of input controller
     * @return input controller
     */
    public ControllerInput getControllerInput(){
        return this.controllerInput;
    }

    /**
     * method that says if at least one character card was used
     * @return true if at least one character card was used, false otherwise
     */
    public boolean isCharacterCardUsed(){
        return this.characterCardUsed;
    }

    /**
     * setter of characterCardUsed
     * @param newValue boolean value to set
     */
    public void setCharacterCardUsed(boolean newValue){
        this.characterCardUsed = newValue;
    }

    /**
     * method that enables and disables the use of persistence
     * @param usePersistence boolean enabler
     */
    public void setUsePersistence(boolean usePersistence) {
        this.usePersistence = usePersistence;
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
    public void update(Message message) throws ControllerException {    //TODO: synchronized for multiple ping received
        if(!controllerInput.checkFormat(message)){
            System.out.println("[Controller, update]: Invalid format");
            throw new ControllerException(ControllerErrorType.FORMAT_ERROR);
        }

        if(!controllerState.checkState(message.getType())){
            System.out.println("[Controller, update]: You can't do that now");
            throw new ControllerException(ControllerErrorType.STATE_ERROR);
        }

        if(!message.manageMessage(this)){
            System.out.println("[Controller, update]: Integrity error");
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
                System.out.println("[Controller, update]: notify");
            }
            else{
                controllerState.setState(State.PLANNING2);
                this.precomputedState = State.PLANNING2;
                try {
                    if(isAdvanced())
                        this.boardAdvanced.moveStudentBagToCloud();
                    else
                        this.board.moveStudentBagToCloud();
                    if(this.board.getBag().getNumStudents() == 0){ // last student extracted: game will end at the end of the round
                        gameEndedBag(this.board.getNumStudentsInBag()); //TODO: test
                    }
                } catch (ExceededMaxStudentsCloudException e) { //TODO: test
                    return; //case of first turn, in which clouds are filled immediately
                } catch (StudentNotFoundException e){ //CASE 2.1 of end of the game
                    gameEndedBag(this.board.getNumStudentsInBag());
                    this.board.notifyPlayers();
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

        BoardFactory factory = new BoardFactory(this.players);
        this.board = factory.createBoard();

        if(this.advanced){
            try {
                this.boardAdvanced = new BoardAdvanced(this.board);
            } catch (ExceededMaxStudentsHallException | StudentNotFoundException | TowerNotFoundException | EmptyCaveauException e) {
                //impossible since it would be an error of Model
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

        System.out.println("[Controller, initMatch]: init match");
        this.addBoardObserver();
        System.out.println("[Controller, initMatch]: add obs");
    }

    /**
     * Compute the expected player that acquires the game turn.
     * @param list list of the players of the match.
     * @param turnPriority turnPriority value of the AssistantCard played by the player.
     * @return an ordered list of players, where the turn priorities are in ascending order.
     */
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
    /*
    private List<Player> whoPlayedAC(){
        List<Player> result = new ArrayList<>();
        for(int i = this.currentPlayerIndex - this.iteratorAC; i <= this.currentPlayerIndex; i++){
            result.add(this.sitPlayers.get(i));
        }

        return result;
    }
     */

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
     * Create the match.
     * @param message message coming from the Client.
     * @return true if the actions has been done, false otherwise
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

        if(this.numPlayers == 4){
            if(colourFirstPlayer == PlayerColour.GRAY){
                return false; //TODO: test (put in integrity)
            }
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
     * Add a player to an existing match.
     * @param message message coming from the Client.
     * @return true if the actions has been done, false otherwise
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
            if(colour == PlayerColour.GRAY){
                return false; //TODO: test (put in integrity)
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
            // are there saved matches?
            PersistenceHandler persistenceHandler = new PersistenceHandler();
            Controller recoveredController = persistenceHandler.restoreMatch();
            if (recoveredController != null && this.usePersistence) { // check if you have to restore
                List<String> currentNicknames = new ArrayList<>();
                for (Player p : this.players) {
                    currentNicknames.add(p.getNickname());
                }
                List<String> recoveredNicknames = new ArrayList<>();
                for (Player p : recoveredController.getPlayers()) {
                    recoveredNicknames.add(p.getNickname());
                }
                boolean recoveredIsAdvanced = recoveredController.isAdvanced();
                if (currentNicknames.containsAll(recoveredNicknames) && recoveredNicknames.containsAll(currentNicknames) && (recoveredIsAdvanced == this.advanced)) {
                    restoreController(recoveredController);
                    System.out.println("[Controller, manageAddPlayer: restoring match]: init match");
                    this.addBoardObserver();
                    System.out.println("[Controller, manageAddPlayer: restoring match]: add obs");
                    if(isAdvanced()){
                        this.boardAdvanced.notifyPlayers();
                    }
                    else{
                        this.board.notifyPlayers();
                    }
                }
                else{ // start from zero: no need to restore (different players' nicknames)
                    this.initMatch();
                    this.precomputedState = State.PLANNING1;
                    controllerState.setState(State.PLANNING1);
                }
            }
            else{ // start from zero
                this.initMatch();
                this.precomputedState = State.PLANNING1;
                controllerState.setState(State.PLANNING1);
            }

        }
        else if(this.players.size() < numPlayers) {
            List<PlayerColour> playerColourList = players.stream().map(Player::getColour).collect(Collectors.toList());
            server.askPlayerInfo(playerColourList, numPlayers);
        }
        return true;
    }

    /**
     * Player plays an AssistantCard in his hand.
     * @param message message coming from the Client.
     * @return true if the actions has been done, false otherwise
     */
    public boolean manageAssistantCard(MessageAssistantCard message){
        String nicknamePlayer = message.getNickname();
        int turnPriority = message.getTurnPriority();
        Player revertErrorPlayer; // in case of an error we have to revert the precomputeNextPLayer result

        // Is him the currentPlayer? Can he use that AssistantCard?
        if(!isCurrentSitPlayer(nicknamePlayer)){
            System.out.println("[Controller, manageAssistantCard]: not current player");
            return false;
        }

        if(!controllerIntegrity.checkAssistantCard(this.usedCards, getCurrentSitPlayer(), turnPriority)) {
            System.out.println("[Controller, manageAssistantCard]: card already played");
            return false;
        }

        revertErrorPlayer = this.precomputedPlayer; // in case this player committed an error we can revert the precompute
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
        } catch(AssistantCardAlreadyPlayedTurnException | NoAssistantCardException ex){ // impossible: controllerIntegrity blocks this //TODO: test
            this.iteratorAC --;
            this.precomputedPlayer = revertErrorPlayer; // reverting the error in precompute
            System.out.println("[Controller, manageAssistantCard]: catch");
            return false;
        }

        this.gameEndedAssistantCards(this.getCurrentSitPlayer().getHandLength());

        this.usedCards.add(getCurrentSitPlayer().getLastCard().getTurnPriority());

        // Go on within the turn
        this.currentPlayerIndex = this.sitPlayers.indexOf(this.precomputedPlayer);

        if(this.iteratorAC == this.numPlayers){ //last player: all players has played their AssistantCard. Now I can set the order
            this.changeTurnOrder(); // reset the order of the Players according to the values of the AssistantCards
            this.currentPlayerIndex = 0; // the new turn will start
            controllerState.setState(State.ACTION1);
            iteratorAC = 0;
            this.usedCards.clear();
        }
        return true;
    }

    /**
     * Compute the expected player that will have to play the next turn.
     * @param turnPriority value of the last card played.
     */
    private void precomputeNextPlayer(int turnPriority) {
        if(this.iteratorAC == 0){
            this.currentPlayerIndex = this.sitPlayers.indexOf(this.precomputedPlayer); //precomputedPlayer was set in the last Cloud
        }

        if(this.iteratorAC < this.numPlayers - 1){
            this.precomputedPlayer = this.sitPlayers.get(this.computeNextACIndex());
        }
        else{
            //need to create a list from the first to the penultimate
            List<Player> inOrderAC = new ArrayList<>();
            int indexOfFirst = this.sitPlayers.indexOf(this.players.get(0));
            for(int i = 0; i < this.sitPlayers.size(); i++){
                inOrderAC.add(this.sitPlayers.get((indexOfFirst + i) % this.sitPlayers.size()));
            }
            List<Player> precomputedNextPlayersList = precomputeTurnOrder(inOrderAC, turnPriority); //TODO: there was this.players or this.sitPlayers before
            this.precomputedPlayer = precomputedNextPlayersList.get(0);
            this.precomputedState = State.ACTION1;
        }
        this.iteratorAC++;
    }

    /**
     * Move students from the Hall to the Diningroom of the School.
     * @param message message coming from the Client.
     * @return true if the actions has been done, false otherwise
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
            try {
                if(this.advanced) {
                    boardAdvanced.moveStudentHallToDiningRoom(getCurrentPlayer(), studentColour);
                    if(boardAdvanced.getTakeProfessorOnEquityFlag()){
                        for(AbstractCharacterCard c : boardAdvanced.getExtractedCards()){
                            if(c.getType() == CharacterCardEnumeration.TAKE_PROFESSOR_ON_EQUITY){
                                ((TakeProfessorOnEquity)c).useEffect(getCurrentPlayer());
                            }
                        }
                        this.manageCCTakeProfessorOnEquity(new MessageCCTakeProfessorOnEquity(nicknamePlayer));
                    }

                } else {
                    board.moveStudentHallToDiningRoom(getCurrentPlayer(), studentColour);
                }
            } catch (ExceededMaxStudentsDiningRoomException | ProfessorNotFoundException | NoProfessorBagException | StudentNotFoundException | TowerNotFoundException | InvalidTowerNumberException | AnotherTowerException | ExceededMaxTowersException e) {//TODO: test
                return false; //TODO: test
            } catch (EmptyCaveauException e) { //case boardAdvanced
                // do nothing: simply he doesn't receive the coin
            }
            this.numStudentsToMoveCurrent--;
            if(this.numStudentsToMoveCurrent == 0 || // all possible Students moved
                    this.board.getPlayerSchool(getCurrentPlayer()).getStudentsHall().size() == 0){//TODO: test // no Students remained
                this.numStudentsToMoveCurrent = this.numStudentsToMove;
                controllerState.setState(State.ACTION2);
            }

            return true;
        }
        return false;
    }

    /**
     * Move a student from the Hall to an archipelago.
     * @param message message coming from the Client.
     * @return true if the actions has been done, false otherwise.
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
            try {
                if (isAdvanced()) {
                    boardAdvanced.moveStudentSchoolToArchipelagos(getCurrentPlayer(), studentColour, destinationArchipelagoIndex);
                } else {
                    board.moveStudentSchoolToArchipelagos(getCurrentPlayer(), studentColour, destinationArchipelagoIndex);
                }
            }
            catch (StudentNotFoundException e) {
                return false; //TODO: test
            }
            this.numStudentsToMoveCurrent--;
            if(this.numStudentsToMoveCurrent == 0 || // all possible Students moved
                    this.board.getPlayerSchool(getCurrentPlayer()).getStudentsHall().size() == 0){ // no Students remained //TODO: test (second condition)
                this.numStudentsToMoveCurrent = this.numStudentsToMove;
                controllerState.setState(State.ACTION2);
            }
            return true;
        }
        return false;//TODO: test
    }

    /**
     * Move MotherNature on an archipelago.
     * @param message message coming from the Client.
     * @return true if the actions has been done, false otherwise.
     */
    public boolean manageMoveMotherNature(MessageMoveMotherNature message){
        String nicknamePlayer = message.getNickname();
        int moves = message.getMoves();

        this.precomputedState = State.ACTION3;
        try {
            this.gameEndedArchipelagos(moves);
        } catch (EmptyCaveauException | ExceededMaxStudentsHallException | StudentNotFoundException | InvalidTowerNumberException | AnotherTowerException | ExceededMaxTowersException e) {
            this.precomputedState = State.ACTION2; //TODO: test
            return false;
        } catch (TowerNotFoundException e){ // No towers left, so I Win
            this.precomputedState = State.END;
            controllerState.setState(State.END);
            if(this.numPlayers < 4){
                this.computeNicknameWinner();//TODO: test
            }
            else{
                this.computeNicknameWinnerFour();
            }
            return false;

        }

        if(!isCurrentPlayer(nicknamePlayer)){
            this.precomputedState = State.ACTION2;
            return false;
        }

        if(controllerIntegrity.checkMoveMotherNature(getCurrentPlayer(), moves)){
            try {
                if (isAdvanced()) {
                    boardAdvanced.moveMotherNature(moves);
                    boardAdvanced.tryToConquer(getCurrentPlayer());
                } else {
                    board.moveMotherNature(moves);
                    board.tryToConquer(getCurrentPlayer());
                }
            } catch (InvalidTowerNumberException | AnotherTowerException | ExceededMaxTowersException | TowerNotFoundException e) {
                this.precomputedState = State.ACTION2;
                return false;
            }
            if(this.precomputedState != State.END){
                controllerState.setState(State.ACTION3);
            }
            else{
                controllerState.setState(State.END);
            }

            return true;
        }
        return false;
    }

    /**
     * Move Student from Cloud to School.
     * @param message message coming from the Client.
     * @return true if the actions has been done, false otherwise.
     */
    public boolean manageStudentCloudToSchool(MessageStudentCloudToSchool message){
        String nicknamePlayer = message.getNickname();
        int indexCloud = message.getIndexCloud();

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(this.currentPlayerIndex == this.players.size() - 1){ //all Players made their move => new turn
            if(this.gameEnded){ //case game ends at the end of the turn
                this.precomputedState = State.END;
                if(this.players.size() <= 3){
                    computeNicknameWinner();
                }
                else{
                    computeNicknameWinnerFour();
                }
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

        String storeNameCardUsed = "";
        if(controllerIntegrity.checkStudentCloudToSchool(getCurrentPlayer(), indexCloud) || this.gameEnded){//TODO: I can choose a void cloud only if the game is going to finish, RIGHT?
            try{
                if(isAdvanced()) {
                    storeNameCardUsed = boardAdvanced.getNameCardUsed();
                    boardAdvanced.resetNameCardUsed();
                    boardAdvanced.moveStudentCloudToSchool(getCurrentPlayer(), indexCloud);
                } else {
                    board.moveStudentCloudToSchool(getCurrentPlayer(), indexCloud);
                }
            } catch(ExceededMaxStudentsHallException ex){
                boardAdvanced.setNameCardUsed(storeNameCardUsed);
                return false;
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

            if(this.usePersistence) {
                PersistenceHandler persistenceHandler = new PersistenceHandler();
                if (controllerState.getState() == State.END) { //if match has ended no need to save
                    persistenceHandler.deleteMatch();
                } else {
                    persistenceHandler.saveMatch(this); //save match
                }
            }

            // reset use of continuative effects of CharacterCards
            if(isAdvanced()) {
                boardAdvanced.setTakeProfessorOnEquityFlag(false);
                boardAdvanced.setTwoExtraPointsFlag(false);
                boardAdvanced.setColourToExclude(null);
                boardAdvanced.setFakeMNMovementFlag(false);

                for(Archipelago a : boardAdvanced.getArchiList()) {
                    a.setTowerNoValueFlag(false);
                }
            }
            //TODO: remove some card effects (colourtoexclude, towernovalue...)

            return true;
        }
        return false;//TODO: test
    }

    //--------------------------------------------------CHARACTER CARDS
    /**
     * Play the CharacterCard ExchangeThreeStudents.
     * @param message message coming from the Client.
     * @return true if the actions has been done, false otherwise.
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
            return false;//TODO: test
        }

        return false;//TODO: test
    }

    /**
     * Play the CharacterCard ExchangeTwoHallDining.
     * @param message message coming from the Client.
     * @return true if the actions has been done, false otherwise.
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
                 CoinNotFoundException | ProfessorNotFoundException | NoProfessorBagException e) {return false;}//TODO: test

        return false;//TODO: test
    }

    /**
     * Play the CharacterCard ExcludeColourFromCounting.
     * @param message message coming from the Client.
     * @return true if the actions has been done, false otherwise.
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
                return false;//TODO: test
            }

            this.characterCardUsed = true;

            return true;
        }

        return false;//TODO: test
    }

    /**
     * Play the CharacterCard ExtraStudentInDining.
     * @param message message coming from the Client.
     * @return true if the actions has been done, false otherwise.
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
                return false;//TODO: test
            }
        }

        return false;//TODO: test
    }

    /**
     * Play the CharacterCard FakeMNMovement.
     * @param message message coming from the Client.
     * @return true if the actions has been done, false otherwise.
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
                return false;//TODO: test
            }
        }

        return false;//TODO: test
    }

    /**
     * Play the CharacterCard ForbidIsland.
     * @param message message coming from the Client.
     * @return true if the actions has been done, false otherwise.
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
                return false;//TODO: test
            }
        }
        return false;//TODO: test
    }

    /**
     * Play the CharacterCard PlaceOneStudent.
     * @param message message coming from the Client.
     * @return true if the actions has been done, false otherwise.
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
            return false;//TODO: test
        }

        return false;//TODO: test
    }

    /**
     * Play the CharacterCard ReduceColourInDining.
     * @param message message coming from the Client.
     * @return true if the actions has been done, false otherwise.
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
            return false;//TODO: test
        }

        return false;//TODO: test
    }

    /**
     * Play the CharacterCard TowerNoValue.
     * @param message message coming from the Client.
     * @return true if the actions has been done, false otherwise.
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
                return false;//TODO: test
            }

            this.characterCardUsed = true;

            return true;
        }

        return false;//TODO: test
    }

    /**
     * Play the CharacterCard TwoExtraPoints.
     * @param message message coming from the Client.
     * @return true if the actions has been done, false otherwise.
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
                return false;//TODO: test
            }

            this.characterCardUsed = true;

            return true;
        }

        return false;//TODO: test
    }

    /**
     * Play the CharacterCard TakeProfessorOnEquity.
     * @param message message coming from the Client.
     * @return true if the actions has been done, false otherwise.
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
            return false;//TODO: test
        }

        return false;//TODO: test
    }

    /**
     * Play the CharacterCard TwoExtraIslands.
     * @param message message coming from the Client.
     * @return true if the actions has been done, false otherwise.
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
                return false;//TODO: test
            }

            this.characterCardUsed = true;

            return true;
        }

        return false;//TODO: test
    }

    /**
     * Check if the type of CharacterCard chosen exists among extracted CharacterCards.
     * @param type typeof the message received.
     * @return index of the CharacterCard in the extracted CharacterCards.
     * @throws NoCharacterCardException if there is no corresponding CharacterCard.
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

    /**
     * Checks if the game is ending based on the rule that states: "The game ends when only 3 groups of Islands remain on the table."
     * @param moves Number of archipelagos that MotherNature has to pass over.
     * @throws TowerNotFoundException When a tower is not found on a conquered archipelago or when there are no towers in a School.
     * @throws EmptyCaveauException Where there are no money in the Bank.
     * @throws ExceededMaxStudentsHallException When is exceeded the maximum number of students in the Hall.
     * @throws StudentNotFoundException When a student is not found.
     * @throws InvalidTowerNumberException When the number of towers is incorrect.
     * @throws AnotherTowerException When there is already a tower on an archipelago.
     * @throws ExceededMaxTowersException When the maximum number of towers is exceeded.
     */
    private void gameEndedArchipelagos(int moves) throws TowerNotFoundException, EmptyCaveauException, ExceededMaxStudentsHallException, StudentNotFoundException, InvalidTowerNumberException, AnotherTowerException, ExceededMaxTowersException {
        BoardAbstract boardCopy;
        if(this.numPlayers == 2){
            boardCopy = new BoardTwo(this.board);
        }
        else if(this.numPlayers == 3){
            boardCopy = new BoardThree(this.board);//TODO: test
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
            if(this.players.size() <= 3){
                computeNicknameWinner();
            }
            else{
                computeNicknameWinnerFour();
            }

        }

        //CASE 0: a player has put all of his towers
        if(this.players.size() == 4){
            Player p1 = this.players.get(this.currentPlayerIndex);
            Player p2 = ((BoardFour)this.board).getTeammates().get(this.players.get(this.currentPlayerIndex));
            if(this.board.getPlayerSchool(p1).getNumTowers() == 0 && this.board.getPlayerSchool(p2).getNumTowers() == 0){
                this.precomputedState = State.END; //game ends immediately
                controllerState.setState(State.END);
                if(this.players.size() <= 3){
                    computeNicknameWinner();//TODO: test
                }
                else{
                    computeNicknameWinnerFour();
                }
            }
        }
        else{
            if(boardCopy.getPlayerSchool(this.players.get(this.currentPlayerIndex)).getNumTowers() == 0){
                this.precomputedState = State.END; //game ends immediately
                controllerState.setState(State.END);
                if(this.players.size() <= 3){
                    computeNicknameWinner();
                }
                else{
                    computeNicknameWinnerFour();//TODO: test
                }
            }
        }
    }

    /**
     * Checks if the game is ending based on the rule that states: "The game ends at the end of the round where the last student
     * has been drawn from the bag."
     * @param numStudentsInBag number of students in the bag.
     */
    private void gameEndedBag(int numStudentsInBag){
        //CASE 2.1: no more Students in the Bag
        //game ends at the end of round
        System.out.println("THESE ARE STUDETNS IN BAG");
        System.out.println(numStudentsInBag);
        if(numStudentsInBag == 0){
            this.gameEnded = true;
        }
    }

    /**
     * Checks if the game is ending based on the rule that states: "The game ends at the end of the round where every
     * player runs out of AssistantCards in their hand."
     * @param handSize number of AssistantCards in the player hand.
     */
    private void gameEndedAssistantCards(int handSize){
        //CASE 2.2: no more assistant cards
        //game ends at the end of the round (?)
        if(handSize == 0){
            this.gameEnded = true;
        }
    }

    /**
     * Compute the nickname of the winner of the game.
     */
    private void computeNicknameWinner(){
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
            this.nicknameWinner = orderedPlayerTowerLeft.get(0).getNickname();
        }
        else if(this.board.getPlayerSchool(orderedPlayerTowerLeft.get(0)).getNumTowers() > this.board.getPlayerSchool(orderedPlayerTowerLeft.get(1)).getNumTowers()){
            this.nicknameWinner = orderedPlayerTowerLeft.get(1).getNickname();//TODO: test
        }
        else{//CASE 2: between the players with same number of towers left, the one with more professors win
            //remove all Players with too many towers
            int maxNumTowers = this.board.getPlayerSchool(orderedPlayerTowerLeft.get(0)).getNumTowers();
            for(int i = 1; i < this.numPlayers; i++){
                Player p = orderedPlayerTowerLeft.get(i);
                if(this.board.getPlayerSchool(p).getNumTowers() > maxNumTowers){
                    orderedPlayerTowerLeft.remove(p);//TODO: test
                }
            }

            Map<Player, Integer> playerProfessors = new HashMap<>();
            for(Player p : orderedPlayerTowerLeft){
                playerProfessors.put(p, this.board.getPlayerSchool(p).getProfessors().size());
            }
            //order the map so that at first place there is the player with fewer towers left
            List<Player> orderedPlayerProfessors = new ArrayList<>();
            while (playerProfessors.size() > 0) {
                Map.Entry<Player, Integer> max = null;
                for (Map.Entry<Player, Integer> e : playerProfessors.entrySet()) {
                    if (max == null || max.getValue() < e.getValue()) {
                        max = e;
                    }
                }
                Player minPlayer = max.getKey();
                orderedPlayerProfessors.add(minPlayer);
                playerProfessors.remove(minPlayer);
            }

            this.nicknameWinner = orderedPlayerProfessors.get(0).getNickname();
        }
    }

    /**
     * Compute the nicknames of the winners if the match is a four players match.
     */
    private void computeNicknameWinnerFour(){
        //CASE 1: one team has fewer towers then the others
        Player t1 = null; //leader (the one with towers) of white team
        for(int i = 0; i < this.players.size(); i++){
            if(players.get(i).getColour() == PlayerColour.WHITE){
                t1 = players.get(i);
            }
        }
        Player t2 = null; //leader (the one with towers) of black team
        for(int i = 0; i < this.players.size(); i++){
            if(players.get(i).getColour() == PlayerColour.BLACK){
                t2 = players.get(i);
            }
        }
        Map<Player, Integer> playerLeaderTowersLeft = new HashMap<>();
        playerLeaderTowersLeft.put(t1, this.board.getPlayerSchool(t1).getNumTowers());
        playerLeaderTowersLeft.put(t2, this.board.getPlayerSchool(t2).getNumTowers());

        //order the map so that at first place there is the player with fewer towers left
        List<Player> orderedPlayerTowerLeft = new ArrayList<>();
        while (playerLeaderTowersLeft.size() > 0) {
            Map.Entry<Player, Integer> min = null;
            for (Map.Entry<Player, Integer> e : playerLeaderTowersLeft.entrySet()) {
                if (min == null || min.getValue() > e.getValue()) {
                    min = e;
                }
            }
            Player minPlayer = min.getKey();
            orderedPlayerTowerLeft.add(minPlayer);
            playerLeaderTowersLeft.remove(minPlayer);
        }
        //if the num of the first player is lower (not equal) he won
        if(this.board.getPlayerSchool(orderedPlayerTowerLeft.get(0)).getNumTowers() < this.board.getPlayerSchool(orderedPlayerTowerLeft.get(1)).getNumTowers()){
            this.nicknameWinner = orderedPlayerTowerLeft.get(0).getNickname() + '+' + ((BoardFour)this.board).getTeammates().get(orderedPlayerTowerLeft.get(0)).getNickname();
        }
        else if(this.board.getPlayerSchool(orderedPlayerTowerLeft.get(0)).getNumTowers() > this.board.getPlayerSchool(orderedPlayerTowerLeft.get(1)).getNumTowers()){
            this.nicknameWinner = orderedPlayerTowerLeft.get(1).getNickname() + '*' + ((BoardFour)this.board).getTeammates().get(orderedPlayerTowerLeft.get(1)).getNickname();//TODO: test
        }
        else{//CASE 2: between the players with same number of towers left, the one with more professors win
            Map<Player, Integer> playerProfessors = new HashMap<>();
            int pt1 = this.board.getPlayerSchool(t1).getProfessors().size();
            pt1 += this.board.getPlayerSchool(((BoardFour)this.board).getTeammates().get(t1)).getProfessors().size();
            int pt2 = this.board.getPlayerSchool(t2).getProfessors().size();
            pt2 += this.board.getPlayerSchool(((BoardFour)this.board).getTeammates().get(t2)).getProfessors().size();
            playerProfessors.put(t1, pt1);
            playerProfessors.put(t2, pt2);

            //order the map so that at first place there is the player with fewer towers left
            List<Player> orderedPlayerProfessors = new ArrayList<>();
            while (playerProfessors.size() > 0) {
                Map.Entry<Player, Integer> max = null;
                for (Map.Entry<Player, Integer> e : playerProfessors.entrySet()) {
                    if (max == null || max.getValue() < e.getValue()) {
                        max = e;
                    }
                }
                Player minPlayer = max.getKey();
                orderedPlayerProfessors.add(minPlayer);
                playerProfessors.remove(minPlayer);
            }

            this.nicknameWinner = orderedPlayerProfessors.get(0).getNickname() + '*' + ((BoardFour)this.board).getTeammates().get(orderedPlayerProfessors.get(0)).getNickname();
        }
    }

    /**
     * Adds observers of the Board to the Board.
     */
    public void addBoardObserver() {
        for(ServerView s : serverViews) {
            this.board.addObserver(s);
            if(isAdvanced())
                this.boardAdvanced.addObserver(s);
        }
    }

    /**
     * method that restores a given controller by coping its information in this controller
     * @param controller controller to restore
     */
    private void restoreController(Controller controller) {
        this.numPlayers = controller.numPlayers;
        this.advanced = controller.advanced;
        this.board = controller.board;
        this.boardAdvanced = controller.boardAdvanced;
        this.sitPlayers = controller.sitPlayers;
        this.players = controller.players;
        this.precomputedPlayer = controller.precomputedPlayer;
        this.iteratorAC = controller.iteratorAC;
        this.precomputedState = controller.precomputedState;
        this.usedCards = controller.usedCards;
        this.gameEnded = controller.gameEnded;
        this.nicknameWinner = controller.nicknameWinner;
        this.currentPlayerIndex = controller.currentPlayerIndex;
        this.controllerInput = controller.controllerInput;
        this.controllerState = controller.controllerState;
        this.controllerIntegrity = controller.controllerIntegrity;
        this.numStudentsToMove = controller.numStudentsToMove;
        this.numStudentsToMoveCurrent = controller.numStudentsToMoveCurrent;
        this.just_started = controller.just_started;
        this.characterCardUsed = controller.characterCardUsed;
    }
}