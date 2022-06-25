package it.polimi.ingsw.Client;

import it.polimi.ingsw.Messages.ActiveMessageView;
import it.polimi.ingsw.Messages.INMessages.PingMessage;
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
    private boolean clientReconnect = false;    // if the player wants to reconnect
    private boolean clientError = false;        // if the disconnection is caused by a player disconnection
    private boolean serverError = false;        // if the disconnection is caused by a server disconnection
    private boolean endGame = false;            // if is the end of the game
    private boolean socketNull = true;          // if the socket is null
    private boolean platformReady = false;      // if the JavaFX platform is ready to operate
    private ActiveMessageView prevMessage = null;   // message used in case of errors
    private GUIViewFX guiViewFX;                // for activating GUIView
    private ScheduledExecutorService pinger;  // for check of connections
    private boolean exception = false; // to check wheather the client experienced an exception
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
     * @param rec value of clientReconnect.
     */
    public synchronized void setClientReconnect(boolean rec) { this.clientReconnect = rec; }

    /**
     * @param clientError whether the error in communication begun with one of the Clients.
     */
    public synchronized void setClientError(boolean clientError) {
        this.clientError = clientError;
    }

    /**
     * @return the value of clientError.
     */
    public synchronized boolean getClientError() {
        return this.clientError;
    }

    /**
     * @param serverError the next value of serverError.
     */
    public synchronized void setServerError(boolean serverError) {
        this.serverError = serverError;
    }

    /**
     * @return the value of serverError.
     */
    public synchronized boolean getServerError() {
        return this.serverError;
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
     * @param ex whether an exception occurred or not.
     */
    public synchronized void setExceptionStatus(boolean ex) {
        this.exception = ex;
    }

    /**
     * @return the value of exception.
     */
    public synchronized boolean getExceptionStatus() {
        return this.exception;
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
     * @param socketnull the next value of socketNull.
     */
    public synchronized void setSocketNull(boolean socketnull) {
        this.socketNull = socketnull;
    }

    /**
     * @return the value of socketNull.
     */
    public synchronized boolean getSocketNull() {
        return this.socketNull;
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
                                // save the message in order to manage errors and let the player repeat the move
                                prevMessage = (ActiveMessageView) inputMessage;
                            }
                        }
                    }

                }
            } catch (SocketException e) {
                enablePinger(false);
                System.out.println("[Client, asyncReadFromSocket]: SocketException");
                //this.view.printErrorMessage("Error. You have been disconnected!");
                this.setExceptionStatus(true);
                checkErrorSource();

                //e.printStackTrace();
            } catch (IOException e) {
                enablePinger(false);
                System.out.println("[Client, asyncReadFromSocket]: IOException");
                this.setExceptionStatus(true);
                checkErrorSource();
            }catch (Exception e) {
                //System.out.println("[Client, asyncReadFromSocket]: exception");
                //e.printStackTrace();
            } finally {
                if (!this.getExceptionStatus() && !this.getEndGame())
                    this.setSocketNull(true);
                System.out.println("[Client, asyncReadFromSocket]: finally");

                this.setActive(false);
                //checkPossibleReconnect(); // TODO: maybe this here and not in asyncWrite
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
            //e.printStackTrace();
            System.out.println("[Client, asyncWriteFromSocket]: asyncWriteException");

            try {
                this.socketIn.close();  // this will generate an IOException in readThread executing asyncReadFromSocket
            } catch (IOException ex) {
                System.out.println("[Client, asyncWriteFromSocket]: socket already closed");
            }

            this.enablePinger(false);
            this.setExceptionStatus(true);

            // if it is a client error or a server error
            checkErrorSource(); // TODO: check if needed, i think not

            this.setActive(false);
            //checkPossibleReconnect();   // TODO: maybe this is in finally of asyncRead or only in connecting
        }
    }

    /**
     * This method is used to check whether the connection error is client side or server side, thus impeding the reconnection.
     */
    private void checkErrorSource() {
        this.view.printErrorMessage("Error. You have been disconnected!");
        this.setClientError(true);
        this.setSocketNull(true);

        // testing reconnection in order to identify server status
        try {
            this.socket = new Socket(this.ip, this.port);
        } catch(IOException ex) {
            this.setServerError(true);
            this.setClientError(false);
            this.view.printErrorMessage("Error 404. Server not responding.");
        } finally {
            if(!this.getServerError() && this.getClientError()) {
                this.setSocketNull(false);
            }
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
        this.setClientReconnect(false);
        this.setClientError(false);
        this.setEndGame(false);

        // connection to the server
        if(this.getSocketNull())
            this.socket = new Socket(this.ip, this.port);

        System.out.println("Connection established");
        this.setActive(true);
        this.setSocketNull(false);

        //Socket for communication
        this.socketOut = new ObjectOutputStream(socket.getOutputStream());
        this.socketOut.flush();
        this.socketIn = new ObjectInputStream(socket.getInputStream());

        // view
        this.view = new CLIView(this);

        try{
            // ready to listen to the server
            //Thread t0 = asyncReadFromSocket();
            this.pinger = Executors.newSingleThreadScheduledExecutor();
            this.enablePinger(true);
            asyncReadFromSocket();
            this.readThread.join();
            //t0.join();

            // when the match is somehow ended (error or normale game end)
            checkPossibleReconnect();

        } catch (/*InterruptedException |*/ NoSuchElementException e){
            System.out.println("[Client, connecting]: Connection closed from the client side");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("[Client, connecting]: interruptException");
        } finally {
            if(!this.getClientError() && !this.getServerError()) {
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
        String response;
        do {
            System.out.println("> Insert the server ip: ");
            System.out.print("> ");
            response = scanner.nextLine();
        }while(invalidIP(response));

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
        }while(invalidPort(response));

        return Integer.parseInt(response);
    }

    /**
     * Checks if the response is a valid IP.
     * @param response server IP from the user.
     * @return true if the response is a valid IP.
     */
    private boolean invalidIP(String response) {
        //TODO: check IP
        return false;
    }

    /**
     * Checks if the response is a valid Port.
     * @param response server Port from the user.
     * @return true if the response is a valid Port.
     */
    private boolean invalidPort(String response) {
        //TODO: check Port (also check that it is an int)
        return false;
    }

    /**
     * This method is used to check if the client can reconnect and start playing again by testing the server response.
     * If the client is able to reconnect it will the player.
     */
    private void checkPossibleReconnect() {
        if((this.getClientError() || this.getEndGame()) && !this.getServerError()) {
            try {
                this.askReconnect();    // TODO: ask reconnect in gui? remove button from client disconnect?
            } catch (IOException e) {
                this.view.printErrorMessage("[Client, checkPossibleReconnect]: reconnecting error");
                e.printStackTrace();
            }
        } else {
            this.view.endView();
            // if it's cli we terminate the process without asking, while if it's gui we wait the button pressed
            if(this.view instanceof CLIView)    // maybe the gui will always terminate without waiting for a button pressed
                System.exit(0);
        }
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
