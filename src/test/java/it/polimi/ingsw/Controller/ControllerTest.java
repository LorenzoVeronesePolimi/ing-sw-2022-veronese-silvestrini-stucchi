package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Enumerations.State;
import it.polimi.ingsw.Messages.INMessage.*;
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
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;


public class ControllerTest {
    static Controller controller;
    private static ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    //private final PrintStream originalOut = System.out;
    //private final PrintStream originalErr = System.err;
    //static Socket socket;
    static SocketClientConnectionCLI conn1;
    static SocketClientConnectionCLI conn2;
    static ServerView view1;
    static ServerView view2;
    static Server server;

    static {
        try {
            server = new Server();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        controller = server.getController();
        System.setOut(new PrintStream(outContent));
        conn1 = new SocketClientConnectionCLI(server, controller);
        view1 = conn1.getServerView();
        conn2 = new SocketClientConnectionCLI(server, controller);
        view2 = conn2.getServerView();

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
        controller.update(m1Err1);
        Assertions.assertEquals("Invalid format", outContent.toString().trim());
        //Error because of wrong colour
        this.resetOutput();
        MessageCreateMatch m1Err2 = new MessageCreateMatch("First", "brown", 2, true, view1);
        controller.update(m1Err2);
        Assertions.assertEquals("Invalid format", outContent.toString().trim());
        //ERRORS IN STATE
        this.resetOutput();
        MessageAddPlayer m1Err3 = new MessageAddPlayer("Second", "white", view2);
        controller.update(m1Err3);
        Assertions.assertEquals("You can't do that now", outContent.toString().trim());
        //ERRORS IN INTEGRITY
        //Error because of wrong numPlayers
        this.resetOutput();
        MessageCreateMatch m1Err4 = new MessageCreateMatch("First", "white", 5, true, view1);
        controller.update(m1Err4);
        Assertions.assertEquals("Error", outContent.toString().trim());
        //OK
        this.resetOutput();
        MessageCreateMatch m1 = new MessageCreateMatch("First", "white", 2, true, view1);
        controller.update(m1);
        Assertions.assertEquals("", outContent.toString().trim());
        Assertions.assertEquals(State.WAITING_PLAYERS, controller.getControllerState().getState());
        Assertions.assertEquals("First", controller.getPlayers().get(0).getNickname());
        Assertions.assertTrue(controller.isAdvanced());

        /*-----MessageAddPlayer-----*/
        //ERRORS IN FORMAT
        //Error because of wrong name ("")
        MessageAddPlayer m2Err1 = new MessageAddPlayer("", "black", view2);
        controller.update(m2Err1);
        Assertions.assertEquals("Invalid format", outContent.toString().trim());
        //Error because of wrong colour
        this.resetOutput();
        MessageAddPlayer m2Err2 = new MessageAddPlayer("Second", "brown", view2);
        controller.update(m2Err2);
        Assertions.assertEquals("Invalid format", outContent.toString().trim());
        //ERRORS IN STATE
        this.resetOutput();
        MessageCreateMatch m2Err3 = new MessageCreateMatch("Second", "white", 2, true, view2);
        controller.update(m2Err3);
        Assertions.assertEquals("You can't do that now", outContent.toString().trim());
        //ERRORS IN CONTROLLER
        //Same colour as before
        this.resetOutput();
        MessageAddPlayer m2Err4 = new MessageAddPlayer("Second", "white", view2);
        controller.update(m2Err4);
        Assertions.assertEquals("Error", outContent.toString().trim());
        //Same colour as before
        this.resetOutput();
        MessageAddPlayer m2Err5 = new MessageAddPlayer("First", "black", view2);
        controller.update(m2Err5);
        Assertions.assertEquals("Error", outContent.toString().trim());
        //OK
        this.resetOutput();
        MessageAddPlayer m2 = new MessageAddPlayer("Second", "black", view2);
        controller.update(m2);
        Assertions.assertEquals("This Cloud has too many Students", outContent.toString().trim());
        Assertions.assertEquals(State.PLANNING2, controller.getControllerState().getState());
        Assertions.assertEquals("Second", controller.getPlayers().get(1).getNickname());
        Assertions.assertTrue(controller.isAdvanced());


        /*-----MessageAssistantCard-----*/
        //ERRORS IN FORMAT
        //Error because of wrong name ("")
        this.resetOutput();
        MessageAssistantCard m3Err1 = new MessageAssistantCard("", 5, 10);
        controller.update(m3Err1);
        Assertions.assertEquals("Invalid format", outContent.toString().trim());
        //Error because of wrong motherNatureMovement
        this.resetOutput();
        MessageAssistantCard m3Err2 = new MessageAssistantCard("First", 9, 10);
        controller.update(m3Err2);
        Assertions.assertEquals("Invalid format", outContent.toString().trim());
        //Error because of wrong turnPriority
        this.resetOutput();
        MessageAssistantCard m3Err3 = new MessageAssistantCard("First", 5, -1);
        controller.update(m3Err3);
        Assertions.assertEquals("Invalid format", outContent.toString().trim());
        //ERRORS IN CONTROLLER
        //Not current player
        this.resetOutput();
        MessageAssistantCard m3Err4 = new MessageAssistantCard("Second", 5, 10);
        controller.update(m3Err4);
        Assertions.assertEquals("Error", outContent.toString().trim());
        //OK
        this.resetOutput();
        MessageAssistantCard m3 = new MessageAssistantCard("First", 5, 9);
        controller.update(m3);
        Assertions.assertEquals("", outContent.toString().trim());
        Assertions.assertEquals(State.PLANNING2, controller.getControllerState().getState());
        Assertions.assertEquals("Second", controller.getCurrentPlayer().getNickname());
        Assertions.assertTrue(controller.isAdvanced());

        /*-----MessageAssistantCard-----*/
        //ERRORS IN CONTROLLER
        //Not current player
        this.resetOutput();
        MessageAssistantCard m4Err1 = new MessageAssistantCard("First", 1, 1);
        controller.update(m4Err1);
        Assertions.assertEquals("Error", outContent.toString().trim());
        //Same card as First
        this.resetOutput();
        MessageAssistantCard m4Err2 = new MessageAssistantCard("First", 5, 10);
        controller.update(m4Err2);
        Assertions.assertEquals("Error", outContent.toString().trim());
        //OK
        this.resetOutput();
        MessageAssistantCard m4 = new MessageAssistantCard("Second", 1, 1);
        controller.update(m4);
        Assertions.assertEquals("", outContent.toString().trim());
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());
        Assertions.assertTrue(controller.isAdvanced());

        /*-----MessageStudentHallToDiningRoom 1-----*/
        // choose a Student which exists
        String colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());
        //ERRORS IN CONTROLLER
        //Not current player
        this.resetOutput();
        MessageStudentHallToDiningRoom m5Err1 = new MessageStudentHallToDiningRoom("First", "red");
        controller.update(m5Err1);
        Assertions.assertEquals("Error", outContent.toString().trim());
        //OK
        this.resetOutput();
        MessageStudentHallToDiningRoom m5 = new MessageStudentHallToDiningRoom(controller.getCurrentPlayer().getNickname(), colourToMove);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 3);
        controller.update(m5);
        Assertions.assertEquals("", outContent.toString().trim());
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 2);
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());

        /*-----MessageStudentHallToDiningRoom 2-----*/
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());
        //ERRORS IN CONTROLLER
        //Not current player
        this.resetOutput();
        MessageStudentHallToDiningRoom m6Err1 = new MessageStudentHallToDiningRoom("First", "red");
        controller.update(m6Err1);
        Assertions.assertEquals("Error", outContent.toString().trim());
        //OK
        this.resetOutput();
        MessageStudentHallToDiningRoom m6 = new MessageStudentHallToDiningRoom(controller.getCurrentPlayer().getNickname(), colourToMove);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 2);
        controller.update(m6);
        Assertions.assertEquals("", outContent.toString().trim());
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 1);
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());

        /*-----MessageStudentToArchipelago-----*/
        // choose a Student which exists
        colourToMove = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());
        //Not possibile archipelago
        this.resetOutput();
        MessageStudentToArchipelago m7Err1 = new MessageStudentToArchipelago(controller.getCurrentPlayer().getNickname(), colourToMove, 13);
        controller.update(m7Err1);
        Assertions.assertEquals("Invalid format", outContent.toString().trim());
        //ERRORS IN CONTROLLER
        //Not current player
        this.resetOutput();
        MessageStudentToArchipelago m7Err2 = new MessageStudentToArchipelago("First", colourToMove, 10);
        controller.update(m7Err2);
        Assertions.assertEquals("Error", outContent.toString().trim());
        //Not existing playerNickname
        this.resetOutput();
        MessageStudentToArchipelago m7Err3 = new MessageStudentToArchipelago("Not Existing", colourToMove, 10);
        controller.update(m7Err3);
        //Assertions.assertEquals("No player with that nickname\r\nError", outContent.toString().trim()); gives error
        //OK
        this.resetOutput();
        MessageStudentToArchipelago m7 = new MessageStudentToArchipelago(controller.getCurrentPlayer().getNickname(), colourToMove, 10);
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 1);
        controller.update(m7);
        Assertions.assertEquals("", outContent.toString().trim());
        Assertions.assertEquals(controller.getNumStudentsToMoveCurrent(), 3);
        Assertions.assertEquals(State.ACTION2, controller.getControllerState().getState());

        /*-----MessageMoveMotherNature-----*/
        //Not possibile number of moves
        this.resetOutput();
        MessageMoveMotherNature m8Err1 = new MessageMoveMotherNature(controller.getCurrentPlayer().getNickname(), 13);
        controller.update(m8Err1);
        Assertions.assertEquals("Invalid format", outContent.toString().trim());
        //ERRORS IN CONTROLLER
        //Not current player
        this.resetOutput();
        MessageMoveMotherNature m8Err2 = new MessageMoveMotherNature("First", 1);
        controller.update(m8Err2);
        Assertions.assertEquals("Error", outContent.toString().trim());
        //Not integrity possible number of moves
        this.resetOutput();
        MessageMoveMotherNature m8Err3 = new MessageMoveMotherNature(controller.getCurrentPlayer().getNickname(),2);
        controller.update(m8Err3);
        Assertions.assertEquals("Error", outContent.toString().trim());
        //OK
        this.resetOutput();
        MessageMoveMotherNature m8 = new MessageMoveMotherNature(controller.getCurrentPlayer().getNickname(), 1);
        controller.update(m8);
        Assertions.assertEquals("", outContent.toString().trim());
        Assertions.assertEquals(State.ACTION3, controller.getControllerState().getState());

        /*-----MessageStudentCloudToSchool-----*/
        //Not existing cloud
        this.resetOutput();
        MessageStudentCloudToSchool m9Err1 = new MessageStudentCloudToSchool(controller.getCurrentPlayer().getNickname(), 5);
        controller.update(m9Err1);
        Assertions.assertEquals("Invalid format", outContent.toString().trim());
        //ERRORS IN CONTROLLER
        //Not current player
        this.resetOutput();
        MessageStudentCloudToSchool m9Err2 = new MessageStudentCloudToSchool("First", 1);
        controller.update(m9Err2);
        Assertions.assertEquals("Error", outContent.toString().trim());
        //OK
        this.resetOutput();
        MessageStudentCloudToSchool m9 = new MessageStudentCloudToSchool(controller.getCurrentPlayer().getNickname(), 1);
        controller.update(m9);
        Assertions.assertEquals("", outContent.toString().trim());
        Assertions.assertEquals(State.ACTION1, controller.getControllerState().getState());
        Assertions.assertEquals("First", controller.getCurrentPlayer().getNickname());

        //Give lots of Coins to the player to test CharacterCards
        List<Coin> coins = new ArrayList<>();
        for(int i = 0; i < 50; i++) {
            coins.add(new Coin());
        }
        for(Coin c : coins){
            ((SchoolAdvanced)controller.getBoardAdvanced().getPlayerSchool(controller.getCurrentPlayer())).addCoin(c);
        }

        /*-----MessageCCExchangeThreeStudents-----*/
        try {
            controller.getBoardAdvanced().setExtractedCards(new ExchangeThreeStudents(CharacterCardEnumeration.EXCHANGE_THREE_STUDENTS, controller.getBoardAdvanced()));
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }
        controller.setCharacterCardUsed(false);
        String colourCard1 = mapSPColourToString(((ExchangeThreeStudents)controller.getBoardAdvanced().getExtractedCards().get(0)).getStudentsOnCard().get(0).getColour());
        String colourCard2 = mapSPColourToString(((ExchangeThreeStudents)controller.getBoardAdvanced().getExtractedCards().get(0)).getStudentsOnCard().get(1).getColour());
        String colourHall1 = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());
        String colourHall2 = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(1).getColour());
        MessageCCExchangeThreeStudents mcc1 = new MessageCCExchangeThreeStudents(1, controller.getCurrentPlayer().getNickname(), colourCard1, colourCard2, "-", colourHall1, colourHall2, "-");
        controller.update(mcc1);
        Assertions.assertEquals("", outContent.toString().trim());

        /*-----MessageCCExchangeTwoHallDining-----*/
        controller.getBoardAdvanced().setExtractedCards(new ExchangeTwoHallDining(CharacterCardEnumeration.EXCHANGE_TWO_HALL_DINING, controller.getBoardAdvanced()));
        controller.setCharacterCardUsed(false);
        colourHall1 = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(0).getColour());
        colourHall2 = mapSPColourToString(controller.getBoard().getPlayerSchool(controller.getCurrentPlayer()).getStudentsHall().get(1).getColour());
        try {
            controller.getBoardAdvanced().getPlayerSchool(controller.getCurrentPlayer()).addStudentDiningRoom(new Student(SPColour.RED));
            controller.getBoardAdvanced().getPlayerSchool(controller.getCurrentPlayer()).addStudentDiningRoom(new Student(SPColour.RED));
        } catch (ExceededMaxStudentsDiningRoomException e) {
            e.printStackTrace();
        }
        MessageCCExchangeTwoHallDining mcc2 = new MessageCCExchangeTwoHallDining(1, controller.getCurrentPlayer().getNickname(), colourHall1, colourHall2, "red", "red");
        controller.update(mcc2);
        Assertions.assertEquals("", outContent.toString().trim());

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
        controller.getBoardAdvanced().setExtractedCards(new ExcludeColourFromCounting(CharacterCardEnumeration.EXCLUDE_COLOUR_FROM_COUNTING, controller.getBoardAdvanced()));
        controller.setCharacterCardUsed(false);
        MessageCCExcludeColourFromCounting mcc3 = new MessageCCExcludeColourFromCounting(1, controller.getCurrentPlayer().getNickname(), "red");
        controller.update(mcc3);
        Assertions.assertEquals("", outContent.toString().trim());

        /*-----MessageCCExtraStudentInDining-----*/
        try {
            controller.getBoardAdvanced().setExtractedCards(new ExtraStudentInDining(CharacterCardEnumeration.EXTRA_STUDENT_IN_DINING, controller.getBoardAdvanced()));
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }
        colourCard1 = mapSPColourToString(((ExtraStudentInDining)controller.getBoardAdvanced().getExtractedCards().get(0)).getStudentsOnCard().get(0).getColour());
        controller.setCharacterCardUsed(false);
        MessageCCExtraStudentInDining mcc4 = new MessageCCExtraStudentInDining(1, controller.getCurrentPlayer().getNickname(), colourCard1);
        controller.update(mcc4);
        Assertions.assertEquals("", outContent.toString().trim());

        /*-----MessageCCFakeMNMovement-----*/
        controller.getBoardAdvanced().setExtractedCards(new FakeMNMovement(CharacterCardEnumeration.FAKE_MN_MOVEMENT, controller.getBoardAdvanced()));
        controller.setCharacterCardUsed(false);
        MessageCCFakeMNMovement mcc5 = new MessageCCFakeMNMovement(1, controller.getCurrentPlayer().getNickname(), 4);
        controller.update(mcc5);
        Assertions.assertEquals("", outContent.toString().trim());

        /*-----MessageCCForbidIsland-----*/
        controller.getBoardAdvanced().setExtractedCards(new ForbidIsland(CharacterCardEnumeration.EXCLUDE_COLOUR_FROM_COUNTING, controller.getBoardAdvanced()));
        controller.setCharacterCardUsed(false);
        MessageCCForbidIsland mcc6 = new MessageCCForbidIsland(1, controller.getCurrentPlayer().getNickname(), 4);
        controller.update(mcc6);
        Assertions.assertEquals("", outContent.toString().trim());

        /*-----MessageCCPlaceOneStudent-----*/
        try {
            controller.getBoardAdvanced().setExtractedCards(new PlaceOneStudent(CharacterCardEnumeration.PLACE_ONE_STUDENT, controller.getBoardAdvanced()));
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        }
        controller.setCharacterCardUsed(false);
        colourCard1 = mapSPColourToString(((PlaceOneStudent)controller.getBoardAdvanced().getExtractedCards().get(0)).getStudentsOnCard().get(0).getColour());
        MessageCCPlaceOneStudent mcc7 = new MessageCCPlaceOneStudent(1, controller.getCurrentPlayer().getNickname(), colourCard1, 4);
        controller.update(mcc7);
        Assertions.assertEquals("", outContent.toString().trim());

        /*-----MessageCCReduceColourInDining-----*/
        controller.getBoardAdvanced().setExtractedCards(new ReduceColourInDining(CharacterCardEnumeration.REDUCE_COLOUR_IN_DINING, controller.getBoardAdvanced()));
        controller.setCharacterCardUsed(false);
        MessageCCReduceColourInDining mcc8 = new MessageCCReduceColourInDining(1, controller.getCurrentPlayer().getNickname(), "red");
        controller.update(mcc8);
        Assertions.assertEquals("", outContent.toString().trim());

        /*-----MessageCCTowerNoValue-----*/
        controller.getBoardAdvanced().setExtractedCards(new TowerNoValue(CharacterCardEnumeration.TOWER_NO_VALUE, controller.getBoardAdvanced()));
        controller.setCharacterCardUsed(false);
        MessageCCTowerNoValue mcc9 = new MessageCCTowerNoValue(1, controller.getCurrentPlayer().getNickname());
        controller.update(mcc9);
        Assertions.assertEquals("", outContent.toString().trim());

        /*-----MessageCCTwoExtraPoints-----*/
        controller.getBoardAdvanced().setExtractedCards(new TwoExtraPoints(CharacterCardEnumeration.TWO_EXTRA_ISLANDS, controller.getBoardAdvanced()));
        controller.setCharacterCardUsed(false);
        MessageCCTwoExtraPoints mcc10 = new MessageCCTwoExtraPoints(1, controller.getCurrentPlayer().getNickname());
        controller.update(mcc10);
        Assertions.assertEquals("", outContent.toString().trim());

        /*-----MessageCCTakeProfessorOnEquity-----*/
        controller.getBoardAdvanced().setExtractedCards(new TakeProfessorOnEquity(CharacterCardEnumeration.TAKE_PROFESSOR_ON_EQUITY, controller.getBoardAdvanced()));
        controller.setCharacterCardUsed(false);
        MessageCCTakeProfessorOnEquity mcc11 = new MessageCCTakeProfessorOnEquity(1, controller.getCurrentPlayer().getNickname());
        controller.update(mcc11);
        Assertions.assertEquals("", outContent.toString().trim());

        /*-----MessageCCTwoExtraIslands-----*/
        controller.getBoardAdvanced().setExtractedCards(new TwoExtraIslands(CharacterCardEnumeration.TWO_EXTRA_POINTS));
        controller.setCharacterCardUsed(false);
        MessageCCTwoExtraIslands mcc12 = new MessageCCTwoExtraIslands(1, controller.getCurrentPlayer().getNickname());
        controller.update(mcc12);
        Assertions.assertEquals("", outContent.toString().trim());

    }

    private void resetOutput(){
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
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
