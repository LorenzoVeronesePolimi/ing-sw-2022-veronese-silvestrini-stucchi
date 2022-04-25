package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Enumerations.State;
import it.polimi.ingsw.Messages.INMessage.MessageAddPlayer;
import it.polimi.ingsw.Messages.INMessage.MessageCreateMatch;
import it.polimi.ingsw.Server.ClientConnection;
import it.polimi.ingsw.Server.Server;
import it.polimi.ingsw.Server.SocketClientConnectionCLI;
import it.polimi.ingsw.View.ServerView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.MouseWheelEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ControllerTest {
    Controller controller;
    private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    /*Socket socket;
    ServerView view;
    Server server;*/

    @BeforeEach
    void init() {
        controller = new Controller();
        System.setOut(new PrintStream(outContent));
        /*try {
            socket = new Socket("127.0.0.1", 54321);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            server = new Server();
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
        view = new ServerView(new SocketClientConnectionCLI(socket, server, controller), controller);*/

    }

    @Test
    void controllerTest(){
        /*-----MessageCreateMatch-----*/
        //ERRORS IN FORMAT
        //Error because of wrong name ("")
        MessageCreateMatch m1Err1 = new MessageCreateMatch("", "white", 2, true);
        controller.update(m1Err1);
        Assertions.assertEquals("Invalid format", outContent.toString().trim());
        //Error because of wrong colour
        this.resetOutput();
        MessageCreateMatch m1Err2 = new MessageCreateMatch("First", "brown", 2, true);
        controller.update(m1Err2);
        Assertions.assertEquals("Invalid format", outContent.toString().trim());
        //ERRORS IN STATE
        //Error because of wrong numPlayers
        this.resetOutput();
        MessageAddPlayer m1Err3 = new MessageAddPlayer("Second", "white");
        controller.update(m1Err3);
        Assertions.assertEquals("You can't do that now", outContent.toString().trim());
        //ERRORS IN INTEGRITY
        this.resetOutput();
        MessageCreateMatch m1Err4 = new MessageCreateMatch("First", "white", 5, true);
        controller.update(m1Err4);
        Assertions.assertEquals("Error", outContent.toString().trim());
        //OK
        this.resetOutput();
        MessageCreateMatch m1 = new MessageCreateMatch("First", "white", 2, true);
        controller.update(m1);
        Assertions.assertEquals("", outContent.toString().trim());
        Assertions.assertEquals(State.WAITING_PLAYERS, controller.getControllerState().getState());
        Assertions.assertEquals("First", controller.getPlayers().get(0).getNickname());
        assertTrue(controller.isAdvanced());


    }

    private void resetOutput(){
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }
}