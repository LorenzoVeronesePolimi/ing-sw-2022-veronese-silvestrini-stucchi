package it.polimi.ingsw.Controller.Enumerations;

/**
 * Enumeration for Controller states.
 */
public enum State {
    CONNECTING,
    WAITING_PLAYERS,
    PLANNING1, //this is an automatic part of the game
    PLANNING2,
    ACTION1,
    ACTION2,
    ACTION3,
    TURN,
    END
}
