package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.List;

public class BoardFactory {
}
package it.polimi.ingsw.Model.Game;

        import it.polimi.ingsw.Model.Player;

        import java.util.ArrayList;
        import java.util.List;

public class GameFactory {

    private List<Player> players;

    public GameFactory(List<Player> param_players) {
        this.players = new ArrayList<Player>();
        this.players.addAll(param_players);

        createGame(players.size());
    }

    //TODO: check if return copy needed
    public Game createGame(Integer numPlayers){
        Game result;
        try {
            if (numPlayers == 2) {
                return result = new GameTwo(this.players);
            } else if (numPlayers == 3) {
                return result = new GameThree(this.players);
            } else if (numPlayers == 4) {
                return result = new GameFour(this.players);
            }
        } catch (Exception e) {

        }
    }
}
