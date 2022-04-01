package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.List;

public class BoardFactory {
    private final List<Player> players;

    public BoardFactory(List<Player> players) {
        this.players = new ArrayList<>();
        this.players.addAll(players);
    }

    //TODO: check if return copy needed
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
