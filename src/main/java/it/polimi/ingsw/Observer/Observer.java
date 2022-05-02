package it.polimi.ingsw.Observer;

import it.polimi.ingsw.Controller.Exceptions.ControllerException;

public interface Observer<T> {

    void update(T message);
}
