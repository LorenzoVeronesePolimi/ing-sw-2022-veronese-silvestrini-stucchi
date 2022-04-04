package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Enumerations.MessageType;
import it.polimi.ingsw.Controller.Enumerations.State;
import it.polimi.ingsw.Controller.Exceptions.NoCorrespondingCharacterCardException;
import it.polimi.ingsw.Controller.Exceptions.NoPlayerException;
import it.polimi.ingsw.Controller.Messages.*;
import it.polimi.ingsw.Model.Board.BoardAbstract;
import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Board.BoardFactory;
import it.polimi.ingsw.Model.Cards.*;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Player;

import java.util.*;

// This is the main Controller: it coordinates all the others
public class Controller implements Observer {
    private int numPlayers;
    private boolean advanced;
    private BoardAbstract board;
    private BoardAdvanced boardAdvanced; //null if advanced=0
    private List<Player> players; //ordered

    private int currentPlayerIndex = 0;

    private ControllerInput controllerInput;
    private ControllerState controllerState;
    private ControllerIntegrity controllerIntegrity;

    private int numStudentsToMove;
    private int numStudentsToMoveCurrent;
    private static final int NUM_STUDENTS_TO_MOVE_TWO = 3;
    private static final int NUM_STUDENTS_TO_MOVE_THREE = 3;


    public Controller(){
        this.players = new ArrayList<>();
        this.controllerInput = new ControllerInput();
        this.controllerState = new ControllerState();
        this.controllerIntegrity = new ControllerIntegrity();
    }

    public Player getCurrentPlayer(){
        return this.players.get(this.currentPlayerIndex);
    }

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
    public void update(Observable o, Object arg) {
        if(!controllerInput.checkFormat(arg)){
            System.out.println("Invalid format");
            return;
        }

        Message message = (Message)arg;
        if(!controllerState.checkState(message.getType())){
            System.out.println("You can't do that now");
            return;
        }

        switch(message.getType()){
            case CREATE_MATCH: //TODO: manage GameFour
                if(message instanceof MessageCreateMatch &&
                        !this.manageCreateMatch((MessageCreateMatch)message)){
                    System.out.println("You can't create a match now");
                }

            case ADD_PLAYER:
                if(message instanceof MessageAddPlayer &&
                    !this.manageAddPlayer((MessageAddPlayer) message)) {
                    System.out.println("Impossible to add this player");
                }

            case ASSISTANT_CARD:
                if(message instanceof MessageAssistantCard &&
                        !this.manageAssistantCard((MessageAssistantCard) message)) {
                    System.out.println("Impossible to play this AssistantCard");
                }

            case STUDENT_HALL_TO_DINING_ROOM:
                if(message instanceof MessageStudentHallToDiningRoom &&
                        !this.manageStudentHallToDiningRoom((MessageStudentHallToDiningRoom) message)) {
                    System.out.println("You can't move a Student in that way");
                }

            case STUDENT_TO_ARCHIPELAGO:
                if(message instanceof MessageStudentToArchipelago &&
                        !this.manageStudentToArchipelago((MessageStudentToArchipelago) message)) {
                    System.out.println("You can't move a Student in that way");
                }

            case MOVE_MOTHER_NATURE:
                if(message instanceof MessageMoveMotherNature &&
                        !this.manageMoveMotherNature((MessageMoveMotherNature) message)) {
                    System.out.println("You can't move the Mother Nature in that way");
                }

            case STUDENT_CLOUD_TO_SCHOOL:
                if(message instanceof MessageStudentCloudToSchool &&
                        !this.manageStudentCloudToSchool((MessageStudentCloudToSchool) message)) {
                    System.out.println("You can't choose that Cloud");
                }

            case CC_EXCHANGE_THREE_STUDENTS:
                if(message instanceof MessageCCExchangeThreeStudents &&
                        !this.manageCCExchangeThreeStudents((MessageCCExchangeThreeStudents) message)) {
                    System.out.println("Wrong parameters");
                }

            case CC_EXCHANGE_TWO_HALL_DINING:
                if(message instanceof MessageCCExchangeTwoHallDining &&
                        !this.manageCCExchangeTwoHallDining((MessageCCExchangeTwoHallDining) message)) {
                    System.out.println("Wrong parameters");
                }

            case CC_EXCLUDE_COLOUR_FROM_COUNTING:
                if(message instanceof MessageCCExcludeColourFromCounting &&
                        !this.manageCCExcludeColourFromCounting((MessageCCExcludeColourFromCounting)message)){
                    System.out.println("Wrong parameters");
                }

            case CC_EXTRA_STUDENT_IN_DINING:
                if(message instanceof MessageCCExtraStudentInDining &&
                        !this.manageCCExtraStudentInDining((MessageCCExtraStudentInDining)message)){
                    System.out.println("Wrong parameters");
                }
            case CC_FAKE_MN_MOVEMENT:
                if(message instanceof MessageCCFakeMNMovement &&
                        !this.manageCCFakeMNMovement((MessageCCFakeMNMovement)message)){
                    System.out.println("Wrong parameters");
                }

            case CC_FORBID_ISLAND:
                if(message instanceof MessageCCForbidIsland &&
                        !this.manageCCForbidIsland((MessageCCForbidIsland)message)){
                    System.out.println("Wrong parameters");
                }

            case CC_PLACE_ONE_STUDENT:
                if(message instanceof MessageCCPlaceOneStudent &&
                        !this.manageCCPlaceOneStudent((MessageCCPlaceOneStudent)message)){
                    System.out.println("Wrong parameters");
                }

            case CC_REDUCE_COLOUR_IN_DINING:
                if(message instanceof MessageCCReduceColourInDining &&
                        !this.manageCCReduceColourInDining((MessageCCReduceColourInDining)message)){
                    System.out.println("Wrong parameters");
                }

            case CC_TOWER_NO_VALUE:
                if(message instanceof MessageCCTowerNoValue &&
                        !this.manageCCTowerNoValue((MessageCCTowerNoValue)message)){
                    System.out.println("Wrong parameters");
                }

            case CC_TWO_EXTRA_POINTS:
                if(message instanceof MessageCCTwoExtraPoints &&
                        !this.manageCCTwoExtraPoints((MessageCCTwoExtraPoints)message)){
                    System.out.println("Wrong parameters");
                }
        }

        //check if I have to make some automatic action (=>PIANIFICATION1)
        if(controllerState.getState() == State.PLANNING1){
            //TODO: surrounded with try catch just to remove errors. It needs to be checked before leaving it like that
            try {
                this.board.moveStudentBagToCloud();
            } catch (ExceededMaxStudentsCloudException | StudentNotFoundException e) {
                e.printStackTrace();
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

    private AbstractCharacterCard mapIndexToCharacterCard(MessageType type, int indexCard) throws NoCorrespondingCharacterCardException{
        AbstractCharacterCard chosenCard = this.boardAdvanced.getExtractedCards().get(indexCard);

        // is this card corresponding to the index chosen?
        switch(type){
            case CC_EXCHANGE_THREE_STUDENTS:
                if (chosenCard instanceof ExchangeThreeStudents){
                    return chosenCard;
                }
            case CC_EXCLUDE_COLOUR_FROM_COUNTING:
                if (chosenCard instanceof ExcludeColourFromCounting){
                    return chosenCard;
                }
            case CC_EXTRA_STUDENT_IN_DINING:
                if (chosenCard instanceof ExtraStudentInDining){
                    return chosenCard;
                }
            case CC_FAKE_MN_MOVEMENT:
                if (chosenCard instanceof FakeMNMovement){
                    return chosenCard;
                }
            case CC_FORBID_ISLAND:
                if (chosenCard instanceof ForbidIsland){
                    return chosenCard;
                }
            case CC_PLACE_ONE_STUDENT:
                if (chosenCard instanceof PlaceOneStudent){
                    return chosenCard;
                }
            case CC_TOWER_NO_VALUE:
                if (chosenCard instanceof TowerNoValue){
                    return chosenCard;
                }
            case CC_TWO_EXTRA_POINTS:
                if (chosenCard instanceof TwoExtraPoints){
                    return chosenCard;
                }
        }
        throw new NoCorrespondingCharacterCardException();
    }

    // checks if nickname is the current Player
    private boolean isCurrentPlayer(String nickname){
        try{
            Player player = this.mapStringToPlayer(nickname);
            if(this.players.get(currentPlayerIndex) == player){return true;}
        } catch(NoPlayerException ex){return true;}
        return false;
    }

    private void initMatch(){
        BoardFactory factory = new BoardFactory(this.players);
        this.board = factory.createBoard();

        if(this.advanced){
            //TODO: surrounded with try catch just to remove errors. It needs to be checked before leaving it like that
            try {
                this.boardAdvanced = new BoardAdvanced(this.board);
            } catch (ExceededMaxStudentsHallException | StudentNotFoundException | TowerNotFoundException | EmptyCaveauExcepion e) {
                e.printStackTrace();
            }
        }

        controllerIntegrity.setBoard(this.board);
        controllerIntegrity.setBoardAdvanced(this.boardAdvanced);
        controllerIntegrity.setAdvanced(this.advanced);

        //set number of Students to move at each ACTION1
        if(this.players.size() == 3){
            this.numStudentsToMove = NUM_STUDENTS_TO_MOVE_THREE;
            this.numStudentsToMoveCurrent = NUM_STUDENTS_TO_MOVE_THREE;
        } else{
            this.numStudentsToMove = NUM_STUDENTS_TO_MOVE_TWO;
            this.numStudentsToMoveCurrent = NUM_STUDENTS_TO_MOVE_TWO;
        }
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

    private boolean manageCreateMatch(MessageCreateMatch message){ //TODO: manage GameFour
        int numPlayers = message.getNumPlayers();
        PlayerColour colourFirstPlayer = mapStringToPlayerColour(message.getColourFirstPlayer());
        // no need to control the boolean "advanced"

        if(!controllerIntegrity.checkCreateMatch(numPlayers)){
            return false;
        }

        Player player = new Player(message.getNicknameFirstPlayer(), colourFirstPlayer);
        this.players.add(player);

        controllerState.setState(State.WAITING_PLAYERS);

        return true;
    }

    private boolean manageAddPlayer(MessageAddPlayer message){ // TODO: manage GameFour
        String nickname = message.getNickname();
        PlayerColour colour = mapStringToPlayerColour(message.getColour());
        // He can't have the name of an existing Player
        for(Player p : this.players){
            if(p.getNickname().equals(nickname)){return false;}
        }

        // No integrity to check

        Player player = new Player(nickname, colour);
        this.players.add(player);

        if(this.players.size() == numPlayers){ // The requested number of players has been reached: let's go on
            this.initMatch();
            controllerState.setState(State.PLANNING1);
        }

        return true;
    }

    private boolean manageAssistantCard(MessageAssistantCard message){
        String nicknamePlayer = message.getNicknamePlayer();
        int turnPriority = message.getTurnPriority();

        // Is him the currentPlayer? Can he use that AssistantCard?
        if(isCurrentPlayer(nicknamePlayer)){return false;}
        controllerIntegrity.checkAssistantCard(this.players, this.currentPlayerIndex, this.players.get(this.currentPlayerIndex), turnPriority);

        // Remove the card from his hand
        try{
            board.useAssistantCard(this.players.get(this.currentPlayerIndex), turnPriority);
        } catch(AssistantCardAlreadyPlayedTurnException | NoAssistantCardException ex){return false;} // card already used or no AssistantCard present

        // Go on within the turn
        this.currentPlayerIndex++;

        if(this.currentPlayerIndex == this.players.size()-1){ //last player: all players has played their AssistantCard. No I can set the order
            this.changeTurnOrder(); // reset the order of the Players according to the values of the AssistantCards
            this.currentPlayerIndex = 0; // the new turn will start
        }
        return true;
    }

    private boolean manageStudentHallToDiningRoom(MessageStudentHallToDiningRoom message){
        String nicknamePlayer = message.getNicknamePlayer();
        SPColour studentColour = mapStringToSPColour(message.getColour());

        if(isCurrentPlayer(nicknamePlayer)){return false;}

        if(controllerIntegrity.checkStudentHallToDiningRoom(this.players.get(this.currentPlayerIndex), studentColour)){
            if(this.advanced){
                try {
                    boardAdvanced.moveStudentHallToDiningRoom(this.players.get(this.currentPlayerIndex), studentColour);
                } catch (StudentNotFoundException | ExceededMaxStudentsDiningRoomException | EmptyCaveauExcepion |
                        ProfessorNotFoundException | NoProfessorBagException e) {
                    return false;
                }
            } else{
                try {
                    board.moveStudentHallToDiningRoom(this.players.get(this.currentPlayerIndex), studentColour);
                } catch (StudentNotFoundException | ExceededMaxStudentsDiningRoomException |
                        ProfessorNotFoundException | NoProfessorBagException e) {
                    return false;
                }
            }
            this.numStudentsToMoveCurrent--;
            if(this.numStudentsToMoveCurrent == 0 || // all possible Students moved
                    this.board.getPlayerSchool(this.players.get(this.currentPlayerIndex)).getStudentsHall().size() == 0){ // no Students remained
                this.numStudentsToMoveCurrent = this.numStudentsToMove;
                controllerState.setState(State.ACTION2);
            }
            return true;
        }
        return false;
    }

    private boolean manageStudentToArchipelago(MessageStudentToArchipelago message){
        String nicknamePlayer = message.getNicknamePlayer();
        SPColour studentColour = mapStringToSPColour(message.getColour());
        int destinationArchipelagoIndex = message.getDestArchipelagoIndex();

        if(isCurrentPlayer(nicknamePlayer)){return false;}

        if(controllerIntegrity.checkStudentToArchipelago(this.players.get(this.currentPlayerIndex), studentColour, destinationArchipelagoIndex)){
            try {
                board.moveStudentSchoolToArchipelagos(this.players.get(this.currentPlayerIndex), studentColour, destinationArchipelagoIndex);
            } catch (StudentNotFoundException e) {
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean manageMoveMotherNature(MessageMoveMotherNature message){
        String nicknamePlayer = message.getNicknamePlayer();
        int moves = message.getMoves();

        if(isCurrentPlayer(nicknamePlayer)){return false;}

        if(controllerIntegrity.checkMoveMotherNature(this.players.get(this.currentPlayerIndex), moves)){
            board.moveMotherNature(moves);
            try {
                board.tryToConquer(this.players.get(this.currentPlayerIndex));
            } catch (InvalidTowerNumberException | AnotherTowerException | ExceededMaxTowersException | TowerNotFoundException e) {
                return false;
            }
            controllerState.setState(State.ACTION3);
            return true;
        }
        return false;
    }

    private boolean manageStudentCloudToSchool(MessageStudentCloudToSchool message){
        String nicknamePlayer = message.getNicknamePlayer();
        int indexCloud = message.getIndexCloud();

        if(isCurrentPlayer(nicknamePlayer)){return false;}

        if(controllerIntegrity.checkStudentCloudToSchool(this.players.get(this.currentPlayerIndex), indexCloud)){
            try{
                board.moveStudentCloudToSchool(this.players.get(this.currentPlayerIndex), indexCloud);
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
    private boolean manageCCExchangeThreeStudents(MessageCCExchangeThreeStudents message){
        int indexCard = message.getIndexCard();
        String nicknamePlayer = message.getNicknamePlayer();
        List<SPColour> coloursCard = this.mapListStringToColour(message.getColoursCard());
        List<SPColour> coloursHall = this.mapListStringToColour(message.getColoursHall());

        if(isCurrentPlayer(nicknamePlayer)){return false;}

        try{
            ExchangeThreeStudents chosenCard = (ExchangeThreeStudents)this.mapIndexToCharacterCard(MessageType.CC_EXCHANGE_THREE_STUDENTS, indexCard);
            if(controllerIntegrity.checkCCExchangeThreeStudents(this.players.get(this.currentPlayerIndex), coloursCard, coloursHall, chosenCard)){
                chosenCard.useEffect(this.players.get(this.currentPlayerIndex), coloursHall, coloursCard);

                return true;
            }
        } catch(NoCorrespondingCharacterCardException |
                WrongNumberOfStudentsTransferExcpetion |
                StudentNotFoundException |
                ExceededMaxStudentsHallException ex){return false;}

        return false;
    }

    private boolean manageCCExchangeTwoHallDining(MessageCCExchangeTwoHallDining message){
        int indexCard = message.getIndexCard();
        String nicknamePlayer = message.getNicknamePlayer();
        List<SPColour> coloursHall = this.mapListStringToColour(message.getColoursHall());
        List<SPColour> coloursDiningRoom = this.mapListStringToColour(message.getColoursDiningRoom());

        if(isCurrentPlayer(nicknamePlayer)){return false;}

        try {
            ExchangeTwoHallDining chosenCard = (ExchangeTwoHallDining)this.mapIndexToCharacterCard(MessageType.CC_EXCHANGE_THREE_STUDENTS, indexCard);
            if(controllerIntegrity.checkCCExchangeTwoHallDining(this.players.get(this.currentPlayerIndex), coloursHall, coloursDiningRoom, chosenCard)){
                chosenCard.useEffect(this.players.get(this.currentPlayerIndex), coloursHall, coloursDiningRoom);

                return true;
            }
        } catch (NoCorrespondingCharacterCardException |
                WrongNumberOfStudentsTransferExcpetion |
                StudentNotFoundException |
                ExceededMaxStudentsHallException |
                ExceededMaxStudentsDiningRoomException e) {return false;}

        return false;
    }

    private boolean manageCCExcludeColourFromCounting(MessageCCExcludeColourFromCounting message){
        int indexCard = message.getIndexCard();
        String nicknamePlayer = message.getNicknamePlayer();

        if(isCurrentPlayer(nicknamePlayer)){return false;}

        if(controllerIntegrity.checkCCGeneric()){
            try {
                ExcludeColourFromCounting chosenCard = (ExcludeColourFromCounting)this.mapIndexToCharacterCard(MessageType.CC_EXCLUDE_COLOUR_FROM_COUNTING, indexCard);
                chosenCard.useEffect(this.players.get(this.currentPlayerIndex), this.mapStringToSPColour(message.getColourToExclude()));

                return true;
            } catch (NoCorrespondingCharacterCardException |
                    InvalidTowerNumberException |
                    AnotherTowerException |
                    ExceededMaxTowersException |
                    TowerNotFoundException e) {
                return false;
            }
        }

        return false;
    }

    private boolean manageCCExtraStudentInDining(MessageCCExtraStudentInDining message){
        int indexCard = message.getIndexCard();
        String nicknamePlayer = message.getNicknamePlayer();
        SPColour colourToMove = mapStringToSPColour(message.getColourToMove());

        if(isCurrentPlayer(nicknamePlayer)){return false;}

        if(controllerIntegrity.checkCCGeneric()){
            try {
                ExtraStudentInDining chosenCard = (ExtraStudentInDining)this.mapIndexToCharacterCard(MessageType.CC_EXTRA_STUDENT_IN_DINING, indexCard);
                chosenCard.useEffect(this.players.get(this.currentPlayerIndex), colourToMove);

                return true;
            } catch (NoCorrespondingCharacterCardException |
                    ExceededMaxStudentsDiningRoomException |
                    StudentNotFoundException e) {
                return false;
            }
        }

        return false;
    }

    private boolean manageCCFakeMNMovement(MessageCCFakeMNMovement message){
        int indexCard = message.getIndexCard();
        String nicknamePlayer = message.getNicknamePlayer();
        int fakeMNPosition = message.getFakeMNPosition();

        if(isCurrentPlayer(nicknamePlayer)){return false;}

        if(controllerIntegrity.checkCCFakeMNMovement(fakeMNPosition)){
            try {
                FakeMNMovement chosenCard = (FakeMNMovement)this.mapIndexToCharacterCard(MessageType.CC_FAKE_MN_MOVEMENT, indexCard);
                chosenCard.useEffect(this.players.get(currentPlayerIndex), fakeMNPosition);

                return true;
            } catch (NoCorrespondingCharacterCardException |
                    TowerNotFoundException |
                    InvalidTowerNumberException |
                    AnotherTowerException |
                    ExceededMaxTowersException e) {
                return false;
            }
        }

        return false;
    }

    private boolean manageCCForbidIsland(MessageCCForbidIsland message){
        int indexCard = message.getIndexCard();
        String nicknamePlayer = message.getNicknamePlayer();
        int archipelagoIndexToForbid = message.getArchipelagoIndexToForbid();

        if(isCurrentPlayer(nicknamePlayer)){return false;}

        if(controllerIntegrity.checkCCForbidIsland(archipelagoIndexToForbid)){
            try {
                ForbidIsland chosenCard = (ForbidIsland)this.mapIndexToCharacterCard(MessageType.CC_FORBID_ISLAND, indexCard);
                chosenCard.useEffect(archipelagoIndexToForbid);

                return true;
            } catch (NoCorrespondingCharacterCardException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean manageCCPlaceOneStudent(MessageCCPlaceOneStudent message){
        int indexCard = message.getIndexCard();
        String nicknamePlayer = message.getNicknamePlayer();
        SPColour colourToMove = mapStringToSPColour(message.getColourToMove());
        int archipelagoIndexDestination = message.getArchipelagoIndexDest();

        if(isCurrentPlayer(nicknamePlayer)){return false;}

        try {
            PlaceOneStudent chosenCard = (PlaceOneStudent)this.mapIndexToCharacterCard(MessageType.CC_PLACE_ONE_STUDENT, indexCard);
            if(controllerIntegrity.checkCCPlaceOneStudent(colourToMove, archipelagoIndexDestination, chosenCard)){
                chosenCard.useEffect(colourToMove, archipelagoIndexDestination);

                return true;
            }
        } catch (NoCorrespondingCharacterCardException |
                StudentNotFoundException e) {
            return false;
        }

        return false;
    }

    private boolean manageCCReduceColourInDining(MessageCCReduceColourInDining message){
        int indexCard = message.getIndexCard();
        String nicknamePlayer = message.getNicknamePlayer();
        SPColour colourToReduce = mapStringToSPColour(message.getColourToReduce());

        if(isCurrentPlayer(nicknamePlayer)){return false;}

        try {
            if(controllerIntegrity.checkCCGeneric()) {
                ReduceColourInDining chosenCard = (ReduceColourInDining) this.mapIndexToCharacterCard(MessageType.CC_REDUCE_COLOUR_IN_DINING, indexCard);
                chosenCard.useEffect(colourToReduce);

                return true;
            }
        } catch (NoCorrespondingCharacterCardException |
                StudentNotFoundException e) {
            return false;
        }

        return false;
    }

    private boolean manageCCTowerNoValue(MessageCCTowerNoValue message){
        int indexCard = message.getIndexCard();
        String nicknamePlayer = message.getNicknamePlayer();

        if(isCurrentPlayer(nicknamePlayer)){return false;}

        try {
            if(controllerIntegrity.checkCCGeneric()){
                TowerNoValue chosenCard = (TowerNoValue)this.mapIndexToCharacterCard(MessageType.CC_TOWER_NO_VALUE, indexCard);
                chosenCard.useEffect(this.players.get(this.currentPlayerIndex));

                return true;
            }
        } catch (NoCorrespondingCharacterCardException |
                InvalidTowerNumberException |
                AnotherTowerException |
                ExceededMaxTowersException |
                TowerNotFoundException e) {
            return false;
        }

        return false;
    }

    private boolean manageCCTwoExtraPoints(MessageCCTwoExtraPoints message){
        int indexCard = message.getIndexCard();
        String nicknamePlayer = message.getNicknamePlayer();

        if(isCurrentPlayer(nicknamePlayer)){return false;}

        try {
            if(controllerIntegrity.checkCCGeneric()){
                TwoExtraPoints chosenCard = (TwoExtraPoints)this.mapIndexToCharacterCard(MessageType.CC_TWO_EXTRA_POINTS, indexCard);
                chosenCard.useEffect(this.players.get(this.currentPlayerIndex));

                return true;
            }
        } catch (NoCorrespondingCharacterCardException |
                TowerNotFoundException |
                InvalidTowerNumberException |
                AnotherTowerException |
                ExceededMaxTowersException e) {
            return false;
        }

        return false;
    }

    private boolean manageCCTakeProfessorOnEquity(MessageCCTakeProfessorOnEquity message){
        int indexCard = message.getIndexCard();
        String nicknamePlayer = message.getNicknamePlayer();

        if(isCurrentPlayer(nicknamePlayer)){return false;}

        try {
            if(controllerIntegrity.checkCCGeneric()){
                TakeProfessorOnEquity chosenCard = (TakeProfessorOnEquity)this.mapIndexToCharacterCard(MessageType.CC_TAKE_PROFESSOR_ON_EQUITY, indexCard);
                chosenCard.useEffect(this.players.get(currentPlayerIndex));

                return true;
            }
        } catch (NoCorrespondingCharacterCardException |
                TowerNotFoundException |
                InvalidTowerNumberException |
                AnotherTowerException |
                ProfessorNotFoundException |
                NoProfessorBagException |
                ExceededMaxTowersException e) {
            return false;
        }

        return false;
    }

    private boolean manageTwoExtraIslands(MessageCCTwoExtraIslands message){
        int indexCard = message.getIndexCard();
        String nicknamePlayer = message.getNicknamePlayer();

        if(isCurrentPlayer(nicknamePlayer)){return false;}

        if(controllerIntegrity.checkCCGeneric()){
            try {
                TwoExtraIslands chosenCard = (TwoExtraIslands)this.mapIndexToCharacterCard(MessageType.CC_TWO_EXTRA_ISLANDS, indexCard);
                //TODO after TwoExtraIslands correction: chosenCard.useEffect(this.players.get(currentPlayerIndex));

                return true;
            } catch (NoCorrespondingCharacterCardException e) {
                return false;
            }
        }

        return false;
    }
}

