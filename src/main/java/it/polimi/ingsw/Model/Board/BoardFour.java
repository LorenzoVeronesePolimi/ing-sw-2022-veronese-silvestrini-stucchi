package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Pawns.MotherNature;
import it.polimi.ingsw.Model.Pawns.Professor;
import it.polimi.ingsw.Model.Pawns.Tower;
import it.polimi.ingsw.Model.Places.Archipelago;
import it.polimi.ingsw.Model.Places.Cloud;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardFour extends BoardAbstract implements Serializable {
    private static final long serialVersionUID = 1L;
    protected final Map<Player, Player> teammates;

    /**
     * constructor of the BoardFour, adds the concept of teammate (managed as a map)
     * @param players list of players
     * @throws ExceedingAssistantCardNumberException
     * @throws StudentNotFoundException
     * @throws ExceededMaxStudentsCloudException
     * @throws ExceededMaxStudentsHallException
     */
    public BoardFour(List<Player> players) throws
            ExceedingAssistantCardNumberException, StudentNotFoundException, ExceededMaxStudentsCloudException, ExceededMaxStudentsHallException {
        super(players);
        this.schools = new ArrayList<>();
        this.playerSchool = new HashMap<>();
        this.clouds = new ArrayList<>();

        teammates = new HashMap<>();
        List<Player> team1 = new ArrayList<>();
        List<Player> team2 = new ArrayList<>();
        for(Player p : players){
            School s;
            if(team1.size() == 0){
                team1.add(p);
                s = new School(team1.get(0), 7, 8);
                this.schools.add(s);
                this.playerSchool.put(p, s);
            }
            else{
                if(p.getColour() == team1.get(0).getColour()){
                    team1.add(p);
                    s = new School(team1.get(1), 7, 0);
                    this.schools.add(s);
                    this.playerSchool.put(p, s);
                }
                else{
                    team2.add(p);
                    if(team2.size() == 1){
                        s = new School(team2.get(0), 7, 8);
                        this.schools.add(s);
                        this.playerSchool.put(p, s);
                    }
                    else{
                        s = new School(team2.get(1), 7, 0);
                        this.schools.add(s);
                        this.playerSchool.put(p, s);
                    }


                }
            }
        }
        teammates.put(team1.get(0), team1.get(1));
        teammates.put(team1.get(1), team1.get(0));
        teammates.put(team2.get(0), team2.get(1));
        teammates.put(team2.get(1), team2.get(0));
        /*
        teammates.put(players.get(0), players.get(1));
        teammates.put(players.get(1), players.get(0));
        teammates.put(players.get(2), players.get(3));
        teammates.put(players.get(3), players.get(2));*/
        /*
        }
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
        }*/
        for(int i = 0; i < 4; i++){
            this.clouds.add(new Cloud(3));
        }
        super.moveStudentBagToCloud();
        super.moveStudentBagToSchool(7);
    }

    /**
     * constructor of the BoardFour: builds a copy of a given BoardFour
     * @param toCopy
     */
    public BoardFour(BoardFour toCopy){
        super(toCopy);

        this.teammates = toCopy.getTeammates();
    }

    /**
     * modifies the abstract method, taking into account the teammate aspect
     * @param currentPlayer player that should conquer the archipelago
     * @return true if the archipelago can be conquered, false otherwise
     */
    @Override
    public boolean checkIfConquerable(Player currentPlayer) {
        Archipelago currentArchipelago = this.archipelagos.get(whereIsMotherNature());
        //if the owner of the Archipelago is the current Player or mate, he conquers nothing
        if (currentArchipelago.getOwner() == null) { //archipelago never conquered before
            List<Professor> conquerorProfessors = new ArrayList<>();
            conquerorProfessors.addAll(this.playerSchool.get(currentPlayer).getProfessors());
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

    /**
     * modifies the abstract method, taking into account the teammate aspect
     * @param owner player that owned the archipelago
     * @param challenger player that wants to conquer the archipelago
     * @param archipelago archipelago on which I want to compute the winner
     * @return challenger if his team has more influence that the current owner, the current owner otherwise
     */
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

    /**
     * modifies the abstract method, taking into account that the towers of the team are stored just in one of the schools of the team
     * @param conqueror new owner of the archipelago
     * @param toConquer old owner of the archipelago
     * @throws InvalidTowerNumberException
     * @throws AnotherTowerException
     * @throws ExceededMaxTowersException
     * @throws TowerNotFoundException
     */
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

    /**
     * getter of the teammates
     * @return the map of teammates
     */
    public Map<Player, Player> getTeammates() {
        return teammates;
    }
}
