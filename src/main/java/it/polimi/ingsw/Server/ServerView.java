package it.polimi.ingsw.Server;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.Exceptions.ControllerException;
import it.polimi.ingsw.Messages.INMessages.*;
import it.polimi.ingsw.Messages.OUTMessages.*;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;
import it.polimi.ingsw.Model.Board.SerializedBoardAdvanced;
import it.polimi.ingsw.Observer.ObservableController;
import it.polimi.ingsw.Observer.Observer;
import it.polimi.ingsw.View.Exceptions.NoCharacterCardException;

/**
 * This class represents the communication channel between server and player. When a message needs to be sent to a player it passes here.
 * This class observer the model (Board classes).
 */
public class ServerView implements Observer<SerializedBoardAbstract> {

    private final SocketClientConnection socketClientConnection;
    private String playerNickname;
    private final Controller controller;

    /**
     * @param connection SocketClientConnection of the client.
     * @param controller Controller of the game.
     */
    public ServerView(SocketClientConnection connection, Controller controller) {
        ConnectionListener connectionListener = new ConnectionListener(this);
        connection.addObserver(connectionListener);
        this.controller = controller;
        connectionListener.addObserver(controller);
        this.socketClientConnection = connection;
    }

    /*
        Inner class created to divide the flow in two:
            - client -> model modification (managed byt this inner class)
            - model modified -> client (managed by ServerView)
     */
    /**
     * This class represents the communication channel between player and Server. When a message needs to be sent to the server it passes here,
     * coming from SocketClientConnection.
     * This class observes the SocketClientConnection and it's observed by the Controller.
     */
    private static class ConnectionListener extends ObservableController<Message> implements Observer<String> {
        private final ServerView serverView;

        /**
         * Constructor that initializ the serverView attribute.
         * @param serverView corresponding player-server Server view.
         */
        private ConnectionListener(ServerView serverView) {
            this.serverView = serverView;
        }
        /*
            This class observe the Connection, and it's observed by the Controller.
            It notifies the controller every time a message is received from the Connection.

            (Maybe here we can do some message checking or something IDK)
         */

        /**
         * Method called from SocketClientConnection when a message is received from the client.
         * @param messageInput String message received from the client.
         */
        @Override
        public void update(String messageInput) {
            //Parsing of messages (from String to Message)
            try{
                // log
                System.out.println("[ServerView, update]: The message contains: " + this.serverView.playerNickname + " " + messageInput);

                // correct message adding player nickname
                Message messageToController = this.parseStringToMessage(messageInput);

                // notify the controller
                notify(messageToController);
            } catch(NoCharacterCardException ex){
                //TODO: send error message to the client (edit: still needed?)
                ex.printStackTrace();
            } catch (ControllerException e) {
                this.serverView.manageControllerError();
            }
        }

        /**
         * This method parses the String message received from the client in order to create Message object that will
         * be handled by the Controller.
         * @param input String message from the Client.
         * @return  Correct Message class for the type of received message.
         * @throws NoCharacterCardException
         */
        // TODO: NoCharacterCardException needed?
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
                    return new PingMessage(serverView.getPlayerNickname());
            }
            throw new NoCharacterCardException();
        }
    }

    /**
     * Method called by the model when performing the update of the changed status to the players.
     * @param message Serializable object that needs to be sent to the client.
     */
    @Override
    public void update(SerializedBoardAbstract message) {
        SerializedBoardAbstract finalMessage;
        // add the player nickname to the message
        if(message instanceof SerializedBoardAdvanced) {
            finalMessage = new SerializedBoardAdvanced(message.getArchipelagos(),
                    message.getClouds(), message.getMn(), message.getSchools(), ((SerializedBoardAdvanced) message).getColourToExclude(),
                    ((SerializedBoardAdvanced) message).getExtractedCards(), this.playerNickname);
        } else {
            finalMessage = new SerializedBoardAbstract(message.getArchipelagos(),
                    message.getClouds(), message.getMn(), message.getSchools(), this.playerNickname);
        }

        // add the precomputed parameters in order to let the client side decide who needs to make the next move
        finalMessage.setCurrentState(this.controller.getPrecomputedState());
        finalMessage.setCurrentPlayer(this.controller.getPrecomputedPlayer());
        finalMessage.setNicknameWinner(this.controller.getNicknameWinner());
        finalMessage.setSitPlayers(this.controller.getSitPlayers());

        // send the Model to the client
        this.socketClientConnection.asyncSendModel(finalMessage);
    }

    /**
     * @return current value of player nickname.
     */
    protected String getPlayerNickname(){
        return this.playerNickname;
    }

    /**
     * @param nickname next value of player nickname.
     */
    public void setPlayerNickname(String nickname) {
        this.playerNickname = nickname;
    }

    /**
     * Called when we want to send to the player this specific message asking what view version does he want.
     * @return the correct type of message that needs to be sent.
     */
    // Messages generated from the ServerView and not the controller
    public OUTMessage askCLIorGUI() {
        return new MessageCLIorGUI();
    }

    /**
     * Called when we want to send to the player this specific message asking match info.
     * @return the correct type of message that needs to be sent.
     */
    public OUTMessage askFirstPlayer( ) {
        return new MessageFirstPlayer();
    }

    /**
     * When an error occurred we generate the correct type of error message and send it.
     */
    public void manageControllerError() {
        this.socketClientConnection.asyncSend(new MessageControllerError());
    }
}
