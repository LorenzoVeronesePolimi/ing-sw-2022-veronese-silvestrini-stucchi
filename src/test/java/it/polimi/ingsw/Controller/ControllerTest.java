package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Enumerations.ControllerErrorType;
import it.polimi.ingsw.Controller.Enumerations.State;
import it.polimi.ingsw.Controller.Exceptions.ControllerException;
import it.polimi.ingsw.Messages.INMessages.*;
import it.polimi.ingsw.Model.Board.BoardAdvanced;
import it.polimi.ingsw.Model.Cards.*;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Pawns.Coin;
import it.polimi.ingsw.Model.Pawns.Professor;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Pawns.Tower;
import it.polimi.ingsw.Model.Places.School.SchoolAdvanced;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Server.Server;
import it.polimi.ingsw.Server.SocketClientConnection;
import it.polimi.ingsw.Server.ServerView;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import org.junit.jupiter.api.Assertions;


public class ControllerTest {
    static Controller controller;
    static SocketClientConnection conn1;
    static SocketClientConnection conn2;
    static SocketClientConnection conn3;
    static SocketClientConnection conn4;
    static ServerView view1;
    static ServerView view2;
    static ServerView view3;
    static ServerView view4;
    static Server server;
    SPColour[] availableColours = {SPColour.BLUE, SPColour.PINK, SPColour.RED, SPColour.GREEN, SPColour.YELLOW};

    static Map<Integer, Integer> priorityMNMovement = Map.of(
            1, 1,
            2, 1,
            3, 2,
            4, 2,
            5, 3,
            6, 3,
            7, 4,
            8, 4,
            9, 5,
            10, 5
    );

    static {
        try {
            server = new Server();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        controller = server.createController();
        conn1 = new SocketClientConnection(server, controller);
        server.addConnection(conn1);
        view1 = conn1.getServerView();
        conn2 = new SocketClientConnection(server, controller);
        server.addConnection(conn2);
        view2 = conn2.getServerView();
        conn3 = new SocketClientConnection(server, controller);
        server.addConnection(conn3);
        view3 = conn1.getServerView();
        conn4 = new SocketClientConnection(server, controller);
        server.addConnection(conn4);
        view4 = conn2.getServerView();


        //view = new ServerView(new SocketClientConnectionCLI(server, controller), controller);

    }

    @Test
    void controllerTest() throws ProfessorNotFoundException {
        controller.setUsePersistence(false);
        //-----MessageCreateMatch-----
        //ERRORS IN FORMAT
        //Error because of wrong name ("")
        Assertions.assertNotNull(controller.getControllerInput());
        Assertions.assertNotNull(controller.getControllerIntegrity());

        MessageCreateMatch m1Err1 = new MessageCreateMatch("", "white", 2, true, view1);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m1Err1));

        //Error because of wrong colour
        MessageCreateMatch m1Err2 = new MessageCreateMatch("First", "brown", 2, true, view1);
        Assertions.assertThrows(ControllerException.class, ()-> controller.update(m1Err2));


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

        //-----MessageAddPlayer-----
        //ERRORS IN FORMAT
        //Error because of wrong name ("")
        MessageAddPlayer m2Err1 = new MessageAddPlayer("", "black", view2);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m2Err1));

        //Error because of wrong colour
        MessageAddPlayer m2Err2 = new MessageAddPlayer("Second", "brown", view2);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m2Err2));

        //ERRORS IN STATE
        MessageCreateMatch m2Err3 = new MessageCreateMatch("Second", "white", 2, true, view2);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m2Err3));

        //ERRORS IN CONTROLLER
        //Same colour as before
        MessageAddPlayer m2Err4 = new MessageAddPlayer("Second", "white", view2);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m2Err4));

        //Same name as before
        MessageAddPlayer m2Err5 = new MessageAddPlayer("First", "black", view2);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m2Err5));

        //OK
        MessageAddPlayer m2 = new MessageAddPlayer("Second", "black", view2);
        try {
            controller.update(m2);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.PLANNING2, controller.getControllerState().getState());
        Assertions.assertEquals("Second", controller.getPlayers().get(1).getNickname());
        Assertions.assertTrue(controller.isAdvanced());



        // First: 9 => 2 to play
        // Second: 1 => 1 to play
        //-----MessageAssistantCard 1-----
        //ERRORS IN FORMAT
        //Error because of wrong name (impossible in format: void)
        MessageAssistantCard m3Err1 = new MessageAssistantCard("", 5, 10);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m3Err1));

        //Error because of not existing name (even if possible one)
        MessageAssistantCard m3Err5 = new MessageAssistantCard("IDontExist", 5, 10);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m3Err5));

        //Error because of wrong motherNatureMovement
        MessageAssistantCard m3Err2 = new MessageAssistantCard("First", 9, 10);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m3Err2));

        //Error because of wrong turnPriority
        MessageAssistantCard m3Err3 = new MessageAssistantCard("First", 5, -1);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m3Err3));
        Assertions.assertEquals(State.PLANNING2, controller.getPrecomputedState());

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
        Assertions.assertEquals(State.PLANNING2, controller.getControllerState().getState());
        Assertions.assertEquals("Second", controller.getCurrentPlayer().getNickname());
        Assertions.assertEquals(1, controller.getCurrentPlayerIndex());
        Assertions.assertTrue(controller.isAdvanced());

        //-----MessageAssistantCard 2-----
        //ERRORS IN CONTROLLER
        //Not current player
        MessageAssistantCard m4Err1 = new MessageAssistantCard("First", 1, 1);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(m4Err1));

        //Same card as First
        MessageAssistantCard m4Err2 = new MessageAssistantCard("Second", 5, 9);
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

        //-----MessageStudentHallToDiningRoom 1-----
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

        //-----MessageStudentHallToDiningRoom 2-----
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

        //-----MessageStudentToArchipelago-----
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

        //-----MessageMoveMotherNature-----
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

        //-----MessageStudentCloudToSchool-----
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

        //-----MessageCCExchangeThreeStudents-----
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

        //CC not valid since all "-"s
        MessageCCExchangeThreeStudents mcc1Err2 = new MessageCCExchangeThreeStudents(controller.getCurrentPlayer().getNickname(), "-", "-", "-", "-", "-", "-");
        Assertions.assertThrows(ControllerException.class, () -> controller.update(mcc1Err2));


        //CC used
        controller.setCharacterCardUsed(true);
        MessageCCExchangeThreeStudents mcc1Err3 = new MessageCCExchangeThreeStudents(controller.getCurrentPlayer().getNickname(), colourCard1, colourCard2, "-", colourHall1, colourHall2, "-");
        Assertions.assertThrows(ControllerException.class, () -> controller.update(mcc1Err3));


        //Not right CC
        controller.setCharacterCardUsed(false);
        ExchangeThreeStudents c1;
        ExchangeTwoHallDining c2;
        try {
            c1 = new ExchangeThreeStudents(controller.getBoardAdvanced());
            c2 = new ExchangeTwoHallDining(controller.getBoardAdvanced());
            controller.getBoardAdvanced().setExtractedCardsTwo(c1, c2);
            MessageCCFakeMNMovement mcc1Err4 = new MessageCCFakeMNMovement(controller.getCurrentPlayer().getNickname(), 3);
            Assertions.assertThrows(ControllerException.class, () -> controller.update(mcc1Err4));
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }

        //Not right CC: all others (others then FakeMNMovement, tested above)
        controller.setCharacterCardUsed(false);
        controller.getBoardAdvanced().setExtractedCards(new FakeMNMovement(controller.getBoardAdvanced()));
        MessageCCExchangeThreeStudents mccErr1 = new MessageCCExchangeThreeStudents(controller.getCurrentPlayer().getNickname(), "red", "red", "-", "red", "red", "-");
        Assertions.assertThrows(ControllerException.class, () -> controller.update(mccErr1));
        MessageCCExchangeTwoHallDining mccErr2 = new MessageCCExchangeTwoHallDining(controller.getCurrentPlayer().getNickname(), "red", "red", "red", "red");
        Assertions.assertThrows(ControllerException.class, () -> controller.update(mccErr2));
        MessageCCExcludeColourFromCounting mccErr3 = new MessageCCExcludeColourFromCounting(controller.getCurrentPlayer().getNickname(), "red");
        Assertions.assertThrows(ControllerException.class, () -> controller.update(mccErr3));
        MessageCCExtraStudentInDining mccErr4 = new MessageCCExtraStudentInDining(controller.getCurrentPlayer().getNickname(), colourCard1);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(mccErr4));
        MessageCCForbidIsland mccErr6 = new MessageCCForbidIsland(controller.getCurrentPlayer().getNickname(), 4);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(mccErr6));
        MessageCCPlaceOneStudent mccErr7 = new MessageCCPlaceOneStudent(controller.getCurrentPlayer().getNickname(), "red", 4);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(mccErr7));
        MessageCCReduceColourInDining mccErr8 = new MessageCCReduceColourInDining(controller.getCurrentPlayer().getNickname(), "red");
        Assertions.assertThrows(ControllerException.class, () -> controller.update(mccErr8));
        MessageCCTowerNoValue mccErr9 = new MessageCCTowerNoValue(controller.getCurrentPlayer().getNickname());
        Assertions.assertThrows(ControllerException.class, () -> controller.update(mccErr9));
        MessageCCTwoExtraPoints mccErr10 = new MessageCCTwoExtraPoints(controller.getCurrentPlayer().getNickname());
        Assertions.assertThrows(ControllerException.class, () -> controller.update(mccErr10));
        MessageCCTakeProfessorOnEquity mccErr11 = new MessageCCTakeProfessorOnEquity(controller.getCurrentPlayer().getNickname());
        Assertions.assertThrows(ControllerException.class, () -> controller.update(mccErr11));
        MessageCCTwoExtraIslands mccErr12 = new MessageCCTwoExtraIslands(controller.getCurrentPlayer().getNickname());
        Assertions.assertThrows(ControllerException.class, () -> controller.update(mccErr12));

        //-----MessageCCExchangeTwoHallDining-----
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
            for(int i = 0; i < 12; i++){
                controller.getBoardAdvanced().getBank().getCoin();
            }
        } catch (EmptyCaveauException e) {
            e.printStackTrace();
        }


        //-----MessageCCExcludeColourFromCounting-----
        controller.getBoardAdvanced().setExtractedCards(new ExcludeColourFromCounting(controller.getBoardAdvanced()));
        controller.setCharacterCardUsed(false);
        MessageCCExcludeColourFromCounting mcc3 = new MessageCCExcludeColourFromCounting(controller.getCurrentPlayer().getNickname(), "red");
        try {
            controller.update(mcc3);
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        //-----MessageCCExtraStudentInDining-----
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

        //-----MessageCCFakeMNMovement-----
        controller.getBoardAdvanced().setExtractedCards(new FakeMNMovement(controller.getBoardAdvanced()));
        controller.setCharacterCardUsed(false);
        MessageCCFakeMNMovement mcc5 = new MessageCCFakeMNMovement(controller.getCurrentPlayer().getNickname(), 4);
        try {
            controller.update(mcc5);
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        //-----MessageCCForbidIsland-----
        controller.getBoardAdvanced().setExtractedCards(new ForbidIsland(controller.getBoardAdvanced()));
        controller.setCharacterCardUsed(false);
        MessageCCForbidIsland mcc6 = new MessageCCForbidIsland(controller.getCurrentPlayer().getNickname(), 4);
        try {
            controller.update(mcc6);
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        //-----MessageCCPlaceOneStudent-----
        controller.setCharacterCardUsed(false);

        PlaceOneStudent cardPlaceOneStudent = null;
        try {
            cardPlaceOneStudent = new PlaceOneStudent(controller.getBoardAdvanced());
            controller.getBoardAdvanced().setExtractedCards(cardPlaceOneStudent);
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }

        //ERROR
        //Student not on the card
        boolean flag = true;
        for(SPColour c : availableColours){
            for(Student s : cardPlaceOneStudent.getStudentsOnCard()){
                if(s.getColour() == c){
                    flag = false;
                }
            }
            if(flag){
                colourCard1 = mapSPColourToString(c);
                break;
            }
            flag = true;
        }
        MessageCCPlaceOneStudent mcc7Err1 = new MessageCCPlaceOneStudent(controller.getCurrentPlayer().getNickname(), colourCard1, 2);
        Assertions.assertThrows(ControllerException.class, () -> controller.update(mcc7Err1));

        //OK
        colourCard1 = mapSPColourToString(((PlaceOneStudent) controller.getBoardAdvanced().getExtractedCards().get(0)).getStudentsOnCard().get(0).getColour());
        MessageCCPlaceOneStudent mcc7 = new MessageCCPlaceOneStudent(controller.getCurrentPlayer().getNickname(), colourCard1, 4);
        try {
            controller.update(mcc7);
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        //-----MessageCCReduceColourInDining-----
        controller.getBoardAdvanced().setExtractedCards(new ReduceColourInDining(controller.getBoardAdvanced()));
        controller.setCharacterCardUsed(false);
        MessageCCReduceColourInDining mcc8 = new MessageCCReduceColourInDining(controller.getCurrentPlayer().getNickname(), "red");
        try {
            controller.update(mcc8);
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        //-----MessageCCTowerNoValue-----
        controller.getBoardAdvanced().setExtractedCards(new TowerNoValue(controller.getBoardAdvanced()));
        controller.setCharacterCardUsed(false);
        MessageCCTowerNoValue mcc9 = new MessageCCTowerNoValue(controller.getCurrentPlayer().getNickname());
        try {
            controller.update(mcc9);
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        //-----MessageCCTwoExtraPoints-----
        controller.getBoardAdvanced().setExtractedCards(new TwoExtraPoints(controller.getBoardAdvanced()));
        controller.setCharacterCardUsed(false);
        MessageCCTwoExtraPoints mcc10 = new MessageCCTwoExtraPoints(controller.getCurrentPlayer().getNickname());
        try {
            controller.update(mcc10);
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        //-----MessageCCTakeProfessorOnEquity-----
        controller.getBoardAdvanced().setExtractedCards(new TakeProfessorOnEquity(controller.getBoardAdvanced()));
        controller.setCharacterCardUsed(false);
        MessageCCTakeProfessorOnEquity mcc11 = new MessageCCTakeProfessorOnEquity(controller.getCurrentPlayer().getNickname());
        try {
            controller.update(mcc11);
        } catch (ControllerException e) {
            e.printStackTrace();
        }

        //-----MessageCCTwoExtraIslands-----
        controller.getBoardAdvanced().setExtractedCards(new TwoExtraIslands());
        controller.setCharacterCardUsed(false);
        MessageCCTwoExtraIslands mcc12 = new MessageCCTwoExtraIslands(controller.getCurrentPlayer().getNickname());
        try {
            controller.update(mcc12);
        } catch (ControllerException e) {
            e.printStackTrace();
        }



        //**********CASE BOARD ADVANCED (2 players): test ending due to 3 archipelagos**********
        server.resetServer();
        ControllerTest.controller = new Controller(server);
        controller.setUsePersistence(false);
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

        //make the number of towers equals
        Player p1 = controller.getBoard().getPlayers().get(0);
        Player p2 = controller.getBoard().getPlayers().get(1);
        while(controller.getBoard().getPlayerSchool(p1).getNumTowers() != controller.getBoard().getPlayerSchool(p2).getNumTowers()){
            if(controller.getBoard().getPlayerSchool(p1).getNumTowers() > controller.getBoard().getPlayerSchool(p2).getNumTowers()){
                try {
                    controller.getBoard().getPlayerSchool(p1).removeTower();
                } catch (TowerNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else{
                try {
                    controller.getBoard().getPlayerSchool(p2).removeTower();
                } catch (TowerNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        //give to p1 all professors to be sure he will win
        for(Professor p : controller.getBoard().getPlayerSchool(p2).getProfessors()){
            controller.getBoard().getPlayerSchool(p2).removeProfessor(p.getColour());
        }

        for(SPColour c : availableColours){
            for(int i = 0; i < 6; i++){
                controller.getBoard().getPlayerSchool(p1).addProfessor(new Professor(c));
            }
        }

        //-----MessageMoveMotherNature-----
        MessageMoveMotherNature ma8 = new MessageMoveMotherNature(controller.getCurrentPlayer().getNickname(), 0);
        try {
            controller.update(ma8);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertFalse(controller.isGameEnded()); //since it ended immediately
        Assertions.assertEquals(State.END, controller.getControllerState().getState());
        System.out.println(controller.getBoard().getPlayerSchool(p1).getProfessors());
        System.out.println(controller.getBoard().getPlayerSchool(p2).getProfessors());
        Assertions.assertEquals(p1.getNickname(), controller.getNicknameWinner());



        //**********CASE BOARD ADVANCED (2 players): test ending due to finished towers**********
        server.resetServer();
        ControllerTest.controller = new Controller(server);
        controller.setUsePersistence(false);
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
        server.resetServer();
        ControllerTest.controller = new Controller(server);
        controller.setUsePersistence(false);
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



        //**********TEST PERSISTENCE: not existing match (4 players)**********
        File file = new File("game_saved.bless");
        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> playersPersistence = new ArrayList<>();
        playersPersistence.add("First");
        playersPersistence.add("Second");
        setMatch(playersPersistence, false, true);
        makeParametricRound(false, new int[]{1, 2}, new String[]{"First", "Second"});

        //**********TEST PERSISTENCE: existing match, can restore since same nicknames (2 players)**********
        setMatch(playersPersistence, false, true);

        //**********TEST PERSISTENCE: existing match, can't restore since different nicknames (2 players)**********
        playersPersistence.clear();
        playersPersistence.add("First1");
        playersPersistence.add("Second2");
        setMatch(playersPersistence, false, true);

        //**********TEST PERSISTENCE(ADVANCED): not existing match (4 players)**********
        file = new File("game_saved.bless");
        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        playersPersistence.clear();
        playersPersistence.add("First");
        playersPersistence.add("Second");
        setMatch(playersPersistence, true, true);
        makeParametricRound(false, new int[]{1, 2}, new String[]{"First", "Second"});

        //**********TEST PERSISTENCE: existing match, can restore since same nicknames (2 players)**********
        setMatch(playersPersistence, true, true);

        //**********TEST PERSISTENCE: existing match, can't restore since different difficulty (2 players)**********
        setMatch(playersPersistence, false, true);



        //**********TEST BOARD TWO NOT ADVANCED**********
        for(int i = 0; i < 2; i++) {
            List<String> players = new ArrayList<>();
            players.add("First");
            players.add("Second");
            setMatch(players, false, false);

            boolean finished = false;
            // Round 1
            finished = makeParametricRound(finished, new int[]{1, 2}, new String[]{"First", "Second"});
            finished = makeParametricRound(finished, new int[]{2, 5}, new String[]{"First", "Second"});
            finished = makeParametricRound(finished, new int[]{3, 4}, new String[]{"First", "Second"});
            finished = makeParametricRound(finished, new int[]{4, 6}, new String[]{"First", "Second"});
            finished = makeParametricRound(finished, new int[]{5, 3}, new String[]{"Second", "First"});
            finished = makeParametricRound(finished, new int[]{6, 1}, new String[]{"Second", "First"});
            finished = makeParametricRound(finished, new int[]{10, 7}, new String[]{"Second", "First"});
            finished = makeParametricRound(finished, new int[]{7, 8}, new String[]{"First", "Second"});
            finished = makeParametricRound(finished, new int[]{8, 9}, new String[]{"First", "Second"});
            finished = makeParametricRound(finished, new int[]{9, 10}, new String[]{"First", "Second"});
            Assertions.assertTrue(finished);
        }

        //**********TEST BOARD TWO ADVANCED**********
        for(int i = 0; i < 2; i++) {
            List<String> players = new ArrayList<>();
            players.add("First");
            players.add("Second");
            setMatch(players, true, false);

            boolean finished = false;
            // Round 1
            finished = makeParametricRound(finished, new int[]{1, 2}, new String[]{"First", "Second"});
            finished = makeParametricRound(finished, new int[]{2, 5}, new String[]{"First", "Second"});
            finished = makeParametricRound(finished, new int[]{3, 4}, new String[]{"First", "Second"});
            finished = makeParametricRound(finished, new int[]{4, 6}, new String[]{"First", "Second"});
            finished = makeParametricRound(finished, new int[]{5, 3}, new String[]{"Second", "First"});
            finished = makeParametricRound(finished, new int[]{6, 1}, new String[]{"Second", "First"});
            finished = makeParametricRound(finished, new int[]{10, 7}, new String[]{"Second", "First"});
            finished = makeParametricRound(finished, new int[]{7, 8}, new String[]{"First", "Second"});
            finished = makeParametricRound(finished, new int[]{8, 9}, new String[]{"First", "Second"});
            finished = makeParametricRound(finished, new int[]{9, 10}, new String[]{"First", "Second"});
            Assertions.assertTrue(finished);
        }

        //**********TEST BOARD THREE NOT ADVANCED**********
        for(int i = 0; i < 2; i++) {
            List<String> players = new ArrayList<>();
            players.add("First");
            players.add("Second");
            players.add("Third");
            setMatch(players, false, false);

            boolean finished = false;
            // Round 1
            finished = makeParametricRound(finished, new int[]{1, 2, 3}, new String[]{"First", "Second", "Third"});
            finished = makeParametricRound(finished, new int[]{2, 5, 4}, new String[]{"First", "Third", "Second"});
            finished = makeParametricRound(finished, new int[]{3, 4, 5}, new String[]{"First", "Second", "Third"});
            finished = makeParametricRound(finished, new int[]{4, 6, 7}, new String[]{"First", "Second", "Third"});
            finished = makeParametricRound(finished, new int[]{5, 3, 2}, new String[]{"Third", "Second", "First"});
            finished = makeParametricRound(finished, new int[]{6, 1, 8}, new String[]{"Second", "First", "Third"});
            finished = makeParametricRound(finished, new int[]{10, 7, 1}, new String[]{"Third", "Second", "First"});
            finished = makeParametricRound(finished, new int[]{7, 8, 6}, new String[]{"Third", "First", "Second"});
            finished = makeParametricRound(finished, new int[]{8, 9, 10}, new String[]{"First", "Second", "Third"});
            finished = makeParametricRound(finished, new int[]{9, 10, 9}, new String[]{"First", "Third", "Second"});
            Assertions.assertTrue(finished);
        }

        //**********TEST BOARD THREE ADVANCED**********
        for(int i = 0; i < 2; i++) {
            List<String> players = new ArrayList<>();
            players.add("First");
            players.add("Second");
            players.add("Third");
            setMatch(players, true, false);

            boolean finished = false;
            // Round 1
            finished = makeParametricRound(finished, new int[]{1, 2, 3}, new String[]{"First", "Second", "Third"});
            finished = makeParametricRound(finished, new int[]{2, 5, 4}, new String[]{"First", "Third", "Second"});
            finished = makeParametricRound(finished, new int[]{3, 4, 5}, new String[]{"First", "Second", "Third"});
            finished = makeParametricRound(finished, new int[]{4, 6, 7}, new String[]{"First", "Second", "Third"});
            finished = makeParametricRound(finished, new int[]{5, 3, 2}, new String[]{"Third", "Second", "First"});
            finished = makeParametricRound(finished, new int[]{6, 1, 8}, new String[]{"Second", "First", "Third"});
            finished = makeParametricRound(finished, new int[]{10, 7, 1}, new String[]{"Third", "Second", "First"});
            finished = makeParametricRound(finished, new int[]{7, 8, 6}, new String[]{"Third", "First", "Second"});
            finished = makeParametricRound(finished, new int[]{8, 9, 10}, new String[]{"First", "Second", "Third"});
            finished = makeParametricRound(finished, new int[]{9, 10, 9}, new String[]{"First", "Third", "Second"});
            Assertions.assertTrue(finished);
        }

        //**********CASE BOARD FOUR NOT ADVANCED**********
        for(int i = 0; i < 5; i++) {
            List<String> players = new ArrayList<>();
            players.add("First");
            players.add("Second");
            players.add("Third");
            players.add("Fourth");
            setMatch(players, false, false);

            boolean finished = false;
            // Round 1
            finished = makeParametricRound(finished, new int[]{1, 2, 3, 4}, new String[]{"First", "Second", "Third", "Fourth"});
            finished = makeParametricRound(finished, new int[]{2, 5, 4, 3}, new String[]{"First", "Fourth", "Third", "Second"});
            finished = makeParametricRound(finished, new int[]{3, 4, 5, 6}, new String[]{"First", "Second", "Third", "Fourth"});
            finished = makeParametricRound(finished, new int[]{4, 6, 7, 8}, new String[]{"First", "Second", "Third", "Fourth"});
            finished = makeParametricRound(finished, new int[]{5, 3, 2, 1}, new String[]{"Fourth", "Third", "Second", "First"});
            finished = makeParametricRound(finished, new int[]{6, 1, 8, 2}, new String[]{"Second", "Fourth", "First", "Third"});
            finished = makeParametricRound(finished, new int[]{10, 7, 1, 5}, new String[]{"Third", "Fourth", "Second", "First"});
            finished = makeParametricRound(finished, new int[]{7, 8, 6, 9}, new String[]{"Third", "First", "Second", "Fourth"});
            finished = makeParametricRound(finished, new int[]{8, 9, 10, 7}, new String[]{"Fourth", "First", "Second", "Third"});
            finished = makeParametricRound(finished, new int[]{9, 10, 9, 10}, new String[]{"First", "Third", "Fourth", "Second"});
            Assertions.assertTrue(finished);
        }

        //**********CASE BOARD FOUR ADVANCED**********
        for(int i = 0; i < 10; i++) {
            List<String> players = new ArrayList<>();
            players.add("First");
            players.add("Second");
            players.add("Third");
            players.add("Fourth");
            setMatch(players, true, false);

            boolean finished = false;
            // Round 1
            finished = makeParametricRound(finished, new int[]{1, 2, 3, 4}, new String[]{"First", "Second", "Third", "Fourth"});
            finished = makeParametricRound(finished, new int[]{2, 5, 4, 3}, new String[]{"First", "Fourth", "Third", "Second"});
            finished = makeParametricRound(finished, new int[]{3, 4, 5, 6}, new String[]{"First", "Second", "Third", "Fourth"});
            finished = makeParametricRound(finished, new int[]{4, 6, 7, 8}, new String[]{"First", "Second", "Third", "Fourth"});
            finished = makeParametricRound(finished, new int[]{5, 3, 2, 1}, new String[]{"Fourth", "Third", "Second", "First"});
            finished = makeParametricRound(finished, new int[]{6, 1, 8, 2}, new String[]{"Second", "Fourth", "First", "Third"});
            finished = makeParametricRound(finished, new int[]{10, 7, 1, 5}, new String[]{"Third", "Fourth", "Second", "First"});
            finished = makeParametricRound(finished, new int[]{7, 8, 6, 9}, new String[]{"Third", "First", "Second", "Fourth"});
            finished = makeParametricRound(finished, new int[]{8, 9, 10, 7}, new String[]{"Fourth", "First", "Second", "Third"});
            finished = makeParametricRound(finished, new int[]{9, 10, 9, 10}, new String[]{"First", "Third", "Fourth", "Second"});
            Assertions.assertTrue(finished);
        }
    }

    /**
     * Sets a new match and a new controller, adding all given players
     * @param nicknames list of players of the match
     * @param isAdvanced true if advanced, false if not
     */
    private void setMatch(List<String> nicknames, boolean isAdvanced, boolean persistenceOn){
        server.resetServer();
        ControllerTest.controller = new Controller(server);
        controller.setUsePersistence(persistenceOn);

        //CREATE MATCH
        List<String> coloursToChoose = new ArrayList<>();
        if(nicknames.size() == 4){
            coloursToChoose.add("white"); coloursToChoose.add("white"); coloursToChoose.add("black"); coloursToChoose.add("black");
        }
        else{
            coloursToChoose.add("white"); coloursToChoose.add("black"); coloursToChoose.add("gray");
        }
        ServerView[] views = {view1, view2, view3, view4};

        MessageCreateMatch m1 = new MessageCreateMatch(nicknames.get(0), coloursToChoose.get(0), nicknames.size(), isAdvanced, views[0]);
        try {
            controller.update(m1);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(State.WAITING_PLAYERS, controller.getControllerState().getState());
        Assertions.assertEquals(nicknames.get(0), controller.getPlayers().get(0).getNickname());

        //ADD PLAYERS
        for(int i = 1; i < nicknames.size(); i++){
            MessageAddPlayer m2 = new MessageAddPlayer(nicknames.get(i), coloursToChoose.get(i), views[i]);
            try {
                controller.update(m2);
            } catch (ControllerException e) {
                e.printStackTrace();
            }
            if(!persistenceOn){ //persistence make States "strange", so it's probable I'll not have PLANNING2
                if(i < nicknames.size() - 1){
                    Assertions.assertEquals(State.WAITING_PLAYERS, controller.getControllerState().getState());
                    Assertions.assertEquals(nicknames.get(i), controller.getPlayers().get(i).getNickname());
                }
                else{
                    Assertions.assertEquals(State.PLANNING2, controller.getControllerState().getState());
                }
            }

        }
    }

    private boolean makeParametricRound(boolean finished, int[] priorities, String[] orderedPlayers){
        String[] sitPLayers = {"First", "Second", "Third", "Fourth"};
        if (!finished) {
            // Set player's priorities
            Map<String, Integer> playerPriority  = new HashMap<>();
            for(int i = 0; i < priorities.length; i++){
                playerPriority.put(sitPLayers[i], priorities[i]);
            }
            // Set ordered players
            List<String> players = new ArrayList<>(Arrays.asList(orderedPlayers));
            // Do round
            try {
                return makeRound(players, playerPriority);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * This makes a round of play making no errors in choices
     * @param orderedNickname list of players in order od play after choosing their assistant cards
     * @param nicknamePriority associates to the nickname, the priority of the assistant card he will play
     * @return true if game ended, false otherwise
     */
    private boolean makeRound(List<String> orderedNickname, Map<String, Integer> nicknamePriority) throws Exception {
        // Find index of the first player who has to play in sitPlayers (AC order starts from him and goes in clockwise sense)
        int sitPlayerIndex = 0;
        for(Player p : controller.getSitPlayers()){
            if(p.getNickname().equals(controller.getPlayers().get(0).getNickname())){
                break;
            }
            sitPlayerIndex++;
        }
        for(int turn = 0; turn < orderedNickname.size(); turn++){
            //-----MessageAssistantCard 1-----
            String nicknameCurrentPlayer = controller.getSitPlayers().get(sitPlayerIndex).getNickname();
            int turnPriority = nicknamePriority.get(nicknameCurrentPlayer);
            int motherNatureMovement = priorityMNMovement.get(turnPriority);
            MessageAssistantCard messageAssistantCard = new MessageAssistantCard(nicknameCurrentPlayer, motherNatureMovement, turnPriority);
            try {
                controller.update(messageAssistantCard);
            } catch (ControllerException e) {
                e.printStackTrace();
            }

            sitPlayerIndex = (sitPlayerIndex + 1) % orderedNickname.size();

            // Controls
            if(turn < orderedNickname.size() - 1){
                Assertions.assertEquals(State.PLANNING2, controller.getControllerState().getState());
                Assertions.assertEquals(controller.getSitPlayers().get(sitPlayerIndex).getNickname(), controller.getCurrentSitPlayer().getNickname());
            }
            else{ //finished PLANNING2 => go to ACTION1
                Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());
                Assertions.assertEquals(orderedNickname.get(0), controller.getCurrentPlayer().getNickname());
            }
        }

        int maxNumStudentsToMove;
        if(orderedNickname.size() == 3){
            maxNumStudentsToMove = 4;
        }
        else{
            maxNumStudentsToMove = 3;
        }
        String colourToMove = null;
        int archipelagoToChoose = 0;
        for(int turn = 0; turn < orderedNickname.size(); turn++){
            Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), maxNumStudentsToMove);
            //-----MessageStudentHallToDiningRoom 1-----
            // choose a Student which exists and such that it doesn't go over the maximum size of the dining room
            SPColour colourGoingToChoose = null;
            for(int i = 0; i < controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().size(); i++){
                colourGoingToChoose = controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(i).getColour();
                if(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getNumStudentColour(colourGoingToChoose) < 10){
                    colourToMove = mapSPColourToString(colourGoingToChoose);
                    break;
                }
            }
            //colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());
            MessageStudentHallToDiningRoom m1 = new MessageStudentHallToDiningRoom(controller.getCurrentPlayer().getNickname(), colourToMove);
            try {
                controller.update(m1);
            } catch (ControllerException e) {
                System.out.println("Current player: " + controller.getCurrentPlayer().getNickname());
                System.out.println("Student chosen " + colourToMove);
                System.out.println("His hall " + controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall());
                System.out.println("Students of that colour in dining " + controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getNumStudentColour(colourGoingToChoose));
                e.printStackTrace();
            }
            Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), maxNumStudentsToMove - 1);
            Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());

            //-----MessageStudentHallToDiningRoom 2-----
            // choose a Student which exists and such that it doesn't go over the maximum size of the dining room
            for(int i = 0; i < controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().size(); i++){
                colourGoingToChoose = controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(i).getColour();
                if(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getNumStudentColour(colourGoingToChoose) < 10){
                    colourToMove = mapSPColourToString(colourGoingToChoose);
                    break;
                }
            }
            //colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());


            MessageStudentHallToDiningRoom m2 = new MessageStudentHallToDiningRoom(controller.getCurrentPlayer().getNickname(), colourToMove);
            try {
                controller.update(m2);
            } catch (ControllerException e) {
                System.out.println("Current player: " + controller.getCurrentPlayer().getNickname());
                System.out.println("Student chosen " + colourToMove);
                System.out.println("His hall " + controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall());
                System.out.println("Students of that colour in dining " + controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getNumStudentColour(colourGoingToChoose));
                e.printStackTrace();
            }
            Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), maxNumStudentsToMove - 2);
            Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());

            //-----MessageStudentToArchipelago-----
            // choose a Student which exists
            if(controller.isAdvanced()){
                colourToMove = mapSPColourToString(controller.getBoardAdvanced().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());
            }
            else{
                colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());
            }

            MessageStudentToArchipelago m3 = new MessageStudentToArchipelago(controller.getCurrentPlayer().getNickname(), colourToMove, archipelagoToChoose);
            Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), maxNumStudentsToMove - 2);
            try {
                controller.update(m3);
            } catch (ControllerException e) {
                e.printStackTrace();
            }
            if(orderedNickname.size() == 3){
                Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());
            }
            else{
                Assertions.assertEquals(State.ACTION2, controller.getControllerState().getState());
                archipelagoToChoose++;
            }

            if(orderedNickname.size() == 3){
                //-----MessageStudentHallToDiningRoom 4 (case BoardThree)-----
                // choose a Student which exists and such that it doesn't go over the maximum size of the dining room
                for(int i = 0; i < controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().size(); i++){
                    colourGoingToChoose = controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(i).getColour();
                    if(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getNumStudentColour(colourGoingToChoose) < 10){
                        colourToMove = mapSPColourToString(colourGoingToChoose);
                        break;
                    }
                }
                //colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());


                MessageStudentHallToDiningRoom m4p3 = new MessageStudentHallToDiningRoom(controller.getCurrentPlayer().getNickname(), colourToMove);
                try {
                    controller.update(m4p3);
                } catch (ControllerException e) {
                    System.out.println("Current player: " + controller.getCurrentPlayer().getNickname());
                    System.out.println("Student chosen " + colourToMove);
                    System.out.println("His hall " + controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall());
                    System.out.println("Students of that colour in dining " + controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getNumStudentColour(colourGoingToChoose));
                    e.printStackTrace();
                }
                Assertions.assertEquals(State.ACTION2, controller.getControllerState().getState());
                archipelagoToChoose++;
            }

            //-----MessageMoveMotherNature-----
            MessageMoveMotherNature m4 = new MessageMoveMotherNature(controller.getCurrentPlayer().getNickname(), priorityMNMovement.get(nicknamePriority.get(controller.getCurrentPlayer().getNickname())));
            try {
                controller.update(m4);
            } catch (ControllerException e) {
                e.printStackTrace();
            }
            if(controller.getControllerState().getState() == State.END){
                if(controller.getBoard().getNumArchipelagos() <= 3){
                    return true;
                }
                else{
                    for(Player p: controller.getBoard().getPlayers()){
                        if(controller.getBoard().getPlayerSchool(p).getNumTowers() == 0){
                            return true;
                        }
                    }
                    throw new Exception();

                }
            }
            Assertions.assertEquals(State.ACTION3, controller.getControllerState().getState());

            //-----MessageStudentCloudToSchool-----
            MessageStudentCloudToSchool m5 = new MessageStudentCloudToSchool(controller.getCurrentPlayer().getNickname(), turn);
            try {
                controller.update(m5);
            } catch (ControllerException e) {
                e.printStackTrace();
            }
            if(controller.getControllerState().getState() == State.END){
                for(int i = 0; i < orderedNickname.size(); i++){
                    if(controller.getBoard().getPlayers().get(i).getPlayerHand().size() == 0){
                        return true;
                    }
                    else if(controller.getBoard().getBag().getNumStudents() == 0){
                        return true;
                    }
                    else if(controller.getBoard().getNumArchipelagos() <= 3){
                        return true;
                    }
                    else{
                        throw new Exception();
                    }
                }

            }
            if(turn < orderedNickname.size() - 1){
                Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());
                Assertions.assertEquals(orderedNickname.get(turn + 1), controller.getCurrentPlayer().getNickname());
            }
            else{
                Assertions.assertEquals(State.PLANNING2, controller.getControllerState().getState());
            }
        }
        return false;
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
        return null; //unreachable
    }
}
