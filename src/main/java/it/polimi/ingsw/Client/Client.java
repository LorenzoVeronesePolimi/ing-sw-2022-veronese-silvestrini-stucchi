package it.polimi.ingsw.Client;

import it.polimi.ingsw.Messages.ActiveMessageView;
import it.polimi.ingsw.View.CLIView;
import it.polimi.ingsw.View.ClientView;
import it.polimi.ingsw.View.GUI.GUIViewFX;
import it.polimi.ingsw.View.GUIView;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Class that represents the via of communication between the User and the game Server.
 * It handles the view (CLI or GUI) and some first steps of the connection.
 */
public class Client {

    private Scanner scanner;
    private String ip;
    private final int port;
    private Socket socket;
    private String nickname;
    private ClientView view;
    private ObjectInputStream socketIn;
    private ObjectOutputStream socketOut;
    private boolean CLIorGUI = false;
    private boolean activeConnection = true;
    private boolean clientReconnect = false;
    private boolean clientError = false;
    private boolean serverError = false;
    private boolean endGame = false;
    private boolean socketNull = true;
    private boolean platformReady = false;
    private ActiveMessageView prevMessage = null;
    private ScheduledExecutorService pinger;
    private GUIViewFX guiViewFX;

    /**
     * Constructor that initializes the Server port.
     * @param port server PORT.
     */
    public Client(int port) {
        this.port = port;
    }

    /**
     * @return whether the Client is active (true) of not (false).
     */
    public synchronized boolean isActive(){
        return activeConnection;
    }

    /**
     * @param active value of the Client status.
     */
    public synchronized void setActive(boolean active){
        this.activeConnection = active;
    }

    /**
     * @param rec value of clientReconnect.
     */
    public synchronized void setClientReconnect(boolean rec) { this.clientReconnect = rec; }

    /**
     * @param clientError whether the error in communication begun with one of the Clients.
     */
    public void setClientError(boolean clientError) {
        this.clientError = clientError;
    }

    /**
     * @param endGame whether the match has ended or not.
     */
    public void setEndGame(boolean endGame) {
        this.endGame = endGame;
    }

    /**
     * @param platformReady whether the JavaFX platform is ready.
     */
    public void setPlatformReady(boolean platformReady) {
        this.platformReady = platformReady;
    }

    /**
     * @return String nickname of the player.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname String nickname of the player.
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Used to receive messages from the Server. It initializes a new Thread in order to not stop the application.
     * @return the inner thread that executes the function.
     */
    public Thread asyncReadFromSocket(){
        Thread t = new Thread(() -> {
            boolean exception = false; // to check wheather the client experienced an exception

            try {
                Object inputMessage = this.socketIn.readObject();

                if (inputMessage instanceof ActiveMessageView)
                    ((ActiveMessageView) inputMessage).manageMessage(this.view);

                if (this.CLIorGUI) {
                    /*
                        The GUI is activated by calling the Platform method startup()
                        It allows us to rely on the parameters passes to the GUI Application
                        without the worry of losing them.

                        When a match begins everyone sees the Intro page
                        The Login page is show at each player in order (like in CLI) so, after the first player has
                        clicked on PLAY, the second player can do the same and so on...
                        In order to manage a better UI we need to implement a waining page for the next players waiting their
                        turn of Login actions.

                        Currently, the Login page is not customized at all by the GUI.
                        In order to do that, and all the other future stuff, we need to add some method to GUIViewFX, like:
                        - askFirstPlayerScene(parameter of the askFirstPLayer in GUIView received via the message) -> passing
                                parameters to controller in order to show and customize them
                        - askNickName(parameters of the askNickName in GUIView received via the message) -> passing parameters to controller...
                        - showBoardScene(parameter os ShowBoard method in GUIView) -> passing parameters to controller..
                        - and so on...


                     */

                    //System.out.println("Activating GUI in client");
                    //System.out.println("Server socket: " + this.socket.hashCode());

                    this.view = new GUIView(this);  //Creating the GUIView which will call methods of GUIViewFX

                    //System.out.println("Passing client: " + this.hashCode());
                    this.guiViewFX = new GUIViewFX(this, (GUIView) this.view);  // Passing setup arguments to FX

                    this.guiViewFX.init(); // this method is mandatory (found on stackoverflow)

                    //System.out.println("pre thread");

                    new Thread(() -> {
                        // Thread that runs JavaFX application
                        Platform.startup(() ->{
                            Stage stage = new Stage();
                            try {
                                this.guiViewFX.start(stage);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }).start();
                    //System.out.println("post thread");

                } else {
                    this.platformReady = true;
                }
                this.view.printCustom("You will be connected soon, wait!");

                //System.out.println("Entro nel loop");
                while (isActive()) {
                    /*
                        When a message is received it is managed by the view.
                        Then, this message is saved as prevMessage (in order to manage future errors).
                        If an error occurs:
                            - the message sets errorStatus = true
                            - the client plays the prevMessage (the one that has generated the error)
                            - the flow continues until next move or error
                     */

                    /*
                    this.platformReady:
                        When the player click on play he can receive the first message.
                        It is NOT necessary to modify this value for future use, because messages are received when required.
                        This is not true for the first message, which is received immediately.

                     */
                    if(this.platformReady) {
                        //System.out.println("platform ready");
                        inputMessage = this.socketIn.readObject();
                        //System.out.println("message received in client");
                        if (inputMessage instanceof ActiveMessageView) {
                            ((ActiveMessageView) inputMessage).manageMessage(this.view);

                            if (this.view.isErrorStatus()) {
                                if (prevMessage != null)
                                    prevMessage.manageMessage(this.view);
                                //TODO: in gui show the error like an alert
                                //TODO: pinger to keep gui awake?

                                this.view.setErrorStatus(false);
                            } else {
                                prevMessage = (ActiveMessageView) inputMessage;
                            }
                        }
                    }

                }
            } catch (SocketException e) {
                System.out.println("[Client, asyncReadFromSocket]: SocketException");
                this.view.printErrorMessage("Error. You have been disconnected!");
                this.clientError = true;
                this.socketNull = true;
                exception = true;

                // testing reconnection in order to identify server status
                try {
                    this.socket = new Socket(this.ip, this.port);
                } catch(IOException ex) {
                    this.serverError = true;
                    this.clientError = false;
                    this.view.printErrorMessage("Error 404. Server not responding.");
                } finally {
                    if(!this.serverError && this.clientError) {
                        this.socketNull = false;
                    }
                }

                Platform.exit();

                //e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                if(!exception)
                    this.socketNull = true;

                Platform.exit();
                this.setActive(false);
            }
        });
        t.start();
        return t;
    }

    /**
     * Used to send messages to the Server. It initializes a thread in order to not stop the application.
     * @param messageToController message that needs to be sent to the Controller in the Server.
     * @return the inner thread that executes the function.
     */
    public Thread asyncWriteToSocket(String messageToController){
        Thread t = new Thread(() -> {
            try {
                this.socketOut.reset();
                this.socketOut.writeObject(messageToController);
                this.socketOut.flush();
            } catch(IOException | NullPointerException e){
                //e.printStackTrace();
            }
        });
        t.start();
        return t;
    }

    /**
     * Asks the Server IP to the user and connects to the Server.
     * @throws IOException
     */
    public void run() throws IOException {
        this.ip = askIP();

        // Connecting to the server
        connecting();
    }

    /**
     * Connection to the server.
     * @throws IOException when an error on the Socket occurs.
     */
    private void connecting() throws IOException {
        // initialization of the parameters of the client
        this.clientReconnect = false;
        this.clientError = false;
        this.endGame = false;

        // connection to the server
        if(this.socketNull)
            this.socket = new Socket(this.ip, this.port);

        System.out.println("Connection established");
        this.setActive(true);
        this.socketNull = false;

        //Ping for establishing connection
        //this.pinger = Executors.newSingleThreadScheduledExecutor();

        //Socket for communication
        this.socketOut = new ObjectOutputStream(socket.getOutputStream());
        this.socketOut.flush();
        this.socketIn = new ObjectInputStream(socket.getInputStream());

        // view
        this.view = new CLIView(this);

        try{
            // ready to listen to the server
            Thread t0 = asyncReadFromSocket();
            t0.join();

            // when the match is somehow ended (error or normale game end)
            if((this.clientError || this.endGame) && !this.serverError) {
                try {
                    this.askReconnect();
                } catch (IOException e) {
                    this.view.printErrorMessage("reconnecting error");
                    e.printStackTrace();
                }
            }

            this.view.endView();

        } catch (InterruptedException | NoSuchElementException e){
            System.out.println("Connection closed from the client side");
            e.printStackTrace();
        } finally {
            if(!this.clientError && !this.serverError) {
                this.socketIn.close();
                this.socketOut.close();
                this.socket.close();
            }
        }
    }

    /**
     * Asks the Server IP to the user.
     * @return the Server IP.
     */
    private String askIP() {
        scanner = new Scanner(System.in);
        String response;
        do {
            System.out.println("> Insert the server ip: ");
            System.out.print("> ");
            response = scanner.nextLine();
        }while(checkIP(response));

        return response;
    }

    /**
     * Checks if the response is a valid IP.
     * @param response server IP from the user.
     * @return true if the response is a valid IP.
     */
    private boolean checkIP(String response) {
        return false;
    }

    /**
     * Ask the user if there is the intention to reconnect to the server after the match has ended(normally or unintentionally)
     * @throws IOException when there is a Socket error.
     */
    private void askReconnect() throws IOException {
        this.view.askReconnect();

        if (clientReconnect)
            connecting();
    }

    /**
     * @param CLIorGUI whether the view selected from the user is CLI (true) or GUI(false)
     */
    public void setCLIorGUI(boolean CLIorGUI){
        this.CLIorGUI = CLIorGUI;
    }
}
