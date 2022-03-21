package it.polimi.ingsw;

import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Board.Board;
import it.polimi.ingsw.Model.Board.BoardFactory;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.AnotherTowerException;
import it.polimi.ingsw.Model.Exceptions.InvalidTowerNumberException;
import it.polimi.ingsw.Model.Exceptions.MergeDifferentOwnersException;
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
        //Bag bag = Bag.instance(); //Bag is a singleton

        Player player1 = new Player("Mario Bianchi", PlayerColour.WHITE);
        Player player2 = new Player("Luigi Neri", PlayerColour.BLACK);
        /*
        TEST ARCHIPELAGO
        List<Student> initialStudents = bag.getInitialStudents();
        Archipelago a1 = new Archipelago();
        Archipelago a2 = new Archipelago();

        a1.addStudent(initialStudents.remove(0));

        Tower t1 = new Tower(player1);
        List<Tower> towerToAdd1 = new ArrayList<Tower>();
        //a1.getNumIslands to know how many Towers I have to add
        towerToAdd1.add(t1);
        try{
            a1.conquerArchipelago(towerToAdd1);
        } catch(InvalidTowerNumberException ex){ex.printStackTrace();}

        System.out.println(a1.toString());

        a2.addStudent(initialStudents.remove(0));

        Tower t2 = new Tower(player2);
        List<Tower> towerToAdd2 = new ArrayList<Tower>();
        //a1.getNumIslands to know how many Towers I have to add
        towerToAdd2.add(t2);
        try{
            a2.conquerArchipelago(towerToAdd2);
        } catch(InvalidTowerNumberException ex){ex.printStackTrace();}
        System.out.println(a2.toString());

        try{a1.mergeArchipelagos(a2);}
        catch(MergeDifferentOwnersException ex){ex.printStackTrace();}
        System.out.println(a1.toString());*/

        List<Player> players = new ArrayList<Player>();
        players.add(player1);
        players.add(player2);

        BoardFactory bf = new BoardFactory(players);
        Board board = bf.createBoard(); // -> BoardTwo

        //board.moveStudentBagToSchool(1);
        System.out.println(board.getArchipelago(2));
        board.moveStudentSchoolToArchipelagos(player1, SPColour.PINK, 2);
        System.out.println(board.getArchipelago(2));
        board.moveMotherNature(2);
        board.makeTurn();
        System.out.println(board.getArchipelago(2));
    }
}
