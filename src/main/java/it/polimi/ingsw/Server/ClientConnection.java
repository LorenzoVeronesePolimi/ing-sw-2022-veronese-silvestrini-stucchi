package it.polimi.ingsw.Server;


import it.polimi.ingsw.Messages.OUTMessages.OUTMessage;
import it.polimi.ingsw.Observer.Observable;

public abstract class ClientConnection extends Observable<String> {

    abstract void closeConnection();

    abstract void asyncSend(OUTMessage message);
}
