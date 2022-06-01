package it.polimi.ingsw.View;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.Exceptions.ControllerException;
import it.polimi.ingsw.Messages.INMessages.*;
import it.polimi.ingsw.Messages.OUTMessages.*;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;
import it.polimi.ingsw.Model.Board.SerializedBoardAdvanced;
import it.polimi.ingsw.Observer.ObservableController;
import it.polimi.ingsw.Observer.Observer;
import it.polimi.ingsw.Server.SocketClientConnection;
import it.polimi.ingsw.View.Exceptions.NoCharacterCardException;

/*
 This class observe the Model. (Observer<JSON> or Observer<CustomObject> ... )
 */
public class ServerView implements Observer<SerializedBoardAbstract> {

    private final SocketClientConnection socketClientConnection;
    private String playerNickname;
    private final Controller controller;
    /*
        Inner class created to divide the flow in two:
            - client -> model modification (managed byt this inner class)
            - model modified -> client (managed by ServerView)
     */
    private static class ConnectionListener extends ObservableController<Message> implements Observer<String> {
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
                System.out.println("The message contains: " + messageInput);
                Message messageToController = this.parseStringToMessage(messageInput);
                notify(messageToController);
            } catch(NoCharacterCardException ex){
                //TODO: send error message to the client
                ex.printStackTrace();
            } catch (ControllerException e) {
                this.serverView.manageControllerError();
            }
        }

        public Message parseStringToMessage(String input) throws NoCharacterCardException {
            String[] split = input.split(" ");
            switch (split[0]){
                case "createMatch":
                    this.serverView.setPlayerNickname(split[1]); //not a problem id< error: I'll re-set it
                    return new MessageCreateMatch(split[1], split[2], Integer.parseInt(split[3]), Boolean.parseBoolean(split[4]), this.serverView);
                case "addPlayer":
                    this.serverView.setPlayerNickname(split[1]); //not a problem id error: I'll re-set it
                    return new MessageAddPlayer(split[1], split[2], this.serverView);
                case "assistantCard":
                    return new MessageAssistantCard(serverView.getPlayerNickname(), Integer.parseInt(split[1]),  Integer.parseInt(split[2]));
                case "studentHallToDiningRoom":
                    return new MessageStudentHallToDiningRoom(serverView.getPlayerNickname(), split[1]);
                case "studentToArchipelago":
                    return new MessageStudentToArchipelago(serverView.getPlayerNickname(), split[1], Integer.parseInt(split[2]));
                case "moveMotherNature":
                    return new MessageMoveMotherNature(serverView.getPlayerNickname(), Integer.parseInt(split[1]));
                case "studentCloudToSchool":
                    return new MessageStudentCloudToSchool(serverView.getPlayerNickname(), Integer.parseInt(split[1]));
                case "exchangeThreeStudents":
                    return new MessageCCExchangeThreeStudents(serverView.getPlayerNickname(), split[1], split[2], split[3], split[4], split[5], split[6]);
                case "exchangeTwoHallDining":
                    return new MessageCCExchangeTwoHallDining(serverView.getPlayerNickname(), split[1], split[2], split[3], split[4]);
                case "excludeColourFromCounting":
                    return new MessageCCExcludeColourFromCounting(serverView.getPlayerNickname(), split[1]);
                case "extraStudentInDining":
                    return new MessageCCExtraStudentInDining(serverView.getPlayerNickname(), split[1]);
                case "fakeMNMovement":
                    return new MessageCCFakeMNMovement(serverView.getPlayerNickname(), Integer.parseInt(split[1]));
                case "forbidIsland":
                    return new MessageCCForbidIsland(serverView.getPlayerNickname(), Integer.parseInt(split[1]));
                case "placeOneStudent":
                    return new MessageCCPlaceOneStudent(serverView.getPlayerNickname(), split[1], Integer.parseInt(split[2]));
                case "reduceColourInDining":
                    return new MessageCCReduceColourInDining(serverView.getPlayerNickname(), split[1]);
                case "takeProfessorOnEquity":
                    return new MessageCCTakeProfessorOnEquity(serverView.getPlayerNickname());
                case "towerNoValue":
                    return new MessageCCTowerNoValue(serverView.getPlayerNickname());
                case "twoExtraIslands":
                    return new MessageCCTwoExtraIslands(serverView.getPlayerNickname());
                case "twoExtraPoints":
                    return new MessageCCTwoExtraPoints(serverView.getPlayerNickname());
                case "Ping":
                    return new Ping(serverView.getPlayerNickname());
            }
            throw new NoCharacterCardException();
        }
    }

    public ServerView(SocketClientConnection connection, Controller controller) {
        ConnectionListener connectionListener = new ConnectionListener(this);
        connection.addObserver(connectionListener);
        this.controller = controller;
        connectionListener.addObserver(controller);
        this.socketClientConnection = connection;
    }

    //TODO: this should not be a Object message but instead a JSON or something, because it's received from the model itself
    @Override
    public void update(SerializedBoardAbstract message) {
        SerializedBoardAbstract finalMessage;
        if(message instanceof SerializedBoardAdvanced) {
            finalMessage = new SerializedBoardAdvanced(message.getArchipelagos(),
                    message.getClouds(), message.getMn(), message.getSchools(), ((SerializedBoardAdvanced) message).getColourToExclude(),
                    ((SerializedBoardAdvanced) message).getExtractedCards(), this.playerNickname);
        } else {
            finalMessage = new SerializedBoardAbstract(message.getArchipelagos(),
                    message.getClouds(), message.getMn(), message.getSchools(), this.playerNickname);
        }

        finalMessage.setCurrentState(this.controller.getPrecomputedState());
        finalMessage.setCurrentPlayer(this.controller.getPrecomputedPlayer());
        finalMessage.setNicknameWinner(this.controller.getNicknameWinner());
        finalMessage.setSitPlayers(this.controller.getSitPlayers());
        this.socketClientConnection.asyncSendModel(finalMessage);
    }

    protected String getPlayerNickname(){
        return this.playerNickname;
    }

    public void setPlayerNickname(String nickname) {
        this.playerNickname = nickname;
    }

    // Messages generated from the ServerView and not the controller
    public OUTMessage askCLIorGUI() {
        return new MessageCLIorGUI();
    }

    public OUTMessage askFirstPlayer( ) {
        return new MessageFirstPlayer();
    }

    public void manageControllerError() {
        this.socketClientConnection.asyncSend(new MessageControllerError());
    }
}
