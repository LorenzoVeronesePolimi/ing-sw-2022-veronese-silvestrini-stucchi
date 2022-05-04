package it.polimi.ingsw.Server;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Messages.OUTMessages.OUTMessage;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;
import it.polimi.ingsw.Model.Board.SerializedBoardAdvanced;
import it.polimi.ingsw.View.ServerView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;

/*
    --- FROM Client TO Controller
    Connection: receive socket message from ClientView and notify to ConnectionListener (inner class);
    ConnectionListener (inner class): notify controller (and maybe more actions);
    Controller: perform the action modifying the model;
    ----
    How to pass the model to the view? How to communicate from Controller to view?
    Steps down here (I think it makes sense)
    ----
    Optional/Error:
        Controller: wants to communicate with client (error or anything), uses his map player->ServerView to send messages
        Connection: send message to ClientView;
        ClientView: shows message (CLI or GUI);

        This step can be done creating a map player->ServerView in order to manage response messages from the Controller,
        because in the controller we don't have a way to pass messages to the client.
        We can add a method addServerView(ServerView sv) to the controller in order to create the map, so we can
        send response messages like "errors" or "well done" to the player that has performed the action.
        In ServerView we can than send the message via send or asyncSend (I don't know which one atm)


    --- FROM Model TO Client
    Model: it's modified by the controller, notify ServerView;
    ServerView: asyncSend(model);

 */

public class SocketClientConnectionCLI extends ClientConnection implements Runnable {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final Server server;

    private boolean active = true;
    private boolean firstPlayer = false;
    private final ServerView serverView;


    public SocketClientConnectionCLI(Socket socket, Server server, Controller controller) {
        this.socket = socket;
        this.server = server;
        this.serverView = new ServerView(this, controller);

        //controller.addBoardObserver();

        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.out.flush();
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Just for testing
    public SocketClientConnectionCLI(Server server, Controller controller) {
        this.server = server;
        this.serverView = new ServerView(this, controller);
    }

    public ServerView getServerView() {
        return serverView;
    }

    @Override
    public void run() {
        String read;
        try{
            // TODO: consider the case of wrong input
            // CLI o GUI
            send(serverView.askCLIorGUI());

            if(firstPlayer) {
                send(serverView.askFirstPlayer());
            } else {
                send(serverView.askName());
            }

            //server.lobby(this, this.nickname);
            while(isActive()){
                read = (String) in.readObject();
                notify(read);
            }

        } catch (NoSuchElementException e) {
            System.err.println("Error! " + e.getMessage());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void setFirstPlayerAction() {
        this.firstPlayer = true;
    }

    private synchronized boolean isActive(){
        return active;
    }

    public synchronized void send(OUTMessage message) {
        try {
            out.reset();
            out.writeObject(message);
            out.flush();
        } catch(IOException | NullPointerException e){
            System.err.println(e.getMessage());
            System.out.println("errore");
        }
    }

    public synchronized void sendModel(SerializedBoardAbstract message) {
        try {
            if (message != null) {
                if(message.getType().equalsIgnoreCase("advanced")) {
                    //System.out.println("sendModel advanced");
                } else if(message.getType().equalsIgnoreCase("standard")) {
                    //System.out.println("sendModel abstract");
                } else {
                    //System.out.println("Errore sendModel");
                }
            }
            out.reset();
            out.writeObject(message);
            out.flush();
        } catch(IOException | NullPointerException e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    public synchronized void closeConnection() {
        //send("Connection closed!");
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error when closing socket!");
        }
        active = false;
    }

    private void close() {
        closeConnection();
        System.out.println("De-registering client...");
        server.deregisterConnection(this);
        System.out.println("Done!");
    }

    public void asyncSend(final OUTMessage message){
        new Thread(() -> send(message)).start();
    }
    public void asyncSendModel(SerializedBoardAbstract message){
        new Thread(() -> sendModel(message)).start();
    }
}
