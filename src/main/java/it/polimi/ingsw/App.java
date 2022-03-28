package it.polimi.ingsw;

import it.polimi.ingsw.Model.Board.Board;
import it.polimi.ingsw.Model.Board.BoardAbstract;
import it.polimi.ingsw.Model.Board.BoardFactory;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Pawns.Coin;
import it.polimi.ingsw.Model.Places.School;
import it.polimi.ingsw.Model.Places.SchoolAdvanced;
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
 * 1. move student (1.1. dining, 1.2. archipelago)
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
        /*
        List<Player> players = new ArrayList<Player>();
        players.add(player1);
        players.add(player2);

        BoardFactory bf = new BoardFactory(players);
        Board board = bf.createBoard(); // -> BoardTwo

        //board.moveStudentBagToSchool(1);
        //Turn of player1. He can't conquer the Archipelago because he has no Professor
        System.out.println(((BoardAbstract)board).getPlayerSchool(player1));
        System.out.println(board.getArchipelago(2));
        board.moveStudentSchoolToArchipelagos(player1, SPColour.PINK, 2);
        System.out.println(board.getArchipelago(2));
        board.moveMotherNature(2);
        board.makeTurn();
        System.out.println(board.getArchipelago(2));
        System.out.println(board.getPlayerSchool(player1));

        //Turn of player 2. He conquer Archipelago 4 with RED Professor
        System.out.println(board.getPlayerSchool(player2));
        board.moveStudentHallToDiningRoom(player2, SPColour.RED);
        System.out.println(board.getPlayerSchool(player2));
        board.moveStudentSchoolToArchipelagos(player1, SPColour.RED, 4);
        System.out.println(board.getArchipelago(4));
        board.moveStudentSchoolToArchipelagos(player2, SPColour.RED, 2);
        System.out.println(board.getArchipelago(4));
        board.moveMotherNature(4);
        board.makeTurn();
        System.out.println(board.getArchipelago(4));
        */


        Coin coin = new Coin();
        List<School> schools = new ArrayList<>();
        SchoolAdvanced schoolAdvanced = new SchoolAdvanced(player1, 2, 2);
        schools.add(schoolAdvanced);
        System.out.println(((SchoolAdvanced)schools.get(0)).getNumCoins());
        ((SchoolAdvanced)schools.get(0)).addCoin(coin);
        System.out.println(((SchoolAdvanced)schools.get(0)).getNumCoins());
    }
}
