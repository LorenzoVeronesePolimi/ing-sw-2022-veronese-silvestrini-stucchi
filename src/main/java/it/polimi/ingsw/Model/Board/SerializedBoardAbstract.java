package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Pawns.MotherNature;
import it.polimi.ingsw.Model.Places.Archipelago;
import it.polimi.ingsw.Model.Places.Cloud;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Player;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SerializedBoardAbstract implements Serializable {
    private List<Archipelago> archipelagos;
    private List<Cloud> clouds;
    private MotherNature mn;
    private Map<Player, School> playerSchool;

    public SerializedBoardAbstract(List<Archipelago> archipelagos, List<Cloud> clouds, MotherNature mn, Map<Player, School> playerSchool) {
        this.archipelagos = archipelagos;
        this.clouds = clouds;
        this.mn = mn;
        this.playerSchool = playerSchool;
    }

    public List<Archipelago> getArchipelagos() {
        return archipelagos;
    }

    public List<Cloud> getClouds() {
        return clouds;
    }

    public MotherNature getMn() {
        return mn;
    }

    public Map<Player, School> getPlayerSchool() {
        return playerSchool;
    }
}
