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
    private boolean active = true;

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
            OUTMessage prevMessage = null;
            try {
                Object inputMessage = socketIn.readObject();

                if(inputMessage instanceof OUTMessage)
                    ((OUTMessage)inputMessage).manageMessage(view);

                if(CLIorGUI) {
                    view = new GUIView(this);
                }

                while (isActive()) {
                    //view.printCustom("attesa");
                    /*
                        When a message is received it is managed by the view.
                        Then, this message is saved as prevMessage (in order to manage future errors).
                        If an error occurs:
                            - the message sets errorStatus = true
                            - the client plays the prevMessage (the one that has generated the error)
                            - the flow continues until next move or error
                     */

                    inputMessage = socketIn.readObject();

                    if(inputMessage instanceof OUTMessage) {
                        ((OUTMessage)inputMessage).manageMessage(view);

                        if (view.isErrorStatus()) {
                            prevMessage.manageMessage(view);
                            view.setErrorStatus(false);
                        } else {
                            prevMessage = (OUTMessage) inputMessage;
                        }
                    } else {
                        SerializedBoardAbstract serializedBoard = (SerializedBoardAbstract) inputMessage;
                        if(serializedBoard.getType().equalsIgnoreCase("standard")) {
                            serializedBoard = (SerializedBoardAbstract) inputMessage;
                            //view.printCustom("model standard");
                        } else if(serializedBoard.getType().equalsIgnoreCase("advanced")) {
                            serializedBoard = (SerializedBoardAdvanced) inputMessage;
                            //view.printCustom("model advanced");
                        } else {
                            //view.printCustom("Client error");
                        }

                        view.showBoard(serializedBoard);
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
