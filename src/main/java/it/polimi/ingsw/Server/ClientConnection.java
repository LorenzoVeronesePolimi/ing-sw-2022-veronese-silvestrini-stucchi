package it.polimi.ingsw.Server;


import it.polimi.ingsw.Messages.OUTMessages.OUTMessage;
import it.polimi.ingsw.Observer.Observable;

public abstract class ClientConnection extends Observable<String> {
    protected boolean activeConnection = false;

    abstract void closeConnection();

    abstract void asyncSend(OUTMessage message);

    public boolean isActiveConnection() {
        return activeConnection;
    }
}
