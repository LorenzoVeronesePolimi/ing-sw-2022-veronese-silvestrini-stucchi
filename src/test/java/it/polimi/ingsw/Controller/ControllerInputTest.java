package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Messages.INMessage.MessageAddPlayer;
import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Board.BoardFour;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.View.ServerView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ControllerInputTest {
    ControllerInput controllerInput;

    @BeforeEach
    void init() {
        controllerInput = new ControllerInput();
    }

    @Test
    void checkFormat() {
        Student wrongInput = new Student(SPColour.RED);
        assertFalse(controllerInput.checkFormat(wrongInput));
    }

    @Test
    void checkNickname() {
        assertFalse(controllerInput.checkNickname(""));
        assertTrue(controllerInput.checkNickname("Fabrizio"));
    }

    @Test
    void checkPlayerColour() {
        assertTrue(controllerInput.checkPlayerColour("WHITE"));
        assertTrue(controllerInput.checkPlayerColour("wHitE"));
        assertTrue(controllerInput.checkPlayerColour("white"));
        assertFalse(controllerInput.checkPlayerColour("RED"));
    }

    @Test
    void checkMotherNatureMovement() {
        assertTrue(controllerInput.checkMotherNatureMovement(0));
        assertFalse(controllerInput.checkMotherNatureMovement(45));
        assertFalse(controllerInput.checkMotherNatureMovement(-1));
    }

    @Test
    void checkTurnPriority() {
        assertTrue(controllerInput.checkTurnPriority(1));
        assertFalse(controllerInput.checkTurnPriority(11));
        assertFalse(controllerInput.checkTurnPriority(-1));
    }

    @Test
    void checkStudentColour() {
        assertTrue(controllerInput.checkStudentColour("RED"));
        assertTrue(controllerInput.checkStudentColour("rED"));
        assertTrue(controllerInput.checkStudentColour("blue"));
        assertFalse(controllerInput.checkStudentColour("white"));
    }

    @Test
    void checkMultipleStudentColour() {
        List<String> colours1 = new ArrayList<>();
        colours1.add("Red"); colours1.add("blue"); colours1.add("-");
        List<String> colours2 = new ArrayList<>();
        colours2.add("Red"); colours2.add("salt"); colours2.add("-");
        assertTrue(controllerInput.checkMultipleStudentColour(colours1));
        assertFalse(controllerInput.checkMultipleStudentColour(colours2));
    }

    @Test
    void checkDestinationArchipelagoIndex() {
        assertTrue(controllerInput.checkDestinationArchipelagoIndex(3));
        assertTrue(controllerInput.checkDestinationArchipelagoIndex(12));
        assertFalse(controllerInput.checkDestinationArchipelagoIndex(13));
    }

    @Test
    void checkCloudIndex() {
        assertTrue(controllerInput.checkCloudIndex(3));
        assertFalse(controllerInput.checkCloudIndex(-1));
        assertFalse(controllerInput.checkCloudIndex(5));
    }

    @Test
    void checkIndexCard() {
        assertTrue(controllerInput.checkIndexCard(0));
        assertFalse(controllerInput.checkIndexCard(-1));
        assertFalse(controllerInput.checkIndexCard(3));
    }
}