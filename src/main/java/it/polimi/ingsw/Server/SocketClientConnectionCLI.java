package it.polimi.ingsw.Server;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.Messages.Message;
import it.polimi.ingsw.OUTMessages.OUTMessage;
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

    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final Server server;

    private boolean active = true;
    private boolean firstPlayer = false;
    private final ServerView serverView;


    public SocketClientConnectionCLI(Socket socket, Server server, Controller controller) {
        this.socket = socket;
        this.server = server;
        this.serverView = new ServerView(this, controller);

        // Commented because I believe it's wrong (already corrected where needed)
        //this.serverView.addObserver(controller);

        try {
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try{
            // CLI o GUI
            send(serverView.CLIorGUI());
            if(firstPlayer) {
                send(serverView.manageFirstPlayer(this));
            } else {
                //send("Attendi!");
            }
            send(serverView.chooseName());


            Message read;

            //server.lobby(this, this.nickname);
            while(isActive()){
                read = (Message) in.readObject();
                notify(read);
            }

        } catch (NoSuchElementException e) {
            System.err.println("Error! " + e.getMessage());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
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
        } catch(IOException e){
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
}
