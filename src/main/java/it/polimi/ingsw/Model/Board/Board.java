package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Pawns.MotherNature;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Places.Archipelago;
import it.polimi.ingsw.Model.Places.Cloud;
import it.polimi.ingsw.Model.Places.School;
import it.polimi.ingsw.Model.Player;

import java.util.List;
import java.util.Map;

public abstract class Board {
    List<School> schools;
    List<Archipelago> archipelago;
    List<Cloud> clouds;
    MotherNature mn;
    Bag bag;
    Map<Player, School> playerSchool;

    public void moveStudentSchoolToArchipelagos(Player p1, SPColour c, Archipelago a) {

    };

    public void moveStudentCloudToSchool(Player p1, Cloud cloud){

    };

    public void moveStudentHallToDiningRoom(Player p1, SPColour c){

    };

    public void moveStudentBagToCloud(Cloud cloud){

    };

    public void moveStudentBagToSchool(Player p1){

    };

    public int getMotherNaturePosition(){
        return 0;
    };

    public void moveMotherNature(int numPos){

    };

    public void moveProfessor(SPColour c, Player dest){

    };

    public School whereIsProfessor(SPColour c){
        return new School((new Player()), 2, 2);
    };
}
