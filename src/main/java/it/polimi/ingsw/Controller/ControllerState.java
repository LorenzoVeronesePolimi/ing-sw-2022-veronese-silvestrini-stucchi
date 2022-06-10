package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Messages.Enumerations.INMessageType;

import it.polimi.ingsw.Controller.Enumerations.State;

/**
 * Class that represents and modifies the Controller states based on the message received from the client.
 */
public class ControllerState {

    private State state;

    /**
     * Constructor that initializes the State of the Controller.
     */
    public ControllerState(){
        this.state = State.CONNECTING;
    }

    /**
     * This method checks if the message received is performed at the correct time, so if the player has made an allowed move with respect to the state of the game.
     * @param type Type of the message received from the Client.
     * @return  true if the action is correctly performed by the client.
     */
    public boolean checkState(INMessageType type){
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
            case CC_TWO_EXTRA_ISLANDS:
                return (this.state != State.CONNECTING &&
                        this.state != State.WAITING_PLAYERS);
            case PING:
                return true;
        }
        return false; //unreachable
    }

    /**
     * @return The Controller state.
     */
    public State getState(){
        return this.state;
    }

    /**
     * @param newState The new Controller state.
     */
    public void setState(State newState){
        this.state = newState;
    }

}
