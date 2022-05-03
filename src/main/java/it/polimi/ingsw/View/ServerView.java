package it.polimi.ingsw.View;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.Exceptions.ControllerException;
import it.polimi.ingsw.Messages.INMessages.*;
import it.polimi.ingsw.Messages.OUTMessages.*;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;
import it.polimi.ingsw.Model.Board.SerializedBoardAdvanced;
import it.polimi.ingsw.Observer.ObservableController;
import it.polimi.ingsw.Observer.Observer;
import it.polimi.ingsw.Server.SocketClientConnectionCLI;
import it.polimi.ingsw.View.Exceptions.NoCharacterCardException;

/*
 This class observe the Model. (Observer<JSON> or Observer<CustomObject> ... )
 */
public class ServerView implements Observer<SerializedBoardAbstract> {

    private final SocketClientConnectionCLI socketClientConnectionCLI;
    private String playerNickname;
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
            String[] splitted = input.split(" ");
            switch (splitted[0]){
                case "createMatch":
                    this.serverView.setPlayerNickname(splitted[1]); //not a problem id error: I'll re-set it
                    return new MessageCreateMatch(splitted[1], splitted[2], Integer.parseInt(splitted[3]), Boolean.parseBoolean(splitted[4]), this.serverView);
                case "addPlayer":
                    this.serverView.setPlayerNickname(splitted[1]); //not a problem id error: I'll re-set it
                    return new MessageAddPlayer(splitted[1], splitted[2], this.serverView);
                case "assistantCard":
                    return new MessageAssistantCard(serverView.getPlayerNickname(), Integer.parseInt(splitted[2]),  Integer.parseInt(splitted[3]));
                case "studentHallToDiningRoom":
                    return new MessageStudentHallToDiningRoom(serverView.getPlayerNickname(), splitted[2]);
                case "studentToArchipelago":
                    return new MessageStudentToArchipelago(serverView.getPlayerNickname(), splitted[2], Integer.parseInt(splitted[3]));
                case "moveMotherNature":
                    return new MessageMoveMotherNature(serverView.getPlayerNickname(), Integer.parseInt(splitted[2]));
                case "studentCloudToSchool":
                    return new MessageStudentCloudToSchool(serverView.getPlayerNickname(), Integer.parseInt(splitted[2]));
                case "exchangeThreeStudents":
                    return new MessageCCExchangeThreeStudents(Integer.parseInt(splitted[1]), serverView.getPlayerNickname(), splitted[3], splitted[4], splitted[5], splitted[6], splitted[7], splitted[8]);
                case "exchangeTwoHallDining":
                    return new MessageCCExchangeTwoHallDining(Integer.parseInt(splitted[1]), serverView.getPlayerNickname(), splitted[3], splitted[4], splitted[5], splitted[6]);
                case "excludeColourFromCounting":
                    return new MessageCCExcludeColourFromCounting(Integer.parseInt(splitted[1]), serverView.getPlayerNickname(), splitted[3]);
                case "extraStudentInDining":
                    return new MessageCCExtraStudentInDining(Integer.parseInt(splitted[1]), serverView.getPlayerNickname(), splitted[3]);
                case "fakeMNMovement":
                    return new MessageCCFakeMNMovement(Integer.parseInt(splitted[1]), serverView.getPlayerNickname(), Integer.parseInt(splitted[3]));
                case "forbidIsland":
                    return new MessageCCForbidIsland(Integer.parseInt(splitted[1]), serverView.getPlayerNickname(), Integer.parseInt(splitted[3]));
                case "placeOneStudent":
                    return new MessageCCPlaceOneStudent(Integer.parseInt(splitted[1]), serverView.getPlayerNickname(), splitted[3], Integer.parseInt(splitted[4]));
                case "reduceColourInDining":
                    return new MessageCCReduceColourInDining(Integer.parseInt(splitted[1]), serverView.getPlayerNickname(), splitted[3]);
                case "takeProfessorOnEquity":
                    return new MessageCCTakeProfessorOnEquity(Integer.parseInt(splitted[1]), serverView.getPlayerNickname());
                case "towerNoValue":
                    return new MessageCCTowerNoValue(Integer.parseInt(splitted[1]), serverView.getPlayerNickname());
                case "twoExtraIslands":
                    return new MessageCCTwoExtraIslands(Integer.parseInt(splitted[1]), serverView.getPlayerNickname());
                case "twoExtraPoints":
                    return new MessageCCTwoExtraPoints(Integer.parseInt(splitted[1]), serverView.getPlayerNickname());
            }
            throw new NoCharacterCardException();
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
        if(message instanceof SerializedBoardAdvanced) {
            this.socketClientConnectionCLI.asyncSendModel(new SerializedBoardAdvanced(message.getArchipelagos(),
                    message.getClouds(), message.getMn(), message.getSchools(), ((SerializedBoardAdvanced) message).getColourToExclude(),
                    ((SerializedBoardAdvanced) message).getExtractedCards(), this.playerNickname));
        } else {
            this.socketClientConnectionCLI.asyncSendModel(new SerializedBoardAbstract(message.getArchipelagos(),
                    message.getClouds(), message.getMn(), message.getSchools(), this.playerNickname));
        }
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

    public OUTMessage askName() {
        return new MessageAskName();
    }

    public void manageControllerError() {
        this.socketClientConnectionCLI.asyncSend(new MessageControllerError());
    }
}
