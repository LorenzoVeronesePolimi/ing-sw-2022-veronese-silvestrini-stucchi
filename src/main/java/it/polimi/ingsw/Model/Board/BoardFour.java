package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Pawns.Professor;
import it.polimi.ingsw.Model.Pawns.Tower;
import it.polimi.ingsw.Model.Places.Archipelago;
import it.polimi.ingsw.Model.Places.Cloud;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardFour extends BoardAbstract {
    protected final Map<Player, Player> teammates;

    public BoardFour(List<Player> players) throws
            ExceedingAssistantCardNumberException, NullContentException, StudentNotFoundException, ExceededMaxStudentsCloudException, ExceededMaxStudentsHallException {
        super(players);
        this.schools = new ArrayList<>();
        this.playerSchool = new HashMap<>();
        this.clouds = new ArrayList<>();

        teammates = new HashMap<>();
        teammates.put(players.get(0), players.get(1));
        teammates.put(players.get(1), players.get(0));
        teammates.put(players.get(2), players.get(3));
        teammates.put(players.get(3), players.get(2));

        //creation of a map player -> school
        for (int i = 0; i < players.size(); i++) {
            School s;
            Cloud cloud = new Cloud(3);

            if (i % 2 == 0) {
                s = new School(players.get(i), 7, 8);
            } else {
                s = new School(players.get(i), 7, 0);

            }

            this.schools.add(s);
            this.playerSchool.put(players.get(i), s);
            this.clouds.add(cloud);
        }
        super.moveStudentBagToCloud();
        super.moveStudentBagToSchool(7);
    }

    @Override
    public boolean checkIfConquerable(Player currentPlayer) {
        Archipelago currentArchipelago = this.archipelagos.get(whereIsMotherNature());
        //if the owner of the Archipelago is the current Player or mate, he conquers nothing
        if (currentArchipelago.getOwner() == null) { //archipelago never conquered before
            List<Professor> conquerorProfessors = this.playerSchool.get(currentPlayer).getProfessors();
            conquerorProfessors.addAll(this.playerSchool.get(teammates.get(currentPlayer)).getProfessors());
            boolean conquerable = false;

            for (Professor p : conquerorProfessors) {
                //can't conquer an Island without Students coloured without the Colour of a Professor of mine, even if no one has conquered it before
                if(!conquerable)
                    conquerable = currentArchipelago.howManyStudents().get(p.getColour()) > 0;
            }
            return conquerable;

        } else if (currentArchipelago.getOwner() == currentPlayer || currentArchipelago.getOwner().getColour().equals(currentPlayer.getColour())) {
            return false;
        }
        //the current Player is not the owner: can he conquer the Archipelago?
        else {
            //who has higher influence according to rules?
            Player winner = this.computeWinner(currentArchipelago.getOwner(), currentPlayer, currentArchipelago);
            return winner == currentPlayer || winner == teammates.get(currentPlayer);
        }
    }

    @Override
    public Player computeWinner(Player owner, Player challenger, Archipelago archipelago) {
        int ownerTeamInfluence = this.computeInfluenceOfPlayer(owner, archipelago) + this.computeInfluenceOfPlayer(teammates.get(owner), archipelago);
        int challengerTeamInfluence = this.computeInfluenceOfPlayer(challenger, archipelago) + this.computeInfluenceOfPlayer(teammates.get(challenger), archipelago);

        if (ownerTeamInfluence > challengerTeamInfluence) {
            return owner;
        } else {
            return challenger;
        }
    }

    // the Player conquers the Archipelago putting his own Towers and removing the previous ones (if present)
    @Override
    protected void conquerArchipelago(Player conqueror, Archipelago toConquer) throws InvalidTowerNumberException, AnotherTowerException, ExceededMaxTowersException, TowerNotFoundException {
        // conqueror's Towers to put on the Archipelago
        List<Tower> conquerorTowers;
        if (this.playerSchool.get(conqueror).getNumMaxTowers() != 0)
            conquerorTowers = this.playerSchool.get(conqueror).removeNumTowers(toConquer.getNumIslands());
        else
            conquerorTowers = this.playerSchool.get(teammates.get(conqueror)).removeNumTowers(toConquer.getNumIslands());

        moveTower(conquerorTowers, toConquer);
    }
}
