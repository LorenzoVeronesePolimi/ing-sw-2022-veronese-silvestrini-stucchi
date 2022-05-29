package it.polimi.ingsw.View.GUI.Controllers;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.View.GUI.GUIViewFX;

public interface GUIController {
    void setGUIFX(GUIViewFX gui);
    void setClient(Client client);
}
