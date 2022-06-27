package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Cards.AssistantCard;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Pawns.MotherNature;
import it.polimi.ingsw.Model.Pawns.Professor;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Pawns.Tower;
import it.polimi.ingsw.Model.Places.Archipelago;
import it.polimi.ingsw.Model.Places.Cloud;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Places.School.SchoolAdvanced;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Observer.Observable;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BoardAbstract extends Observable implements Board, Serializable {
    private static final long serialVersionUID = 1L;
    protected List<School> schools;   //list of all school in the game (one for each player)
    protected List<Player> players;   //list of all players in the game (in order)
    protected Map<Player, School> playerSchool;   //map of players and their relative school
    protected List<Archipelago> archipelagos;     //list of all the archipelagos in the game (in order)
    protected List<Cloud> clouds;     //list of all clouds in the game
    protected MotherNature mn;    //reference to MotherNature(Singleton)
    protected Bag bag;   //reference to the Bag
    private final static int playerHandLength = 10;


    //--------------------------------------------------INITIALIZATION OF BOARD

    /**
     * Constructor of the board: it builds 12 archipelagos (1 island each), one mother nature, one bag
     * (that builds all the students and professors of the game)
     * @param players list of players of the game
     * @throws ExceedingAssistantCardNumberException
     */
    public BoardAbstract(List<Player> players) throws ExceedingAssistantCardNumberException {
        this.archipelagos = new ArrayList<>();

        for(int i = 0; i < 12; i++) {
            Archipelago a = new Archipelago();

            archipelagos.add(a);
        }

        this.players = new ArrayList<>();
        this.players.addAll(players);
        mn = new MotherNature();
        bag = new Bag();


        this.initializePlayersHands();
        this.placeMotherNatureInitialBoard();
        this.placeStudentInitialBoard();
        //notifyPlayers();
    }

    /**
     * Constructor that builds a board, given another board (creates a copy of the board: useful in handle gameEndedArchipelagos
     * in Controller)
     * @param toCopy board that must be copied
     */
    public BoardAbstract(BoardAbstract toCopy){ //added for gameEndedArchipelagos in Controller
        this.archipelagos = new ArrayList<>();
        int posOfMn = 0;
        for(int i = 0; i < toCopy.getNumArchipelagos(); i++){
            this.archipelagos.add(new Archipelago(toCopy.getArchipelago(i)));
            if(toCopy.getArchipelago(i) == toCopy.getMn().getCurrentPosition()){
                posOfMn = i;
            }
        }

        this.players = new ArrayList<>();
        this.players.addAll(toCopy.getPlayers());

        this.mn = new MotherNature();
        this.mn.putInPosition(this.archipelagos.get(posOfMn));

        this.bag = new Bag();

        this.schools = new ArrayList<>();
        this.playerSchool = new HashMap<>();
        for(Player p : this.players){
            School toAdd = toCopy.getPlayerSchool(p).makeCopy();
            this.schools.add(toAdd);
            this.playerSchool.put(p, toAdd);
        }

        this.clouds = new ArrayList<>(); //no need to copy them for conquer
        this.clouds = toCopy.getClouds();
    }

    /**
     *method that build all the 10 assistant cards available for each player
     * @throws ExceedingAssistantCardNumberException if you are trying to add more than 10 assistant cards to the same player
     */
    private void initializePlayersHands() throws ExceedingAssistantCardNumberException {
        // Create all needed AssistantCards
        List<AssistantCard> cardsCreated = new ArrayList<>();
        int[] cardsMotherNatureMoves= {1, 1, 2, 2, 3, 3, 4, 4, 5, 5};
        for(int i = 0; i < playerHandLength; i++){
            cardsCreated.add(new AssistantCard(cardsMotherNatureMoves[i], i+1));
        }

        // give them to Players
        for(Player p : this.players){
            for(AssistantCard c : cardsCreated){
                p.addAssistantCard(new AssistantCard(c.getMotherNatureMovement(), c.getTurnPriority()));
            }
        }
    }

    /**
     * method that, during the initialization of the board, for each archipelago extracts one random student from the bag and places
     * it on that archipelago.
     */
    private void placeStudentInitialBoard(){
        //get 10 initial students to be placed on the archipelagos (one each, except mn position and the opposite)
        List<Student> initialStudents;
        initialStudents = bag.getInitialStudents();

        for(int i = 1; i < this.archipelagos.size(); i++) {
            if(i < 6) {
                this.archipelagos.get(i).addStudent(initialStudents.get(i-1));
            } else if(i > 6) {
                this.archipelagos.get(i).addStudent(initialStudents.get(i-2));
            }
        }
    }

    /**
     * method that, during the initialization of the board, places mother nature in the first archipelago
     */

    public void placeMotherNatureInitialBoard() {
        mn.putInPosition(archipelagos.get(0));
    }




    //--------------------------------------------------GETTER AND SETTER

    /**
     * getter of the bag
     * @return bag
     */
    public Bag getBag() {
        return bag;
    }

    /**
     * getter of players
     * @return players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * getter of mother nature
     * @return mother nature
     */
    public MotherNature getMn() {
        return mn;
    }

    /**
     * getter of clouds
     * @return clouds
     */
    public List<Cloud> getClouds() {
        return new ArrayList<>(this.clouds);
    }

    /**
     * getter of archipelago of given index
     * @param archipelagoIndex index of the archipelago that is wanted to be returned
     * @return archipelago of archipelagoIndex index
     */
    public Archipelago getArchipelago(int archipelagoIndex){
        return this.archipelagos.get(archipelagoIndex);
    }

    /**
     * getter of number of archipelagos (added for isGameEnded in Controller)
     * @return number of archipelagos
     */
    public int getNumArchipelagos(){return this.archipelagos.size();}

    /**
     * getter of the number of the student in the bag at the moment (added for isGameEnded in Controller)
     * @return number of the student in the bag at the moment
     */
    public int getNumStudentsInBag(){return this.bag.getNumStudents();}

    /**
     * getter of data about the students on a given archipelago
     * @param archipelagoIndex index of the archipelago of which information is required
     * @return map of student colours and quantity of students of that colour on the archipelago
     */
    public Map<SPColour, Integer> getNumStudentsInArchipelago(int archipelagoIndex) {
        return this.archipelagos.get(archipelagoIndex).howManyStudents();
    }

    /**
     * getter of the Archipelago's index where MotherNature is positioned
     * @return Archipelago's index where MotherNature is positioned
     */
    public int whereIsMotherNature(){
        return archipelagos.indexOf(mn.getCurrentPosition());
    }

    /**
     * getter of player's school
     * @param player owner of the school that I want to get
     * @return player's school
     */
    public School getPlayerSchool(Player player) {
        return playerSchool.get(player);
    }

    /**
     * method that says if there is a student of a given colour into a given student hall
     * @param player is the owner of the school where the student of c colour is supposed to be
     * @param c colour of student that is being looked for
     * @return true if exists a student with colour c in player's hall, false otherwise
     */
    public boolean isStudentInSchoolHall(Player player, SPColour c){
        for(Student s : this.playerSchool.get(player).getStudentsHall()){
            if(s.getColour() == c){
                return true;
            }
        }
        return false;
    }




    //--------------------------------------------------PAWNS MOVEMENTS

    /**
     * method that moves mother nature of a given number or archipelagos
     * @param mnMoves number of archipelagos that I want mother nature to move through
     */
    public void moveMotherNature(int mnMoves){
        mn.putInPosition(archipelagos.get((whereIsMotherNature() + mnMoves) % archipelagos.size()));
        //notifyPlayers();
    }

    /**
     * method that moves mother nature to a specific archipelago (with given index)
     * @param index index of archipelago where mother nature is wanted to be moved to
     */
    public void moveMotherNatureInArchipelagoIndex(int index){
        mn.putInPosition(archipelagos.get(index));
        notifyPlayers(); //TODO: check if ok
    }

    /**
     * method that moves a student of a given colour from a given player's hall to a given archipelago
     * @param player owner of the school from which I want to move the student
     * @param colour of the student that I want to move
     * @param archipelagoIndex index of the archipelago where I want to move the student to
     * @throws StudentNotFoundException if there is no such colour student in player's hall
     */
    public void moveStudentSchoolToArchipelagos(Player player, SPColour colour, int archipelagoIndex) throws StudentNotFoundException {
        //school related to the player that made the move
        School currentSchool = playerSchool.get(player);
        if(currentSchool != null) {
            Student toBeMoved = currentSchool.removeStudentHall(colour);
            archipelagos.get(archipelagoIndex).addStudent(toBeMoved);
        }

        if(!(this.schools.get(0) instanceof SchoolAdvanced)) {
            notifyPlayers();
        }

    }

    /**
     * method that moves the students from a given cloud to a given player's hall
     * @param player owner of the school where I want to move students
     * @param cloudIndex index of the cloud that I want to empty
     * @throws ExceededMaxStudentsHallException if you are trying to add to the hall more students that it could hold
     */
    public void moveStudentCloudToSchool(Player player, int cloudIndex) throws ExceededMaxStudentsHallException {
        //remove all the students from one particular cloud
        List<Student> toBeMoved = clouds.get(cloudIndex).empty();

        //school related to the player that made the move
        School currentSchool = playerSchool.get(player);

        if(!toBeMoved.isEmpty()) {
            for(Student s: toBeMoved) {
                currentSchool.addStudentHall(s);
            }
        }

        if(!(this.schools.get(0) instanceof SchoolAdvanced))
            notifyPlayers();
    }

    /**
     * method that moves a student from a player's hall to the same player's dining room
     * @param player owner of the school from which I want to move the student
     * @param colour of the student that I want to move to the dining room
     * @throws StudentNotFoundException if there is not any student of that colour in the dining room
     * @throws ExceededMaxStudentsDiningRoomException if the movement is not possible because there are already 10 students of that colour
     * in the dining room
     * @throws ProfessorNotFoundException if the professor of that colour is not in anu school
     * @throws NoProfessorBagException if the professor of that colour is not in the bag
     */
    public void moveStudentHallToDiningRoom(Player player, SPColour colour) throws
            StudentNotFoundException, ExceededMaxStudentsDiningRoomException, ProfessorNotFoundException, NoProfessorBagException {

        //school related to the player that made the move
        School currentSchool = playerSchool.get(player);

        Student toBeMoved = currentSchool.removeStudentHall(colour);
        currentSchool.addStudentDiningRoom(toBeMoved);
        this.conquerProfessor(player, colour); //has the movement of the Student caused the conquering of the Professor?
        notifyPlayers();
    }

    /**
     * method that moves a student from the gab to a cloud
     * @throws ExceededMaxStudentsCloudException if the movement fails because the cloud if already full
     * @throws StudentNotFoundException if the movement fail because the bag is empty
     */
    public void moveStudentBagToCloud() throws ExceededMaxStudentsCloudException, StudentNotFoundException {
        int numStudents = this.clouds.get(0).getNumMaxStudents();
        for(Cloud c: clouds) {
            List<Student> toBePlaced;
            toBePlaced = bag.extractStudents(numStudents);
            c.fill(toBePlaced);
        }

        if(!(this.schools.get(0) instanceof SchoolAdvanced))
            notifyPlayers();
    }

    /**
     * method that moves a given number of students from the gab to a school
     * @param numStudents number of student that I want to move
     * @throws StudentNotFoundException if there are not enough students in the bag
     * @throws ExceededMaxStudentsHallException if during the movement the hall exceeds the maximum number of students
     */
    public void moveStudentBagToSchool(int numStudents) throws StudentNotFoundException, ExceededMaxStudentsHallException {
        for(School s: this.schools) {
            List<Student> toBePlaced;
            toBePlaced = bag.extractStudents(numStudents);

            for(Student student: toBePlaced) {
                s.addStudentHall(student);
            }
        }
    }

    /**
     * method that moves the professor of a given colour from its current position to a given school
     * @param destinationPlayer owner of the school that I want to move mother the professor to
     * @param colour of the professor I want to move
     * @throws NoProfessorBagException if that professor is not in the bag
     * @throws ProfessorNotFoundException if that professor is not in any school
     */
    public void moveProfessor(Player destinationPlayer, SPColour colour) throws NoProfessorBagException, ProfessorNotFoundException {
        //school related to the player that gets the professor
        School receiverSchool = playerSchool.get(destinationPlayer);
        School senderSchool;
        Professor toBeMoved;

        if(isProfessorInSchool(colour)) {
            senderSchool = whereIsProfessor(colour);

            toBeMoved = senderSchool.removeProfessor(colour);
        } else {
            toBeMoved = bag.takeProfessor(colour);
        }

        receiverSchool.addProfessor(toBeMoved);

        if(!(this.schools.get(0) instanceof SchoolAdvanced))
            notifyPlayers();
    }




    //--------------------------------------------------PROFESSORS
    /**
     * method that checks if professor is in any of the schools or in the bag
     */
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

    /**
     * method that finds the School where the Professor is in
     */
    public School whereIsProfessor(SPColour colour){
        for(School s: schools) {
            for(Professor p: s.getProfessors()) {
                if(p.getColour().equals(colour)) {
                    return s;
                }
            }
        }

        // This happens when the Professor I'm looking for is in the Bag
        return null;
    }

    /**
     * method that moves the professor of a given colour from its current school to the school of the challenger if he has more students
     * of that colour in his dining room
     * @param currentPlayer is the current player
     * @param colour of the professor to conquer
     * @throws NoProfessorBagException if that professor is not in the bag
     * @throws ProfessorNotFoundException if that professor is not in any school
     */
    public void conquerProfessor(Player currentPlayer, SPColour colour) throws NoProfessorBagException, ProfessorNotFoundException {
        School currentSchool = this.whereIsProfessor(colour);
        if(currentSchool == null){ // it's in the bag
            this.playerSchool.get(currentPlayer).addProfessor(this.bag.takeProfessor(colour));
            return;
        }
        School challengerSchool = this.playerSchool.get(currentPlayer);
        int numStudentsCurrentSchool;
        int numStudentsChallenger;

        numStudentsCurrentSchool = currentSchool.getNumStudentColour(colour);
        numStudentsChallenger = challengerSchool.getNumStudentColour(colour);


        if(numStudentsChallenger > numStudentsCurrentSchool){ // can take the Professor
            Professor removed;

            removed = currentSchool.removeProfessor(colour);

            challengerSchool.addProfessor(removed);
        }
    }



    //--------------------------------------------------CONQUERING ISLANDS

    /**
     * method that tries to conquer the archipelago for a given player
     * @param currentPlayer player that is trying to conquer the archipelago
     * @throws InvalidTowerNumberException
     * @throws AnotherTowerException
     * @throws ExceededMaxTowersException
     * @throws TowerNotFoundException
     */
    public void tryToConquer(Player currentPlayer) throws
            InvalidTowerNumberException, AnotherTowerException, ExceededMaxTowersException, TowerNotFoundException {

        int currPosMotherNature = this.whereIsMotherNature();
        boolean archipelagoConquerable = this.checkIfConquerable(currentPlayer);
        if(archipelagoConquerable){
            this.conquerArchipelago(currentPlayer, this.archipelagos.get(currPosMotherNature));

            //let's merge Archipelagos
            this.mergeArchipelagos();
        }

        notifyPlayers();
    }


    /**
     * method that verifies if a player can conquer the archipelago on which is placed mother nature
     * @param currentPlayer player that should conquer the archipelago
     * @return true if the current Player (who moved MotherNature) will conquer the Archipelago, false otherwise
     */
    public boolean checkIfConquerable(Player currentPlayer){
        int currPosMotherNature = this.whereIsMotherNature();
        Archipelago currentArchipelago = this.archipelagos.get(currPosMotherNature);

        //if the owner of the Archipelago is the current Player, he conquers nothing
        if(currentArchipelago.getOwner() == currentPlayer){
            return false;
        }
        else if(currentArchipelago.getOwner() == null){ //archipelago never conquered before
            List<Professor> conquerorProfessors = this.playerSchool.get(currentPlayer).getProfessors();
            boolean conquerable = false;
            for(Professor p : conquerorProfessors){
                //can't conquer an Island without Students coloured without the Colour of a Professor of mine, even if no one has conquered it before
                if(!conquerable)
                    conquerable = currentArchipelago.howManyStudents().get(p.getColour()) > 0;
            }
            return conquerable;
        }
        //the current Player is not the owner: can he conquer the Archipelago?
        else{
            //who has higher influence according to rules?
            Player winner = this.computeWinner(currentArchipelago.getOwner(), currentPlayer, currentArchipelago);
            return winner == currentPlayer;
        }
    }



    /**
     * method that computes which of two players has most influence on an Archipelago
     * @param owner player that owned the archipelago
     * @param challenger player that wants to conquer the archipelago
     * @param archipelago archipelago on which I want to compute the winner
     * @return the player who has most influence
     */
    public Player computeWinner(Player owner, Player challenger, Archipelago archipelago){
        int ownerInfluence = this.computeInfluenceOfPlayer(owner, archipelago);
        int challengerInfluence = this.computeInfluenceOfPlayer(challenger, archipelago);

        if(ownerInfluence > challengerInfluence){
            return owner;
        }
        else{
            return challenger;
        }
    }


    /**
     * Returns the influence of a Player on an Archipelago
     * @param player of whom I want to compute the influence on an archipelago
     * @param archipelago on which I want to compute the influence of the player
     * @return  Returns the influence of a Player on an Archipelago
     */
    public int computeInfluenceOfPlayer(Player player, Archipelago archipelago){
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

    /**
     * the Player conquers the Archipelago putting his own Towers and removing the previous ones (if present)
     * @param conqueror new owner of the archipelago
     * @param toConquer old owner of the archipelago
     * @throws InvalidTowerNumberException
     * @throws AnotherTowerException
     * @throws ExceededMaxTowersException
     * @throws TowerNotFoundException
     */
    protected void conquerArchipelago(Player conqueror, Archipelago toConquer) throws InvalidTowerNumberException, AnotherTowerException, ExceededMaxTowersException, TowerNotFoundException {
        // conqueror's Towers to put on the Archipelago
        List<Tower> conquerorTowers;
        conquerorTowers = this.playerSchool.get(conqueror).removeNumTowers(toConquer.getNumIslands());

        moveTower(conquerorTowers, toConquer);
    }

    /**
     * method that mover a list of towers of the new owner of an archipelago on that archipelago
     * @param conquerorTowers list of towers of the conqueror
     * @param toConquer archipelago to conquer
     * @throws InvalidTowerNumberException
     * @throws AnotherTowerException
     * @throws ExceededMaxTowersException
     */
    protected void moveTower(List<Tower> conquerorTowers, Archipelago toConquer) throws InvalidTowerNumberException, AnotherTowerException, ExceededMaxTowersException {
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

    /**
     * This merges as much adjacent Archipelagos as possible removing the old one from the this.archipelagos
     */
    protected void mergeArchipelagos(){
        if(this.isThereRightMerge()){
            Archipelago currentArchipelago = this.archipelagos.get(this.whereIsMotherNature());
            Archipelago rightArchipelago = this.archipelagos.get(getPreviousArchipelagoIndex(whereIsMotherNature()));
            this.archipelagos.remove(rightArchipelago);
            try{currentArchipelago.mergeArchipelagos((rightArchipelago));}
            catch(MergeDifferentOwnersException ex){ex.printStackTrace();}
            this.mergeArchipelagos();
        }
        else if(this.isThereLeftMerge()){
            Archipelago currentArchipelago = this.archipelagos.get(this.whereIsMotherNature());
            Archipelago leftArchipelago = this.archipelagos.get((this.whereIsMotherNature() + 1) % this.archipelagos.size());
            this.archipelagos.remove(leftArchipelago);
            try{currentArchipelago.mergeArchipelagos((leftArchipelago));}
            catch(MergeDifferentOwnersException ex){ex.printStackTrace();}
            this.mergeArchipelagos();
        }
    }


    /**
     *
     * @return true if you can merge the current Island (on which there is MotherNature) with the previous one, false otherwise
     */
    private boolean isThereRightMerge(){
        return this.archipelagos.get(this.whereIsMotherNature()).getOwner() == this.archipelagos.get(this.getPreviousArchipelagoIndex(this.whereIsMotherNature())).getOwner();
    }

    /**
     *
     * @return true if you can merge the current Island (on which there is MotherNature) with the next one, false otherwise
     */
    private boolean isThereLeftMerge(){
        return this.archipelagos.get(this.whereIsMotherNature()).getOwner() == this.archipelagos.get((this.whereIsMotherNature() + 1) % archipelagos.size()).getOwner();
    }

    /**
     * method that returns the index of the previous archipelago, given the index of an archipelago
     * @param index
     * @return
     */
    private int getPreviousArchipelagoIndex(int index) {
        if(index == 0) return this.archipelagos.size() - 1;
        if(index == 1) return 0;
        if(index == 2) return 1;
        if(index == 3) return 2;
        if(index == 4) return 3;
        if(index == 5) return 4;
        if(index == 6) return 5;
        if(index == 7) return 6;
        if(index == 8) return 7;
        if(index == 9) return 8;
        if(index == 10) return 9;
        if(index == 11) return 10;

        return -1;
    }


    //--------------------------------------------------ASSISTANT CARD

    /**
     * method that activate the choice of an assistant card
     * @param usedCards list of assistant cards already used
     * @param player player that wants to use the assistant card
     * @param turnPriority number of turn priority that the player has chosen
     * @throws AssistantCardAlreadyPlayedTurnException if a card with the same priority has been already chosen by one of the opponent during
     * the same turn
     * @throws NoAssistantCardException if doesn't exist any assistant card whith such turn priority
     */
    public void useAssistantCard(List<Integer> usedCards, Player player, int turnPriority) throws
            AssistantCardAlreadyPlayedTurnException, NoAssistantCardException { //used in the controller

        // control that no previous Player used that (but if it's his last card, let him use it)
        if(player.getHandLength() > 1){ // only one AssistantCard in hand: he has no alternative
            if(usedCards.size() > 0) {
                if (usedCards.contains(turnPriority)) {
                    throw new AssistantCardAlreadyPlayedTurnException();
                }
            }
        }

        player.useAssistantCard(turnPriority);
        if(!(this.schools.get(0) instanceof SchoolAdvanced))
            notifyPlayers();
    }

    /**
     * method that notifies every change of the board to all the players connected
     */
    @Override
    public void notifyPlayers() {
        SerializedBoardAbstract serializedBoardAbstract =
                new SerializedBoardAbstract(this.archipelagos, this.clouds, this.mn, this.schools);
        notify(serializedBoardAbstract);
    }
}