package it.polimi.ingsw.Server;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Messages.OUTMessages.MessageAskName;
import it.polimi.ingsw.Messages.OUTMessages.OUTMessage;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        this.serverSocket = new ServerSocket(PORT);
        this.controller = new Controller(this);
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
                connections++;
                System.out.println("Ready for the new connection - " + connections);
                SocketClientConnectionCLI socketConnection = new SocketClientConnectionCLI(newSocket, this, controller);
                socketConnections.add(socketConnection);

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

    public void reserServer() {
        alreadyin = 0;
    }

    public void addConnection(SocketClientConnectionCLI clientConnectionCLI) {
        socketConnections.add(clientConnectionCLI);
    }

    /*
    public synchronized void lobby(ClientConnection c, String name){
        List<String> keys = new ArrayList<>(waitingConnection.keySet());
        for(int i = 0; i < keys.size(); i++){
            ClientConnection connection = waitingConnection.get(keys.get(i));
            connection.asyncSend("Connected User: " + keys.get(i));
        }
        waitingConnection.put(name, c);

        if(waitingConnection.size() == 1)
            c.asyncSend("Choose how many players");

        keys = new ArrayList<>(waitingConnection.keySet());

        if (waitingConnection.size() == 0) {
            ClientConnection c1 = waitingConnection.get(keys.get(0));
            ClientConnection c2 = waitingConnection.get(keys.get(1));
            
            Player player1 = new Player(keys.get(0), Cell.X);
            Player player2 = new Player(keys.get(1), Cell.O);
            View player1View = new RemoteView(player1, keys.get(0), c1);
            View player2View = new RemoteView(player2, keys.get(1), c2);
            Model model = new Model();
            Controller controller = new Controller(model);
            model.addObserver(player1View);
            model.addObserver(player2View);
            player1View.addObserver(controller);
            player2View.addObserver(controller);
            playingConnection.put(c1, c2);
            playingConnection.put(c2, c1);
            waitingConnection.clear();

            c1.asyncSend(model.getBoardCopy());
            c2.asyncSend(model.getBoardCopy());
            //if(model.getBoardCopy().)
            if(model.isPlayerTurn(player1)){
                c1.asyncSend(gameMessage.moveMessage);
                c2.asyncSend(gameMessage.waitMessage);
            } else {
                c2.asyncSend(gameMessage.moveMessage);
                c1.asyncSend(gameMessage.waitMessage);
            }
            
        }
    }*/

    //De-register connection
    public synchronized void deregisterConnection(ClientConnection c) {
        ClientConnection opponent = playingConnection.get(c);
        if(opponent != null) {
            opponent.closeConnection();
        }
        playingConnection.remove(c);
        playingConnection.remove(opponent);
        Iterator<String> iterator = waitingConnection.keySet().iterator();
        while(iterator.hasNext()){
            if(waitingConnection.get(iterator.next())==c){
                iterator.remove();
            }
        }
    }
}
