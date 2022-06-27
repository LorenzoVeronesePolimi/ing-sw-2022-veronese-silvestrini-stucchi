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

    private Scanner scanner;
    private int PORT = 54321;
    private ServerSocket serverSocket;
    private ExecutorService executor = Executors.newFixedThreadPool(128);   // thread that execurte the SocketClientConnections
    private int connections = 0;    // number of clients connected to the server
    private int alreadyin =0; //number of players that have already given information about nick & colour
    private List<SocketClientConnection> socketConnections = new ArrayList<>();
    private List<OUTMessage> pendingMessage;    // if a message needs to be sent to a client but it was not ready we store it here.

    private Controller controller;  // controller of the game

    /**
     * Create a Server and opens the port for connections.
     * @throws IOException
     */
    public Server() throws IOException {
        this.scanner = new Scanner(System.in);
        this.PORT = this.askPort();

        try {
            this.serverSocket = new ServerSocket(PORT);
            System.out.println("[Server, Server]: Server started in port: " + PORT);
        } catch(IOException e) {
            System.err.println("[Server, Server]: Server could not start.");
            return;
        }

        this.pendingMessage = new ArrayList<>();
    }

    /**
     * This method is used just for testing, because it doesn't need the user to input the server port.
     * @throws IOException
     */
    public Server(String test) throws IOException {
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
     * Asks the Server Port to the user.
     * @return the Server Port.
     */
    private Integer askPort() {
        String response;
        do {
            System.out.println("> Insert the server port: ");
            System.out.print("> ");
            response = scanner.nextLine();
        }while(!validPort(response));

        return Integer.parseInt(response);
    }

    /**
     * Checks if the response is a valid Port.
     * @param chosenPort server Port from the user.
     * @return true if the response is a valid Port.
     */
    private boolean validPort(String chosenPort) {
        int port;
        try {
            port = Integer.parseInt(chosenPort);
        } catch(NumberFormatException e) {
            return false;
        }

        if(port >= 0 && port <= 1023) {
            System.out.println("[Server, askPort]: you chose a well-known port");
            return false;
        }
        if(port >= 1024 && port <= 49151) {
            System.out.println("[Server, askPort]: you chose a registered port at your own risk");
            return true;
        }
        if(port >= 50152 && port <= 65535) {
            System.out.println("[Server, askPort]: port set");
            return true;
        }

        return false;
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
                // server ready to accept connecitons
                Socket newSocket = serverSocket.accept();

                // controller of the game
                // if the controller already exists, the method will not create a new one. Instead, it will pass the existing one.
                controller = this.createController();

                connections++;
                System.out.println("[Server, run]: Player connecting - " + connections);
                SocketClientConnection socketConnection = new SocketClientConnection(newSocket, this, controller);
                socketConnections.add(socketConnection);

                // log
                System.out.println("[Server, run]: Connection: " + socketConnection.hashCode());
                System.out.println("[Server, run]: List: " + socketConnections.stream().map(x -> x.hashCode()).collect(Collectors.toList()));


                if(connections == 1) {
                    // if first player connecting
                    socketConnection.setFirstPlayerAction();
                }

                // execute the SocketClientConnection
                executor.submit(socketConnection);

                // if there were pending messages one is passed to the new player
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
