package it.polimi.ingsw;

import it.polimi.ingsw.Client.Client;

import java.io.IOException;

public class ClientApp {
    public static void main( String[] args ) {
        Client client = new Client("127.0.0.1", 54321);
        try{
            client.run();
        }catch (IOException e){
            System.out.println("ClientApp error");
            System.err.println(e.getMessage());
        }
    }
}
