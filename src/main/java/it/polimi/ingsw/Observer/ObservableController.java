package it.polimi.ingsw.Observer;

import it.polimi.ingsw.Controller.Exceptions.ControllerException;

import java.util.ArrayList;
import java.util.List;

public class ObservableController<T> {

    private final List<ObserverController<T>> observers = new ArrayList<>();

    public void addObserver(ObserverController<T> observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    public void removeObserve(ObserverController<T> observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    protected void notify(T message) throws ControllerException {
        synchronized (observers) {
            for(ObserverController<T> observer : observers){
                observer.update(message);
            }
        }
    }

}
