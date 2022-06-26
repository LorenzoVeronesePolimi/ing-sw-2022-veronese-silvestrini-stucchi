package it.polimi.ingsw.View;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Messages.OUTMessages.OUTMessage;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;

import java.util.List;
import java.util.Scanner;

/**
 * abstract class of the client view: both CLI and GUI views will have each one of these methods
 */
public abstract class ClientView {
    protected Client client;
    protected Scanner input;
    private boolean errorStatus = false;

    /**
     * default constructor
     */
    public ClientView() {}

    /**
     * constructor of a view of a given client
     * @param client
     */
    public ClientView(Client client) {
        this.client = client;
        this.input = new Scanner(System.in);
    }

    /**
     * method that says is a view is in an error status
     * @return true if it is in an error status, false otherwise
     */
    public synchronized boolean isErrorStatus() {
        return errorStatus;
    }

    /**
     * setter of the error status (true if in error, false otherwise)
     * @param errorStatus status to be set
     */
    public void setErrorStatus(boolean errorStatus) {
        this.errorStatus = errorStatus;
    }

    /**
     * method that prints an error message
     * @param err error to be printed
     */
    public abstract void printErrorMessage(String err);

    /**
     * method that ends (closes) a view
     */
    public abstract void endView();

    /**
     * method that disconnects a client
     */
    public abstract void clientDisconnectionEnd();

    /**
     * method that asks a client if he wants to reconnect to the server, after the disconnection of another client from the game
     */
    public abstract void askReconnect();

    /**
     * method that asks a client if he wants to play through a CLI or GUI view
     */
    public abstract void askCLIorGUI();

    /**
     * method that asks a client which nickname and colour (among the available one) he wants to use
     * @param list list of available colours
     * @param numPlayer number of players of the game
     */
    public abstract void askNickName(List<PlayerColour> list, int numPlayer);

    /**
     * method that asks the first player who connects to the server his nickname and colour, how many players we want the game to contain,
     * and the game mode (advanced or standard)
     */
    public abstract void askFirstPlayerInfo();

    /**
     * method that prints the board information every time something is updated in the model board
     * @param serializedBoardAbstract serialized board that is notified by the model
     */
    public abstract void showBoard(SerializedBoardAbstract serializedBoardAbstract);

    /**
     * method that shows the winner
     * @param serializedBoardAbstract serialized board that is notified by the model
     */
    public abstract void showWinner(SerializedBoardAbstract serializedBoardAbstract);

    /**
     * setter of the status of a client
     * @param active status to be setted
     */
    public void setClientActive(boolean active) {
        this.client.setActive(active);
    }

    /**
     * setter of error status of a client
     * @param err status to be setted
     */
    public void setClientError(boolean err) {
        this.client.setClientError(err);
    }

    /**
     * enabler of the pinger
     * @param en enable status
     */
    public void enablePinger(boolean en) {
        this.client.enablePinger(en);
    }

    //TODO: just for test
    /**
     * printer of errors. just for tests
     * @param err error to be printed
     */
    public abstract void printCustom(String err);
}
