package it.polimi.ingsw.Model.Places;

import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Pawns.Coin;
import it.polimi.ingsw.Model.Places.School.SchoolAdvanced;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class SchoolAdvancedTest {

    @Test
    void getNumCoins(){
        Player p = new Player("owner", PlayerColour.WHITE);
        SchoolAdvanced schoolAdvanced = new SchoolAdvanced(p,7,8);
        Assertions.assertEquals(0,schoolAdvanced.getNumCoins());

        Coin coin = new Coin();
        schoolAdvanced.addCoin(coin);
        Assertions.assertEquals(1, schoolAdvanced.getNumCoins());

        Assertions.assertEquals("nickname=" + p.getNickname() + ", colour=" + p.getColour() +
                "\n\t Hall=" + schoolAdvanced.getStudentsHall() +
                "\n\t Dining=[RED=" + schoolAdvanced.getNumStudentColour(SPColour.RED) +
                ", PINK=" + schoolAdvanced.getNumStudentColour(SPColour.PINK) +
                ", GREEN=" + schoolAdvanced.getNumStudentColour(SPColour.GREEN) +
                ", YELLOW=" + schoolAdvanced.getNumStudentColour(SPColour.YELLOW) +
                ", BLUE=" + schoolAdvanced.getNumStudentColour(SPColour.BLUE) +
                "]\n\t professors=" + schoolAdvanced.getProfessors() +
                "\t towers=" + schoolAdvanced.getTowers().size() +
                "\t coins=" + schoolAdvanced.getNumCoins()
        , schoolAdvanced.toString());
    }
}
