package it.polimi.ingsw.Server;


import it.polimi.ingsw.Messages.INMessage.Message;
import it.polimi.ingsw.Messages.OUTMessages.OUTMessage;
import it.polimi.ingsw.Observer.Observable;

public abstract class ClientConnection extends Observable<Message> {

    abstract void closeConnection();

    abstract void asyncSend(OUTMessage message);
}
