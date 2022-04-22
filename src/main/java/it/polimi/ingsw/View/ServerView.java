package it.polimi.ingsw.View;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.Messages.Message;
import it.polimi.ingsw.OUTMessages.OUTMessage;
import it.polimi.ingsw.Observer.Observable;
import it.polimi.ingsw.Observer.Observer;
import it.polimi.ingsw.Server.ClientConnection;

/*
 This class observe the Model.
 */
public class ServerView implements Observer {
    private ClientConnection connection;

    /*
        Inner class created to divide the flow in two:
            - client -> model modification (managed byt this inner class)
            - model modified -> client (managed by ServerView)
     */
    private class ConnectionListener extends Observable<Message> implements Observer<Message> {
        /*
            This class observe the Connection, and it's observed by the Controller.
            It notifies the controller every time a message is received from the Connection.

            (Maybe here we can do some message checking or something IDK)
         */
        @Override
        public void update(Message message) {
            notify(message);
        }
    }

    public ServerView(ClientConnection connection, Controller controller) {
        this.connection = connection;
        ConnectionListener connectionListener = new ConnectionListener();
        connection.addObserver(connectionListener);
        connectionListener.addObserver(controller);
    }

    //TODO: this should not be a Object message but instead a JSON or something, because it's received from the model itself
    @Override
    public void update(Object message) {

    }

    // Messages generated from the ServerView and not the controller
    public OUTMessage CLIorGUI() {
        return new OUTMessage();
    }

    public OUTMessage manageFirstPlayer(ClientConnection cc) {
        return new OUTMessage();
    }

    public OUTMessage chooseName() {
        return new OUTMessage();
    }
}
