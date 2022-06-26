package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;
import it.polimi.ingsw.Model.Board.SerializedBoardAdvanced;
import it.polimi.ingsw.Model.Enumerations.CharacterCardEnumeration;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.View.GUI.Controllers.*;
import it.polimi.ingsw.View.GUIView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.*;

//TODO: implement method changeScene<particolare> in order to pass to the controller the parameter needed (the ones passed in GUIView
//TODO: when calling an ask method
public class GUIViewFX extends Application {
    private Stage stage;
    private Client client;
    private GUIView clientView;
    private Scene currentScene;
    private static final String INTRO = "Intro.fxml";
    private static final String LOGIN = "Login.fxml";
    private static final String LOADING = "LoadingPage.fxml";
    private static final String DISCONNECT = "Disconnect.fxml";
    private static final String ASSISTANT_CARD = "AssistantCardChoice.fxml";
    private static final String BOARD_FOUR_ADVANCED = "BoardGrid.fxml"; //TODO: to be changed
    private static final String CHARACTER_CARD_DIALOG = "CharacterCardDialog.fxml";
    private static final String SHOW_WINNER = "ShowWinner.fxml";
    private static final String INTRO_CSS = "Intro.css";
    private static final String LOGIN_CSS = "Login.css";
    private static final String LOADING_CSS = "LoadingCSS.css";
    private static final String BOARD_FOUR_ADVANCED_CSS = "BoardGrid.css"; //TODO: to be changed
    private static final String CHARACTER_CARD_DIALOG_CSS = "CharacterCardDialog.css";
    private static final String SHOW_WINNER_CSS = "ShowWinner.css";
    private final HashMap<String, Scene> sceneMap = new HashMap<>();
    private final HashMap<String, GUIController> controllerMap = new HashMap<>();

    /**
     * default constructor
     */
    public GUIViewFX() {

    }

    /**
     * constructor of GUI view fx, given a client and a GUI view
     * @param client owner of GUI view fx
     * @param view GUI view
     */
    public GUIViewFX(Client client, GUIView view) {
        this.client = client;
        this.clientView = view;
        this.clientView.setGuiViewFX(this);
    }
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * method that starts the application with the primary stage, which is given as parameter
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        this.setupControllers();    // This method is called to setup all scene prior to the display
        this.stage = stage;
        this.run(); // running the real stage
    }

    /**
     * method that prepares all the stages, and sets the Intro one as the current scene
     */
    private void setupControllers() {
        // creating an array of scenes (All the scenes of the application)
        List<String> sceneList = new ArrayList<>(Arrays.asList(INTRO, LOADING, LOGIN, DISCONNECT, ASSISTANT_CARD, BOARD_FOUR_ADVANCED, SHOW_WINNER));
        try {
            for(String path : sceneList) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + path)); // prepare the scene
                Parent root = loader.load();    //load the scene
                GUIController controller = loader.getController();  // get the controller of the scene

                sceneMap.put(path, new Scene(root));    // creating a map of scenes based on their path
                controller.setGUIFX(this);  // set the GUIFX in the controller (don't know if it's necessary)
                controller.setClient(this.client);  // set the client in controller (mandatory to call asyncWriteToSocket)
                //this.client.printOut("client in gui");
                controllerMap.put(path, controller);    // create a map of controller and path (don't know if it's necessary)

                // applying css to the scenes
                String css;
                switch (path) {
                    case "Intro.fxml":
                        css = getClass().getResource("/css/" + INTRO_CSS).toExternalForm();
                        sceneMap.get(INTRO).getStylesheets().add(css);
                        break;

                    case "Loading.fxml":
                        css = getClass().getResource("/css/" + LOADING_CSS).toExternalForm();
                        sceneMap.get(LOADING).getStylesheets().add(css);
                        break;
                    case "Login.fxml":
                        css = getClass().getResource("/css/" + LOGIN_CSS).toExternalForm();
                        sceneMap.get(LOGIN).getStylesheets().add(css);
                        break;
                    case "BoardGrid.fxml":
                        css = getClass().getResource("/css/" + BOARD_FOUR_ADVANCED_CSS).toExternalForm();
                        sceneMap.get(BOARD_FOUR_ADVANCED).getStylesheets().add(css);
                        break;
                    case "ShowWinner.fxml":
                        css = getClass().getResource("/css/" + SHOW_WINNER_CSS).toExternalForm();
                        sceneMap.get(SHOW_WINNER).getStylesheets().add(css);
                        break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentScene = sceneMap.get(INTRO); //initial scene

        /*
            The first scene shown in the intro page.
            After a player clicks on the play button the GUIView is set free (platformReady = true)
            and the GUI can execute the askFirstPlayerInfo.
            When the second player clicks on the play button it waits until the previous player has sent his login info (as in CLI)
            It's necessary to implement a waiting scene (loading scene)
         */
    }

    /**
     * method that runs the GUI application
     */
    public void run() {
        //System.out.println("GUI running");
        Locale.setDefault(new Locale("en", "english"));
        stage.setResizable(false);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

        // Eriantys logo
        Image icon = new Image("/images/eriantys.jpg");
        stage.getIcons().add(icon);

        // Windows title
        stage.setTitle("Eriantys board game");
        stage.setFullScreen(false);

        stage.setScene(currentScene);
        stage.show();
    }

    /**
     * setter of the stage title
     * @param title stage title to be set
     */
    public void setStageTitle(String title) {
        stage.setTitle(title);
    }

    /**
     * setter of the client
     * @param client client to be set for the application
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * method that changes the scene into the Loading one
     * @param message message that has to be shown in the loading stage
     */
    public void sceneLoading(String message) {
        LoaderController currentController = (LoaderController) controllerMap.get(LOADING);
        currentController.setMessage(message);

        this.currentScene = sceneMap.get(LOADING);
        this.stage.setScene(this.currentScene);
        this.stage.show();
    }

    /**
     * method that changes the scene into the askPlayerInfo one
     */
    public void sceneAskFirstPlayerInfo(){
        LoginController currentController = (LoginController) controllerMap.get(LOGIN);
        currentController.setFirstPlayer(true);

        this.currentScene = sceneMap.get(LOGIN);
        this.stage.setScene(this.currentScene);
        this.stage.show();
    }

    /**
     * method that changes the scene into the askNickname one
     */
    public void sceneAskNickname(List<PlayerColour> colourList, int numPlayers){
        /*  Example of what should be done for every scene:
             - receiving the parameters from Message->GUIView
             - passing them to the scene controller in order to show personalized info
             - when showing a scene the controller SHOULD execute the initialize method first (try this, I'm not sure)
        */
        LoginController currentController = (LoginController) controllerMap.get(LOGIN);
        currentController.setFirstPlayer(false);    //used to show firstPlayer or not
        currentController.setNumPlayers(numPlayers);    // used to choose the colour automatically
        currentController.setAvailableColours(colourList);  // used to modify the possible colour (it uses the numPlayer value)


        this.currentScene = sceneMap.get(LOGIN);
        this.stage.setScene(this.currentScene);
        this.stage.show();
    }

    /**
     * method that generates an alert for the scene
     * @param msg message that describes the alert
     * @param type type of alert
     */
    public void sceneAlert(String msg, Alert.AlertType type) {
        Alert alert = new Alert(type, msg);
        alert.showAndWait();
    }

    /*
    public void characterCardAlert(String effect){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, effect);
        alert.showAndWait();
    }*/

    /**
     *
     * @param type
     * @param board
     */
    public void characterCardAlert(CharacterCardEnumeration type, SerializedBoardAdvanced board) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/CharacterCardDialog.fxml"));
        Parent parent = null;
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        CharacterCardDialogController controller = fxmlLoader.<CharacterCardDialogController>getController();

        controller.setCardType(type);
        controller.setBoard(board);
        controller.setClient(this.client);
        controller.setGuiViewFX(this);
        controller.setVisualization();
        /*controller.setCardName(name);
        controller.setCardEffect(effect);
        controller.setCardImage(imagePath);
        controller.setCharacterCardActions(type);*/

        Stage alertStage = new Stage();
        Scene alertScene = new Scene(parent);
        Image icon = new Image("/images/eriantys.jpg");
        alertStage.getIcons().add(icon);
        alertScene.getStylesheets().add("/css/" + CHARACTER_CARD_DIALOG_CSS);
        alertStage.initModality(Modality.APPLICATION_MODAL);
        alertStage.setScene(alertScene);
        alertStage.showAndWait();
    }

    /**
     * method that sets the correct scene according to the game phase
     * @param board serialized board notified by the model
     */
    public void manageScene(SerializedBoardAbstract board){
        switch (board.getCurrentState()) {
            case PLANNING2:
                this.sceneAssistantCard("AssistantCardChoice.fxml", board);
                break;

            case ACTION1:
            case ACTION2:
            case ACTION3:
                this.sceneShowBoard("BoardGrid.fxml", board);
                break;

            case END:
                this.sceneShowWinner("ShowWinner.fxml", board);
                break;

        }
    }

    /**
     * method that manages the stage for the choice of the assistant card
     * @param scene name of the scene that has to be loaded (assistant card choice)
     * @param board serialized board notified by the model
     */
    private void sceneAssistantCard(String scene, SerializedBoardAbstract board) {
        if(!board.getCurrentPlayer().getNickname().equals(this.client.getNickname())) {
            Platform.runLater(() -> {
                this.sceneLoading("Wait for other player to do their move!");
            });
        } else {
            AssistantCardChoiceController currentController = (AssistantCardChoiceController) controllerMap.get(scene);
            currentController.setSerializedBoardAbstract(board);
            currentController.setDataStructures();

            this.currentScene = sceneMap.get(scene);
            this.stage.setScene(this.currentScene);
            this.stage.show();
        }
    }

    /**
     * method that manages the main stage (the one that shows the board)
     * @param scene name of the scene that has to be loaded (board)
     * @param board serialized board notified by the model
     */
    protected void sceneShowBoard(String scene, SerializedBoardAbstract board) {
        BoardFourAdvancedController currentController = (BoardFourAdvancedController) controllerMap.get(scene);
        if(board.getType().equals("standard")){
            currentController.setStandardSetup(); // set advanced elements to not visible in case of standard match
        }

        currentController.setBoard(board);
        currentController.setArchipelagosFxmlVisualization();
        currentController.setSchoolsFxmlVisualization();
        currentController.setCloudsVisualization();
        currentController.setAssistantCardsVisualization();
        if(board.getType().equals("advanced")) {
            currentController.setCharacterCardsVisualization();
        }
        currentController.setInstructionLabels();

        this.currentScene = sceneMap.get(scene);
        this.stage.setScene(this.currentScene);
        this.stage.show();
    }

    /**
     * method that manages the stage that shows the winner
     * @param scene name of the scene that has to be loaded
     * @param board serialized board notified by the model
     */
    public void sceneShowWinner(String scene, SerializedBoardAbstract board){
        ShowWinnerController currentController = (ShowWinnerController) controllerMap.get(scene);

        boolean amIWinner = false;
        String[] winners = board.getNicknameWinner().split("\\*");
        for(String winner : winners){
            if(winner.equals(this.client.getNickname())){
                amIWinner = true;
                break;
            }
        }

        currentController.setVisualization(board.getSitPlayers().size(), board.getNicknameWinner(), amIWinner);

        this.currentScene = sceneMap.get(scene);
        this.stage.setScene(this.currentScene);
        this.stage.show();
    }

    /**
     * method that manages the stage that notifies that a client has disconnected
     * @param msg string that must be printed
     */
    public void sceneClientDisconnect(String msg) {
        DisconnectController currentController = (DisconnectController) controllerMap.get(DISCONNECT);
        currentController.setMessage(msg);

        this.currentScene = sceneMap.get(DISCONNECT);
        this.stage.setScene(this.currentScene);
        this.stage.show();
    }

}

