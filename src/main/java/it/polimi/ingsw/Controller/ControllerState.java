package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Enumerations.MessageType;
import it.polimi.ingsw.Controller.Enumerations.State;

import it.polimi.ingsw.Controller.Enumerations.State;
import it.polimi.ingsw.Controller.Messages.MessageStudentToArchipelago;

public class ControllerState {

    private State state;

    public ControllerState(){
        this.state = State.CONNECTING;
    }

    public boolean checkState(MessageType type){
        switch(type){
            case CREATE_MATCH:
                return (this.state == State.CONNECTING);
            case ADD_PLAYER:
                return (this.state == State.WAITING_PLAYERS);
            case ASSISTANT_CARD:
                return (this.state == State.PLANNING2);
            case STUDENT_HALL_TO_DINING_ROOM:
            case STUDENT_TO_ARCHIPELAGO:
                return (this.state == State.ACTION1);
            case MOVE_MOTHER_NATURE:
                return (this.state == State.ACTION2);
        }
        return false;
    }

    public State getState(){
        return this.state;
    }

    public void setState(State newState){
        this.state = newState;
    }

}
