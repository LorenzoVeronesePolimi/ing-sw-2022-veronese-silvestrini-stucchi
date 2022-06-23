package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * class that builds the right concrete (not advanced) board (BoardTwo/BoardThree/BoardFour), given the list of players connected to the server
 */
public class BoardFactory {
    private final List<Player> players;

    /**
     * constructor of the factory
     * @param players list of player connected to the server
     */
    public BoardFactory(List<Player> players) {
        this.players = new ArrayList<>();
        this.players.addAll(players);
    }

    /**
     * method that creates the right board
     * @return
     */
    public BoardAbstract createBoard(){
        try {
            if (this.players.size() == 2) {
                return new BoardTwo(this.players);
            } else if (this.players.size() == 3) {
                return new BoardThree(this.players);
            } else if (this.players.size() == 4) {
                return new BoardFour(this.players);
            }
        } catch (Exception e) {e.printStackTrace();}

        return null;
    }
}
