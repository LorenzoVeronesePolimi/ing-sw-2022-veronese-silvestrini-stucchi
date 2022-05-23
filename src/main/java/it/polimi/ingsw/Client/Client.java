package it.polimi.ingsw.Client;

import it.polimi.ingsw.Messages.ActiveMessageView;
import it.polimi.ingsw.View.CLIView;
import it.polimi.ingsw.View.ClientView;
import it.polimi.ingsw.View.GUIView;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.NoSuchElementException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Client {

    private final String ip;
    private final int port;
    private Socket socket;
    private ClientView view;
    private ObjectInputStream socketIn;
    private ObjectOutputStream socketOut;
    private boolean CLIorGUI = false;
    private boolean activeConnection = true;
    private boolean clientReconnect = false;
    private boolean clientError = false;
    private boolean serverError = false;
    private boolean endGame = false;
    private boolean socketNull = true;
    private ActiveMessageView prevMessage = null;
    private ScheduledExecutorService pinger;

    public Client(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public synchronized boolean isActive(){
        return activeConnection;
    }

    public synchronized void setActive(boolean active){
        this.activeConnection = active;
    }
    public synchronized void setClientReconnect(boolean rec) { this.clientReconnect = rec; }

    public void setClientError(boolean clientError) {
        this.clientError = clientError;
    }

    public void setServerError(boolean serverError) {
        this.serverError = serverError;
    }
    public void setEndGame(boolean endGame) {
        this.endGame = endGame;
    }

    public Thread asyncReadFromSocket(){
        Thread t = new Thread(() -> {
            try {
                Object inputMessage = this.socketIn.readObject();

                if (inputMessage instanceof ActiveMessageView)
                    ((ActiveMessageView) inputMessage).manageMessage(this.view);

                if (this.CLIorGUI) {
                    this.view = new GUIView(this);

                }
                this.view.printCustom("You will be connected soon, wait!");

                while (isActive()) {
                    /*
                        When a message is received it is managed by the view.
                        Then, this message is saved as prevMessage (in order to manage future errors).
                        If an error occurs:
                            - the message sets errorStatus = true
                            - the client plays the prevMessage (the one that has generated the error)
                            - the flow continues until next move or error
                     */

                    inputMessage = this.socketIn.readObject();
                    System.out.println("client");
                    if (inputMessage instanceof ActiveMessageView) {
                        ((ActiveMessageView) inputMessage).manageMessage(this.view);

                        if (this.view.isErrorStatus()) {
                            if (prevMessage != null)
                                prevMessage.manageMessage(this.view);

                            this.view.setErrorStatus(false);
                        } else {
                            prevMessage = (ActiveMessageView) inputMessage;
                        }
                    }

                }
            } catch (SocketException e) {
                this.view.printErrorMessage("Error. You have been disconnected!");
                this.setClientError(true);
                this.socketNull = true;

                // testing reconnection in order to identify server status
                try {
                    this.socket = new Socket(this.ip, this.port);
                } catch(IOException ex) {
                    this.serverError = true;
                    this.clientError = false;
                    this.view.printErrorMessage("Error 404. Server not responding.");
                } finally {
                    if(!this.serverError && this.clientError) {
                        this.socketNull = false;
                        /*try {
                            this.socket.close();
                        } catch (IOException ex) {
                            e.printStackTrace();
                        }*/
                    }
                }

                //e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                this.setActive(false);
            }
        });
        t.start();
        return t;
    }

    public Thread asyncWriteToSocket(String messageToController){
        Thread t = new Thread(() -> {
            try {
                this.socketOut.reset();
                this.socketOut.writeObject(messageToController);
                this.socketOut.flush();
            } catch(IOException | NullPointerException e){
                //e.printStackTrace();
            }
        });
        t.start();
        return t;
    }

    public void run() throws IOException {
        // Connecting to the server
        connecting();
    }

    private void connecting() throws IOException {
        // initialization of the parameters of the client
        this.setClientReconnect(false);
        this.setClientError(false);
        this.setEndGame(false);

        // connection to the server
        if(this.socketNull)
            this.socket = new Socket(this.ip, this.port);

        System.out.println("Connection established");
        this.setActive(true);
        this.socketNull = false;

        //Ping for establishing connection
        this.pinger = Executors.newSingleThreadScheduledExecutor();

        //Socket for communication
        this.socketOut = new ObjectOutputStream(socket.getOutputStream());
        this.socketOut.flush();
        this.socketIn = new ObjectInputStream(socket.getInputStream());

        // view
        this.view = new CLIView(this);

        try{
            // ready to listen to the server
            Thread t0 = asyncReadFromSocket();
            t0.join();

            // when the match is somehow ended (error or normale game end)
            if((this.clientError || this.endGame) && !this.serverError) {
                try {
                    this.askReconnet();
                } catch (IOException e) {
                    this.view.printErrorMessage("reconnecting error");
                    //e.printStackTrace();
                }
            }

            this.view.endView();

        } catch (InterruptedException | NoSuchElementException e){
            System.out.println("Connection closed from the client side");
            e.printStackTrace();
        } finally {
            if(!this.clientError && !this.serverError) {
                this.socketIn.close();
                this.socketOut.close();
                this.socket.close();
            }
        }
    }

    public void setCLIorGUI(boolean CLIorGUI){
        this.CLIorGUI = CLIorGUI;
    }

    private void askReconnet() throws IOException {
        this.view.askReconnect();

        if (clientReconnect)
            connecting();
    }
}
