package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Cards.AbstractCharacterCard;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Pawns.MotherNature;
import it.polimi.ingsw.Model.Places.Archipelago;
import it.polimi.ingsw.Model.Places.Cloud;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Player;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SerializedBoardAdvanced extends SerializedBoardAbstract implements Serializable {
    private SPColour colourToExclude;
    private List<AbstractCharacterCard> extractedCards;

    public SerializedBoardAdvanced(List<Archipelago> archipelagos, List<Cloud> clouds, MotherNature mn, Map<Player, School> playerSchool, SPColour colourToExclude, List<AbstractCharacterCard> extractedCards) {
        super(archipelagos, clouds, mn, playerSchool);
        this.colourToExclude = colourToExclude;
        this.extractedCards = extractedCards;
    }

    public SPColour getColourToExclude() {
        return colourToExclude;
    }

    public List<AbstractCharacterCard> getExtractedCards() {
        return extractedCards;
    }
}
