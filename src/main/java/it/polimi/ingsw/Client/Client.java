package it.polimi.ingsw.Client;

import it.polimi.ingsw.HelloFX;
import it.polimi.ingsw.Messages.ActiveMessageView;
import it.polimi.ingsw.View.CLIView;
import it.polimi.ingsw.View.ClientView;
import it.polimi.ingsw.View.GUIView;
import javafx.application.Application;

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
    private ClientView view;
    private ObjectInputStream socketIn;
    private ObjectOutputStream socketOut;
    private boolean CLIorGUI = false;
    private boolean activeConnection = true;
    private boolean clientReconnect = false;
    private boolean clientError = false;
    private boolean serverError = false;
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
                this.view.printCustom("Error. You have been disconnected!");
                this.setClientError(true);

            } catch (EOFException e){
                e.printStackTrace();
            } catch (Exception e) {
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
                System.err.println(e.getMessage());
            }
        });
        t.start();
        return t;
    }

    public void run() throws IOException {
        // Connecting
        connecting();
    }

    private void connecting() throws IOException {
        this.setActive(true);
        this.setClientReconnect(false);
        this.setClientError(false);

        Socket socket = new Socket(this.ip, this.port);
        System.out.println("Connection established");

        //Ping for establishing connection
        this.pinger = Executors.newSingleThreadScheduledExecutor();

        //Socket for communication
        this.socketOut = new ObjectOutputStream(socket.getOutputStream());
        this.socketOut.flush();
        this.socketIn = new ObjectInputStream(socket.getInputStream());

        this.view = new CLIView(this);

        try{
            Thread t0 = asyncReadFromSocket();
            t0.join();

            if(this.clientError && !this.serverError) {
                try {
                    this.askReconnet();
                } catch (IOException e) {
                    System.out.println("reconnecting error");
                    this.view.endView();
                    //e.printStackTrace();
                }
            }

            this.view.endView();

        } catch(InterruptedException | NoSuchElementException e){
            System.out.println("Connection closed from the client side");
        } finally {
            this.socketIn.close();
            this.socketOut.close();
            socket.close();
        }
    }

    public void setCLIorGUI(boolean CLIorGUI){
        this.CLIorGUI = CLIorGUI;
    }

    private void askReconnet() throws IOException {
        this.view.askReconnect();

        if(clientReconnect)
            connecting();
    }

    public void managePinger() {
        //pinger.scheduleAtFixedRate(() -> this.asyncWriteToSocket(new Ping()), 0, 1000, TimeUnit.MILLISECONDS);
    }
}
