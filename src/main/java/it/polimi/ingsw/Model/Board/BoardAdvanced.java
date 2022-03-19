package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Places.Archipelago;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoardAdvanced extends Board {
    public BoardAdvanced(List<Player> players) {
        super(players);
    }

    public List<Archipelago> getArchiList(){
        return new ArrayList<Archipelago>(archipelagos);
    }

    //void moveStudentSchoolToBag(SPColour c);
}
