package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Places.School;
import it.polimi.ingsw.Model.Player;

import java.util.List;

public class BoardThree extends Board {

    public BoardThree(List<Player> players) {
        super(players);
        //creation of a map player -> school
        for(int i = 0; i < players.size(); i++) {
            School s =  new School(players.get(i), 9, 6);
            schools.add(s);
            playerSchool.put(players.get(i), s);
        }

        moveStudentBagToCloud(4);
        moveStudentBagToSchool(9);
    }
}
