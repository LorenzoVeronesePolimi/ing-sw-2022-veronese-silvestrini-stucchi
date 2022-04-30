package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Enumerations.State;
import it.polimi.ingsw.Controller.Exceptions.NoPlayerException;
import it.polimi.ingsw.Messages.Enumerations.INMessageType;
import it.polimi.ingsw.Messages.INMessage.*;
import it.polimi.ingsw.Model.Board.BoardAbstract;
import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Board.BoardFactory;
import it.polimi.ingsw.Model.Cards.*;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Observer.Observer;
import it.polimi.ingsw.View.Exceptions.NoCharacterCardException;
import it.polimi.ingsw.View.ServerView;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// This is the main Controller: it coordinates all the others
public class Controller implements Observer<Message> {
    private int numPlayers;
    private boolean advanced;
    private BoardAbstract board;
    private BoardAdvanced boardAdvanced; //null if advanced=0
    private List<Player> players; //ordered

    private int currentPlayerIndex = 0;
    private List<ServerView> serverViews;

    private final ControllerInput controllerInput;
    private final ControllerState controllerState;
    private final ControllerIntegrity controllerIntegrity;

    private int numStudentsToMove;
    private int numStudentsToMoveCurrent;
    private final int NUM_STUDENTS_TO_MOVE_TWO_PLAYERS = 3;
    private final int NUM_STUDENTS_TO_MOVE_THREE_PLAYERS = 4;

    private boolean characterCardUsed = false; //true when a CC has been used

    public Controller(){
        this.players = new ArrayList<>();
        this.controllerInput = new ControllerInput();
        this.controllerState = new ControllerState();
        this.controllerIntegrity = new ControllerIntegrity();
        this.serverViews = new ArrayList<>();
    }

    public Player getCurrentPlayer(){
        return this.players.get(this.currentPlayerIndex);
    } //TODO

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

    /* To be used if we want the client to write only "characterCard" when he use one CC
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
     * Call the Model and applicate the move requested
     * */
    public void update(Message message) {
        if(!controllerInput.checkFormat(message)){
            System.out.println("Invalid format");
            return;
        }

        if(!controllerState.checkState(message.getType())){
            System.out.println("You can't do that now");
            return;
        }

        if(!message.manageMessage(this)){
            System.out.println("Error");
        }

        //check if I have to make some automatic action (=>PIANIFICATION1)
        if(controllerState.getState() == State.PLANNING1){
            //TODO: surrounded with try catch just to remove errors. It needs to be checked before leaving it like that
            controllerState.setState(State.PLANNING2);
            try {
                this.board.moveStudentBagToCloud();
            } catch (ExceededMaxStudentsCloudException | StudentNotFoundException e) {
                return; //case of first turn, in which clouds are filled immediately
            }

        }
    }

    //associate the String to its SPColour. Note that I'm sure this association exists, since I made a control
    // in ControllerInput (checkStudentColour())
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

    //this eliminates all "-"s and converts the others
    private List<SPColour> mapListStringToColour(List<String> colours){
        List<SPColour> converted = new ArrayList<>();

        for(String s : colours){
            if(!s.equals("-")){
                converted.add(this.mapStringToSPColour(s));
            }
        }

        return converted;
    }

    private PlayerColour mapStringToPlayerColour(String s){
        switch(s.toLowerCase()){
            case "white":
                return PlayerColour.WHITE;
            case "black":
                return PlayerColour.BLACK;
            case "gray":
                return PlayerColour.GRAY;
        }
        return null; //possible only in case of "-"
    }

    private Player mapStringToPlayer(String s) throws NoPlayerException{
        for(Player p : this.players){
            if(p.getNickname().equals(s)){
                return p;
            }
        }

        throw new NoPlayerException();
    }

    // checks if nickname is the current Player
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

    private void initMatch(){
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

        this.addBoardObserver();
    }

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

    // TODO: maybe divide the CreateMatch and addPlayer even for the first player
    public boolean manageCreateMatch(MessageCreateMatch message){
        this.numPlayers = message.getNumPlayers();
        this.advanced = message.isAdvanced();
        ServerView serverView = message.getServerView();
        PlayerColour colourFirstPlayer = mapStringToPlayerColour(message.getColourFirstPlayer());
        // no need to control the boolean "advanced"

        if(!controllerIntegrity.checkCreateMatch(numPlayers)){
            return false;
        }

        Player player = new Player(message.getNicknameFirstPlayer(), colourFirstPlayer);
        this.players.add(player);
        serverView.setPlayerNickname(message.getNicknameFirstPlayer());
        this.serverViews.add(serverView);

        controllerState.setState(State.WAITING_PLAYERS);

        return true;
    }

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
            int numSameColour = 0;
            for(Player p : this.players){
                if(p.getColour() == colour){
                    numSameColour++;
                }
            }
            if(numSameColour >= 2){
                return false;
            }
        }
        else{
            for(Player p : this.players){
                if(p.getColour() == colour){
                    return false;
                }
            }
        }

        // No integrity to check
        Player player = new Player(nickname, colour);
        this.players.add(player);
        serverView.setPlayerNickname(message.getNickname());
        this.serverViews.add(serverView);

        if(this.players.size() == numPlayers){ // The requested number of players has been reached: let's go on
            this.initMatch();
            controllerState.setState(State.PLANNING1);
        }

        return true;
    }

    public boolean manageAssistantCard(MessageAssistantCard message){
        String nicknamePlayer = message.getNicknamePlayer();
        int turnPriority = message.getTurnPriority();

        // Is him the currentPlayer? Can he use that AssistantCard?
        if(!isCurrentPlayer(nicknamePlayer)){return false;}
        controllerIntegrity.checkAssistantCard(this.players, this.currentPlayerIndex, getCurrentPlayer(), turnPriority);

        // Remove the card from his hand
        try{
            board.useAssistantCard(getCurrentPlayer(), turnPriority);
        } catch(AssistantCardAlreadyPlayedTurnException | NoAssistantCardException ex){return false;} // card already used or no AssistantCard present

        // Go on within the turn
        this.currentPlayerIndex++;

        if(this.currentPlayerIndex == this.players.size()){ //last player: all players has played their AssistantCard. No I can set the order
            this.changeTurnOrder(); // reset the order of the Players according to the values of the AssistantCards
            this.currentPlayerIndex = 0; // the new turn will start
            controllerState.setState(State.ACTION1);
        }
        return true;
    }

    public boolean manageStudentHallToDiningRoom(MessageStudentHallToDiningRoom message){
        String nicknamePlayer = message.getNicknamePlayer();
        SPColour studentColour = mapStringToSPColour(message.getColour());

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

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

    public boolean manageStudentToArchipelago(MessageStudentToArchipelago message){
        String nicknamePlayer = message.getNicknamePlayer();
        SPColour studentColour = mapStringToSPColour(message.getColour());
        int destinationArchipelagoIndex = message.getDestArchipelagoIndex();

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(controllerIntegrity.checkStudentToArchipelago(getCurrentPlayer(), studentColour, destinationArchipelagoIndex)){
            try {
                board.moveStudentSchoolToArchipelagos(getCurrentPlayer(), studentColour, destinationArchipelagoIndex);
            } catch (StudentNotFoundException e) {
                return false;
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

    public boolean manageMoveMotherNature(MessageMoveMotherNature message){
        String nicknamePlayer = message.getNicknamePlayer();
        int moves = message.getMoves();

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(controllerIntegrity.checkMoveMotherNature(getCurrentPlayer(), moves)){
            board.moveMotherNature(moves);
            try {
                board.tryToConquer(getCurrentPlayer());
            } catch (InvalidTowerNumberException | AnotherTowerException | ExceededMaxTowersException | TowerNotFoundException e) {
                return false;
            }
            controllerState.setState(State.ACTION3);
            return true;
        }
        return false;
    }

    public boolean manageStudentCloudToSchool(MessageStudentCloudToSchool message){
        String nicknamePlayer = message.getNicknamePlayer();
        int indexCloud = message.getIndexCloud();

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(controllerIntegrity.checkStudentCloudToSchool(getCurrentPlayer(), indexCloud)){
            try{
                board.moveStudentCloudToSchool(getCurrentPlayer(), indexCloud);
            } catch(ExceededMaxStudentsHallException ex){return false;}

            // change current Player
            this.currentPlayerIndex++;
            if(this.currentPlayerIndex >= this.players.size()){ //all Players made their move => new turn
                controllerState.setState(State.PLANNING1);
                this.currentPlayerIndex = 0;
            }
            else{
                controllerState.setState(State.ACTION1);
            }
            return true;
        }
        return false;
    }

    //--------------------------------------------------CHARACTER CARDS
    public boolean manageCCExchangeThreeStudents(MessageCCExchangeThreeStudents message){
        int indexCard = message.getIndexCard();
        String nicknamePlayer = message.getNicknamePlayer();
        List<SPColour> coloursCard = this.mapListStringToColour(message.getColoursCard());
        List<SPColour> coloursHall = this.mapListStringToColour(message.getColoursHall());

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(isCharacterCardUsed()){return false;}

        if(!isRightMapIndexToCharacterCard(message.getType(), indexCard)){return false;}

        try{
            if(controllerIntegrity.checkCCExchangeThreeStudents(getCurrentPlayer(), coloursCard, coloursHall,
                    (ExchangeThreeStudents) this.boardAdvanced.getExtractedCards().get(indexCard))) {

                this.boardAdvanced.useExchangeThreeStudents(getCurrentPlayer(), coloursHall, coloursCard, indexCard);

                this.characterCardUsed = true;

                return true;
            }

        } catch(WrongNumberOfStudentsTransferException | StudentNotFoundException | ExceededMaxStudentsHallException ex){
            return false;
        } catch (CoinNotFoundException | EmptyCaveauException | ExceededMaxNumCoinException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean manageCCExchangeTwoHallDining(MessageCCExchangeTwoHallDining message){
        int indexCard = message.getIndexCard();
        String nicknamePlayer = message.getNicknamePlayer();
        List<SPColour> coloursHall = this.mapListStringToColour(message.getColoursHall());
        List<SPColour> coloursDiningRoom = this.mapListStringToColour(message.getColoursDiningRoom());

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(isCharacterCardUsed()){return false;}

        if(!isRightMapIndexToCharacterCard(message.getType(), indexCard)){return false;}

        try {
            if(controllerIntegrity.checkCCExchangeTwoHallDining(getCurrentPlayer(), coloursHall, coloursDiningRoom)){
                this.boardAdvanced.useExchangeTwoHallDining(getCurrentPlayer(), coloursHall, coloursDiningRoom, indexCard);

                this.characterCardUsed = true;

                return true;
            }
        } catch (WrongNumberOfStudentsTransferException |
                StudentNotFoundException |
                ExceededMaxStudentsHallException |
                ExceededMaxStudentsDiningRoomException |
                EmptyCaveauException |
                ExceededMaxNumCoinException |
                CoinNotFoundException e) {return false;}

        return false;
    }

    public boolean manageCCExcludeColourFromCounting(MessageCCExcludeColourFromCounting message){
        int indexCard = message.getIndexCard();
        String nicknamePlayer = message.getNicknamePlayer();
        SPColour colourToExclude = this.mapStringToSPColour(message.getColourToExclude());

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(isCharacterCardUsed()){return false;}

        if(!isRightMapIndexToCharacterCard(message.getType(), indexCard)){return false;}

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

    public boolean manageCCExtraStudentInDining(MessageCCExtraStudentInDining message){
        int indexCard = message.getIndexCard();
        String nicknamePlayer = message.getNicknamePlayer();
        SPColour colourToMove = mapStringToSPColour(message.getColourToMove());

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(isCharacterCardUsed()){return false;}

        if(!isRightMapIndexToCharacterCard(message.getType(), indexCard)){return false;}

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

    public boolean manageCCFakeMNMovement(MessageCCFakeMNMovement message){
        int indexCard = message.getIndexCard();
        String nicknamePlayer = message.getNicknamePlayer();
        int fakeMNPosition = message.getFakeMNPosition();

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(isCharacterCardUsed()){return false;}

        if(!isRightMapIndexToCharacterCard(message.getType(), indexCard)){return false;}

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

    public boolean manageCCForbidIsland(MessageCCForbidIsland message){
        int indexCard = message.getIndexCard();
        String nicknamePlayer = message.getNicknamePlayer();
        int archipelagoIndexToForbid = message.getArchipelagoIndexToForbid();

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(isCharacterCardUsed()){return false;}

        if(!isRightMapIndexToCharacterCard(message.getType(), indexCard)){return false;}

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

    public boolean manageCCPlaceOneStudent(MessageCCPlaceOneStudent message){
        int indexCard = message.getIndexCard();
        String nicknamePlayer = message.getNicknamePlayer();
        SPColour colourToMove = mapStringToSPColour(message.getColourToMove());
        int archipelagoIndexDestination = message.getArchipelagoIndexDest();

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(isCharacterCardUsed()){return false;}

        if(!isRightMapIndexToCharacterCard(message.getType(), indexCard)){return false;}

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

    public boolean manageCCReduceColourInDining(MessageCCReduceColourInDining message){
        int indexCard = message.getIndexCard();
        String nicknamePlayer = message.getNicknamePlayer();
        SPColour colourToReduce = mapStringToSPColour(message.getColourToReduce());

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(isCharacterCardUsed()){return false;}

        if(!isRightMapIndexToCharacterCard(message.getType(), indexCard)){return false;}

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

    public boolean manageCCTowerNoValue(MessageCCTowerNoValue message){
        int indexCard = message.getIndexCard();
        String nicknamePlayer = message.getNicknamePlayer();

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(isCharacterCardUsed()){return false;}

        if(!isRightMapIndexToCharacterCard(message.getType(), indexCard)){return false;}

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

    public boolean manageCCTwoExtraPoints(MessageCCTwoExtraPoints message){
        int indexCard = message.getIndexCard();
        String nicknamePlayer = message.getNicknamePlayer();

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(isCharacterCardUsed()){return false;}

        if(!isRightMapIndexToCharacterCard(message.getType(), indexCard)){return false;}

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

    public boolean manageCCTakeProfessorOnEquity(MessageCCTakeProfessorOnEquity message){
        int indexCard = message.getIndexCard();
        String nicknamePlayer = message.getNicknamePlayer();

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(isCharacterCardUsed()){return false;}

        if(!isRightMapIndexToCharacterCard(message.getType(), indexCard)){return false;}

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

    public boolean manageCCTwoExtraIslands(MessageCCTwoExtraIslands message){
        int indexCard = message.getIndexCard();
        String nicknamePlayer = message.getNicknamePlayer();

        if(!isCurrentPlayer(nicknamePlayer)){return false;}

        if(isCharacterCardUsed()){return false;}

        if(!isRightMapIndexToCharacterCard(message.getType(), indexCard)){return false;}

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

    private boolean isRightMapIndexToCharacterCard(INMessageType type, int indexCard){
        AbstractCharacterCard chosenCard = this.boardAdvanced.getExtractedCards().get(indexCard);

        // is this card corresponding to the index chosen?
        switch(type) {
            case CC_EXCHANGE_THREE_STUDENTS:
                if (chosenCard instanceof ExchangeThreeStudents) {
                    return true;
                }
            case CC_EXCHANGE_TWO_HALL_DINING:
                if (chosenCard instanceof ExchangeTwoHallDining) {
                    return true;
                }
            case CC_EXCLUDE_COLOUR_FROM_COUNTING:
                if (chosenCard instanceof ExcludeColourFromCounting) {
                    return true;
                }
            case CC_EXTRA_STUDENT_IN_DINING:
                if (chosenCard instanceof ExtraStudentInDining) {
                    return true;
                }
            case CC_FAKE_MN_MOVEMENT:
                if (chosenCard instanceof FakeMNMovement) {
                    return true;
                }
            case CC_FORBID_ISLAND:
                if (chosenCard instanceof ForbidIsland) {
                    return true;
                }
            case CC_PLACE_ONE_STUDENT:
                if (chosenCard instanceof PlaceOneStudent) {
                    return true;
                }
            case CC_REDUCE_COLOUR_IN_DINING:
                if (chosenCard instanceof ReduceColourInDining) {
                    return true;
                }
            case CC_TAKE_PROFESSOR_ON_EQUITY:
                if (chosenCard instanceof TakeProfessorOnEquity) {
                    return true;
                }
            case CC_TOWER_NO_VALUE:
                if (chosenCard instanceof TowerNoValue) {
                    return true;
                }
            case CC_TWO_EXTRA_ISLANDS:
                if (chosenCard instanceof TwoExtraIslands) {
                    return true;
                }
            case CC_TWO_EXTRA_POINTS:
                if (chosenCard instanceof TwoExtraPoints) {
                    return true;
                }
        }
        return false;
    }

    public void addBoardObserver() {
        for(ServerView s : serverViews) {
            this.board.addObserver(s);
            this.boardAdvanced.addObserver(s);
        }
    }
}

