package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Pawns.Coin;
import it.polimi.ingsw.Model.Places.Cloud;
import it.polimi.ingsw.Model.Player;

import java.util.List;

public interface BoardAdvanced extends Board{
    List<Coin> bank;
    void moveStudentSchoolToBag(SPColour c);

}
