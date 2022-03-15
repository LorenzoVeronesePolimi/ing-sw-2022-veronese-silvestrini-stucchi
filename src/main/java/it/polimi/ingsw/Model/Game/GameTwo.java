package it.polimi.ingsw.Model.Game;

import it.polimi.ingsw.Model.Board;
import it.polimi.ingsw.Model.Player;

import java.util.List;

public class GameTwo implements Game {
    // This will have specific methods for a Game of 2 players
    private Player player1, player2;

    public GameTwo(List<Player> param_players){
        this.player1 = param_players.get(0);
        this.player2 = param_players.get(1);
    }

    public void addPlayer(Player player){};

    public Board buildBoard(){};
}
