package it.polimi.ingsw;

import it.polimi.ingsw.Server.Server;

import java.io.IOException;

/**
 * class that starts the server
 */
public class ServerApp {
    public static void main( String[] args ) {
        Server server;
        try {
            server = new Server();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        server.run();
    }
}
