package it.polimi.ingsw.Model.Pawns.Board;

import it.polimi.ingsw.Model.Places.Cloud;
import it.polimi.ingsw.Model.Places.School;
import it.polimi.ingsw.Model.Player;

import java.util.List;

public class BoardTwo extends Board{

    public BoardTwo(List<Player> players) {
        super();

        //creation of schools, map player -> school, clouds.
        for(int i = 0; i < players.size(); i++) {
            School s =  new School(players.get(i), 7, 8);
            Cloud c =  new Cloud(3);

            schools.add(s);
            playerSchool.put(players.get(i), s);
            clouds.add(c);
        }





    }

    @Override
    public void moveStudentBagToCloud() {

    }

    @Override
    public void moveStudentBagToSchool() {

    }
}
