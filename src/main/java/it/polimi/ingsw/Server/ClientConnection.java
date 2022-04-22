package it.polimi.ingsw.Server;


import it.polimi.ingsw.Controller.Messages.Message;
import it.polimi.ingsw.OUTMessages.OUTMessage;
import it.polimi.ingsw.Observer.Observable;
import it.polimi.ingsw.Observer.Observer;
import it.polimi.ingsw.View.ServerView;

public abstract class ClientConnection extends Observable<Message> {

    abstract void closeConnection();

    abstract void asyncSend(OUTMessage message);
}
