package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Board.Board;
import it.polimi.ingsw.View.View;

import java.util.Observable;
import java.util.Observer;

public class Controller implements Observer {
    private Board model;
    private boolean boardAdvanced;
    private int numPlayers;
    private View view;

    public Controller(Board model, View view){
        this.model = model;
        this.view = view;
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
