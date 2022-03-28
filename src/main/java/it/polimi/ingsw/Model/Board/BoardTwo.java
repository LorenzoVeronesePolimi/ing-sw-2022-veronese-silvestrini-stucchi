package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Places.Cloud;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BoardTwo extends BoardAbstract {
    public BoardTwo(List<Player> playersParam) {
        super(playersParam);

        this.schools = new ArrayList<School>();
        this.playerSchool = new HashMap<Player, School>();
        this.clouds = new ArrayList<Cloud>();
        //creation of schools, map player -> school, clouds.
        for(int i = 0; i < playersParam.size(); i++) {
            School s =  new School(playersParam.get(i), 7, 8);
            Cloud c =  new Cloud(3);

            schools.add(s);
            playerSchool.put(playersParam.get(i), s);

            clouds.add(c);
        }

        super.moveStudentBagToCloud();
        super.moveStudentBagToSchool(7);
    }
}
