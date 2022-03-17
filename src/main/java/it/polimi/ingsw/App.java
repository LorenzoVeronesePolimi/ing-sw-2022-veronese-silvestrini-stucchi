package it.polimi.ingsw;

import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Exceptions.AnotherTowerException;
import it.polimi.ingsw.Model.Exceptions.InvalidTowerNumberException;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Pawns.Tower;
import it.polimi.ingsw.Model.Places.Archipelago;
import it.polimi.ingsw.Model.Places.Island;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world! Giada
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        System.out.println( "Hello World!" );
        Bag bag = new Bag();

        Player player1 = new Player("Mario Rossi", PlayerColour.WHITE);

        List<Student> initialStudents = bag.getInitialStudents();
        Archipelago a1 = new Archipelago();
        Archipelago a2 = new Archipelago();

        Tower t = new Tower(player1);

        List<Tower> towerToAdd = new ArrayList<Tower>();
        //a1.getNumIslands to know how many Towers I have to add
        towerToAdd.add(t);
        try{
            a1.conquerArchipelago(towerToAdd);
        } catch(InvalidTowerNumberException ex){ex.printStackTrace();}
    }
}
