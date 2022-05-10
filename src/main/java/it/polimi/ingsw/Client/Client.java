package it.polimi.ingsw.Client;

import it.polimi.ingsw.Messages.OUTMessages.OUTMessage;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;
import it.polimi.ingsw.Model.Board.SerializedBoardAdvanced;
import it.polimi.ingsw.View.CLIView;
import it.polimi.ingsw.View.ClientView;
import it.polimi.ingsw.View.GUIView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;

public class Client {

    private final String ip;
    private final int port;
    private ClientView view;
    private ObjectInputStream socketIn;
    private ObjectOutputStream socketOut;
    private boolean CLIorGUI = false;
    private boolean active = true;
    private OUTMessage prevErrorOUTMessage = null;
    private SerializedBoardAbstract prevErrorModelMessage = null;

    public Client(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public synchronized boolean isActive(){
        return active;
    }

    public synchronized void setActive(boolean active){
        this.active = active;
    }

    public Thread asyncReadFromSocket(final ObjectInputStream socketIn){
        Thread t = new Thread(() -> {
            try {
                Object inputMessage = this.socketIn.readObject();

                if(inputMessage instanceof OUTMessage)
                    ((OUTMessage)inputMessage).manageMessage(this.view);

                if(this.CLIorGUI) {
                    this.view = new GUIView(this);
                }
                this.view.printCustom("Stai per essere connesso, attendi!");

                while (isActive()) {
                    //view.printCustom("Attendi!");
                    /*
                        When a message is received it is managed by the view.
                        Then, this message is saved as prevMessage (in order to manage future errors).
                        If an error occurs:
                            - the message sets errorStatus = true
                            - the client plays the prevMessage (the one that has generated the error)
                            - the flow continues until next move or error
                     */

                    inputMessage = this.socketIn.readObject();

                    if(inputMessage instanceof OUTMessage) {
                        ((OUTMessage)inputMessage).manageMessage(this.view);

                        if (this.view.isErrorStatus()) {
                            if(this.prevErrorOUTMessage != null) {
                                this.prevErrorOUTMessage.manageMessage(this.view);
                            } else if(this.prevErrorModelMessage != null) {
                                this.prevErrorModelMessage.manageMessage(this.view);
                            }

                            this.view.setErrorStatus(false);
                        } else {
                            this.prevErrorModelMessage = null;
                            this.prevErrorOUTMessage = (OUTMessage) inputMessage;
                        }
                    } else {
                        System.out.println("\n\t The current board is this:");

                        SerializedBoardAbstract serializedBoard = (SerializedBoardAbstract) inputMessage;

                        // QUESTA PARTE PUO' ESSERE TOLTA, È SOLO PER DEBUG
                        /*
                        if(serializedBoard.getType().equalsIgnoreCase("standard")) {
                            view.printCustom("model standard");
                        } else if(serializedBoard.getType().equalsIgnoreCase("advanced")) {
                            view.printCustom("model advanced");
                        } else {
                            //view.printCustom("Client error");
                        }
                         */
                        //---------------------------------

                        this.prevErrorOUTMessage = null;
                        this.prevErrorModelMessage = serializedBoard;
                        serializedBoard.manageMessage(this.view);
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
                this.socketOut.reset();
                this.socketOut.writeObject(messageToController);
                this.socketOut.flush();
            } catch(IOException | NullPointerException e){
                System.err.println(e.getMessage());
            }
        });
        t.start();
        return t;
    }

    public void run() throws IOException {
        // Connecting
        Socket socket = new Socket(this.ip, this.port);
        System.out.println("Connection established");

        this.socketOut = new ObjectOutputStream(socket.getOutputStream());
        this.socketOut.flush();
        this.socketIn = new ObjectInputStream(socket.getInputStream());

        this.view = new CLIView(this);

        try{
            Thread t0 = asyncReadFromSocket(this.socketIn);
            t0.join();
        } catch(InterruptedException | NoSuchElementException e){
            System.out.println("Connection closed from the client side");
        } finally {
            this.socketIn.close();
            this.socketOut.close();
            socket.close();
        }
    }

    public void setCLIorGUI(boolean CLIorGUI){
        this.CLIorGUI = CLIorGUI;
    }
}
