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

public interface Board {
    List<School> schools=null;
    List<Archipelago> archipelago=null;
    List<Cloud> clouds=null;
    MotherNature mn=null;
    Bag bag=null;

    Map<Player, School> playerSchool=null;
    void Board(List<Player> players);
    void moveStudentSchoolToArchipelagos(Player p1, SPColour c, Archipelago a);
    void moveStudentCloudToSchool(Player p1, Cloud cloud);
    void moveStudentHallToDiningRoom(Player p1, SPColour c);
    void moveStudentBagToCloud(Cloud cloud);
    void moveStudentBagToSchool(Player p1);
    int getMotherNaturePosition();
    void moveMotherNature(int numPos);
    void moveProfessor(SPColour c, Player dest);
    School whereIsProfessor(SPColour c);
}
