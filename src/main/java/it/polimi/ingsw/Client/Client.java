package it.polimi.ingsw.Client;

import it.polimi.ingsw.Messages.ActiveMessageView;
import it.polimi.ingsw.Messages.OUTMessages.PongMessage;
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
import java.util.concurrent.TimeUnit;

/**
 * Class that represents the via of communication between the User and the game Server.
 * It handles the view (CLI or GUI) and some first steps of the connection.
 */
public class Client {

    private Scanner scanner;    // to handle player input
    private String ip;          // server ip
    private int port;     // server port
    private Socket socket;      // connection socket
    private String nickname;    // player nickname
    private ClientView view;    // player view (cli or gui)
    private ObjectInputStream socketIn;     // input socket
    private ObjectOutputStream socketOut;   // output socket
    private boolean CLIorGUI = false;       // if this.view is a CLIView or a GUIView
    private boolean activeConnection = true;    // if the connection is still active
    private boolean endGame = false;            // if is the end of the game
    private boolean platformReady = false;      // if the JavaFX platform is ready to operate
    private boolean connectionError = false;
    private ActiveMessageView prevMessage = null;   // message used in case of errors
    private GUIViewFX guiViewFX;                // for activating GUIView
    private ScheduledExecutorService pinger;  // for check of connections
    private Thread readThread;

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
     * @param endGame whether the match has ended or not.
     */
    public synchronized void setEndGame(boolean endGame) {
        this.endGame = endGame;
    }

    /**
     * @return the value of endGame.
     */
    public synchronized boolean getEndGame() {
        return this.endGame;
    }

    /**
     * @param platformReady whether the JavaFX platform is ready.
     */
    public synchronized void setPlatformReady(boolean platformReady) {
        this.platformReady = platformReady;
    }

    /**
     * @return the value of platformReady.
     */
    public synchronized boolean getPlatformReady() {
        return this.platformReady;
    }

    /**
     * @return String nickname of the player.
     */
    public synchronized String getNickname() {
        return nickname;
    }

    /**
     * @param nickname String nickname of the player.
     */
    public synchronized void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Default constructor that initialize the class scanner.
     */
    public Client() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Used to receive messages from the Server. It initializes a new Thread in order to not stop the application.
     * @return the inner thread that executes the function.
     */
    public void asyncReadFromSocket(){
        this.readThread = new Thread(() -> {
            this.setActive(true);
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
                    this.setPlatformReady(false); // in order to way for JavaFX Platform to initialize

                    this.view = new GUIView(this);  //Creating the GUIView which will call methods of GUIViewFX

                    this.guiViewFX = new GUIViewFX(this, (GUIView) this.view);  // Passing setup arguments to FX

                    this.guiViewFX.init(); // this method is mandatory (found on stackoverflow)

                    new Thread(() -> {
                        // Thread that runs JavaFX application
                        Platform.startup(() -> {
                            Stage stage = new Stage();
                            try {
                                this.guiViewFX.start(stage);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }).start();

                } else {
                    this.setPlatformReady(true);
                }
                this.view.printCustom("You will be connected soon, wait!");

                while (isActive() && !this.getEndGame()) {
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
                    if (this.getPlatformReady()) {
                        inputMessage = this.socketIn.readObject();

                        if (inputMessage instanceof ActiveMessageView) {
                            ((ActiveMessageView) inputMessage).manageMessage(this.view);

                            if (this.view.isErrorStatus()) {
                                // let the player repeat the move that generated an error
                                if (prevMessage != null)
                                    prevMessage.manageMessage(this.view);

                                this.view.setErrorStatus(false);
                            } else {
                                //ignoring pong messages from server
                                if(!(inputMessage instanceof PongMessage)) {
                                    // save the message in order to manage errors and let the player repeat the move
                                    prevMessage = (ActiveMessageView) inputMessage;
                                }
                            }
                        }
                    }

                }
            } catch (SocketException e) {
                enablePinger(false);    // repeated, but i want to be sure it stops in time
                System.out.println("[Client, asyncReadFromSocket]: SocketException");
                checkErrorSource();

            } catch (IOException e) {
                enablePinger(false); // repeated, but i want to be sure it stops in time
                System.out.println("[Client, asyncReadFromSocket]: IOException");
                checkErrorSource();

            }catch (Exception e) {
                enablePinger(false); // repeated, but i want to be sure it stops in time
                System.out.println("[Client, asyncReadFromSocket]: Exception");
                checkErrorSource();

            } finally {
                System.out.println("[Client, asyncReadFromSocket]: finally");

                enablePinger(false); // repeated, but i want to be sure it stops in time
                this.setActive(false);
                disconnect();
            }
        });
        this.readThread.start();
    }

    /**
     * Used to send messages to the Server. It initializes a thread in order to not stop the application.
     * @param messageToController message that needs to be sent to the Controller in the Server.
     * @return the inner thread that executes the function.
     */
    public void asyncWriteToSocket(String messageToController){
        try {
            this.socketOut.reset();
            this.socketOut.writeObject(messageToController);
            this.socketOut.flush();
        } catch(IOException | NullPointerException e){
            System.out.println("[Client, asyncWriteFromSocket]: asyncWriteException");

            this.setActive(false);
            this.enablePinger(false);
            // if it is a client error or a server error
            checkErrorSource();
            disconnect();
        }
    }

    /**
     * This method is used to check whether the connection error is client side or server side, thus impeding the reconnection.
     */
    private void checkErrorSource() {
        this.view.printErrorMessage("Error. You have been disconnected!");

        // testing reconnection in order to identify server status
        try {
            this.socket = new Socket(this.ip, this.port);
        } catch(IOException ex) {
            this.view.printErrorMessage("Error 404. Server not responding.");
        }
    }


    /**
     * Asks the Server IP to the user and connects to the Server.
     * @throws IOException
     */
    public void run() throws IOException {
        this.ip = askIP();
        this.port = askPort();

        // Connecting to the server
        connecting();
    }

    /**
     * Connection to the server.
     * @throws IOException when an error on the Socket occurs.
     */
    private void connecting() throws IOException {
        // initialization of the parameters of the client
        // connection to the server
        this.socket = new Socket(this.ip, this.port);

        System.out.println("Connection established");
        this.setActive(true);

        //Socket for communication
        this.socketOut = new ObjectOutputStream(socket.getOutputStream());
        this.socketOut.flush();
        this.socketIn = new ObjectInputStream(socket.getInputStream());

        // view
        this.view = new CLIView(this);

        try{
            // ready to listen to the server
            this.pinger = Executors.newSingleThreadScheduledExecutor();
            this.enablePinger(true);
            asyncReadFromSocket();
            this.readThread.join();
            disconnect();

        } catch (NoSuchElementException e){
            System.out.println("[Client, connecting]: Connection closed from the client side");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("[Client, connecting]: interruptException");
        } finally {
            this.socketIn.close();
            this.socketOut.close();
            this.socket.close();
        }
    }

    /**
     * Asks the Server IP to the user.
     * @return the Server IP.
     */
    private String askIP() {
        String response;
        do {
            System.out.println("> Insert the server ip: ");
            System.out.print("> ");
            response = scanner.nextLine();
        }while(!validIP(response));

        return response;
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
     * Checks if the response is a valid IP.
     * @param chosenIP server IP from the user.
     * @return true if the response is a valid IP.
     */
    private boolean validIP(String chosenIP) {
        try {
            if (chosenIP == null || chosenIP.isEmpty()) {
                System.out.println("[Client, validIP]: invalid ip, empty");
                return false;
            }

            String[] nets = chosenIP.split( "\\.", -1);
            if (nets.length != 4) {
                System.out.println("[Client, validIP]: invalid ip, wrong number of nets");
                return false;
            }

            for (String net : nets) {
                int i = Integer.parseInt(net);
                if (i < 0 || i > 255) {
                    System.out.println("[Client, validIP]: invalid ip, wrong net number");
                    return false;
                }
            }

            return true;
        } catch (NumberFormatException e) {
            System.out.println("[Client, validIP]: invalid ip, number format exception");
            return false;
        }
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

        return port > 0 && port <= 65535;
    }

    /**
     * This method is used to check if the client can reconnect and start playing again by testing the server response.
     * If the client is able to reconnect it will the player.
     */
    private void disconnect() {
        this.view.endView();
        // if it's cli we terminate the process without asking, while if it's gui we wait the button pressed
        if(this.view instanceof CLIView)    // maybe the gui will always terminate without waiting for a button pressed
            System.exit(0);
    }

    /**
     * @param CLIorGUI whether the view selected from the user is CLI (true) or GUI(false)
     */
    public void setCLIorGUI(boolean CLIorGUI){
        this.CLIorGUI = CLIorGUI;
    }

    /**
     * Enabling the pinger allows to check if the connection with the server is lost from one of the clients.
     * @param enabled true if we want to start ping the server; false to shut down the pinger.
     */
    public void enablePinger(boolean enabled) {
        if (enabled) {
            // ping the server in order to detect socket closed
            pinger.scheduleAtFixedRate(() -> this.asyncWriteToSocket("Ping"), 0, 500, TimeUnit.MILLISECONDS);
        } else {
            pinger.shutdownNow();
        }
    }
}
