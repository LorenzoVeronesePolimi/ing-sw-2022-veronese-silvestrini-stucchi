package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.Model.Board.SerializedBoardAbstract;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.View.GUI.Controllers.*;
import it.polimi.ingsw.View.GUIView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.swing.text.html.Option;
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
    private static final String BOARD_FOUR_ADVANCED = "BoardGrid.fxml"; //TODO: to be changed
    private static final String INTRO_CSS = "Intro.css";
    private static final String LOGIN_CSS = "Login.css";
    private static final String LOADING_CSS = "LoadingCSS.css";
    private static final String BOARD_FOUR_ADVANCED_CSS = "Intro.css"; //TODO: to be changed
    private final HashMap<String, Scene> sceneMap = new HashMap<>();
    private final HashMap<String, GUIController> controllerMap = new HashMap<>();

    public GUIViewFX() {

    }

    public GUIViewFX(Client client, GUIView view) {
        this.client = client;
        this.clientView = view;
        this.clientView.setGuiViewFX(this);
    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.setupControllers();    // This method is called to setup all scene prior to the display
        this.stage = stage;
        this.run(); // running the real stage
    }

    private void setupControllers() {
        // creating an array of scenes (All the scenes of the application)
        List<String> sceneList = new ArrayList<>(Arrays.asList(INTRO, LOADING, LOGIN, BOARD_FOUR_ADVANCED));
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
                    /*case "BoardGrid.fxml":
                        css = getClass().getResource("/css" + BOARD_FOUR_ADVANCED_CSS).toExternalForm();
                        sceneMap.get(BOARD_FOUR_ADVANCED).getStylesheets().add(css);
                        break;*/
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

    public void run() {
        System.out.println("GUI running");
        Locale.setDefault(new Locale("en", "english"));
        stage.setResizable(false);

        // Eriantys logo
        Image icon = new Image("/images/eriantys.jpg");
        stage.getIcons().add(icon);

        // Windows title
        stage.setTitle("Eriantys board game");
        stage.setFullScreen(false);

        stage.setScene(currentScene);
        stage.show();
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void sceneLoading(String scene, String message) {
        LoaderController currentController = (LoaderController) controllerMap.get(scene);
        currentController.setMessage(message);

        this.currentScene = sceneMap.get(scene);
        this.stage.setScene(this.currentScene);
        this.stage.show();
    }

    public void sceneAskFirstPlayerInfo(String scene){
        LoginController currentController = (LoginController) controllerMap.get(scene);
        currentController.setFirstPlayer(true);

        this.currentScene = sceneMap.get(scene);
        this.stage.setScene(this.currentScene);
        this.stage.show();
    }

    public void sceneAskNickname(String scene, List<PlayerColour> colourList, int numPlayers){
        /*  Example of what should be done for every scene:
             - receiving the parameters from Message->GUIView
             - passing them to the scene controller in order to show personalized info
             - when showing a scene the controller SHOULD execute the initialize method first (try this, I'm not sure)
        */
        LoginController currentController = (LoginController) controllerMap.get(scene);
        currentController.setFirstPlayer(false);    //used to show firstPlayer or not
        currentController.setNumPlayers(numPlayers);    // used to choose the colour automatically
        currentController.setAvailableColours(colourList);  // used to modify the possible colour (it uses the numPlayer value)


        this.currentScene = sceneMap.get(scene);
        this.stage.setScene(this.currentScene);
        this.stage.show();
    }

    public void sceneAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.showAndWait();
    }

    public void sceneShowBoard(String scene, SerializedBoardAbstract board){
        BoardFourAdvancedController currentController = (BoardFourAdvancedController) controllerMap.get(scene);
        currentController.setArchipelagoFxml(board);

        this.currentScene = sceneMap.get(scene);
        this.stage.setScene(this.currentScene);
        this.stage.show();
    }
}

