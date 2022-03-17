package it.polimi.ingsw.Model.Pawns.Board;

import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.List;

public class BoardFactory {
    private List<Player> players;

    public BoardFactory(List<Player> param_players) {
        this.players = new ArrayList<Player>();
        this.players.addAll(param_players);
    }

    //TODO: check if return copy needed
    public Board createBoard(){
        try {
            if (players.size() == 2) {
                return new BoardTwo(this.players);
            } else if (players.size() == 3) {
                return new BoardThree(this.players);
            } else if (players.size() == 4) {
                return new BoardFour(this.players);
            }
        } catch (Exception e) {

        }

        return null;
    }
}
