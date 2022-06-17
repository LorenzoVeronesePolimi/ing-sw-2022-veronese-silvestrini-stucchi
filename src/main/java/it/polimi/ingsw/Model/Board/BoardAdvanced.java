package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Bank;
import it.polimi.ingsw.Model.Cards.*;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Pawns.Professor;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Places.Archipelago;
import it.polimi.ingsw.Model.Places.Cloud;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Places.School.SchoolAdvanced;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Observer.Observable;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.*;

public class BoardAdvanced extends Observable implements Board, Serializable{
    private static final long serialVersionUID = 1L;
    private final BoardAbstract board;
    private boolean twoExtraPointsFlag = false;
    private SPColour colourToExclude = null;
    private final List<AbstractCharacterCard> extractedCards; //is final... temporarily removed just for testing card usage
    private final Bank bank;


    public BoardAdvanced(BoardAbstract boardToExtend) throws
            ExceededMaxStudentsHallException, StudentNotFoundException, TowerNotFoundException, EmptyCaveauException {

        bank = new Bank();
        this.board = boardToExtend;
        List<School> schoolsAdvanced = new ArrayList<>();
        for(School s: this.board.schools){
            schoolsAdvanced.add(new SchoolAdvanced(s.getPlayer(),s.getNumMaxStudentsHall(),s.getNumMaxTowers()));
        }

        SPColour[] availableColours = {SPColour.BLUE, SPColour.PINK, SPColour.RED, SPColour.GREEN, SPColour.YELLOW};

        for(int i = 0; i < this.board.schools.size(); i++) {
            //((SchoolAdvanced)schoolsAdvanced.get(i)).addCoin(bank.getCoin());
            for(int j = 0; j < this.board.schools.get(i).getStudentsHall().size(); j++) {
                schoolsAdvanced.get(i).addStudentHall(this.board.schools.get(i).getStudentsHall().get(j));
            }
            while(!this.board.schools.get(i).getStudentsHall().isEmpty()) {
                for(SPColour c : availableColours) {
                    if(this.board.schools.get(i).getStudentsHall().stream().anyMatch(x -> x.getColour().equals(c)))
                        this.board.schools.get(i).removeStudentHall(c);
                }
            }

            this.board.schools.get(i).removeNumTowers(this.board.schools.get(i).getNumTowers());
        }

        this.board.schools=schoolsAdvanced;

        Map<Player, School> playerSchoolAdvancedMap = new HashMap<>();
        for(int i = 0; i < this.board.schools.size(); i++)
            playerSchoolAdvancedMap.put(this.board.players.get(i), this.board.schools.get(i));

        this.board.playerSchool = playerSchoolAdvancedMap;

        for(School s: this.board.schools){
            ((SchoolAdvanced)s).addCoin(bank.getCoin());
        }

        List<AbstractCharacterCard> cards = new ArrayList<>();
        cards.add(new PlaceOneStudent(this));
        cards.add(new TakeProfessorOnEquity(this));
        cards.add(new FakeMNMovement(this));
        cards.add(new TwoExtraIslands());
        cards.add(new ForbidIsland(this));
        cards.add(new TowerNoValue(this));
        cards.add(new ExchangeThreeStudents(this));
        cards.add(new TwoExtraPoints(this));
        cards.add(new ExcludeColourFromCounting(this));
        cards.add(new ExchangeTwoHallDining(this));
        cards.add(new ExtraStudentInDining(this));
        cards.add(new ReduceColourInDining(this));

        Collections.shuffle(cards, new Random());

        extractedCards = new ArrayList<>();
        extractedCards.add(cards.get(0));
        extractedCards.add(cards.get(1));
        extractedCards.add(cards.get(2));

        notifyPlayers();
    }

    public BoardAdvanced(BoardAbstract boardToCopy, BoardAdvanced toCopy){
        this.board = boardToCopy;
        this.twoExtraPointsFlag = toCopy.twoExtraPointsFlag;
        this.colourToExclude = toCopy.colourToExclude;
        this.extractedCards = toCopy.extractedCards; //is final... temporarily removed just for testing card usage
        this.bank = new Bank(toCopy.bank);
    }

    //TODO: remove this method ad set final the extractedCard list...added just for testing
    public void setExtractedCards(AbstractCharacterCard c){
        extractedCards.clear();
        extractedCards.add(c);
    }

    // For testing
    public void setExtractedCardsTwo(AbstractCharacterCard c1, AbstractCharacterCard c2){
        extractedCards.clear();
        extractedCards.add(c1);
        extractedCards.add(c2);
    }

    public Bag getBag(){return this.board.bag;}

    public Bank getBank() {
        return bank;
    }

    public SPColour getColourToExclude() {
        return colourToExclude;
    }

    public List<Archipelago> getArchiList(){
        return new ArrayList<>(this.board.archipelagos);
    }

    public List<School> getSchools(){return new ArrayList<>(this.board.schools);}

    public List<Cloud> getClouds(){return new ArrayList<>(this.board.clouds);}

    public Archipelago getArchipelago(int archipelagoIndex) {
        return this.board.getArchipelago(archipelagoIndex);
    }

    public Map<SPColour, Integer> getNumStudentsInArchipelago(int archipelagoIndex) {
        return this.board.getNumStudentsInArchipelago(archipelagoIndex);
    }

    public List<AbstractCharacterCard> getExtractedCards(){
        return new ArrayList<>(this.extractedCards);
    }

    public boolean isStudentInSchoolHall(Player player, SPColour c){
        return this.board.isStudentInSchoolHall(player, c);
    }

    @Override
    public void moveStudentBagToCloud() throws StudentNotFoundException, ExceededMaxStudentsCloudException {
        this.board.moveStudentBagToCloud();
        this.notifyPlayers();
    }

    public void moveStudentSchoolToArchipelagos(Player player, SPColour colour, int archipelagoIndex) throws StudentNotFoundException {
        System.out.println("[BoardAdvanced, moveStudentSchoolToArchipelagos]: arriva in adv");
        this.board.moveStudentSchoolToArchipelagos(player, colour, archipelagoIndex);
        System.out.println("[BoardAdvanced, moveStudentSchoolToArchipelagos]: notify advanced");
        this.notifyPlayers();
    }

    public void moveStudentCloudToSchool(Player player, int cloudIndex) throws ExceededMaxStudentsHallException {
        this.board.moveStudentCloudToSchool(player, cloudIndex);
        this.notifyPlayers();
    }

    public void moveStudentHallToDiningRoom(Player player, SPColour colour) throws
            StudentNotFoundException, ExceededMaxStudentsDiningRoomException, EmptyCaveauException, ProfessorNotFoundException,
            NoProfessorBagException {

                //school related to the player that made the move
        School currentSchool = this.board.playerSchool.get(player);
        int numRed, numBlue, numGreen, numPink , numYellow;

        Student toBeMoved = currentSchool.removeStudentHall(colour);
        numRed=currentSchool.getNumStudentColour(SPColour.RED);
        numBlue=currentSchool.getNumStudentColour(SPColour.BLUE);
        numGreen=currentSchool.getNumStudentColour(SPColour.GREEN);
        numPink=currentSchool.getNumStudentColour(SPColour.PINK);
        numYellow=currentSchool.getNumStudentColour(SPColour.YELLOW);

        currentSchool.addStudentDiningRoom(toBeMoved);
        checkCoinNeed(currentSchool, numRed,numBlue, numGreen,numPink,numYellow);
        this.conquerProfessor(player, colour); //has the movement of the Student caused the conquering of the Professor?

        notifyPlayers();
    }

    public void moveStudentBagToSchool(int numStudents) throws ExceededMaxStudentsHallException, StudentNotFoundException {
        this.board.moveStudentBagToSchool(numStudents);
    }

    @Override
    public void placeMotherNatureInitialBoard() {
        this.board.placeMotherNatureInitialBoard();
    }

    public void moveMotherNature(int mnMoves) {
        this.board.moveMotherNature(mnMoves);
    }

    public void moveMotherNatureInArchipelagoIndex(int index){
        this.board.moveMotherNatureInArchipelagoIndex(index);
    }

    public void moveProfessor(Player destinationPlayer, SPColour colour) throws ProfessorNotFoundException, NoProfessorBagException {
        this.board.moveProfessor(destinationPlayer, colour);
        this.notifyPlayers();
    }

    public boolean isProfessorInSchool(SPColour colour) {
        return this.board.isProfessorInSchool(colour);
    }

    public School whereIsProfessor(SPColour colour) {
        return this.board.whereIsProfessor(colour);
    }

    public void conquerProfessor(Player currentPlayer, SPColour colour) throws ProfessorNotFoundException, NoProfessorBagException {
        this.board.conquerProfessor(currentPlayer, colour);
    }

    public int whereIsMotherNature() {
        return this.board.whereIsMotherNature();
    }

    public School getPlayerSchool(Player player) {
        return this.board.getPlayerSchool(player);
    }

    public void tryToConquer(Player currentPlayer) throws
            InvalidTowerNumberException, AnotherTowerException, ExceededMaxTowersException, TowerNotFoundException {
        int currPosMotherNature = this.board.whereIsMotherNature();
        boolean archipelagoConquerable = this.checkIfConquerable(currentPlayer);
        if(archipelagoConquerable){
            this.board.conquerArchipelago(currentPlayer, this.board.archipelagos.get(currPosMotherNature));

            //let's merge Archipelagos
            this.board.mergeArchipelagos();
        }

        notifyPlayers();
    }


    public boolean checkIfConquerable(Player currentPlayer){
        Archipelago currentArchipelago = this.board.archipelagos.get(this.board.whereIsMotherNature());
        if(this.board instanceof BoardTwo || this.board instanceof BoardThree) {
            //if the owner of the Archipelago is the current Player, he conquers nothing
            if (currentArchipelago.getForbidFlag()>0) { //This is an advanced function => see comment above(*)
                currentArchipelago.removeForbidFlag();
                return false;
            }

            if (currentArchipelago.getOwner() == currentPlayer) {
                return false;
            } else if (currentArchipelago.getOwner() == null) { //archipelago never conquered before
                List<Professor> conquerorProfessors = this.board.playerSchool.get(currentPlayer).getProfessors();
                return setConquerable(currentArchipelago, conquerorProfessors);
            }
            //the current Player is not the owner: can he conquer the Archipelago?
            else {
                //who has higher influence according to rules?
                Player winner = this.computeWinner(currentArchipelago.getOwner(), currentPlayer, currentArchipelago, twoExtraPointsFlag, colourToExclude);
                currentArchipelago.setTowerNoValueFlag(false);
                twoExtraPointsFlag = false;

                return winner == currentPlayer;
            }
        } else if(this.board instanceof BoardFour){
            if (currentArchipelago.getForbidFlag()>0) { //This is an advanced function => see comment above(*)
                currentArchipelago.removeForbidFlag();
                return false;
            }
            if (currentArchipelago.getOwner() == currentPlayer || currentArchipelago.getOwner() == ((BoardFour)this.board).teammates.get(currentPlayer)) {
                return false;
            } else if (currentArchipelago.getOwner() == null) { //archipelago never conquered before
                List<Professor> conquerorProfessors = new ArrayList<>();
                conquerorProfessors.addAll(this.board.playerSchool.get(currentPlayer).getProfessors());
                conquerorProfessors.addAll(this.board.playerSchool.get(((BoardFour)this.board).teammates.get(currentPlayer)).getProfessors());
                return setConquerable(currentArchipelago, conquerorProfessors);
            }
            //the current Player is not the owner: can he conquer the Archipelago?
            else {
                //who has higher influence according to rules?
                Player winner = this.computeWinner(currentArchipelago.getOwner(), currentPlayer, currentArchipelago, twoExtraPointsFlag, colourToExclude);
                currentArchipelago.setTowerNoValueFlag(false);
                twoExtraPointsFlag = false;
                return winner == currentPlayer || winner == ((BoardFour)this.board).teammates.get(currentPlayer);
            }
        }
        return false;
    }

    private boolean setConquerable(Archipelago currentArchipelago, List<Professor> conquerorProfessors) {
        boolean conquerable = false;
        for(Professor p : conquerorProfessors){
            //can't conquer an Island without Students coloured without the Colour of a Professor of mine, even if no one has conquered it before
            if(!conquerable && !p.getColour().equals(colourToExclude))
                conquerable = currentArchipelago.howManyStudents().get(p.getColour()) > 0;
        }
        this.colourToExclude = null;
        return conquerable;
    }

    public Player computeWinner(Player owner, Player challenger, Archipelago archipelago) {
        return null;
    }

    public int computeInfluenceOfPlayer(Player player, Archipelago archipelago) {
        return 0;
    }

    public void useAssistantCard(List<Integer> usedCards, Player player, int turnPriority) throws AssistantCardAlreadyPlayedTurnException,
            NoAssistantCardException {
        this.board.useAssistantCard(usedCards, player, turnPriority);
        notifyPlayers();
    }

    protected Player computeWinner(Player owner, Player challenger, Archipelago archipelago, boolean twoExtraPointsFlag, SPColour colourToExclude){
        if(this.board instanceof BoardTwo || this.board instanceof BoardThree) {
            int ownerInfluence = this.computeInfluenceOfPlayer(owner, archipelago, colourToExclude);
            int challengerInfluence = this.computeInfluenceOfPlayer(challenger, archipelago, colourToExclude);
            this.colourToExclude = null;

            if (twoExtraPointsFlag) {
                challengerInfluence += 2;
            }

            if (ownerInfluence >= challengerInfluence) {
                return owner;
            } else {
                return challenger;
            }
        } else if(this.board instanceof BoardFour){
            int ownerTeamInfluence = this.computeInfluenceOfPlayer(owner, archipelago, colourToExclude) + this.computeInfluenceOfPlayer(((BoardFour)board).teammates.get(owner), archipelago,colourToExclude);
            int challengerTeamInfluence = this.computeInfluenceOfPlayer(challenger, archipelago,colourToExclude) + this.computeInfluenceOfPlayer(((BoardFour)board).teammates.get(challenger), archipelago, colourToExclude);

            if (twoExtraPointsFlag) {
                challengerTeamInfluence += 2;
            }

            if (ownerTeamInfluence > challengerTeamInfluence) {
                return owner;
            } else {
                return challenger;
            }
        }
        return null;
    }


    // Returns the influence of a Player on an Archipelago
    private int computeInfluenceOfPlayer(Player player, Archipelago archipelago, SPColour colourToExclude){
        int influence = 0;

        if(this.board instanceof BoardTwo || this.board instanceof BoardThree) {
            //if the player owns the Archipelago, the number of Towers (= number of Islands) counts
            if (player == archipelago.getOwner()) {
                if(!archipelago.getTowerNoValueFlag())
                    influence += archipelago.getNumIslands();
            }

            Map<SPColour, Integer> archipelagoStudentsData = archipelago.howManyStudents(); //data about Students on the Archipelago
            List<Professor> playerProfessors = this.board.playerSchool.get(player).getProfessors(); //Professors of the player
            for (Professor p : playerProfessors) {
                if (!p.getColour().equals(colourToExclude))
                    influence += archipelagoStudentsData.get(p.getColour());
            }
        } else if(this.board instanceof BoardFour){
            if (player == archipelago.getOwner() || player == ((BoardFour)board).teammates.get(archipelago.getOwner())) {
                if(!archipelago.getTowerNoValueFlag())
                    influence += archipelago.getNumIslands();
            }
            Map<SPColour, Integer> archipelagoStudentsData = archipelago.howManyStudents(); //data about Students on the Archipelago
            List<Professor> teamProfessors = this.board.playerSchool.get(player).getProfessors(); //Professors of the player
            teamProfessors.addAll(this.board.playerSchool.get(((BoardFour)board).teammates.get(player)).getProfessors());
            for (Professor p : teamProfessors) {
                if (!p.getColour().equals(colourToExclude))
                    influence += archipelagoStudentsData.get(p.getColour());
            }
        }
        return influence;
    }

    private void checkCoinNeed(School currentSchool, int numRed, int numBlue, int numGreen, int numPink,int numYellow) throws
            EmptyCaveauException {

        if(numRed!=currentSchool.getNumStudentColour(SPColour.RED)){
            if(currentSchool.getNumStudentColour(SPColour.RED)==3 || currentSchool.getNumStudentColour(SPColour.RED)==6 || currentSchool.getNumStudentColour(SPColour.RED)==9){
                ((SchoolAdvanced)currentSchool).addCoin(bank.getCoin());
            }
        }
        else if(numBlue!=currentSchool.getNumStudentColour(SPColour.BLUE)){
            if(currentSchool.getNumStudentColour(SPColour.BLUE)==3 || currentSchool.getNumStudentColour(SPColour.BLUE)==6 || currentSchool.getNumStudentColour(SPColour.BLUE)==9){
                ((SchoolAdvanced)currentSchool).addCoin(bank.getCoin());
            }
        }
        else if(numGreen!=currentSchool.getNumStudentColour(SPColour.GREEN)){
            if(currentSchool.getNumStudentColour(SPColour.GREEN)==3 || currentSchool.getNumStudentColour(SPColour.GREEN)==6 || currentSchool.getNumStudentColour(SPColour.GREEN)==9){
                ((SchoolAdvanced)currentSchool).addCoin(bank.getCoin());
            }
        }
        else if(numPink!=currentSchool.getNumStudentColour(SPColour.PINK)){
            if(currentSchool.getNumStudentColour(SPColour.PINK)==3 || currentSchool.getNumStudentColour(SPColour.PINK)==6 || currentSchool.getNumStudentColour(SPColour.PINK)==9){
                ((SchoolAdvanced)currentSchool).addCoin(bank.getCoin());
            }
        }
        else if(numYellow!=currentSchool.getNumStudentColour(SPColour.YELLOW)){
            if(currentSchool.getNumStudentColour(SPColour.YELLOW)==3 || currentSchool.getNumStudentColour(SPColour.YELLOW)==6 || currentSchool.getNumStudentColour(SPColour.YELLOW)==9){
                ((SchoolAdvanced)currentSchool).addCoin(bank.getCoin());
            }
        }
    }

    public void setColourToExclude(SPColour colourToExclude){
        this.colourToExclude = colourToExclude;
    }

    public void setTwoExtraPointsFlag(boolean twoExtraPointsFlag) {
        this.twoExtraPointsFlag = twoExtraPointsFlag;
    }

    public void usePlaceOneStudent(Player player, SPColour chosen, int archipelago, int indexCard) throws
            StudentNotFoundException , EmptyCaveauException, ExceededMaxNumCoinException, CoinNotFoundException {

        System.out.println("[BoardAdvanced, usePlaceOneStudent]: 1stud" + this.extractedCards.get(indexCard).getCurrentPrice());
        System.out.println("[BoardAdvanced, usePlaceOneStudent]: type" + this.extractedCards.get(indexCard).getType());
        if(this.makePayment(player, this.extractedCards.get(indexCard))) {
            ((PlaceOneStudent) this.extractedCards.get(indexCard)).useEffect(chosen, archipelago);
            this.extractedCards.get(indexCard).updatePrice(this.bank.getCoin());
        } else {
            throw new CoinNotFoundException();
        }

        notifyPlayers();
    }

    public void useTakeProfessorOnEquity(Player player, int indexCard) throws
            EmptyCaveauException, ExceededMaxNumCoinException, CoinNotFoundException, InvalidTowerNumberException,
            AnotherTowerException, ProfessorNotFoundException, NoProfessorBagException, ExceededMaxTowersException, TowerNotFoundException {
        System.out.println("[BoardAdvanced, useTakeProfessorOnEquity]: takep" + this.extractedCards.get(indexCard).getCurrentPrice());
        System.out.println("[BoardAdvanced, useTakeProfessorOnEquity]: type" + this.extractedCards.get(indexCard).getType());
        if(this.makePayment(player, this.extractedCards.get(indexCard))) {
            ((TakeProfessorOnEquity) this.extractedCards.get(indexCard)).useEffect(player);
            this.extractedCards.get(indexCard).updatePrice(this.bank.getCoin());
        } else {
            throw new CoinNotFoundException();
        }

        notifyPlayers();
    }

    public void useFakeMNMovement(Player player, int fakeMNPosition, int indexCard) throws
            EmptyCaveauException, ExceededMaxNumCoinException, CoinNotFoundException, InvalidTowerNumberException,
            AnotherTowerException, ExceededMaxTowersException, TowerNotFoundException {
        System.out.println("[BoardAdvanced, useFakeMNMovement]: fake" + this.extractedCards.get(indexCard).getCurrentPrice());
        System.out.println("[BoardAdvanced, useFakeMNMovement]: type" + this.extractedCards.get(indexCard).getType());
        if(this.makePayment(player, this.extractedCards.get(indexCard))) {
            ((FakeMNMovement) this.extractedCards.get(indexCard)).useEffect(player, fakeMNPosition);
            this.extractedCards.get(indexCard).updatePrice(this.bank.getCoin());
        } else {
            throw new CoinNotFoundException();
        }

        notifyPlayers();
    }

    public void useTwoExtraIslands(Player player, int indexCard) throws
            EmptyCaveauException, ExceededMaxNumCoinException, CoinNotFoundException{
        System.out.println("[BoardAdvanced, useTwoExtraIslands]: 2extrai" + this.extractedCards.get(indexCard).getCurrentPrice());
        System.out.println("[BoardAdvanced, useTwoExtraIslands]: type" + this.extractedCards.get(indexCard).getType());
        if(this.makePayment(player, this.extractedCards.get(indexCard))) {
            ((TwoExtraIslands) this.extractedCards.get(indexCard)).useEffect(player);
            this.extractedCards.get(indexCard).updatePrice(this.bank.getCoin());
        } else {
            throw new CoinNotFoundException();
        }

        notifyPlayers();
    }

    public void useForbidIsland(Player player, int archipelago, int indexCard) throws
            EmptyCaveauException, ExceededMaxNumCoinException, CoinNotFoundException, ExceededNumberForbidFlagException {
        System.out.println("[BoardAdvanced, useForbidIsland]: forbid" + this.extractedCards.get(indexCard).getCurrentPrice());
        System.out.println("[BoardAdvanced, useForbidIsland]: type" + this.extractedCards.get(indexCard).getType());
        if(this.makePayment(player, this.extractedCards.get(indexCard))) {
            ((ForbidIsland) this.extractedCards.get(indexCard)).useEffect(archipelago);
            this.extractedCards.get(indexCard).updatePrice(this.bank.getCoin());
        } else {
            throw new CoinNotFoundException();
        }

        notifyPlayers();
    }

    public void useTowerNoValue(Player player, int indexCard) throws
            EmptyCaveauException, ExceededMaxNumCoinException, CoinNotFoundException {
        System.out.println("[BoardAdvanced, useTowerNoValue]: tnov" + this.extractedCards.get(indexCard).getCurrentPrice());
        System.out.println("[BoardAdvanced, useTowerNoValue]: type" + this.extractedCards.get(indexCard).getType());
        if(this.makePayment(player, this.extractedCards.get(indexCard))) {
            ((TowerNoValue) this.extractedCards.get(indexCard)).useEffect();
            this.extractedCards.get(indexCard).updatePrice(this.bank.getCoin());
        } else {
            throw new CoinNotFoundException();
        }

        notifyPlayers();
    }

    public void useExchangeThreeStudents(Player player, List<SPColour> hallStudents, List<SPColour> exchangeStudents, int indexCard) throws
            EmptyCaveauException, ExceededMaxNumCoinException, CoinNotFoundException, WrongNumberOfStudentsTransferException,
            StudentNotFoundException, ExceededMaxStudentsHallException {
        System.out.println("[BoardAdvanced, useExchangeThreeStudents]: ex3s" + this.extractedCards.get(indexCard).getCurrentPrice());
        System.out.println("[BoardAdvanced, useExchangeThreeStudents]: type" + this.extractedCards.get(indexCard).getType());
        if(this.makePayment(player, this.extractedCards.get(indexCard))) {
            ((ExchangeThreeStudents) this.extractedCards.get(indexCard)).useEffect(player, hallStudents, exchangeStudents);
            this.extractedCards.get(indexCard).updatePrice(this.bank.getCoin());
        } else {
            throw new CoinNotFoundException();
        }

        notifyPlayers();
    }

    public void useTwoExtraPoints(Player player, int indexCard) throws
            EmptyCaveauException, ExceededMaxNumCoinException, CoinNotFoundException {
        System.out.println("[BoardAdvanced, useTwoExtraPoints]: 2exp" + this.extractedCards.get(indexCard).getCurrentPrice());
        System.out.println("[BoardAdvanced, useTwoExtraPoints]: type" + this.extractedCards.get(indexCard).getType());
        if(this.makePayment(player, this.extractedCards.get(indexCard))) {
            ((TwoExtraPoints) this.extractedCards.get(indexCard)).useEffect();
            this.extractedCards.get(indexCard).updatePrice(this.bank.getCoin());
        } else {
            throw new CoinNotFoundException();
        }

        notifyPlayers();
    }

    public void useExcludeColourFromCounting(Player player, SPColour colourToExclude, int indexCard) throws
            EmptyCaveauException, ExceededMaxNumCoinException, CoinNotFoundException, InvalidTowerNumberException,
            AnotherTowerException, ExceededMaxTowersException, TowerNotFoundException {
        System.out.println("[BoardAdvanced, useExcludeColourFromCounting]: excolour" + this.extractedCards.get(indexCard).getCurrentPrice());
        System.out.println("[BoardAdvanced, useExcludeColourFromCounting]: type" + this.extractedCards.get(indexCard).getType());
        if(this.makePayment(player, this.extractedCards.get(indexCard))) {
            ((ExcludeColourFromCounting) this.extractedCards.get(indexCard)).useEffect(colourToExclude);
            this.extractedCards.get(indexCard).updatePrice(this.bank.getCoin());
        } else {
            throw new CoinNotFoundException();
        }

        notifyPlayers();
    }

    public void useExchangeTwoHallDining(Player player, List<SPColour> hallStudents, List<SPColour> diningStudents, int indexCard) throws
            EmptyCaveauException, ExceededMaxNumCoinException, CoinNotFoundException, WrongNumberOfStudentsTransferException,
            ExceededMaxStudentsDiningRoomException, ExceededMaxStudentsHallException, StudentNotFoundException, ProfessorNotFoundException, NoProfessorBagException {
        System.out.println("[BoardAdvanced, useExchangeTwoHallDining]: type" + this.extractedCards.get(indexCard).getType());
        System.out.println("[BoardAdvanced, useExchangeTwoHallDining]: ex2hall" + this.extractedCards.get(indexCard).getCurrentPrice());
        if(this.makePayment(player, this.extractedCards.get(indexCard))) {
            ((ExchangeTwoHallDining) this.extractedCards.get(indexCard)).useEffect(player, hallStudents, diningStudents);
            this.extractedCards.get(indexCard).updatePrice(this.bank.getCoin());
        } else {
            throw new CoinNotFoundException();
        }

        notifyPlayers();
    }

    public void useExtraStudentInDining(Player player, SPColour cardToDining, int indexCard) throws
            EmptyCaveauException, ExceededMaxNumCoinException, CoinNotFoundException, StudentNotFoundException,
            ExceededMaxStudentsDiningRoomException {
        System.out.println("[BoardAdvanced, useExtraStudentInDining]: extradin" + this.extractedCards.get(indexCard).getCurrentPrice());
        System.out.println("[BoardAdvanced, useExtraStudentInDining]: type" + this.extractedCards.get(indexCard).getType());
        if(this.makePayment(player, this.extractedCards.get(indexCard))) {
            ((ExtraStudentInDining) this.extractedCards.get(indexCard)).useEffect(player, cardToDining);
            this.extractedCards.get(indexCard).updatePrice(this.bank.getCoin());
        } else {
            throw new CoinNotFoundException();
        }

        notifyPlayers();
    }

    public void useReduceColourInDining(Player player, SPColour colour, int indexCard) throws
            EmptyCaveauException, ExceededMaxNumCoinException, CoinNotFoundException, StudentNotFoundException {
        System.out.println("[BoardAdvanced, useReduceColourInDining]: reduce" + this.extractedCards.get(indexCard).getCurrentPrice());
        System.out.println("[BoardAdvanced, useReduceColourInDining]: type" + this.extractedCards.get(indexCard).getType());
        if(this.makePayment(player, this.extractedCards.get(indexCard))) {
            ((ReduceColourInDining) this.extractedCards.get(indexCard)).useEffect(colour);
            this.extractedCards.get(indexCard).updatePrice(this.bank.getCoin());
        } else {
            throw new CoinNotFoundException();
        }

        notifyPlayers();
    }

    private boolean makePayment(Player player, AbstractCharacterCard card) throws ExceededMaxNumCoinException, EmptyCaveauException {
        School currentSchool = this.board.getPlayerSchool(player);
        System.out.println("[BoardAdvanced, makePayment]: make payment " + card.getCurrentPrice());
        for(int i = 0; i < card.getCurrentPrice(); i++) {
            System.out.println("[BoardAdvanced, makePayment]: extracting coin #" + (i+1));
            try {
                bank.addCoin(((SchoolAdvanced)currentSchool).removeCoin());
            } catch (CoinNotFoundException e) {
                System.out.println("[BoardAdvanced, makePayment]: Coin not found");
                for(int j = 0; j < i; j++) {
                    ((SchoolAdvanced)currentSchool).addCoin(bank.getCoin());
                }
                return false;
            }
        }

        return true;
    }

    @Override
    public void notifyPlayers() {
        System.out.println("[BoardAdvanced, notifyPlayers]: advanced");
        SerializedBoardAdvanced serializedBoardAdvanced =
                new SerializedBoardAdvanced(this.board.archipelagos, this.board.clouds, this.board.mn,
                        this.board.schools, this.colourToExclude, this.extractedCards);
        notify(serializedBoardAdvanced);
    }
}
