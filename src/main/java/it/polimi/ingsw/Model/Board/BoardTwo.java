package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Places.Cloud;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class BoardTwo extends BoardAbstract {
    public BoardTwo(List<Player> playersParam) throws
            StudentNotFoundException, ExceededMaxStudentsCloudException, ExceededMaxStudentsHallException,
            ExceedingAssistantCardNumberException {

        super(playersParam.stream().filter(x -> ((playersParam.indexOf(x) == 0) || (playersParam.indexOf(x) == 1))).collect(Collectors.toList()));

        this.schools = new ArrayList<>();
        this.playerSchool = new HashMap<>();
        this.clouds = new ArrayList<>();
        //creation of schools, map player -> school, clouds.
        for(int i = 0; i < 2; i++) {
            School s = new School(playersParam.get(i), 7, 8);
            Cloud c = new Cloud(3);

            schools.add(s);
            playerSchool.put(playersParam.get(i), s);

            clouds.add(c);
        }

        super.moveStudentBagToCloud();
        super.moveStudentBagToSchool(7);
    }
}
