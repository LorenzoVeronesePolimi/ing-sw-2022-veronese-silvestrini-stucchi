package it.polimi.ingsw.View;

import java.io.PrintStream;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class View extends Observable implements Runnable, Observer {
    private Scanner scanner;
    private PrintStream outputStream;
    private boolean done = false;

    public View(){
        scanner = new Scanner(System.in);
        outputStream = new PrintStream(System.out);
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    public void run(){
        /*
        * CONNECTING
        * First player connects
        * He chooses number of Players and modality -> notifyObservers
        * Controller: memorizes modality (he has to know it to choose which methods to use) and number of Players.
        * Waiting for other Players coming -> notifyObservers
        * Controller: memorizes all Players (he doesn't give them to the Model, until all  Players has come)
        * Model: all Players has come: initialize Board and starts the game [createBoard, initializeBoard, moveStudentsBagToSchool]
        * View: update.
        *
        * PLANNING PHASE
        * Model: fill Clouds [fillClouds]
        * Each Player choose and play one AssistantCard -> notifyObservers
        * Model: links each Player with its AssistantCard [playAssistantCard] ***
        *
        * ACTION PHASE
        * Current Player moves n Students from the Hall -> for each Player chosen notifyObservers
        * Model: update [moveStudentHallToDiningRoom, moveStudentSchoolToArchipelagos]
        * View: update
        * Move MotherNature -> notifyObservers
        * Model: update (conquer e merge) [tryToConquer]
        * Model: has he won? [checkGameEnd] ***
        * View: update
        * If won, the match ends otherwise next Player of next turn
        * */
    }
}
