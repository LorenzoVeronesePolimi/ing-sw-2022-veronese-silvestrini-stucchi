package it.polimi.ingsw.Model.Pawns;

import it.polimi.ingsw.Model.Player;

import java.io.Serializable;

/**
 * This class represents the entity of a tower, that is used to mark the archipelagos that have been
 * conquered, and by whom.
 */
public class Tower implements Serializable {
    private final Player player;

    /**
     * The constructor builds a Tower owned by player p.
     * @param p is the owner of the tower to build.
     */
    public Tower(Player p) {
        player = p;
    }

    /**
     *
     * @return the owner of the tower that calls the method.
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     *
     * @return the conversion to string of the Tower object.
     */
    @Override
    public String toString() {
        return "Tower{" +
                "player=" + player +
                '}';
    }
}
