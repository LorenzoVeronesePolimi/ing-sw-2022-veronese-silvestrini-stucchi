package it.polimi.ingsw.View;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Messages.Enumerations.INMessageType;
import it.polimi.ingsw.Messages.Enumerations.OUTMessageType;
import it.polimi.ingsw.Messages.INMessage.*;
import it.polimi.ingsw.Messages.OUTMessages.OUTMessage;
import it.polimi.ingsw.Messages.OUTMessages.OUTMessageInfo;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;
import it.polimi.ingsw.Model.Board.SerializedBoardAdvanced;
import it.polimi.ingsw.Observer.Observable;
import it.polimi.ingsw.Observer.Observer;
import it.polimi.ingsw.Server.ClientConnection;
import it.polimi.ingsw.Server.SocketClientConnectionCLI;
import it.polimi.ingsw.View.Exceptions.NoCharacterCardException;

/*
 This class observe the Model. (Observer<JSON> or Observer<CustomObject> ... )
 */
public class ServerView implements Observer<SerializedBoardAbstract> {

    private SocketClientConnectionCLI socketClientConnectionCLI;
    private String playerNickname;
    /*
        Inner class created to divide the flow in two:
            - client -> model modification (managed byt this inner class)
            - model modified -> client (managed by ServerView)
     */
    private static class ConnectionListener extends Observable<Message> implements Observer<String> {
        private final ServerView serverView;

        private ConnectionListener(ServerView serverView) {
            this.serverView = serverView;
        }


        /*
            This class observe the Connection, and it's observed by the Controller.
            It notifies the controller every time a message is received from the Connection.

            (Maybe here we can do some message checking or something IDK)
         */
        @Override
        public void update(String messageInput) {
            //Parsing of messages (from String to Message)
            try{
                Message messageToController = this.serverView.parseStringToMessage(messageInput);
                notify(messageToController);
            } catch(NoCharacterCardException ex){
                //TODO: send message to the client
                ex.printStackTrace();
            }
        }
    }

    public ServerView(SocketClientConnectionCLI connection, Controller controller) {
        ConnectionListener connectionListener = new ConnectionListener(this);
        connection.addObserver(connectionListener);
        connectionListener.addObserver(controller);
        this.socketClientConnectionCLI = connection;
    }

    //TODO: this should not be a Object message but instead a JSON or something, because it's received from the model itself
    @Override
    public void update(SerializedBoardAbstract message) {
        if(message instanceof SerializedBoardAbstract) {
            this.socketClientConnectionCLI.asyncSendModel(new SerializedBoardAbstract(message.getArchipelagos(),
                    message.getClouds(), message.getMn(), message.getSchools(), this.playerNickname));
        }

        if(message instanceof SerializedBoardAdvanced) {
            this.socketClientConnectionCLI.asyncSendModel(new SerializedBoardAdvanced(message.getArchipelagos(),
                    message.getClouds(), message.getMn(), message.getSchools(), ((SerializedBoardAdvanced) message).getColourToExclude(),
                    ((SerializedBoardAdvanced) message).getExtractedCards(), this.playerNickname));
        }
    }

    // Messages generated from the ServerView and not the controller
    public OUTMessage CLIorGUI() {
        return new OUTMessageInfo("Select between CLI[0] or GUI[1]:", OUTMessageType.ASK_CLI_GUI);
    }

    public OUTMessage manageFirstPlayer(ClientConnection cc) {
        return new OUTMessageInfo("Select the number of players and the modality.", OUTMessageType.ASK_FIRST_PLAYER);
    }

    public OUTMessage chooseName() {
        return new OUTMessageInfo("What's your name?", OUTMessageType.ASK_NICKNAME);
    }

    public void setPlayerNickname(String nickname) {
        this.playerNickname = nickname;
    }

    public Message parseStringToMessage(String input) throws NoCharacterCardException {
        String[] splitted = input.split(" ");
        switch (splitted[0]){
            case "createMatch":
                return new MessageCreateMatch(splitted[1], splitted[2], Integer.parseInt(splitted[3]), Boolean.parseBoolean(splitted[4]), this);
            case "addPlayer":
                return new MessageAddPlayer(splitted[1], splitted[2], this);
            case "assistantCard":
                return new MessageAssistantCard(splitted[1], Integer.parseInt(splitted[2]),  Integer.parseInt(splitted[3]));
            case "studentHallToDiningRoom":
                return new MessageStudentHallToDiningRoom(splitted[1], splitted[2]);
            case "studentToArchipelago":
                return new MessageStudentToArchipelago(splitted[1], splitted[2], Integer.parseInt(splitted[3]));
            case "moveMotherNature":
                return new MessageMoveMotherNature(splitted[1], Integer.parseInt(splitted[2]));
            case "studentCloudToSchool":
                return new MessageStudentCloudToSchool(splitted[1], Integer.parseInt(splitted[2]));
            case "exchangeThreeStudents":
                return new MessageCCExchangeThreeStudents(Integer.parseInt(splitted[1]), splitted[2], splitted[3], splitted[4], splitted[5], splitted[6], splitted[7], splitted[8]);
            case "exchangeTwoHallDining":
                return new MessageCCExchangeTwoHallDining(Integer.parseInt(splitted[1]), splitted[2], splitted[3], splitted[4], splitted[5], splitted[6]);
            case "excludeColourFromCounting":
                return new MessageCCExcludeColourFromCounting(Integer.parseInt(splitted[1]), splitted[2], splitted[3]);
            case "extraStudentInDining":
                return new MessageCCExtraStudentInDining(Integer.parseInt(splitted[1]), splitted[2], splitted[3]);
            case "fakeMNMovement":
                return new MessageCCFakeMNMovement(Integer.parseInt(splitted[1]), splitted[2], Integer.parseInt(splitted[3]));
            case "forbidIsland":
                return new MessageCCForbidIsland(Integer.parseInt(splitted[1]), splitted[2], Integer.parseInt(splitted[3]));
            case "placeOneStudent":
                return new MessageCCPlaceOneStudent(Integer.parseInt(splitted[1]), splitted[2], splitted[3], Integer.parseInt(splitted[4]));
            case "reduceColourInDining":
                return new MessageCCReduceColourInDining(Integer.parseInt(splitted[1]), splitted[2], splitted[3]);
            case "takeProfessorOnEquity":
                return new MessageCCTakeProfessorOnEquity(Integer.parseInt(splitted[1]), splitted[2]);
            case "towerNoValue":
                return new MessageCCTowerNoValue(Integer.parseInt(splitted[1]), splitted[2]);
            case "twoExtraIslands":
                return new MessageCCTwoExtraIslands(Integer.parseInt(splitted[1]), splitted[2]);
            case "twoExtraPoints":
                return new MessageCCTwoExtraPoints(Integer.parseInt(splitted[1]), splitted[2]);
        }
        throw new NoCharacterCardException();
    }
}
