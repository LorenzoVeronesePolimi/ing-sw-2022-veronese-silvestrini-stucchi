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
 * Init Board:
 *
 *
 * Planning:
 * 1. fillCloud (all)
 * 2. AssistantCard (all)
 *      2.1. check turn order
 *
 *
 * Action:
 * 1. move student (1.1. dining, 1.2. archi)
 *      1.1. check coin, check professors
 *      1.2. check update of archipelago (Student colour)
 * 2. move MN
 *      2.1. check if conquerable
 *          2.1.1. conquer archipelago
 *          2.1.2. check merge archipelago, merge archi
 *3. choose cloud
 */
public class App 
{
    public static void main( String[] args )
    {

        System.out.println( "Hello World!" );

        //This is accomplished in Board (abstract)
        //Bag bag = new Bag(); //Bag is now a singleton, so this should be replaced with [bag = Bag.instance();]


        Player player1 = new Player("Mario Rossi", PlayerColour.WHITE);

        //List<Student> initialStudents = bag.getInitialStudents();
        //Archipelago a1 = new Archipelago();
        //Archipelago a2 = new Archipelago();

        Tower t = new Tower(player1);

        List<Tower> towerToAdd = new ArrayList<Tower>();
        //a1.getNumIslands to know how many Towers I have to add
        towerToAdd.add(t);
        /*try{
            a1.conquerArchipelago(towerToAdd);
        } catch(InvalidTowerNumberException ex){ex.printStackTrace();}*/
    }
}
