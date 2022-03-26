package it.polimi.ingsw.Model;

import static org.junit.Assert.*;

import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Exceptions.ExceedingAssistantCardNumberException;
import it.polimi.ingsw.Model.Exceptions.NoAssistantCardException;
import org.junit.jupiter.api.Test;

public class PlayerTest {
    @Test
    void getNickname() {
        Player tested = new Player("nickname", PlayerColour.BLACK);
        assertEquals("nickname", tested.getNickname());
    }

    @Test
    void getLastCard() {
        Player tested = new Player("nickname", PlayerColour.BLACK);
        AssistantCard assistantCard = new AssistantCard(2, 6);
        try {
            tested.addAssistantCard(assistantCard);
        } catch (ExceedingAssistantCardNumberException e) {
            e.printStackTrace();
        }

        //no card was played, so no value to the attribute lastCard
        assertEquals(null, tested.getLastCard());

        try {
            tested.useAssistantCard(6);
        } catch (NoAssistantCardException e) {
            e.printStackTrace();
        }

        //card was played, so value to the attribute lastCard
        assertEquals(assistantCard, tested.getLastCard());
    }

    @Test
    public void getColour() {
        Player tested = new Player("nickname", PlayerColour.BLACK);
        assertEquals(PlayerColour.BLACK, tested.getColour());

        tested = new Player("nickname", PlayerColour.WHITE);
        assertEquals(PlayerColour.WHITE, tested.getColour());

        tested = new Player("nickname", PlayerColour.GRAY);
        assertEquals(PlayerColour.GRAY, tested.getColour());
    }

    @Test
    public void getHandLength() {
        Player testedPlayer = new Player("nickname", PlayerColour.BLACK);
        AssistantCard assistantCard = null;

        //test the input of the cards (increasing playerHand)
        for(int i = 0; i < 10; i++) {
            assistantCard = new AssistantCard(i+1, i + 1);
            try {
                testedPlayer.addAssistantCard(assistantCard);
            } catch (ExceedingAssistantCardNumberException e) {
                e.printStackTrace();
            }
            //check the length of the playerHand
            assertEquals(testedPlayer.getHandLength(), i + 1);
        }

        //test the usage of the cards (decreasing playerHand)
        for(int i = 9; i >= 0; i--) {
            try {
                testedPlayer.useAssistantCard(i+1);
            } catch (NoAssistantCardException e) {
                e.printStackTrace();
            }

            //check the length of the playerHand
            assertEquals(testedPlayer.getHandLength(), i);
        }

        //no cards left in playerHand
        assertEquals(testedPlayer.getHandLength(), 0);
    }

    @Test
    public void addAssistantCard() {
        Player testedPlayer = new Player("nickname", PlayerColour.BLACK);
        AssistantCard assistantCard = null;

        //test the input of the cards
        for(int i = 0; i < 10; i++) {
            assistantCard = new AssistantCard(i+1, i + 1);
            try {
                testedPlayer.addAssistantCard(assistantCard);
            } catch (ExceedingAssistantCardNumberException e) {
                e.printStackTrace();
            }
            //check the length of the playerHand
            assertEquals(testedPlayer.getHandLength(), i + 1);
        }

        assertThrows(ExceedingAssistantCardNumberException.class, () -> testedPlayer.addAssistantCard(new AssistantCard(1,1)));

    }

    @Test
    public void useAssistantCard() {
        Player testedPlayer = new Player("nickname", PlayerColour.BLACK);
        AssistantCard assistantCard = null;

        //test the input of the cards (increasing playerHand)
        for(int i = 0; i < 10; i++) {
            assistantCard = new AssistantCard(i+1, i + 1);
            try {
                testedPlayer.addAssistantCard(assistantCard);
            } catch (ExceedingAssistantCardNumberException e) {
                e.printStackTrace();
            }
            //check the length of the playerHand
            assertEquals(testedPlayer.getHandLength(), i + 1);
        }

        //test the usage of the cards (decreasing playerHand)
        for(int i = 9; i >= 0; i--) {
            try {
                testedPlayer.useAssistantCard(i+1);
            } catch (NoAssistantCardException e) {
                e.printStackTrace();
            }

            //check the length of the playerHand
            assertEquals(testedPlayer.getHandLength(), i);
        }

        assertThrows(NoAssistantCardException.class, () -> testedPlayer.useAssistantCard(1000));
    }

    @Test
    public void toStringTest() {
        Player testedPlayer = new Player("nickname", PlayerColour.BLACK);

        assertEquals("Player{" +
                "nickname='" + testedPlayer.getNickname() + '\'' +
                ", colour=" + testedPlayer.getColour() +
                '}', testedPlayer.toString());
    }
}
