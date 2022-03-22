package it.polimi.ingsw.Model.Pawns;

import it.polimi.ingsw.Model.Player;

public class Tower {
    private Player player;

    public Tower(Player p) {
        player = p;
    }

    public Player getPlayer() {
        return this.player;
    }

    @Override
    public String toString() {
        return "Tower{" +
                "player=" + player +
                '}';
    }
}
