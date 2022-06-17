package it.polimi.ingsw.Controller;

import java.io.*;
import java.nio.file.Files;

public class PersistenceHandler {
    String GAME_SAVED_PATH = "game_saved.bless";

    public void saveMatch(Controller controller) {
        SerializedController serializedController = new SerializedController(controller);

        try {
            FileOutputStream fileOutput = new FileOutputStream(GAME_SAVED_PATH);
            ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
            objectOutput.writeObject(serializedController);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Controller restoreMatch() {
        SerializedController serializedController;

        try {
            FileInputStream fileInput = new FileInputStream(GAME_SAVED_PATH);

            ObjectInputStream objectInput = new ObjectInputStream(fileInput);

            serializedController = (SerializedController) objectInput.readObject();

            return serializedController.getController();

        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public void deleteMatch() {
        File file = new File(GAME_SAVED_PATH);
        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
