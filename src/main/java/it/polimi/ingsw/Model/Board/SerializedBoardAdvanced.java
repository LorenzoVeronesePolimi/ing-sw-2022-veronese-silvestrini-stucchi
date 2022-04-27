package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Cards.AbstractCharacterCard;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Pawns.MotherNature;
import it.polimi.ingsw.Model.Places.Archipelago;
import it.polimi.ingsw.Model.Places.Cloud;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Player;

import java.util.List;
import java.util.Map;

public class SerializedBoardAdvanced {
    private List<Archipelago> archipelagos;
    private List<Cloud> clouds;
    private MotherNature mn;
    private Map<Player, School> playerSchool;
    private SPColour colourToExclude = null;
    private List<AbstractCharacterCard> extractedCards;

    public SerializedBoardAdvanced(List<Archipelago> archipelagos, List<Cloud> clouds, MotherNature mn, Map<Player, School> playerSchool, SPColour colourToExclude, List<AbstractCharacterCard> extractedCards) {
        this.archipelagos = archipelagos;
        this.clouds = clouds;
        this.mn = mn;
        this.playerSchool = playerSchool;
        this.colourToExclude = colourToExclude;
        this.extractedCards = extractedCards;
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

    public SPColour getColourToExclude() {
        return colourToExclude;
    }

    public List<AbstractCharacterCard> getExtractedCards() {
        return extractedCards;
    }
}
