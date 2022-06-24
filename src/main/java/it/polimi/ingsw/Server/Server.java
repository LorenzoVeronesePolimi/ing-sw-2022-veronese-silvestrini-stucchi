package it.polimi.ingsw.Server;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Messages.OUTMessages.MessageAskName;
import it.polimi.ingsw.Messages.OUTMessages.MessageClientDisconnection;
import it.polimi.ingsw.Messages.OUTMessages.OUTMessage;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Main server of the Project. It handles connections and the phase of the starting game.
 */
public class Server {

    private static final int PORT = 54321;
    private ServerSocket serverSocket;
    private ExecutorService executor = Executors.newFixedThreadPool(128);
    private Map<String, ClientConnection> waitingConnection = new HashMap<>();
    private Map<ClientConnection, ClientConnection> playingConnection = new HashMap<>();
    private int connections = 0;
    private int alreadyin =0; //number of players that have already given information about nick & colour
    private List<SocketClientConnection> socketConnections = new ArrayList<>();
    private List<OUTMessage> pendingMessage;

    private Controller controller;

    /**
     * Create a Server and opens the port for connections.
     * @throws IOException
     */
    public Server() throws IOException {
        try {
            this.serverSocket = new ServerSocket(PORT);
            System.out.println("Server started in port: " + PORT);
        } catch(IOException e) {
            System.err.println("Server could not start.");
            return;
        }

        this.pendingMessage = new ArrayList<>();
    }

    /**
     * @return The controller of the game.
     */
    public Controller getController() {
        return controller;
    }

    /**
     * Main method executed by the server. It accepts connections and manages first phase messages.
     */
    public void run(){
        System.out.println("[Server, run]: Server is running");

        while(true){
            try {
                Socket newSocket = serverSocket.accept();

                controller = this.createController();

                connections++;
                System.out.println("[Server, run]: Player connecting - " + connections);
                SocketClientConnection socketConnection = new SocketClientConnection(newSocket, this, controller);
                socketConnections.add(socketConnection);

                System.out.println("[Server, run]: Connection: " + socketConnection.hashCode());
                System.out.println("[Server, run]: List: " + socketConnections.stream().map(x -> x.hashCode()).collect(Collectors.toList()));


                if(connections == 1) {
                    socketConnection.setFirstPlayerAction();
                }

                executor.submit(socketConnection);

                if(pendingMessage.size() > 0) {
                    socketConnection.setPendingMessage(pendingMessage.get(0));
                    pendingMessage.clear();
                    alreadyin++;
                }
            } catch (IOException e) {
                System.out.println("[Server, run]: Connection Error!");
            }
        }
    }

    /**
     * Manages the first messages sent to the client that has just connected, like asking the nickname. If a client has connected
     * after the previous player has completed his duty, then the server memorizes the message that needs to be sent,
     * and only when a player connects to the server it is sent.
     * @param list list of PlayerColours chosen by the previous players.
     * @param numPlayers number of players connected.
     */
    public void askPlayerInfo(List<PlayerColour> list, int numPlayers) {
        if (socketConnections.size() > (alreadyin + 1)) {
            alreadyin++;
            socketConnections.get(alreadyin).send(new MessageAskName(list, numPlayers));
        } else {
            pendingMessage.add(new MessageAskName(list, numPlayers));
        }
    }

    /**
     * Method that creates the controller.
     * @return Controller of the game.
     */
    public Controller createController() {
        if(this.connections == 0) {
            this.controller = new Controller(this);
            return this.controller;
        }

        return this.controller;
    }

    /**
     * Reset the server to it's starting status.
     */
    public void resetServer() {
        alreadyin = 0;
    }

    //just for testing

    /**
     * Add connections to the Server.
     * @param clientConnectionCLI
     */
    public void addConnection(SocketClientConnection clientConnectionCLI) {
        socketConnections.add(clientConnectionCLI);
    }

    //De-register connection

    /**
     * Removes all connections from the server and sends a message stating the issue that occurred.
     * @param c SocketClientConnection of the client that has generated an error.
     */
    public synchronized void deregisterConnection(SocketClientConnection c) {
        // if a player connected but not in a match (because the match has already begun with other players) tryes to close the connection, we don't affect other players
        if((socketConnections.indexOf(c) >= this.controller.getPlayers().size()) && this.controller.getPlayers().size() > 0) {
            c.close();
            connections--;
            alreadyin--;
            return;
        }

        for(SocketClientConnection sc : socketConnections) {
            if(sc != c)
                sc.send(new MessageClientDisconnection());

            System.out.println("[Server, deregisterConnection]: Chiudendo " + sc.hashCode());
            sc.close();
        }

        // because this method is synchronized, if multiple players loose connection at the same time, the first that enters will block all other connections
        // by emptying the socketConnections list
        resetServer();
        socketConnections.clear();
        connections = 0;
        System.out.println("[Server, deregisterConnection]: Server is running");
    }

    /**
     * @param c ClientConnection.
     * @return the status of the ClientConnection c.
     */
    public boolean isActiveConnection(ClientConnection c) {
        return socketConnections.contains(c) && c.isActiveConnection();
    }
}
