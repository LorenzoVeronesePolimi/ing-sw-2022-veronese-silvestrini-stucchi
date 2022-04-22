package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Enumerations.MessageType;
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
        assertTrue(controllerState.checkState(MessageType.CREATE_MATCH));
        controllerState.setState(State.WAITING_PLAYERS);
        assertTrue(controllerState.checkState(MessageType.ADD_PLAYER));
        controllerState.setState(State.PLANNING2);
        assertTrue(controllerState.checkState(MessageType.ASSISTANT_CARD));
        controllerState.setState(State.ACTION1);
        assertTrue(controllerState.checkState(MessageType.STUDENT_HALL_TO_DINING_ROOM));
        controllerState.setState(State.ACTION1);
        assertTrue(controllerState.checkState(MessageType.STUDENT_TO_ARCHIPELAGO));
        controllerState.setState(State.ACTION2);
        assertTrue(controllerState.checkState(MessageType.MOVE_MOTHER_NATURE));
        controllerState.setState(State.ACTION3);
        assertTrue(controllerState.checkState(MessageType.STUDENT_CLOUD_TO_SCHOOL));
        assertTrue(controllerState.checkState(MessageType.CC_EXCHANGE_THREE_STUDENTS));
        assertTrue(controllerState.checkState(MessageType.CC_EXCHANGE_TWO_HALL_DINING));
        assertTrue(controllerState.checkState(MessageType.CC_EXCLUDE_COLOUR_FROM_COUNTING));
        assertTrue(controllerState.checkState(MessageType.CC_EXTRA_STUDENT_IN_DINING));
        assertTrue(controllerState.checkState(MessageType.CC_FAKE_MN_MOVEMENT));
        assertTrue(controllerState.checkState(MessageType.CC_FORBID_ISLAND));
        assertTrue(controllerState.checkState(MessageType.CC_PLACE_ONE_STUDENT));
        assertTrue(controllerState.checkState(MessageType.CC_REDUCE_COLOUR_IN_DINING));
        assertTrue(controllerState.checkState(MessageType.CC_TAKE_PROFESSOR_ON_EQUITY));
        assertTrue(controllerState.checkState(MessageType.CC_TOWER_NO_VALUE));
        assertTrue(controllerState.checkState(MessageType.CC_TWO_EXTRA_POINTS));

        assertFalse(controllerState.checkState(MessageType.CREATE_MATCH));
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