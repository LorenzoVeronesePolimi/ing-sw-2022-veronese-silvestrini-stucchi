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
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Observer.Observable;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class BoardAbstract extends Observable implements Board {
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

    private void initializePlayersHands() throws ExceedingAssistantCardNumberException {
        // Create all needed AssistantCards
        List<AssistantCard> cardsCreated = new ArrayList<>();
        int[] cardsMotherNatureMoves= {1, 1, 2, 2, 3, 3, 4, 4, 5, 5}; //TODO: check if these values ar correct
        for(int i = 0; i < playerHandLength; i++){
            cardsCreated.add(new AssistantCard(cardsMotherNatureMoves[i], i+1));
        }

        // give them to Players
        for(Player p : this.players){
            for(AssistantCard c : cardsCreated){
                p.addAssistantCard(c);
            }
        }
    }

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

    //Mother Nature is put in the first archipelago
    public void placeMotherNatureInitialBoard() {
        mn.putInPosition(archipelagos.get(0));
    }




    //--------------------------------------------------GETTER AND SETTER
    public List<Cloud> getClouds() {
        return new ArrayList<>(this.clouds);
    }

    public Archipelago getArchipelago(int archipelagoIndex){
        return this.archipelagos.get(archipelagoIndex);
    }

    public Map<SPColour, Integer> getNumStudentsInArchipelago(int archipelagoIndex) {
        return this.archipelagos.get(archipelagoIndex).howManyStudents();
    }

    // Returns the Archipelago's index where MotherNature is positioned
    public int whereIsMotherNature(){
        return archipelagos.indexOf(mn.getCurrentPosition());
    }

    public School getPlayerSchool(Player player) {
        return playerSchool.get(player);
    }

    //The following, were introduced because useful for the Controller
    public boolean isStudentInSchoolHall(Player player, SPColour c){
        for(Student s : this.playerSchool.get(player).getStudentsHall()){
            if(s.getColour() == c){
                return true;
            }
        }
        return false;
    }




    //--------------------------------------------------PAWNS MOVEMENTS
    public void moveMotherNature(int mnMoves){
        mn.putInPosition(archipelagos.get((whereIsMotherNature() + mnMoves) % archipelagos.size()));
        notifyPlayers();
    }

    public void moveMotherNatureInArchipelagoIndex(int index){
        mn.putInPosition(archipelagos.get(index));
        notifyPlayers();
    }

    public void moveStudentSchoolToArchipelagos(Player player, SPColour colour, int archipelagoIndex) throws StudentNotFoundException {
        //school related to the player that made the move
        School currentSchool = playerSchool.get(player);

        if(currentSchool != null) {
            Student toBeMoved = currentSchool.removeStudentHall(colour);
            archipelagos.get(archipelagoIndex).addStudent(toBeMoved);
        }
        notifyPlayers();
    }

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
        notifyPlayers();
    }

    public void moveStudentHallToDiningRoom(Player player, SPColour colour) throws
            StudentNotFoundException, ExceededMaxStudentsDiningRoomException, ProfessorNotFoundException, NoProfessorBagException {

        //school related to the player that made the move
        School currentSchool = playerSchool.get(player);

        Student toBeMoved = currentSchool.removeStudentHall(colour);
        currentSchool.addStudentDiningRoom(toBeMoved);
        this.conquerProfessor(player, colour); //has the movement of the Student caused the conquering of the Professor?
        notifyPlayers();
    }

    public void moveStudentBagToCloud() throws ExceededMaxStudentsCloudException, StudentNotFoundException {
        int numStudents = this.clouds.get(0).getNumMaxStudents();
        for(Cloud c: clouds) {
            List<Student> toBePlaced;
            toBePlaced = bag.extractStudents(numStudents);
            c.fill(toBePlaced);
        }
        //notifyPlayers();
    }

    public void moveStudentBagToSchool(int numStudents) throws StudentNotFoundException, ExceededMaxStudentsHallException {
        for(School s: this.schools) {
            List<Student> toBePlaced;
            toBePlaced = bag.extractStudents(numStudents);

            for(Student student: toBePlaced) {
                s.addStudentHall(student);
            }
        }
        //notifyPlayers();
    }

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
        notifyPlayers();
    }




    //--------------------------------------------------PROFESSORS
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

    // Find the School where the Professor is in
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

    public void conquerProfessor(Player currentPlayer, SPColour colour) throws NoProfessorBagException, ProfessorNotFoundException {
        School currentSchool = this.whereIsProfessor(colour);
        if(currentSchool == null){ //it's in the bag
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

        notifyPlayers();
    }




    //--------------------------------------------------CONQUERING ISLANDS
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

    // true if the current Player (who moved MotherNature) will conquer the Archipelago, false otherwise
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

    // Computes which of two players has most influence on an Archipelago
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

    // Returns the influence of a Player on an Archipelago
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

    // the Player conquers the Archipelago putting his own Towers and removing the previous ones (if present)
    protected void conquerArchipelago(Player conqueror, Archipelago toConquer) throws InvalidTowerNumberException, AnotherTowerException, ExceededMaxTowersException, TowerNotFoundException {
        // conqueror's Towers to put on the Archipelago
        List<Tower> conquerorTowers;
        conquerorTowers = this.playerSchool.get(conqueror).removeNumTowers(toConquer.getNumIslands());
        moveTower(conquerorTowers, toConquer);
    }

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

    // This merges as much adjacent Archipelagos as possible removing the old one from the this.archipelagos
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

    // Check if you can merge the current Island (on which there is MotherNature) with the previous one
    private boolean isThereRightMerge(){
        return this.archipelagos.get(this.whereIsMotherNature()).getOwner() == this.archipelagos.get(this.getPreviousArchipelagoIndex(this.whereIsMotherNature())).getOwner();
    }

    // Check if you can merge the current Island (on which there is MotherNature) with the next one
    private boolean isThereLeftMerge(){
        return this.archipelagos.get(this.whereIsMotherNature()).getOwner() == this.archipelagos.get((this.whereIsMotherNature() + 1) % archipelagos.size()).getOwner();
    }

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


    //--------------------------------------------------ASSISTANT CARDS
    public void useAssistantCard(Player player, int turnPriority) throws
            AssistantCardAlreadyPlayedTurnException, NoAssistantCardException {

        // control that no previous Player used that (but if it's his last card, let him use it)
        int currentPlayerIndex = this.players.indexOf(player);
        for(int i = 0; i < currentPlayerIndex; i++){
            if(players.get(i).getLastCard().getTurnPriority() == turnPriority && player.getHandLength() > 1) {
                throw new AssistantCardAlreadyPlayedTurnException();
            }
        }

        player.useAssistantCard(turnPriority);
        notifyPlayers();
    }

    @Override
    public void notifyPlayers() {
        SerializedBoardAbstract serializedBoardAbstract =
                new SerializedBoardAbstract(this.archipelagos, this.clouds, this.mn, this.schools);
        notify(serializedBoardAbstract);
    }
}