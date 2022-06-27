package it.polimi.ingsw.Observer;

import it.polimi.ingsw.Controller.Exceptions.ControllerException;

import java.util.ArrayList;
import java.util.List;

public class ObservableController<T> {

    private final List<ObserverController<T>> observers = new ArrayList<>();

    /**
     * method that adds an observer to the list of the observers of the observable object
     * @param observer observer
     */
    public void addObserver(ObserverController<T> observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * method that removes an observer to the list of the observers of the observable object
     * @param observer
     */
    public void removeObserve(ObserverController<T> observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    /**
     * method that sends a message of type T to all the observers
     * @param message message of type T
     */
    protected void notify(T message) throws ControllerException {
        synchronized (observers) {
            for(ObserverController<T> observer : observers){
                observer.update(message);
            }
        }
    }

}
