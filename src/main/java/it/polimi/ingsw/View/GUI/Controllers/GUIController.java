package it.polimi.ingsw.View.GUI.Controllers;

import it.polimi.ingsw.Client.Client;
import it.polimi.ingsw.View.GUI.GUIViewFX;

/**
 * interface of gui controllers
 */
public interface GUIController {
    /**
     * setter of gui fx
     * @param gui gui fx to be set
     */
    void setGUIFX(GUIViewFX gui);

    /**
     * setter of client
     * @param client client to be set
     */
    void setClient(Client client);
}
