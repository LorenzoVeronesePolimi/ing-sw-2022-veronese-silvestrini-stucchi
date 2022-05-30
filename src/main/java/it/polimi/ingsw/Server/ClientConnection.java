package it.polimi.ingsw.Server;


import it.polimi.ingsw.Messages.INMessages.Message;
import it.polimi.ingsw.Messages.OUTMessages.OUTMessage;
import it.polimi.ingsw.Observer.Observable;

/**
 * Abstract class that describes the structure of a ClientConnection class.
 */
public abstract class ClientConnection extends Observable<String> {
    protected boolean activeConnection = false;

    /**
     * Method called when closing a connection.
     */
    abstract void closeConnection();

    /**
     * Method called when sending a message from the server to the Client.
     * @param message message sent to the Client.
     */
    abstract void asyncSend(OUTMessage message);

    /**
     * Method that states whether the connection is active or not.
     * @return boolean for the activeConnection.
     */
    public boolean isActiveConnection() {
        return activeConnection;
    }
}
