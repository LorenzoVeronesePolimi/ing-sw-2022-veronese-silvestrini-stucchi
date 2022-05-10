package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Enumerations.ControllerErrorType;
import it.polimi.ingsw.Controller.Enumerations.State;
import it.polimi.ingsw.Controller.Exceptions.ControllerException;
import it.polimi.ingsw.Messages.INMessages.*;
import it.polimi.ingsw.Model.Cards.*;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.EmptyCaveauException;
import it.polimi.ingsw.Model.Exceptions.ExceededMaxStudentsDiningRoomException;
import it.polimi.ingsw.Model.Exceptions.StudentNotFoundException;
import it.polimi.ingsw.Model.Pawns.Coin;
import it.polimi.ingsw.Model.Pawns.Student;
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
    //private static ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    //private final PrintStream originalOut = System.out;
    //private final PrintStream originalErr = System.err;
    //static Socket socket;
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
        //Assertions.assertEquals("", outContent.toString().trim());
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



        //**********CASE BOARD NOT ADVANCED (4 players)**********
        server.reserServer();
        ControllerTest.controller = new Controller(server);
        //CREATE MATCH
        MessageCreateMatch mb1 = new MessageCreateMatch("First", "white", 4, false, view1);
        try {
            controller.update(mb1);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        //Assertions.assertEquals("", outContent.toString().trim());
        Assertions.assertEquals(State.WAITING_PLAYERS, controller.getControllerState().getState());
        Assertions.assertEquals("First", controller.getPlayers().get(0).getNickname());
        Assertions.assertFalse(controller.isAdvanced());

        //ADD PLAYER 2
        MessageAddPlayer mb2 = new MessageAddPlayer("Second", "black", view2);
        try {
            controller.update(mb2);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.WAITING_PLAYERS, controller.getControllerState().getState());
        Assertions.assertEquals("Second", controller.getPlayers().get(1).getNickname());
        Assertions.assertFalse(controller.isAdvanced());

        //ADD PLAYER 3
        MessageAddPlayer mb3 = new MessageAddPlayer("Third", "black", view3);
        try {
            controller.update(mb3);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.WAITING_PLAYERS, controller.getControllerState().getState());
        Assertions.assertEquals("Third", controller.getPlayers().get(2).getNickname());
        Assertions.assertFalse(controller.isAdvanced());

        //ADD PLAYER 4
        // Colour not permitted
        MessageAddPlayer mb4Err1 = new MessageAddPlayer("Fourth", "black", view4);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(mb4Err1));

        // Colour not permitted
        MessageAddPlayer mb4Err2 = new MessageAddPlayer("Fourth", "gray", view4);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(mb4Err2));

        //ok
        MessageAddPlayer mb4 = new MessageAddPlayer("Fourth", "white", view4);
        try {
            controller.update(mb4);
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
