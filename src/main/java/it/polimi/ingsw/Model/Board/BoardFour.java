package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Player;

import java.util.List;

public class BoardFour extends BoardAbstract {
    public BoardFour(List<Player> players) {
        super(players);

        //creation of a map player -> school
        for(int i = 0; i < players.size(); i++) {
            //TODO: only one team member has to get all 8 towers, the other one gets only students. We need to consider the fact that
            //TODO: two palyers are in the same team

            School s =  new School(players.get(i), 7, 8);
            schools.add(s);
            playerSchool.put(players.get(i), s);
        }
    }

    //TODO: redefine tryToConquer method (and check if other methods need a redefinition) in order to manage
    // the presence of two players with the same tower colour and the same professor influence
}
