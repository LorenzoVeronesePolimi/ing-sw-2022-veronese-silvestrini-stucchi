package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Pawns.MotherNature;
import it.polimi.ingsw.Model.Pawns.Professor;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Pawns.Tower;
import it.polimi.ingsw.Model.Places.Archipelago;
import it.polimi.ingsw.Model.Places.Cloud;
import it.polimi.ingsw.Model.Places.School;
import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Board {
    List<School> schools;   //list of all school in the game (one for each player)
    List<Player> players;   //list of all players in the game (in order)
    int currentPlayer; //index of the current Player in the list players
    Map<Player, School> playerSchool;   //map of players and their relative school
    List<Archipelago> archipelagos;     //list of all the archipelagos in the game (in order)
    List<Cloud> clouds;     //list of all clouds in the game
    MotherNature mn;    //reference to MotherNature(Singleton)
    Bag bag;   //reference to the Bag


    public Board(List<Player> players) {
        this.archipelagos = new ArrayList<Archipelago>();

        for(int i = 0; i < 12; i++) {
            Archipelago a = new Archipelago();

            archipelagos.add(a);
        }

        this.players = new ArrayList<Player>();
        this.players.addAll(players);
        mn = MotherNature.instance();
        bag = Bag.instance();

        placeMotherNatureInitialBoard();
        placeStudentInitialBoard();
    }

    public void moveStudentSchoolToArchipelagos(Player player, SPColour colour, int archipelagoIndex) {
        //school related to the player that made the move
        School currentSchool = playerSchool.get(player);

        if(currentSchool != null) {
            try {
                Student toBeMoved = currentSchool.removeStudentHall(colour);
                archipelagos.get(archipelagoIndex).addStudent(toBeMoved);
            } catch (StudentNotFoundException e) {
                e.printStackTrace();
            }
        }
    };

    public void moveStudentCloudToSchool(Player player, int cloudIndex){
        //remove all the students from one particular cloud
        List<Student> toBeMoved = clouds.get(cloudIndex).empty();

        //school related to the player that made the move
        School currentSchool = playerSchool.get(player);

        if(!toBeMoved.isEmpty()) {
            for(Student s: toBeMoved) {
                try {
                    currentSchool.addStudentHall(s);
                } catch (ExceededMaxStudentsHallException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public void moveStudentHallToDiningRoom(Player player, SPColour colour){
        //school related to the player that made the move
        School currentSchool = playerSchool.get(player);

        try {
            Student toBeMoved = currentSchool.removeStudentHall(colour);
            currentSchool.addStudentDiningRoom(toBeMoved);
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        } catch (ExceededMaxStudentsDiningRoomException e) {
            e.printStackTrace();
        }
    };

    private void placeStudentInitialBoard() {
        //get 10 initial students to be placed on the archipelagos (one each, except mn position and the opposite)
        List<Student> initialStudents = bag.getInitialStudents();

        for(int i = 1; i < this.archipelagos.size(); i++) {
            if(i < 6) {
                this.archipelagos.get(i).addStudent(initialStudents.get(i-1));
            } else if(i > 6) {
                this.archipelagos.get(i).addStudent(initialStudents.get(i-2));
            }
        }

    }

    protected void moveStudentBagToCloud(int numStudents) {
        for(Cloud c: clouds) {
            List<Student> toBePlaced = bag.extractStudents(numStudents);
            try {
                c.fill(toBePlaced);
            } catch (ExceededMaxStudentsCloudException e) {
                e.printStackTrace();
            }
        }
    }

    public void moveStudentBagToSchool(int numStudents) {
        for(School s: this.schools) {
            List<Student> toBePlaced = bag.extractStudents(numStudents);

            for(Student student: toBePlaced) {
                try {
                    s.addStudentHall(student);
                } catch (ExceededMaxStudentsHallException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Mother Nature is put in the first archipelago
    private void placeMotherNatureInitialBoard() {
        mn.putInPosition(archipelagos.get(0));
    }

    public void moveMotherNature(int archipelagoIndex){
        //TODO: check if the move is permitted (by the number of moves set in the AssistantCard)
        //TODO: this check can be done before showing possible moves for MN

        mn.putInPosition(archipelagos.get(archipelagoIndex));
    };

    public int getMotherNaturePosition(){
        return archipelagos.indexOf(mn.getCurrentPosition());
    };

    public void moveProfessor(Player destinationPlayer, SPColour colour){
        //school related to the player that gets the professor
        School receiverSchool = playerSchool.get(destinationPlayer);
        School senderSchool;
        Professor toBeMoved = null;

        if(isProfessorInSchool(colour)) {
            senderSchool = whereIsProfessor(colour);

            try {
                toBeMoved = senderSchool.removeProfessor(colour);
            } catch (ProfessorNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                toBeMoved = bag.takeProfessor(colour);
            } catch (NoProfessorBagException e) {
                e.printStackTrace();
            }
        }

        receiverSchool.addProfessor(toBeMoved);
    };

    //Check if professor is in onw of the schools or in the bag
    public boolean isProfessorInSchool(SPColour colour) {
        for(School s: schools) {
            for(Professor p: s.getProfessors()) {
                if(p.getColour().equals(colour)) {
                    return true;
                }
            }
        }

        return false;
    }

    //che the school of the professor
    public School whereIsProfessor(SPColour colour){
        for(School s: schools) {
            for(Professor p: s.getProfessors()) {
                if(p.getColour().equals(colour)) {
                    return s;
                }
            }
        }

        //TODO: this case is an error state and should be addressed (we don't want to arrive here)
        return null;
    };

    public void conquerArchipelago(Player player, int archipelagoIndex) {
        School currentSchool = playerSchool.get(player);

        //archipelagos.get(idArchi)

        Tower toBeMoved = currentSchool.removeTower();
        //archipelagos.get(idArchi).addTower(toBeMoved);

    }


    // Returns the Archipelago's index where MotherNature is positioned
    private int whereIsMotherNature(){
        Archipelago current = this.mn.getCurrentPosition();

        int index = 0;
        for(Archipelago a : this.archipelagos){
            if(current == a){
                return index;
            }
            index++;
        }

        //TODO: exception
        return -1;
    }



    // MotherNature has been positioned: let's check if the Archipelago where she is, has to be conquered
    public void checkIfConquerable(Player currentPlayer){
        Archipelago currentArchipelago = this.archipelagos.get(this.whereIsMotherNature());

        //if(currentPlayer == currentArchipelago.get)
    }

    public School getPlayerSchool(Player player) {
        return playerSchool.get(player);
    }


    // This probably will be in the Controller
    public void makeTurn(){
        //TODO: the current Player moves students

        //MOTHER NATURE HAS BEEN MOVED
        //Is the Archipelago conquerable?
        int currPosMotherNature = this.whereIsMotherNature();
        boolean archipelagoConquerable = checkIfConquerable(currPosMotherNature);
        if(archipelagoConquerable){
            this.conquerArchipelago(this.players.get(this.currentPlayer), this.archipelagos.get(currPosMotherNature));

            //let's merge Archipelagos
            this.mergeArchipelagos();
        }
        else { //the Archipelago remains to the owner
        }

        //THE CURRENT PLAYER CHOOSES THE CLOUD TO EMPTY

        //SET CURRENT PLAYER FOR THE NEXT TURN
        if(this.currentPlayer < this.players.size() - 1){
            this.currentPlayer++;
        }
        else{
            this.currentPlayer = 0;
        }

    }

    // (*) The controller will call a specific method (ex. checkIfConquerableAdvance) only if we have BoardAdvanced
    // (*) and we have ForbidIsland and/or TowerNoValue as CharacterCards
    // true if the current Player (who moved MotherNature) will conquer the Archipelago, false otherwise
    private boolean checkIfConquerable(int currPosMotherNature){
        Archipelago currentArchipelago = this.archipelagos.get(currPosMotherNature);
        //if the owner of the Archipelago is the current Player, he conquers nothing
        if(currentArchipelago.getOwner() == this.players.get(currentPlayer)){
            return false;
        }
        else if(currentArchipelago.getOwner() == null){ //archipelago never conquered before
            return true;
        }
        else if(currentArchipelago.getForbidFlag()){ //TODO: this is an advanced function => see comment above(*)
            currentArchipelago.setForbidFlag(false);
        }
        else if(currentArchipelago.getTowerNoValueFlag()){ //TODO: this is an advanced function => see comment above(*)
            currentArchipelago.setTowerNoValueFlag(false);
        }
        //the current Player is not the owner: can he conquer the Archipelago?
        else{
            //who has higher influence according to rules?
            Player winner = this.computeWinner(currentArchipelago.getOwner(), this.players.get(currentPlayer), currentArchipelago);
            if(winner == this.players.get(currentPlayer)){
                return true;
            }
            else{
                return false;
            }
        }
        return false;
    }


    // Computes which of two players has most influence on a Archipelago
    private Player computeWinner(Player owner, Player challenger, Archipelago archipelago){
        int ownerInfluence = this.computeInfluenceOfPlayer(owner, archipelago);
        int challengerInfluence = this.computeInfluenceOfPlayer(challenger, archipelago);

        if(ownerInfluence > challengerInfluence){
            return owner;
        }
        else{
            return challenger;
        }
    }


    // Returns the influence of a Player on a Archipelago
    private int computeInfluenceOfPlayer(Player player, Archipelago archipelago){
        int influence = 0;

        //if the player owns the Archipelago, the number of Towers (= number of Islands) counts
        if(player == archipelago.getOwner()){
            influence += archipelago.getNumIslands();
        }

        Map<SPColour, Integer> archipelagoStudentsData = archipelago.howManyStudents(); //data about Students on the Archipelago
        List<Professor> playerProfessors = this.playerSchool.get(player).getProfessors(); //Professors of the player
        for(Professor p : playerProfessors){
            influence += archipelagoStudentsData.get(p.getColour());
        }

        return influence;
    }


    // the Player conquers the Archipelago putting his own Towers and removing the previous ones (if present)
    private void conquerArchipelago(Player conqueror, Archipelago toConquer){
        // conqueror's Towers to put on the Archipelago
        List<Tower> conquerorTowers = this.playerSchool.get(conqueror).removeNumTowers(toConquer.getNumIslands());

        try{
            List<Tower> looserTowers = toConquer.conquerArchipelago(conquerorTowers);
            // == 0 would be the case in which the Archipelago is conquered for the first time => no Towers to reposition
            // otherwise I replace the looser Towers
            if(looserTowers.size() != 0){
                Player looser = looserTowers.get(0).getPlayer();
                this.playerSchool.get(looser).addNumTower(looserTowers);
            }
        } catch(InvalidTowerNumberException ex){ex.printStackTrace();}
    }


    // This merges as much adjacent Archipelagos as possible removing the old one from the this.archipelagos
    private void mergeArchipelagos(){
        if(this.isThereRightMerge()){
            Archipelago currentArchipelago = this.archipelagos.get(this.whereIsMotherNature());
            Archipelago rightArchipelago = this.archipelagos.get((this.whereIsMotherNature() - 1) % 12);
            this.archipelagos.remove(rightArchipelago);
            try{currentArchipelago.mergeArchipelagos((rightArchipelago));}
            catch(MergeDifferentOwnersException ex){ex.printStackTrace();}
            this.mergeArchipelagos();
        }
        else if(this.isThereLeftMerge()){
            Archipelago currentArchipelago = this.archipelagos.get(this.whereIsMotherNature());
            Archipelago leftArchipelago = this.archipelagos.get((this.whereIsMotherNature() + 1) % 12);
            this.archipelagos.remove(leftArchipelago);
            try{currentArchipelago.mergeArchipelagos((leftArchipelago));}
            catch(MergeDifferentOwnersException ex){ex.printStackTrace();}
            this.mergeArchipelagos();
        }
    }


    // Check if you can merge the current Island (on which there is MotherNature) with the previous one
    private boolean isThereRightMerge(){
        return this.archipelagos.get(this.whereIsMotherNature()).getOwner() == this.archipelagos.get((this.whereIsMotherNature() - 1) % 12).getOwner();
    }

    // Check if you can merge the current Island (on which there is MotherNature) with the next one
    private boolean isThereLeftMerge(){
        return this.archipelagos.get(this.whereIsMotherNature()).getOwner() == this.archipelagos.get((this.whereIsMotherNature() + 1) % 12).getOwner();
    }
}
