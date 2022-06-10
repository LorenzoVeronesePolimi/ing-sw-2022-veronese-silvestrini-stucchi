package it.polimi.ingsw.Server;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Messages.OUTMessages.OUTMessage;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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

/**
 * Class that manages the communication between the Client and the Server.
 * There is one SocketClientConnection for each Client.
 */
public class SocketClientConnection extends ClientConnection implements Runnable {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final Server server;

    private boolean firstPlayer = false;
    private List<OUTMessage> pendingMessage;
    private final ServerView serverView;


    /**
     * Constructor that initializes some parameters mandatory for the correct communication between Client and Server.
     * @param socket Socket of the connection between Client and Server.
     * @param server Server of the game.
     * @param controller Controller of the game.
     */
    public SocketClientConnection(Socket socket, Server server, Controller controller) {
        this.socket = socket;
        this.server = server;
        this.serverView = new ServerView(this, controller);
        this.pendingMessage = new ArrayList<>();
        this.activeConnection = true;

        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.out.flush();
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            //throw new RuntimeException(e);
            System.out.println("[SocketClientConnection, SocketClientConnection]: error in player connecting");
        }
    }

    /**
     * Constructor used just in testing. It doesn't initialize the socket do the server.
     * @param server Server of the game.
     * @param controller Controller of the game.
     */
    // Just for testing
    public SocketClientConnection(Server server, Controller controller) {
        this.server = server;
        this.serverView = new ServerView(this, controller);
    }

    /**
     * @return ServerView associated with the Client.
     */
    public ServerView getServerView() {
        return serverView;
    }

    /**
     * Main method executed by the SocketClientConnection. It manages messages received by the client passing them to its observer.
     */
    @Override
    public void run() {
        String read;
        try {
            // TODO: consider the case of wrong input
            // CLI o GUI
            send(serverView.askCLIorGUI());

            if (firstPlayer) {
                send(serverView.askFirstPlayer());
            }
            if (pendingMessage.size() > 0) {
                this.send(pendingMessage.get(0));
            }

            //server.lobby(this, this.nickname);
            while (isActiveConnection()) {
                read = (String) in.readObject();
                notify(read);
            }

        } catch (NoSuchElementException e) {
            System.err.println("Error! " + e.getMessage());
        } catch (IOException e) {
            if(server.isActiveConnection(this))
                server.deregisterConnection(this);
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the value of FirstPlayerAction.
     */
    public void setFirstPlayerAction() {
        this.firstPlayer = true;
    }

    /**
     * Set the value of pendingMessage. This message is the one sent to the client after its connection.
     */
    public void setPendingMessage(OUTMessage pending) {
        this.pendingMessage.add(pending);
    }

    /**
     * Method that send the message to the Client.
     * @param message OUTMessage sent to the Client.
     */
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

    /**
     * Method that send the Board to the Client.
     * @param message SerializedBoardAbstract sent to the Client.
     */
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

    /**
     * Method that closes the connection to the Server. Called by close() and by the server.
     */
    @Override
    public synchronized void closeConnection() {
        //send("Connection closed!");
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error when closing socket!");
        }
        activeConnection = false;
    }

    /**
     * method that closes the connection to the server and calls closeConnection();
     */
    public void close() {
        closeConnection();
        System.out.println("De-registering client " + this.hashCode());
        System.out.println("Done!");
    }

    /**
     * Asynchronous send of the OUTMessage.
     * @param message message sent to the Client.
     */
    public void asyncSend(final OUTMessage message){
        new Thread(() -> send(message)).start();
    }

    /**
     * Asynchronous send of the SerializedBoardAbstract.
     * @param message
     */
    public void asyncSendModel(SerializedBoardAbstract message){
        new Thread(() -> sendModel(message)).start();
    }
}
