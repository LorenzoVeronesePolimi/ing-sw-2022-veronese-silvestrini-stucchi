package it.polimi.ingsw;

import it.polimi.ingsw.Client.Client;

import java.io.IOException;

/**
 * class that starts a client
 */
public class ClientApp {

    public static void main( String[] args ) {
        Client client = new Client();
        try{
            client.run();
        }catch (IOException e){
            System.out.println("ClientApp error");
            System.err.println(e.getMessage());
        }
    }

}
