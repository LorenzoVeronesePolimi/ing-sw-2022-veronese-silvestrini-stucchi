package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Enumerations.CharacterCardEnumeration;
import it.polimi.ingsw.Model.Exceptions.AnotherTowerException;
import it.polimi.ingsw.Model.Exceptions.ExceededMaxTowersException;
import it.polimi.ingsw.Model.Exceptions.InvalidTowerNumberException;
import it.polimi.ingsw.Model.Exceptions.TowerNotFoundException;
import it.polimi.ingsw.Model.Player;

import java.io.Serializable;

/**
 * This class represents the card that has this effect:
 * the player chooses an archipelago and tries to conquer it as if mother nature has ended her movement on that archipelago.
 * After that, the round continues normally.
 */
public class FakeMNMovement extends AbstractCharacterCard implements Serializable {

    /**
     * Constructor of the card.
     * @param boardAdvanced The object modified by the card.
     */
    public FakeMNMovement(BoardAdvanced boardAdvanced){
        super(CharacterCardEnumeration.FAKE_MN_MOVEMENT, boardAdvanced,3);
    }

    /**
     * This method activates the effect of the card.
     * @param currentPlayer The player that has activated the card.
     * @param fakeMNPosition The index of the archipelago where the player wants to put Mother Nature.
     * @throws InvalidTowerNumberException When conquering, if the number of towers on the archipelago si different from
     *                                     the number of tower moved from the conquering school.
     * @throws AnotherTowerException When conquering, if the tower on the conquered archipelago has not been removed.
     * @throws ExceededMaxTowersException When conquering, if there is already the maximum number of towers in the school.
     * @throws TowerNotFoundException When conquering, if there are no towers in the school or in the archipelago.
     */
    public void useEffect(Player currentPlayer, int fakeMNPosition) throws
            InvalidTowerNumberException, AnotherTowerException, ExceededMaxTowersException, TowerNotFoundException {

        int mnRealPosition = boardAdvanced.whereIsMotherNature();

        boardAdvanced.moveMotherNature(fakeMNPosition);
        boardAdvanced.tryToConquer(currentPlayer); //if conquerable -> conquer
        boardAdvanced.moveMotherNatureInArchipelagoIndex(mnRealPosition);
    }
}
