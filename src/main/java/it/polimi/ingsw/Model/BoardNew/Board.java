package it.polimi.ingsw.Model.BoardNew;

import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Places.Archipelago;
import it.polimi.ingsw.Model.Player;

public interface Board {
    public void tryToConquer();

    public boolean checkIfConquerable();

}
