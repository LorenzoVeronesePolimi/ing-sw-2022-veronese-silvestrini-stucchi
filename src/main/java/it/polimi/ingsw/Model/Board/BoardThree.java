package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Places.Cloud;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class BoardThree extends BoardAbstract {
    public BoardThree(List<Player> playersParam) throws
            StudentNotFoundException, ExceededMaxStudentsCloudException, ExceededMaxStudentsHallException,
            ExceedingAssistantCardNumberException {

        super(playersParam.stream().filter(x -> ((playersParam.indexOf(x) == 0) || (playersParam.indexOf(x) == 1) || (playersParam.indexOf(x) == 2))).collect(Collectors.toList()));

        this.schools = new ArrayList<>();
        this.playerSchool = new HashMap<>();
        this.clouds = new ArrayList<>();
        //creation of a map player -> school
        for(int i = 0; i < 3; i++) {
            School s =  new School(players.get(i), 9, 6);
            Cloud c =  new Cloud(4);
            schools.add(s);
            playerSchool.put(players.get(i), s);
            clouds.add(c);
        }

        moveStudentBagToCloud();
        moveStudentBagToSchool(9);
    }
}
