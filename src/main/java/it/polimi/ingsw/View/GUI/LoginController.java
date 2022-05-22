package it.polimi.ingsw.View.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LoginController {
    @FXML
    private String nicknameLogin;
    private String[] availableColours = {"Black", "White", "Gray"};
    private Integer[] availableNumPlayers = {2, 3, 4};
    private String[] availableModes = {"Normal", "Advanced"};

    public void loginControl(ActionEvent e){
        //client.asyncWriteToSocket("addPlayer " + nickname + " " + colour);
    }
}
