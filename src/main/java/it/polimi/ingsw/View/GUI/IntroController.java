package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Client.Client;
import javafx.event.ActionEvent;
import java.io.IOException;


public class IntroController {

    public void startButton(ActionEvent e) throws IOException {
        synchronized (Client.lock){ //wake up Client thread so that it displays login fxml
            Client.lock.notify();
        }
    }

}
