package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Pawns.Professor;
import it.polimi.ingsw.Model.Pawns.Tower;
import it.polimi.ingsw.Model.Places.Archipelago;
import it.polimi.ingsw.Model.Places.Cloud;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardFour extends BoardAbstract {
    protected final Map<Player, Player> teammates;

    public BoardFour(List<Player> players) throws
            ExceedingAssistantCardNumberException, NullContentException, StudentNotFoundException, ExceededMaxStudentsCloudException, ExceededMaxStudentsHallException {
        super(players);
        School s;

        teammates = new HashMap<>();
        teammates.put(players.get(0), players.get(1));
        teammates.put(players.get(1), players.get(0));
        teammates.put(players.get(2), players.get(3));
        teammates.put(players.get(3), players.get(2));

        //creation of a map player -> school
        for (int i = 0; i < players.size(); i++) {
            if (i % 2 == 0) {
                s = new School(players.get(i), 7, 8);
            } else {
                s = new School(players.get(i), 7, 0);

            }
            schools.add(s);
            playerSchool.put(players.get(i), s);

            Cloud cloud = new Cloud(3);
            clouds.add(cloud);
        }
        super.moveStudentBagToCloud();
        super.moveStudentBagToSchool(7);
    }

    @Override
    public boolean checkIfConquerable(Player currentPlayer) {
        Archipelago currentArchipelago = this.archipelagos.get(whereIsMotherNature());
        //if the owner of the Archipelago is the current Player or mate, he conquers nothing
        if (currentArchipelago.getOwner() == currentPlayer || currentArchipelago.getOwner().getColour().equals(currentPlayer.getColour())) {
            return false;
        } else if (currentArchipelago.getOwner() == null) { //archipelago never conquered before
            List<Professor> conquerorProfessors = this.playerSchool.get(currentPlayer).getProfessors();
            conquerorProfessors.addAll(this.playerSchool.get(teammates.get(currentPlayer)).getProfessors());
            for (Professor p : conquerorProfessors) {
                //can't conquer an Island without Students coloured without the Colour of a Professor of mine, even if no one has conquered it before
                return currentArchipelago.howManyStudents().get(p.getColour()) > 0;
            }
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
        List<Tower> conquerorTowers = null;
        if (this.playerSchool.get(conqueror).getNumMaxTowers() != 0)
            conquerorTowers = this.playerSchool.get(conqueror).removeNumTowers(toConquer.getNumIslands());
        else
            conquerorTowers = this.playerSchool.get(teammates.get(conqueror)).removeNumTowers(toConquer.getNumIslands());

        List<Tower> looserTowers = null;
        if (conquerorTowers != null) {
            looserTowers = toConquer.conquerArchipelago(conquerorTowers);
        }
        // == 0 would be the case in which the Archipelago is conquered for the first time => no Towers to reposition
        // otherwise I replace the looser Towers
        if (looserTowers != null && looserTowers.size() != 0) {
            Player looser = looserTowers.get(0).getPlayer();
            this.playerSchool.get(looser).addNumTower(looserTowers);
        }

    }
}
