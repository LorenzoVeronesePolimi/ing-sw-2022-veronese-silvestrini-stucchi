package it.polimi.ingsw.Client;

import it.polimi.ingsw.Messages.OUTMessages.OUTMessage;
import it.polimi.ingsw.View.CLIView;
import it.polimi.ingsw.View.ClientView;
import it.polimi.ingsw.View.GUIView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {

    private String ip;
    private int port;
    private ClientView view;
    private ObjectInputStream socketIn;
    private ObjectOutputStream socketOut;
    private boolean CLIorGUI = false;

    public Client(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    private boolean active = true;

    public synchronized boolean isActive(){
        return active;
    }

    public synchronized void setActive(boolean active){
        this.active = active;
    }

    public Thread asyncReadFromSocket(final ObjectInputStream socketIn){
        Thread t = new Thread(() -> {
            OUTMessage prevMessage = null;
            try {
                OUTMessage inputMessage = (OUTMessage) socketIn.readObject();
                inputMessage.manageMessage(view);

                if(CLIorGUI) {
                    view = new GUIView(this);
                }

                while (isActive()) {
                    /*
                        When a message is received it is managed by the view.
                        Then, this message is saved as prevMessage (in order to manage future errors).
                        If an error occurs:
                            - the message sets errorStatus = true
                            - the client plays the prevMessage (the one that has generated the error)
                            - the flow continues until next move or error
                     */

                    inputMessage = (OUTMessage) socketIn.readObject();
                    inputMessage.manageMessage(view);

                    if(view.isErrorStatus()) {
                        prevMessage.manageMessage(view);
                        view.setErrorStatus(false);
                    } else {
                        prevMessage = inputMessage;
                    }

                }
            } catch (Exception e){
                setActive(false);
            }
        });
        t.start();
        return t;
    }

    public Thread asyncWriteToSocket(String messageToController){
        Thread t = new Thread(() -> {
            try {
                socketOut.reset();
                socketOut.writeObject(messageToController);
                socketOut.flush();
            } catch(IOException | NullPointerException e){
                System.err.println(e.getMessage());
            }
        });
        t.start();
        return t;
    }

    public void run() throws IOException {
        // Connecting
        Socket socket = new Socket(ip, port);
        System.out.println("Connection established");

        socketOut = new ObjectOutputStream(socket.getOutputStream());
        socketOut.flush();
        socketIn = new ObjectInputStream(socket.getInputStream());

        view = new CLIView(this);

        try{
            Thread t0 = asyncReadFromSocket(socketIn);
            t0.join();
        } catch(InterruptedException | NoSuchElementException e){
            System.out.println("Connection closed from the client side");
        } finally {
            socketIn.close();
            socketOut.close();
            socket.close();
        }
    }

    public void setCLIorGUI(boolean CLIorGUI){
        this.CLIorGUI = CLIorGUI;
    }
}
