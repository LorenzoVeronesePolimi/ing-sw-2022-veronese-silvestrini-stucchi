package it.polimi.ingsw.Controller.Messages;

import static it.polimi.ingsw.Controller.Enumerations.MessageType.MOVE_MOTHER_NATURE;

public class MessageMoveMotherNature extends Message{
    private final String nicknamePlayer;
    private final int moves;

    public MessageMoveMotherNature(String nicknamePlayer, int moves){
        super(MOVE_MOTHER_NATURE);
        this.nicknamePlayer = nicknamePlayer;
        this.moves = moves;
    }

    public String getNicknamePlayer() {
        return nicknamePlayer;
    }

    public int getMoves() {
        return moves;
    }
}
