package it.polimi.ingsw.Model.Game;

import it.polimi.ingsw.Model.Player;

public interface Game {

    public void addPlayer(Player player);

    public Board buildBoard();
}
