package it.polimi.ingsw.Observer;

import it.polimi.ingsw.Controller.Exceptions.ControllerException;

import java.util.ArrayList;
import java.util.List;

/**
 * class that implements an object that can be observed
 * @param <T> type of the message that is notified by the observable
 */
public class Observable<T> {

    private final List<Observer<T>> observers = new ArrayList<>();

    /**
     * method that adds an observer to the list of the observers of the observable object
     * @param observer observer
     */
    public void addObserver(Observer<T> observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * method that removes an observer to the list of the observers of the observable object
     * @param observer
     */
    public void removeObserve(Observer<T> observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    /**
     * method that sends a message of type T to all the observers
     * @param message message of type T
     */
    protected void notify(T message) {
        synchronized (observers) {
            for(Observer<T> observer : observers){
                observer.update(message);
            }
        }
    }

}
