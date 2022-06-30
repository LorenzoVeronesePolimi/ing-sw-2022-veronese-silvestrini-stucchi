package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Controller.Controller;
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

/**
 * class for the serialization of the board advanced
 */
public class SerializedBoardAdvanced extends SerializedBoardAbstract implements Serializable {
    private static final long serialVersionUID = 1L;
    private SPColour colourToExclude;
    private List<AbstractCharacterCard> extractedCards;
    private String nameCardUsed = "";

    /**
     * constructor of the serialized board abstract with complete information
     * @param archipelagos list of archipelagos with all the information
     * @param clouds list of clouds with all the information
     * @param mn mother nature
     * @param schoolList list of opponent schools
     * @param colourToExclude colour that has ben excluded form counting
     * @param extractedCards list of extracted cards
     */
    public SerializedBoardAdvanced(List<Archipelago> archipelagos, List<Cloud> clouds, MotherNature mn,
                                   List<School> schoolList, SPColour colourToExclude, String nameCardUsed, List<AbstractCharacterCard> extractedCards) {
        super(archipelagos, clouds, mn, schoolList);
        super.type = "advanced";
        this.colourToExclude = colourToExclude;
        this.extractedCards = extractedCards;
        this.nameCardUsed = nameCardUsed;
    }


    /**
     * constructor of the serialized board abstract with hidden information about the opponent hand of usable assistant cards
     * @param archipelagos list of archipelagos with all the information
     * @param clouds list of clouds with all the information
     * @param mn mother nature
     * @param schoolList list of opponent schools
     * @param colourToExclude colour that has ben excluded form counting
     * @param extractedCards list of extracted cards
     * @param nickname nick of the player to which the serialized board will arrive
     */
    public SerializedBoardAdvanced(List<Archipelago> archipelagos, List<Cloud> clouds, MotherNature mn,
                                   List<School> schoolList, SPColour colourToExclude, String nameCardUsed,
                                   List<AbstractCharacterCard> extractedCards, String nickname) {
        super(archipelagos, clouds, mn, schoolList, nickname);
        super.type = "advanced";
        this.colourToExclude = colourToExclude;
        this.extractedCards = extractedCards;
        this.nameCardUsed = nameCardUsed;
    }

    /**
     * getter of the colour that has been excluded from counting
     * @return colour that has been excluded from counting
     */
    public SPColour getColourToExclude() {
        return colourToExclude;
    }

    /**
     * getter of the list of extracted cards
     * @return list of extracted cards
     */
    public List<AbstractCharacterCard> getExtractedCards() {
        return extractedCards;
    }

    /**
     * getter of nameCardUsed
     * @return the value of nameCardUsed
     */
    public String getNameCardUsed() {
        return this.nameCardUsed;
    }
}
