package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Enumerations.MessageType;

import it.polimi.ingsw.Controller.Enumerations.State;

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
            case STUDENT_CLOUD_TO_SCHOOL:
                return (this.state == State.ACTION3);
            case CC_EXCHANGE_THREE_STUDENTS:
            case CC_EXCHANGE_TWO_HALL_DINING:
            case CC_EXCLUDE_COLOUR_FROM_COUNTING:
            case CC_EXTRA_STUDENT_IN_DINING:
            case CC_FAKE_MN_MOVEMENT:
            case CC_FORBID_ISLAND:
            case CC_PLACE_ONE_STUDENT:
            case CC_REDUCE_COLOUR_IN_DINING:
            case CC_TAKE_PROFESSOR_ON_EQUITY:
            case CC_TOWER_NO_VALUE:
            case CC_TWO_EXTRA_POINTS:
                return (this.state != State.CONNECTING &&
                        this.state != State.WAITING_PLAYERS);
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
