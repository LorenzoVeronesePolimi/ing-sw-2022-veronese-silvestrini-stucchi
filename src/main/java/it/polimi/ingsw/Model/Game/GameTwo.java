package it.polimi.ingsw.Model.Game;

import it.polimi.ingsw.Model.Board;
import it.polimi.ingsw.Model.Player;

public class GameTwo implements Game {
    // This will have specific methods for a Game of 2 players
    private Player player1, player2;

    public GameTwo(Player p1, Player p2){
        this.player1 = p1;
        this.player2 = p2;
    }

    public void addPlayer(Player player){};

    public Board buildBoard(){};
}
