package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Enumerations.ControllerErrorType;
import it.polimi.ingsw.Controller.Enumerations.State;
import it.polimi.ingsw.Controller.Exceptions.ControllerException;
import it.polimi.ingsw.Messages.INMessages.*;
import it.polimi.ingsw.Model.Cards.*;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Pawns.Coin;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Pawns.Tower;
import it.polimi.ingsw.Model.Places.School.SchoolAdvanced;
import it.polimi.ingsw.Server.Server;
import it.polimi.ingsw.Server.SocketClientConnectionCLI;
import it.polimi.ingsw.View.ServerView;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;

import javax.naming.ldap.Control;


public class ControllerTest {
    static Controller controller;
    /*private static ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    static Socket socket;*/
    static SocketClientConnectionCLI conn1;
    static SocketClientConnectionCLI conn2;
    static SocketClientConnectionCLI conn3;
    static SocketClientConnectionCLI conn4;
    static ServerView view1;
    static ServerView view2;
    static ServerView view3;
    static ServerView view4;
    static Server server;

    static {
        try {
            server = new Server();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        controller = server.getController();
        //System.setOut(new PrintStream(outContent));
        conn1 = new SocketClientConnectionCLI(server, controller);
        server.addConnection(conn1);
        view1 = conn1.getServerView();
        conn2 = new SocketClientConnectionCLI(server, controller);
        server.addConnection(conn2);
        view2 = conn2.getServerView();
        conn3 = new SocketClientConnectionCLI(server, controller);
        server.addConnection(conn3);
        view3 = conn1.getServerView();
        conn4 = new SocketClientConnectionCLI(server, controller);
        server.addConnection(conn4);
        view4 = conn2.getServerView();


        //view = new ServerView(new SocketClientConnectionCLI(server, controller), controller);

    }

    @Test
    void controllerTest(){
        /*-----MessageCreateMatch-----*/
        //ERRORS IN FORMAT
        //Error because of wrong name ("")
        Assertions.assertNotNull(controller.getControllerInput());
        Assertions.assertNotNull(controller.getControllerIntegrity());

        MessageCreateMatch m1Err1 = new MessageCreateMatch("", "white", 2, true, view1);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m1Err1));
        //Assertions.assertEquals("Invalid format", outContent.toString().trim());

        //Error because of wrong colour
        //this.resetOutput();
        MessageCreateMatch m1Err2 = new MessageCreateMatch("First", "brown", 2, true, view1);
        Assertions.assertThrows(ControllerException.class, ()-> controller.update(m1Err2));
        //Assertions.assertEquals("Invalid format", outContent.toString().trim());


        try {
            controller.update(m1Err2);
        } catch (ControllerException e) {
            Assertions.assertEquals(ControllerErrorType.FORMAT_ERROR, e.getType());
        }

        //ERRORS IN STATE
        MessageAddPlayer m1Err3 = new MessageAddPlayer("Second", "white", view2);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m1Err3));
        //Assertions.assertEquals("You can't do that now", outContent.toString().trim());

        try {
            controller.update(m1Err3);
        } catch (ControllerException e) {
            Assertions.assertEquals(ControllerErrorType.STATE_ERROR, e.getType());
        }

        //ERRORS IN INTEGRITY
        //Error because of wrong numPlayers
        MessageCreateMatch m1Err4 = new MessageCreateMatch("First", "white", 5, true, view1);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m1Err4));
        //Assertions.assertEquals("Error", outContent.toString().trim());

        try {
            controller.update(m1Err4);
        } catch (ControllerException e) {
            Assertions.assertEquals(ControllerErrorType.INTEGRITY_ERROR, e.getType());
        }

        //OK
        MessageCreateMatch m1 = new MessageCreateMatch("First", "white", 2, true, view1);
        try {
            controller.update(m1);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.WAITING_PLAYERS, controller.getControllerState().getState());
        Assertions.assertEquals("First", controller.getPlayers().get(0).getNickname());
        Assertions.assertTrue(controller.isAdvanced());

        /*-----MessageAddPlayer-----*/
        //ERRORS IN FORMAT
        //Error because of wrong name ("")
        MessageAddPlayer m2Err1 = new MessageAddPlayer("", "black", view2);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m2Err1));
        //Assertions.assertEquals("Invalid format", outContent.toString().trim());

        //Error because of wrong colour
        MessageAddPlayer m2Err2 = new MessageAddPlayer("Second", "brown", view2);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m2Err2));
        //Assertions.assertEquals("Invalid format", outContent.toString().trim());

        //ERRORS IN STATE
        MessageCreateMatch m2Err3 = new MessageCreateMatch("Second", "white", 2, true, view2);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m2Err3));
        //Assertions.assertEquals("You can't do that now", outContent.toString().trim());

        //ERRORS IN CONTROLLER
        //Same colour as before
        MessageAddPlayer m2Err4 = new MessageAddPlayer("Second", "white", view2);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m2Err4));
        //Assertions.assertEquals("Error", outContent.toString().trim());

        //Same name as before
        MessageAddPlayer m2Err5 = new MessageAddPlayer("First", "black", view2);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m2Err5));
        //Assertions.assertEquals("Error", outContent.toString().trim());

        //OK
        MessageAddPlayer m2 = new MessageAddPlayer("Second", "black", view2);
        try {
            controller.update(m2);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        //Assertions.assertEquals("This Cloud has too many Students", outContent.toString().trim());
        Assertions.assertEquals(State.PLANNING2, controller.getControllerState().getState());
        Assertions.assertEquals("Second", controller.getPlayers().get(1).getNickname());
        Assertions.assertTrue(controller.isAdvanced());


        /*
        * First: 9 => 2
        * Second: 1 => 1
        */
        /*-----MessageAssistantCard 1-----*/
        //ERRORS IN FORMAT
        //Error because of wrong name ("")
        MessageAssistantCard m3Err1 = new MessageAssistantCard("", 5, 10);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m3Err1));

        //Error because of wrong motherNatureMovement
        MessageAssistantCard m3Err2 = new MessageAssistantCard("First", 9, 10);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m3Err2));

        //Error because of wrong turnPriority
        MessageAssistantCard m3Err3 = new MessageAssistantCard("First", 5, -1);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m3Err3));

        //ERRORS IN CONTROLLER
        //Not current player
        MessageAssistantCard m3Err4 = new MessageAssistantCard("Second", 5, 10);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m3Err4));

        //OK
        MessageAssistantCard m3 = new MessageAssistantCard("First", 5, 9);
        try {
            controller.update(m3);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        //Assertions.assertEquals("", outContent.toString().trim());
        Assertions.assertEquals(State.PLANNING2, controller.getControllerState().getState());
        Assertions.assertEquals("Second", controller.getCurrentPlayer().getNickname());
        Assertions.assertEquals(1, controller.getCurrentPlayerIndex());
        Assertions.assertTrue(controller.isAdvanced());

        /*-----MessageAssistantCard 2-----*/
        //ERRORS IN CONTROLLER
        //Not current player
        MessageAssistantCard m4Err1 = new MessageAssistantCard("First", 1, 1);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m4Err1));

        //Same card as First
        MessageAssistantCard m4Err2 = new MessageAssistantCard("First", 5, 10);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m4Err2));

        //OK
        MessageAssistantCard m4 = new MessageAssistantCard("Second", 1, 1);
        try {
            controller.update(m4);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());
        Assertions.assertEquals(0, controller.getCurrentPlayerIndex());
        Assertions.assertEquals("Second", controller.getCurrentPlayer().getNickname());
        Assertions.assertTrue(controller.isAdvanced());

        /*-----MessageStudentHallToDiningRoom 1-----*/
        // choose a Student which exists
        String colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());

        //ERRORS IN CONTROLLER
        //Not current player
        MessageStudentHallToDiningRoom m5Err1 = new MessageStudentHallToDiningRoom("First", "red");
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m5Err1));

        //OK
        MessageStudentHallToDiningRoom m5 = new MessageStudentHallToDiningRoom(controller.getCurrentPlayer().getNickname(), colourToMove);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 3);
        try {
            controller.update(m5);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 2);
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());

        /*-----MessageStudentHallToDiningRoom 2-----*/
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());

        //ERRORS IN CONTROLLER
        //Not current player
        MessageStudentHallToDiningRoom m6Err1 = new MessageStudentHallToDiningRoom("First", "red");
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m6Err1));

        //OK
        MessageStudentHallToDiningRoom m6 = new MessageStudentHallToDiningRoom(controller.getCurrentPlayer().getNickname(), colourToMove);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 2);
        try {
            controller.update(m6);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 1);
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());

        /*-----MessageStudentToArchipelago-----*/
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());

        //Not possibile archipelago
        MessageStudentToArchipelago m7Err1 = new MessageStudentToArchipelago(controller.getCurrentPlayer().getNickname(), colourToMove, 13);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m7Err1));

        //ERRORS IN CONTROLLER
        //Not current player
        MessageStudentToArchipelago m7Err2 = new MessageStudentToArchipelago("First", colourToMove, 10);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m7Err2));

        //Not existing playerNickname
        MessageStudentToArchipelago m7Err3 = new MessageStudentToArchipelago("Not Existing", colourToMove, 10);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m7Err3));

        //OK
        MessageStudentToArchipelago m7 = new MessageStudentToArchipelago(controller.getCurrentPlayer().getNickname(), colourToMove, 10);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 1);
        try {
            controller.update(m7);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 3);
        Assertions.assertEquals(State.ACTION2, controller.getControllerState().getState());

        /*-----MessageMoveMotherNature-----*/
        //Not possibile number of moves
        MessageMoveMotherNature m8Err1 = new MessageMoveMotherNature(controller.getCurrentPlayer().getNickname(), 13);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m8Err1));

        //ERRORS IN CONTROLLER
        //Not current player
        MessageMoveMotherNature m8Err2 = new MessageMoveMotherNature("First", 1);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m8Err2));

        //Not integrity possible number of moves
        MessageMoveMotherNature m8Err3 = new MessageMoveMotherNature(controller.getCurrentPlayer().getNickname(), 2);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m8Err3));

        //OK
        MessageMoveMotherNature m8 = new MessageMoveMotherNature(controller.getCurrentPlayer().getNickname(), 1);
        try {
            controller.update(m8);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.ACTION3, controller.getControllerState().getState());

        /*-----MessageStudentCloudToSchool-----*/
        //Not existing cloud
        MessageStudentCloudToSchool m9Err1 = new MessageStudentCloudToSchool(controller.getCurrentPlayer().getNickname(), 5);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m9Err1));

        //ERRORS IN CONTROLLER
        //Not current player
        MessageStudentCloudToSchool m9Err2 = new MessageStudentCloudToSchool("First", 1);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m9Err2));

        //OK
        MessageStudentCloudToSchool m9 = new MessageStudentCloudToSchool(controller.getCurrentPlayer().getNickname(), 1);
        try {
            controller.update(m9);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());
        Assertions.assertEquals(1, controller.getCurrentPlayerIndex());
        Assertions.assertEquals("First", controller.getCurrentPlayer().getNickname());

        //Give lots of Coins to the player to test CharacterCards
        List<Coin> coins = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            coins.add(new Coin());
        }
        for (Coin c : coins) {
            ((SchoolAdvanced) controller.getBoardAdvanced().getPlayerSchool(controller.getCurrentPlayer())).addCoin(c);
        }

        /*-----MessageCCExchangeThreeStudents-----*/
        try {
            controller.getBoardAdvanced().setExtractedCards(new ExchangeThreeStudents(controller.getBoardAdvanced()));
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }

        controller.setCharacterCardUsed(false);
        String colourCard1 = mapSPColourToString(((ExchangeThreeStudents) controller.getBoardAdvanced().getExtractedCards().get(0)).getStudentsOnCard().get(0).getColour());
        String colourCard2 = mapSPColourToString(((ExchangeThreeStudents) controller.getBoardAdvanced().getExtractedCards().get(0)).getStudentsOnCard().get(1).getColour());
        String colourHall1 = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());
        String colourHall2 = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(1).getColour());
        MessageCCExchangeThreeStudents mcc1 = new MessageCCExchangeThreeStudents(controller.getCurrentPlayer().getNickname(), colourCard1, colourCard2, "-", colourHall1, colourHall2, "-");
        try {
            controller.update(mcc1);
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        //ERRORS
        //Not current Player
        controller.setCharacterCardUsed(false);
        MessageCCExchangeThreeStudents mcc1Err1 = new MessageCCExchangeThreeStudents("Second", colourCard1, colourCard2, "-", colourHall1, colourHall2, "-");
        Assertions.assertThrows(ControllerException.class, () -> controller.update(mcc1Err1));

        //CC used
        controller.setCharacterCardUsed(true);
        MessageCCExchangeThreeStudents mcc1Err2 = new MessageCCExchangeThreeStudents(controller.getCurrentPlayer().getNickname(), colourCard1, colourCard2, "-", colourHall1, colourHall2, "-");
        Assertions.assertThrows(ControllerException.class, () -> controller.update(mcc1Err2));

        //TODO:
        /*
        //Not right CC
        controller.setCharacterCardUsed(false);
        ExchangeThreeStudents c1;
        ExchangeTwoHallDining c2;
        try {
            c1 = new ExchangeThreeStudents(controller.getBoardAdvanced());
            c2 = new ExchangeTwoHallDining(controller.getBoardAdvanced());
            controller.getBoardAdvanced().setExtractedCardsTwo(c1, c2);
            MessageCCFakeMNMovement mcc1Err3 = new MessageCCFakeMNMovement(controller.getCurrentPlayer().getNickname(), 3);
            Assertions.assertThrows(ControllerException.class, () -> controller.update(mcc1Err3));
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }*/

        /*-----MessageCCExchangeTwoHallDining-----*/
        controller.getBoardAdvanced().setExtractedCards(new ExchangeTwoHallDining(controller.getBoardAdvanced()));
        controller.setCharacterCardUsed(false);
        colourHall1 = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());
        colourHall2 = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(1).getColour());
        try {
            controller.getBoardAdvanced().getPlayerSchool(controller.getCurrentPlayer()).addStudentDiningRoom(new Student(SPColour.RED));
            controller.getBoardAdvanced().getPlayerSchool(controller.getCurrentPlayer()).addStudentDiningRoom(new Student(SPColour.RED));
        } catch (ExceededMaxStudentsDiningRoomException e) {
            e.printStackTrace();
        }
        MessageCCExchangeTwoHallDining mcc2 = new MessageCCExchangeTwoHallDining(controller.getCurrentPlayer().getNickname(), colourHall1, colourHall2, "red", "red");
        try {
            controller.update(mcc2);
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        //ERRORS
        //Not current Player
        controller.setCharacterCardUsed(false);
        MessageCCExchangeTwoHallDining mcc2Err1 = new MessageCCExchangeTwoHallDining("Second", colourHall1, colourHall2, "red", "red");
        Assertions.assertThrows(ControllerException.class, () -> controller.update(mcc2Err1));

        //CC used
        controller.setCharacterCardUsed(true);
        MessageCCExchangeTwoHallDining mcc2Err2 = new MessageCCExchangeTwoHallDining(controller.getCurrentPlayer().getNickname(), colourHall1, colourHall2, "red", "red");
        Assertions.assertThrows(ControllerException.class, () -> controller.update(mcc2Err2));

        //prepare next
        try {
            controller.getBoardAdvanced().getBank().getCoin();
            controller.getBoardAdvanced().getBank().getCoin();
            controller.getBoardAdvanced().getBank().getCoin();
            controller.getBoardAdvanced().getBank().getCoin();
            controller.getBoardAdvanced().getBank().getCoin();
            controller.getBoardAdvanced().getBank().getCoin();
            controller.getBoardAdvanced().getBank().getCoin();
            controller.getBoardAdvanced().getBank().getCoin();
            controller.getBoardAdvanced().getBank().getCoin();
            controller.getBoardAdvanced().getBank().getCoin();
            controller.getBoardAdvanced().getBank().getCoin();
            controller.getBoardAdvanced().getBank().getCoin();
            controller.getBoardAdvanced().getBank().getCoin();
            controller.getBoardAdvanced().getBank().getCoin();
        } catch (EmptyCaveauException e) {
            e.printStackTrace();
        }


        /*-----MessageCCExcludeColourFromCounting-----*/
        controller.getBoardAdvanced().setExtractedCards(new ExcludeColourFromCounting(controller.getBoardAdvanced()));
        controller.setCharacterCardUsed(false);
        MessageCCExcludeColourFromCounting mcc3 = new MessageCCExcludeColourFromCounting(controller.getCurrentPlayer().getNickname(), "red");
        try {
            controller.update(mcc3);
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        /*-----MessageCCExtraStudentInDining-----*/
        try {
            controller.getBoardAdvanced().setExtractedCards(new ExtraStudentInDining(controller.getBoardAdvanced()));
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }
        colourCard1 = mapSPColourToString(((ExtraStudentInDining) controller.getBoardAdvanced().getExtractedCards().get(0)).getStudentsOnCard().get(0).getColour());
        controller.setCharacterCardUsed(false);
        MessageCCExtraStudentInDining mcc4 = new MessageCCExtraStudentInDining(controller.getCurrentPlayer().getNickname(), colourCard1);
        try {
            controller.update(mcc4);
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        /*-----MessageCCFakeMNMovement-----*/
        controller.getBoardAdvanced().setExtractedCards(new FakeMNMovement(controller.getBoardAdvanced()));
        controller.setCharacterCardUsed(false);
        MessageCCFakeMNMovement mcc5 = new MessageCCFakeMNMovement(controller.getCurrentPlayer().getNickname(), 4);
        try {
            controller.update(mcc5);
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        /*-----MessageCCForbidIsland-----*/
        controller.getBoardAdvanced().setExtractedCards(new ForbidIsland(controller.getBoardAdvanced()));
        controller.setCharacterCardUsed(false);
        MessageCCForbidIsland mcc6 = new MessageCCForbidIsland(controller.getCurrentPlayer().getNickname(), 4);
        try {
            controller.update(mcc6);
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        /*-----MessageCCPlaceOneStudent-----*/
        try {
            controller.getBoardAdvanced().setExtractedCards(new PlaceOneStudent(controller.getBoardAdvanced()));
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }
        controller.setCharacterCardUsed(false);
        colourCard1 = mapSPColourToString(((PlaceOneStudent) controller.getBoardAdvanced().getExtractedCards().get(0)).getStudentsOnCard().get(0).getColour());
        MessageCCPlaceOneStudent mcc7 = new MessageCCPlaceOneStudent(controller.getCurrentPlayer().getNickname(), colourCard1, 4);
        try {
            controller.update(mcc7);
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        /*-----MessageCCReduceColourInDining-----*/
        controller.getBoardAdvanced().setExtractedCards(new ReduceColourInDining(controller.getBoardAdvanced()));
        controller.setCharacterCardUsed(false);
        MessageCCReduceColourInDining mcc8 = new MessageCCReduceColourInDining(controller.getCurrentPlayer().getNickname(), "red");
        try {
            controller.update(mcc8);
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        /*-----MessageCCTowerNoValue-----*/
        controller.getBoardAdvanced().setExtractedCards(new TowerNoValue(controller.getBoardAdvanced()));
        controller.setCharacterCardUsed(false);
        MessageCCTowerNoValue mcc9 = new MessageCCTowerNoValue(controller.getCurrentPlayer().getNickname());
        try {
            controller.update(mcc9);
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        /*-----MessageCCTwoExtraPoints-----*/
        controller.getBoardAdvanced().setExtractedCards(new TwoExtraPoints(controller.getBoardAdvanced()));
        controller.setCharacterCardUsed(false);
        MessageCCTwoExtraPoints mcc10 = new MessageCCTwoExtraPoints(controller.getCurrentPlayer().getNickname());
        try {
            controller.update(mcc10);
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        /*-----MessageCCTakeProfessorOnEquity-----*/
        controller.getBoardAdvanced().setExtractedCards(new TakeProfessorOnEquity(controller.getBoardAdvanced()));
        controller.setCharacterCardUsed(false);
        MessageCCTakeProfessorOnEquity mcc11 = new MessageCCTakeProfessorOnEquity(controller.getCurrentPlayer().getNickname());
        try {
            controller.update(mcc11);
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        /*-----MessageCCTwoExtraIslands-----*/
        controller.getBoardAdvanced().setExtractedCards(new TwoExtraIslands());
        controller.setCharacterCardUsed(false);
        MessageCCTwoExtraIslands mcc12 = new MessageCCTwoExtraIslands(controller.getCurrentPlayer().getNickname());
        try {
            controller.update(mcc12);
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        //ROUND 1: TURN 2: First
        Assertions.assertEquals(controller.getCurrentPlayer().getNickname(), "First");
        //-----MessageStudentHallToDiningRoom 1-----
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());

        MessageStudentHallToDiningRoom m10 = new MessageStudentHallToDiningRoom(controller.getCurrentPlayer().getNickname(), colourToMove);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 3);
        try {
            controller.update(m10);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 2);
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());

        //-----MessageStudentHallToDiningRoom 2-----
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());

        MessageStudentHallToDiningRoom m11 = new MessageStudentHallToDiningRoom(controller.getCurrentPlayer().getNickname(), colourToMove);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 2);
        try {
            controller.update(m11);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 1);
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());

        //-----MessageStudentToArchipelago-----
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());

        MessageStudentToArchipelago m12 = new MessageStudentToArchipelago(controller.getCurrentPlayer().getNickname(), colourToMove, 10);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 1);
        try {
            controller.update(m12);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 3);
        Assertions.assertEquals(State.ACTION2, controller.getControllerState().getState());

        //-----MessageMoveMotherNature-----
        MessageMoveMotherNature m13 = new MessageMoveMotherNature(controller.getCurrentPlayer().getNickname(), 1);
        try {
            controller.update(m13);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.ACTION3, controller.getControllerState().getState());

        //-----MessageStudentCloudToSchool-----
        MessageStudentCloudToSchool m14 = new MessageStudentCloudToSchool(controller.getCurrentPlayer().getNickname(), 0);
        try {
            controller.update(m14);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.PLANNING2, controller.getControllerState().getState());
        Assertions.assertEquals("Second", controller.getCurrentSitPlayer().getNickname());

        //ROUND 2: Second plays AC
        List<Integer> usedCards = new ArrayList<>();

        try { //for the end due to AC finished
            controller.getBoard().useAssistantCard(usedCards, controller.getCurrentSitPlayer(), 3);
            controller.getBoard().useAssistantCard(usedCards, controller.getCurrentSitPlayer(), 4);
            controller.getBoard().useAssistantCard(usedCards, controller.getCurrentSitPlayer(), 5);
            controller.getBoard().useAssistantCard(usedCards, controller.getCurrentSitPlayer(), 6);
            controller.getBoard().useAssistantCard(usedCards, controller.getCurrentSitPlayer(), 7);
            controller.getBoard().useAssistantCard(usedCards, controller.getCurrentSitPlayer(), 8);
            controller.getBoard().useAssistantCard(usedCards, controller.getPlayers().get(1), 3); //other-ways I couldn't use the 9 in the next line
            controller.getBoard().useAssistantCard(usedCards, controller.getCurrentSitPlayer(), 9);
            controller.getBoard().useAssistantCard(usedCards, controller.getCurrentSitPlayer(), 10);
        } catch (AssistantCardAlreadyPlayedTurnException | NoAssistantCardException e) {
            e.printStackTrace();
        }
        //-----MessageAssistantCard 1-----
        MessageAssistantCard m15 = new MessageAssistantCard("Second", 1, 2);
        try {
            controller.update(m15);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.PLANNING2, controller.getControllerState().getState());
        Assertions.assertEquals("First", controller.getCurrentSitPlayer().getNickname());
        Assertions.assertTrue(controller.isGameEnded());

        //ROUND 2: First plays AC
        //-----MessageAssistantCard 2-----
        MessageAssistantCard m16 = new MessageAssistantCard("First", 3, 5);
        try {
            controller.update(m16);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());
        Assertions.assertEquals(0, controller.getCurrentPlayerIndex());
        Assertions.assertEquals("Second", controller.getCurrentPlayer().getNickname());

        //ROUND 2: Second plays
        //-----MessageStudentHallToDiningRoom 1-----
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());

        MessageStudentHallToDiningRoom m165 = new MessageStudentHallToDiningRoom(controller.getCurrentPlayer().getNickname(), colourToMove);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 3);
        try {
            controller.update(m165);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 2);
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());

        //-----MessageStudentHallToDiningRoom 2-----
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());

        MessageStudentHallToDiningRoom m17 = new MessageStudentHallToDiningRoom(controller.getCurrentPlayer().getNickname(), colourToMove);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 2);
        try {
            controller.update(m17);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 1);
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());

        //-----MessageStudentToArchipelago-----
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());

        MessageStudentToArchipelago m18 = new MessageStudentToArchipelago(controller.getCurrentPlayer().getNickname(), colourToMove, 10);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 1);
        try {
            controller.update(m18);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 3);
        Assertions.assertEquals(State.ACTION2, controller.getControllerState().getState());

        //-----MessageMoveMotherNature-----
        MessageMoveMotherNature m19 = new MessageMoveMotherNature(controller.getCurrentPlayer().getNickname(), 1);
        try {
            controller.update(m19);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.ACTION3, controller.getControllerState().getState());

        //-----MessageStudentCloudToSchool-----
        MessageStudentCloudToSchool m20 = new MessageStudentCloudToSchool(controller.getCurrentPlayer().getNickname(), 0);
        try {
            controller.update(m20);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());
        Assertions.assertEquals("First", controller.getCurrentPlayer().getNickname());

        //ROUND 2: First plays
        //-----MessageStudentHallToDiningRoom 1-----
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());

        MessageStudentHallToDiningRoom m21 = new MessageStudentHallToDiningRoom(controller.getCurrentPlayer().getNickname(), colourToMove);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 3);
        try {
            controller.update(m21);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 2);
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());

        //-----MessageStudentHallToDiningRoom 2-----
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());

        MessageStudentHallToDiningRoom m22 = new MessageStudentHallToDiningRoom(controller.getCurrentPlayer().getNickname(), colourToMove);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 2);
        try {
            controller.update(m22);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 1);
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());

        //-----MessageStudentToArchipelago-----
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());

        MessageStudentToArchipelago m23 = new MessageStudentToArchipelago(controller.getCurrentPlayer().getNickname(), colourToMove, 10);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 1);
        try {
            controller.update(m23);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 3);
        Assertions.assertEquals(State.ACTION2, controller.getControllerState().getState());

        //-----MessageMoveMotherNature-----
        MessageMoveMotherNature m24 = new MessageMoveMotherNature(controller.getCurrentPlayer().getNickname(), 1);
        try {
            controller.update(m24);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.ACTION3, controller.getControllerState().getState());

        //-----MessageStudentCloudToSchool-----
        MessageStudentCloudToSchool m25 = new MessageStudentCloudToSchool(controller.getCurrentPlayer().getNickname(), 1);
        try {
            controller.update(m25);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.END, controller.getControllerState().getState());



        //**********CASE BOARD ADVANCED (2 players): test ending due to 3 archipelagos**********
        server.reserServer();
        ControllerTest.controller = new Controller(server);
        //CREATE MATCH
        MessageCreateMatch ma1 = new MessageCreateMatch("First", "white", 2, true, view1);
        try {
            controller.update(ma1);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        //ADD PLAYER 2
        MessageAddPlayer ma2 = new MessageAddPlayer("Second", "black", view2);
        try {
            controller.update(ma2);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.PLANNING2, controller.getControllerState().getState());

        //ROUND 1: AC: First -> 1, Second -> 10
        //-----MessageAssistantCard 1-----
        MessageAssistantCard ma3 = new MessageAssistantCard("First", 1, 1);
        try {
            controller.update(ma3);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.PLANNING2, controller.getControllerState().getState());
        Assertions.assertEquals("Second", controller.getCurrentSitPlayer().getNickname());

        //-----MessageAssistantCard 2-----
        //ERRORS IN CONTROLLER
        MessageAssistantCard ma4 = new MessageAssistantCard("Second", 5, 10);
        try {
            controller.update(ma4);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());
        Assertions.assertEquals("First", controller.getCurrentPlayer().getNickname());

        //ROUND 1: TURN 1: First
        // Force conquering of ten archipelago, so that 3 will be remained
        // So I put a student of the colour First will have the professor of
        SPColour SPColourToMove = controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour();
        //SPColourToMove is the same as colourToMove but is not a String
        List<Tower> toAdd = new ArrayList<>();
        toAdd.add(new Tower(controller.getCurrentPlayer()));
        controller.getBoard().getArchipelago(0).addStudent(new Student(SPColourToMove));
        for(int i = 1; i < 11; i++){
            controller.getBoard().getArchipelago(i).addStudent(new Student(SPColourToMove));
            toAdd.clear();
            toAdd.add(new Tower(controller.getCurrentPlayer()));
            try {
                controller.getBoard().getArchipelago(i).conquerArchipelago(toAdd);//First is white
            } catch (InvalidTowerNumberException | AnotherTowerException e) {
                e.printStackTrace();
            }
        }

        //-----MessageStudentHallToDiningRoom 1-----
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());
        MessageStudentHallToDiningRoom ma5 = new MessageStudentHallToDiningRoom(controller.getCurrentPlayer().getNickname(), colourToMove);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 3);
        try {
            controller.update(ma5);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 2);
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());

        //-----MessageStudentHallToDiningRoom 2-----
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());

        MessageStudentHallToDiningRoom ma6 = new MessageStudentHallToDiningRoom(controller.getCurrentPlayer().getNickname(), colourToMove);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 2);
        try {
            controller.update(ma6);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 1);
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());

        //-----MessageStudentToArchipelago-----
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());

        MessageStudentToArchipelago ma7 = new MessageStudentToArchipelago(controller.getCurrentPlayer().getNickname(), colourToMove, 10);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 1);
        try {
            controller.update(ma7);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 3);
        Assertions.assertEquals(State.ACTION2, controller.getControllerState().getState());

        //-----MessageMoveMotherNature-----
        MessageMoveMotherNature ma8 = new MessageMoveMotherNature(controller.getCurrentPlayer().getNickname(), 0);
        try {
            controller.update(ma8);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.END, controller.getControllerState().getState());



        //**********CASE BOARD NOT ADVANCED (2 players): test ending due to 3 archipelagos**********
        server.reserServer();
        ControllerTest.controller = new Controller(server);
        //CREATE MATCH
        MessageCreateMatch mna1 = new MessageCreateMatch("First", "white", 2, false, view1);
        try {
            controller.update(mna1);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        //ADD PLAYER 2
        MessageAddPlayer mna2 = new MessageAddPlayer("Second", "black", view2);
        try {
            controller.update(mna2);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.PLANNING2, controller.getControllerState().getState());

        //ROUND 1: AC: First -> 1, Second -> 10
        //-----MessageAssistantCard 1-----
        MessageAssistantCard mna3 = new MessageAssistantCard("First", 1, 1);
        try {
            controller.update(mna3);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.PLANNING2, controller.getControllerState().getState());
        Assertions.assertEquals("Second", controller.getCurrentSitPlayer().getNickname());

        //-----MessageAssistantCard 2-----
        //ERRORS IN CONTROLLER
        MessageAssistantCard mna4 = new MessageAssistantCard("Second", 5, 10);
        try {
            controller.update(mna4);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());
        Assertions.assertEquals("First", controller.getCurrentPlayer().getNickname());

        //ROUND 1: TURN 1: First
        // Force conquering of ten archipelago, so that 3 will be remained
        // So I put a student of the colour First will have the professor of
        SPColourToMove = controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour();
        //SPColourToMove is the same as colourToMove but is not a String
        toAdd = new ArrayList<>();
        toAdd.add(new Tower(controller.getCurrentPlayer()));
        controller.getBoard().getArchipelago(0).addStudent(new Student(SPColourToMove));
        for(int i = 1; i < 11; i++){
            controller.getBoard().getArchipelago(i).addStudent(new Student(SPColourToMove));
            toAdd.clear();
            toAdd.add(new Tower(controller.getCurrentPlayer()));
            try {
                controller.getBoard().getArchipelago(i).conquerArchipelago(toAdd);//First is white
            } catch (InvalidTowerNumberException | AnotherTowerException e) {
                e.printStackTrace();
            }
        }

        //-----MessageStudentHallToDiningRoom 1-----
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());
        MessageStudentHallToDiningRoom mna5 = new MessageStudentHallToDiningRoom(controller.getCurrentPlayer().getNickname(), colourToMove);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 3);
        try {
            controller.update(mna5);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 2);
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());

        //-----MessageStudentHallToDiningRoom 2-----
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());

        MessageStudentHallToDiningRoom mna6 = new MessageStudentHallToDiningRoom(controller.getCurrentPlayer().getNickname(), colourToMove);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 2);
        try {
            controller.update(mna6);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 1);
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());

        //-----MessageStudentToArchipelago-----
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());

        MessageStudentToArchipelago mna7 = new MessageStudentToArchipelago(controller.getCurrentPlayer().getNickname(), colourToMove, 10);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 1);
        try {
            controller.update(mna7);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 3);
        Assertions.assertEquals(State.ACTION2, controller.getControllerState().getState());

        //-----MessageMoveMotherNature-----
        MessageMoveMotherNature mna8 = new MessageMoveMotherNature(controller.getCurrentPlayer().getNickname(), 0);
        try {
            controller.update(mna8);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.END, controller.getControllerState().getState());



        //**********CASE BOARD ADVANCED (2 players): test ending due to finished towers**********
        server.reserServer();
        ControllerTest.controller = new Controller(server);
        //CREATE MATCH
        MessageCreateMatch mt1 = new MessageCreateMatch("First", "white", 2, false, view1);
        try {
            controller.update(mt1);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        //ADD PLAYER 2
        MessageAddPlayer mt2 = new MessageAddPlayer("Second", "black", view2);
        try {
            controller.update(mt2);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.PLANNING2, controller.getControllerState().getState());

        //ROUND 1: AC: First -> 1, Second -> 10
        //-----MessageAssistantCard 1-----
        MessageAssistantCard mt3 = new MessageAssistantCard("First", 1, 1);
        try {
            controller.update(mt3);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.PLANNING2, controller.getControllerState().getState());
        Assertions.assertEquals("Second", controller.getCurrentSitPlayer().getNickname());

        //-----MessageAssistantCard 2-----
        MessageAssistantCard mt4 = new MessageAssistantCard("Second", 5, 10);
        try {
            controller.update(mt4);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());
        Assertions.assertEquals("First", controller.getCurrentPlayer().getNickname());

        //ROUND 1: TURN 1: First
        // Force put away 7 towers so that at the next conquering he will win
        Tower t;
        try {
            for(int i = 0; i < 7; i++){
                t = controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).removeTower();
            }
        } catch (TowerNotFoundException e) {
            e.printStackTrace();
        }

        //-----MessageStudentHallToDiningRoom 1-----
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());
        //force conquering of archipelago 1
        controller.getBoard().getArchipelago(1).addStudent(new Student(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour()));
        MessageStudentHallToDiningRoom mt5 = new MessageStudentHallToDiningRoom(controller.getCurrentPlayer().getNickname(), colourToMove);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 3);
        try {
            controller.update(mt5);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 2);
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());

        //-----MessageStudentHallToDiningRoom 2-----
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());

        MessageStudentHallToDiningRoom mt6 = new MessageStudentHallToDiningRoom(controller.getCurrentPlayer().getNickname(), colourToMove);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 2);
        try {
            controller.update(mt6);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 1);
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());

        //-----MessageStudentToArchipelago-----
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());

        MessageStudentToArchipelago mt7 = new MessageStudentToArchipelago(controller.getCurrentPlayer().getNickname(), colourToMove, 1);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 1);
        try {
            controller.update(mt7);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 3);
        Assertions.assertEquals(State.ACTION2, controller.getControllerState().getState());

        //-----MessageMoveMotherNature-----
        MessageMoveMotherNature mt8 = new MessageMoveMotherNature(controller.getCurrentPlayer().getNickname(), 1);
        try {
            controller.update(mt8);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.END, controller.getControllerState().getState());



        //**********CASE BOARD ADVANCED (2 players): test ending due to void bag**********
        server.reserServer();
        ControllerTest.controller = new Controller(server);
        //CREATE MATCH
        MessageCreateMatch mb1 = new MessageCreateMatch("First", "white", 2, true, view1);
        try {
            controller.update(mb1);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        //ADD PLAYER 2
        MessageAddPlayer mb2 = new MessageAddPlayer("Second", "black", view2);
        try {
            controller.update(mb2);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.PLANNING2, controller.getControllerState().getState());

        //ROUND 1: AC: First -> 1, Second -> 10
        //Make the bag void
        try {
            controller.getBoard().getBag().extractStudents(controller.getBoard().getBag().getNumStudents());
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }

        //-----MessageAssistantCard 1-----
        MessageAssistantCard mb3 = new MessageAssistantCard("First", 1, 1);
        try {
            controller.update(mb3);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.PLANNING2, controller.getControllerState().getState());
        Assertions.assertEquals("Second", controller.getCurrentSitPlayer().getNickname());

        //-----MessageAssistantCard 2-----
        MessageAssistantCard mb4 = new MessageAssistantCard("Second", 5, 10);
        try {
            controller.update(mb4);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());
        Assertions.assertEquals("First", controller.getCurrentPlayer().getNickname());

        //ROUND 1: TURN 1: First
        //-----MessageStudentHallToDiningRoom 1-----
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());
        MessageStudentHallToDiningRoom mb5 = new MessageStudentHallToDiningRoom(controller.getCurrentPlayer().getNickname(), colourToMove);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 3);
        try {
            controller.update(mb5);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 2);
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());

        //-----MessageStudentHallToDiningRoom 2-----
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());

        MessageStudentHallToDiningRoom mb6 = new MessageStudentHallToDiningRoom(controller.getCurrentPlayer().getNickname(), colourToMove);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 2);
        try {
            controller.update(mb6);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 1);
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());

        //-----MessageStudentToArchipelago-----
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());

        MessageStudentToArchipelago mb7 = new MessageStudentToArchipelago(controller.getCurrentPlayer().getNickname(), colourToMove, 10);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 1);
        try {
            controller.update(mb7);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 3);
        Assertions.assertEquals(State.ACTION2, controller.getControllerState().getState());

        //-----MessageMoveMotherNature-----
        MessageMoveMotherNature mb8 = new MessageMoveMotherNature(controller.getCurrentPlayer().getNickname(), 1);
        try {
            controller.update(mb8);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.ACTION3, controller.getControllerState().getState());

        //-----MessageStudentCloudToSchool-----
        MessageStudentCloudToSchool mb9 = new MessageStudentCloudToSchool(controller.getCurrentPlayer().getNickname(), 1);
        try {
            controller.update(mb9);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());

        //ROUND 1: TURN 2: Second
        //-----MessageStudentHallToDiningRoom 1-----
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());
        MessageStudentHallToDiningRoom mb10 = new MessageStudentHallToDiningRoom(controller.getCurrentPlayer().getNickname(), colourToMove);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 3);
        try {
            controller.update(mb10);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 2);
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());

        //-----MessageStudentHallToDiningRoom 2-----
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());

        MessageStudentHallToDiningRoom mb11 = new MessageStudentHallToDiningRoom(controller.getCurrentPlayer().getNickname(), colourToMove);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 2);
        try {
            controller.update(mb11);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 1);
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());

        //-----MessageStudentToArchipelago-----
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());

        MessageStudentToArchipelago mb12 = new MessageStudentToArchipelago(controller.getCurrentPlayer().getNickname(), colourToMove, 10);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 1);
        try {
            controller.update(mb12);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 3);
        Assertions.assertEquals(State.ACTION2, controller.getControllerState().getState());

        //-----MessageMoveMotherNature-----
        MessageMoveMotherNature mb13 = new MessageMoveMotherNature(controller.getCurrentPlayer().getNickname(), 1);
        try {
            controller.update(mb13);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.ACTION3, controller.getControllerState().getState());

        //-----MessageStudentCloudToSchool-----
        MessageStudentCloudToSchool mb14 = new MessageStudentCloudToSchool(controller.getCurrentPlayer().getNickname(), 0);
        try {
            controller.update(mb14);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.PLANNING2, controller.getControllerState().getState());

        //ROUND 2: AC
        //-----MessageAssistantCard 1-----
        MessageAssistantCard mb15 = new MessageAssistantCard("First", 1, 2);
        try {
            controller.update(mb15);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.PLANNING2, controller.getControllerState().getState());
        Assertions.assertEquals("Second", controller.getCurrentSitPlayer().getNickname());

        //-----MessageAssistantCard 2-----
        MessageAssistantCard mb16 = new MessageAssistantCard("Second", 5, 9);
        try {
            controller.update(mb16);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());
        Assertions.assertEquals("First", controller.getCurrentPlayer().getNickname());

        //ROUND 2: TURN 1: First
        //-----MessageStudentHallToDiningRoom 1-----
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());
        MessageStudentHallToDiningRoom mb17 = new MessageStudentHallToDiningRoom(controller.getCurrentPlayer().getNickname(), colourToMove);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 3);
        try {
            controller.update(mb17);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 2);
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());

        //-----MessageStudentHallToDiningRoom 2-----
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());

        MessageStudentHallToDiningRoom mb18 = new MessageStudentHallToDiningRoom(controller.getCurrentPlayer().getNickname(), colourToMove);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 2);
        try {
            controller.update(mb18);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 1);
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());

        //-----MessageStudentToArchipelago-----
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());

        MessageStudentToArchipelago mb19 = new MessageStudentToArchipelago(controller.getCurrentPlayer().getNickname(), colourToMove, 10);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 1);
        try {
            controller.update(mb19);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 3);
        Assertions.assertEquals(State.ACTION2, controller.getControllerState().getState());

        //-----MessageMoveMotherNature-----
        MessageMoveMotherNature mb20 = new MessageMoveMotherNature(controller.getCurrentPlayer().getNickname(), 1);
        try {
            controller.update(mb20);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.ACTION3, controller.getControllerState().getState());

        //-----MessageStudentCloudToSchool-----
        MessageStudentCloudToSchool mb21 = new MessageStudentCloudToSchool(controller.getCurrentPlayer().getNickname(), 1);
        try {
            controller.update(mb21);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());

        //ROUND 1: TURN 2: Second
        //-----MessageStudentHallToDiningRoom 1-----
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());
        MessageStudentHallToDiningRoom mb22 = new MessageStudentHallToDiningRoom(controller.getCurrentPlayer().getNickname(), colourToMove);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 3);
        try {
            controller.update(mb22);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 2);
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());

        //-----MessageStudentHallToDiningRoom 2-----
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());

        MessageStudentHallToDiningRoom mb23 = new MessageStudentHallToDiningRoom(controller.getCurrentPlayer().getNickname(), colourToMove);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 2);
        try {
            controller.update(mb23);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 1);
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());

        //-----MessageStudentToArchipelago-----
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());

        MessageStudentToArchipelago mb24 = new MessageStudentToArchipelago(controller.getCurrentPlayer().getNickname(), colourToMove, 10);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 1);
        try {
            controller.update(mb24);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 3);
        Assertions.assertEquals(State.ACTION2, controller.getControllerState().getState());

        //-----MessageMoveMotherNature-----
        MessageMoveMotherNature mb25 = new MessageMoveMotherNature(controller.getCurrentPlayer().getNickname(), 1);
        try {
            controller.update(mb25);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.ACTION3, controller.getControllerState().getState());

        //-----MessageStudentCloudToSchool-----
        MessageStudentCloudToSchool mb26 = new MessageStudentCloudToSchool(controller.getCurrentPlayer().getNickname(), 0);
        try {
            controller.update(mb26);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.END, controller.getControllerState().getState());



        //**********CASE BOARD NOT ADVANCED (4 players)**********
        server.reserServer();
        ControllerTest.controller = new Controller(server);
        //CREATE MATCH
        MessageCreateMatch m4p1 = new MessageCreateMatch("First", "white", 4, false, view1);
        try {
            controller.update(m4p1);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.WAITING_PLAYERS, controller.getControllerState().getState());
        Assertions.assertEquals("First", controller.getPlayers().get(0).getNickname());
        Assertions.assertFalse(controller.isAdvanced());

        //ADD PLAYER 2
        MessageAddPlayer m4p2 = new MessageAddPlayer("Second", "black", view2);
        try {
            controller.update(m4p2);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.WAITING_PLAYERS, controller.getControllerState().getState());
        Assertions.assertEquals("Second", controller.getPlayers().get(1).getNickname());
        Assertions.assertFalse(controller.isAdvanced());

        //ADD PLAYER 3
        MessageAddPlayer m4p3 = new MessageAddPlayer("Third", "black", view3);
        try {
            controller.update(m4p3);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.WAITING_PLAYERS, controller.getControllerState().getState());
        Assertions.assertEquals("Third", controller.getPlayers().get(2).getNickname());
        Assertions.assertFalse(controller.isAdvanced());

        //ADD PLAYER 4
        // Colour not permitted
        MessageAddPlayer m4p4Err1 = new MessageAddPlayer("Fourth", "black", view4);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m4p4Err1));

        // Colour not permitted
        MessageAddPlayer m4p4Err2 = new MessageAddPlayer("Fourth", "gray", view4);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m4p4Err2));

        //ok
        MessageAddPlayer m4p4 = new MessageAddPlayer("Fourth", "white", view4);
        try {
            controller.update(m4p4);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.PLANNING2, controller.getControllerState().getState());
        // After reordering
        Assertions.assertEquals("Second", controller.getPlayers().get(3).getNickname());
        Assertions.assertFalse(controller.isAdvanced());
    }

    private String mapSPColourToString(SPColour c){
        switch(c){
            case RED:
                return "red";
            case PINK:
                return "pink";
            case BLUE:
                return "blue";
            case YELLOW:
                return "yellow";
            case GREEN:
                return "green";
        }
        return "red";
    }
}
