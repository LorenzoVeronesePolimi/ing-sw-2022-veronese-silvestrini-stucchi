package it.polimi.ingsw.Model.Game;

import it.polimi.ingsw.Model.Player;

import java.util.List;

public class GameFour implements Game {
    // This will have specific methods for a Game of 4 players
    private Player player1, player2, player3, player4;

    public GameFour(List<Player> param_players){
        this.player1 = param_players.get(0);
        this.player2 = param_players.get(1);
        this.player3 = param_players.get(2);
        this.player3 = param_players.get(3);
    }

    public void addPlayer(Player player){}

    public Board buildBoard(){}
}
