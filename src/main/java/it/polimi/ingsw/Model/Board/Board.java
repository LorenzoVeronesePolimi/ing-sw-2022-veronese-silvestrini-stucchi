package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Pawns.MotherNature;
import it.polimi.ingsw.Model.Places.Archipelago;
import it.polimi.ingsw.Model.Places.Cloud;
import it.polimi.ingsw.Model.Places.School;
import it.polimi.ingsw.Model.Player;

public interface Board {
    private School schools[];
    private Archipelago archipelago[];
    private Cloud clouds[];
    private MotherNature mn;
    private Bag bag;

    public void Board(int numberPlayers){

    }
    public int getMotherNaturePosition(){

    }
    public void moveStudentSchoolToCloud(Player p1, SPColour c, Cloud cl){

    }
    public void moveStudentSchoolToArchipelagus(Player p1, SPColour c, Cloud cl, Archipelago a){

    }
    public void moveMotherNature(int numPos){

    }
    public void moveProfessor(SPColour c, Player dest){

    }
    public School whereIsProfessor(SPColour c){

    }
}
