package it.polimi.ingsw.Observer;

import it.polimi.ingsw.Controller.Exceptions.ControllerException;

public interface ObserverController<T> {

    void update(T message) throws ControllerException;
}
