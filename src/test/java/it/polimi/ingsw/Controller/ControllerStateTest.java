package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Messages.Enumerations.INMessageType;
import it.polimi.ingsw.Controller.Enumerations.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerStateTest {

    ControllerState controllerState;

    @BeforeEach
    public void init() { controllerState = new ControllerState(); }

    @Test
    void checkState() {
        controllerState.setState(State.CONNECTING);
        assertEquals(controllerState.checkState(INMessageType.CREATE_MATCH), true);
        controllerState.setState(State.WAITING_PLAYERS);
        assertEquals(controllerState.checkState(INMessageType.ADD_PLAYER), true);
        controllerState.setState(State.PLANNING2);
        assertEquals(controllerState.checkState(INMessageType.ASSISTANT_CARD), true);
        controllerState.setState(State.ACTION1);
        assertEquals(controllerState.checkState(INMessageType.STUDENT_HALL_TO_DINING_ROOM), true);
        controllerState.setState(State.ACTION1);
        assertEquals(controllerState.checkState(INMessageType.STUDENT_TO_ARCHIPELAGO), true);
        controllerState.setState(State.ACTION2);
        assertEquals(controllerState.checkState(INMessageType.MOVE_MOTHER_NATURE), true);
        controllerState.setState(State.ACTION3);
        assertEquals(controllerState.checkState(INMessageType.STUDENT_CLOUD_TO_SCHOOL), true);
        assertEquals(controllerState.checkState(INMessageType.CC_EXCHANGE_THREE_STUDENTS), true);
        assertEquals(controllerState.checkState(INMessageType.CC_EXCHANGE_TWO_HALL_DINING), true);
        assertEquals(controllerState.checkState(INMessageType.CC_EXCLUDE_COLOUR_FROM_COUNTING), true);
        assertEquals(controllerState.checkState(INMessageType.CC_EXTRA_STUDENT_IN_DINING), true);
        assertEquals(controllerState.checkState(INMessageType.CC_FAKE_MN_MOVEMENT), true);
        assertEquals(controllerState.checkState(INMessageType.CC_FORBID_ISLAND), true);
        assertEquals(controllerState.checkState(INMessageType.CC_PLACE_ONE_STUDENT), true);
        assertEquals(controllerState.checkState(INMessageType.CC_REDUCE_COLOUR_IN_DINING), true);
        assertEquals(controllerState.checkState(INMessageType.CC_TAKE_PROFESSOR_ON_EQUITY), true);
        assertEquals(controllerState.checkState(INMessageType.CC_TOWER_NO_VALUE), true);
        assertEquals(controllerState.checkState(INMessageType.CC_TWO_EXTRA_POINTS), true);
    }

    @Test
    void getState() {
        controllerState.setState(State.CONNECTING);
        assertEquals(controllerState.getState(), State.CONNECTING);
    }

    @Test
    void setState() {
        controllerState.setState(State.CONNECTING);
        assertEquals(controllerState.getState(), State.CONNECTING);
    }
}