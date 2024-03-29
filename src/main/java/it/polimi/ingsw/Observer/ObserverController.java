package it.polimi.ingsw.Observer;

import it.polimi.ingsw.Controller.Exceptions.ControllerException;

/**
 * class that implements the interface: observer of and object that notifies a message of type T
 * @param <T> type of the observed object
 */
public interface ObserverController<T> {

    /**
     * method that receives from the observer an object of type T, when notified
     * @param message message of type T notified by the observed object
     */
    void update(T message) throws ControllerException;
}
