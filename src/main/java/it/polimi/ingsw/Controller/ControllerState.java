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
            case STUDENT_TO_ARCHIPELAGO:
                return (this.state == State.TURN);
        }
        return false;
    }

}
