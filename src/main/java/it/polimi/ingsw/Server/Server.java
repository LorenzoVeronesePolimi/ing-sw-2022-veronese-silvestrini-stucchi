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

public class Server {

    private static final int PORT = 54321;
    private ServerSocket serverSocket;
    private ExecutorService executor = Executors.newFixedThreadPool(128);
    private Map<String, ClientConnection> waitingConnection = new HashMap<>();
    private Map<ClientConnection, ClientConnection> playingConnection = new HashMap<>();
    private int connections = 0;
    private int alreadyin =0; //number of players that have already given information about nick & colour
    private List<SocketClientConnectionCLI> socketConnections = new ArrayList<>();
    private List<OUTMessage> pendingMessage;

    private Controller controller;

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

    public Controller getController() {
        return controller;
    }

    public void run(){
        System.out.println("Server is running");

        while(true){
            try {
                Socket newSocket = serverSocket.accept();

                controller = this.createController();

                connections++;
                System.out.println("Player connecting - " + connections);
                SocketClientConnectionCLI socketConnection = new SocketClientConnectionCLI(newSocket, this, controller);
                socketConnections.add(socketConnection);

                System.out.println("Connection: " + socketConnection.hashCode());
                System.out.println("List: " + socketConnections.stream().map(x -> x.hashCode()).collect(Collectors.toList()));


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
                System.out.println("Connection Error!");
            }
        }
    }

    public void askPlayerInfo(List<PlayerColour> list, int numPlayers) {
        if (socketConnections.size() > (alreadyin + 1)) {
            alreadyin++;
            socketConnections.get(alreadyin).send(new MessageAskName(list, numPlayers));
        } else {
            pendingMessage.add(new MessageAskName(list, numPlayers));
        }
    }

    public Controller createController() {
        if(this.connections == 0) {
            this.controller = new Controller(this);
            return this.controller;
        }

        return this.controller;
    }

    public void reserServer() {
        alreadyin = 0;
    }

    //just for testing
    public void addConnection(SocketClientConnectionCLI clientConnectionCLI) {
        socketConnections.add(clientConnectionCLI);
    }

    //De-register connection
    public synchronized void deregisterConnection(SocketClientConnectionCLI c) {
        for(SocketClientConnectionCLI sc : socketConnections) {
            if(sc != c)
                sc.send(new MessageClientDisconnection());

            System.out.println("Chiudendo " + sc.hashCode());
            sc.close();
        }

        reserServer();
        socketConnections.clear();
        connections = 0;
    }

    public boolean isActiveConnection(ClientConnection c) {
        return socketConnections.contains(c) && c.isActiveConnection();
    }
}
