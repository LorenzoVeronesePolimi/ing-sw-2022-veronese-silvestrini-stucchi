package it.polimi.ingsw.Persistence;

import it.polimi.ingsw.Controller.Controller;

import java.io.*;
import java.nio.file.Files;

/**
 * class that implements the advanced functionality of persistence (when a match is interrupted because of a client disconnection,
 * the game status is saved, and if all the players reconnect to the server the match will continue from where it stopped)
 */
public class PersistenceHandler {
    String GAME_SAVED_PATH = "game_saved.bless";

    /**
     * method creates a serialized controller and saves it in a file
     * @param controller controller to be saved
     */
    public void saveMatch(Controller controller) {
        SerializedController serializedController = new SerializedController(controller);

        try {
            FileOutputStream fileOutput = new FileOutputStream(GAME_SAVED_PATH);
            ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
            objectOutput.writeObject(serializedController);
            objectOutput.close();
        }
        catch (IOException e) {
            System.out.println("Impossible to save the match");
        }
    }

    /**
     * method that reads from a file (where the controller was saved after the disconnection) the serialized controller
     * @return controller that was saved
     */
    public Controller restoreMatch() {
        SerializedController serializedController;

        try {
            FileInputStream fileInput = new FileInputStream(GAME_SAVED_PATH);
            ObjectInputStream objectInput = new ObjectInputStream(fileInput);
            serializedController = (SerializedController) objectInput.readObject();
            objectInput.close();

            return serializedController.getController();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Save file doesn't exists, or error during it's opening: no match to recover");
            return null;
        }
    }

    /**
     * method that deletes the file containing information about the match
     */
    public void deleteMatch() {
        File file = new File(GAME_SAVED_PATH);
        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
