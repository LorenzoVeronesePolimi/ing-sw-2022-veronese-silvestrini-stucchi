package it.polimi.ingsw.Model.Game;

import it.polimi.ingsw.Model.Cards.CharacterCard;

public interface GameAdvanced {
    public void getRandomCharCards();

    public void activateEffect(CharacterCard card);
}
