package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Bank;
import it.polimi.ingsw.Model.Cards.*;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.*;
import it.polimi.ingsw.Model.Pawns.Professor;
import it.polimi.ingsw.Model.Pawns.Student;
import it.polimi.ingsw.Model.Pawns.Tower;
import it.polimi.ingsw.Model.Places.Archipelago;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Places.School.SchoolAdvanced;
import it.polimi.ingsw.Model.Player;

import java.util.*;

public class BoardAdvanced implements Board{
    private BoardAbstract board;
    private boolean twoExtraPointsFlag = false;
    private SPColour colourToExclude=null;
    private List<AbstractCharacterCard> extractedCards;
    private Bank bank = new Bank();


    public BoardAdvanced(BoardAbstract boardToExtend) {
        this.board = boardToExtend;
        List<School> schoolsAdvanced = new ArrayList<>();
        for(School s: this.board.schools){
            schoolsAdvanced.add(new SchoolAdvanced(s.getPlayer(),s.getNumMaxStudentsHall(),s.getNumMaxTowers()));
        }

        SPColour[] availableColours = {SPColour.BLUE, SPColour.PINK, SPColour.RED, SPColour.GREEN, SPColour.YELLOW};

        for(int i = 0; i < this.board.schools.size(); i++) {
            for(int j = 0; j < this.board.schools.get(i).getStudentsHall().size(); j++) {
                try {
                    schoolsAdvanced.get(i).addStudentHall(this.board.schools.get(i).getStudentsHall().get(j));
                } catch (ExceededMaxStudentsHallException e) {
                    e.printStackTrace();
                }
            }
            while(!this.board.schools.get(i).getStudentsHall().isEmpty()) {
                for(SPColour c : availableColours) {
                    try {
                        if(this.board.schools.get(i).getStudentsHall().stream().filter(x -> x.getColour().equals(c)).count() > 0)
                            this.board.schools.get(i).removeStudentHall(c);
                    } catch (StudentNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                this.board.schools.get(i).removeNumTowers(this.board.schools.get(i).getNumTowers());
            } catch (TowerNotFoundException e) {
                e.printStackTrace();
            }
        }
        this.board.schools=schoolsAdvanced;

        Map<Player, School> playerSchoolAdvancedMap = new HashMap<>();
        for(int i = 0; i < this.board.schools.size(); i++)
            playerSchoolAdvancedMap.put(this.board.players.get(i), this.board.schools.get(i));

        this.board.playerSchool = playerSchoolAdvancedMap;

        for(School s: this.board.schools){
            try {
                ((SchoolAdvanced)s).addCoin(bank.getCoin());
            } catch (EmptyCaveauExcepion e) {
                e.printStackTrace();
            }
        }

        List<AbstractCharacterCard> cards = new ArrayList<>();
        cards.add(new PlaceOneStudent(this));
        cards.add(new TakeProfessorOnEquity(this));
        cards.add(new FakeMNMovement(this));
        cards.add(new TwoExtraIslands(this));
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
    }

    public Bag getBag(){return this.board.bag;}

    public List<Archipelago> getArchiList(){
        return new ArrayList<Archipelago>(this.board.archipelagos);
    }

    public List<School> getSchools(){return new ArrayList<School>(this.board.schools);}

    public Archipelago getArchipelago(int archipelagoIndex) {
        return this.board.getArchipelago(archipelagoIndex);
    }

    public boolean isStudentInSchoolHall(Player player, SPColour c){
        return this.board.isStudentInSchoolHall(player, c);
    }

    @Override
    public void moveStudentBagToCloud() {
        this.board.moveStudentBagToCloud();
    }

    public void moveStudentSchoolToArchipelagos(Player player, SPColour colour, int archipelagoIndex) {
        this.board.moveStudentSchoolToArchipelagos(player, colour, archipelagoIndex);
    }

    public void moveStudentCloudToSchool(Player player, int cloudIndex) {
        this.board.moveStudentCloudToSchool(player, cloudIndex);
    }

    public void moveStudentHallToDiningRoom(Player player, SPColour colour) {
        //school related to the player that made the move
        School currentSchool = this.board.playerSchool.get(player);
        int numRed=0;
        int numBlue=0;
        int numGreen=0;
        int numPink=0;
        int numYellow=0;

        try {
            Student toBeMoved = currentSchool.removeStudentHall(colour);
            try {
                numRed=currentSchool.getNumStudentColour(SPColour.RED);
                numBlue=currentSchool.getNumStudentColour(SPColour.BLUE);
                numGreen=currentSchool.getNumStudentColour(SPColour.GREEN);
                numPink=currentSchool.getNumStudentColour(SPColour.PINK);
                numYellow=currentSchool.getNumStudentColour(SPColour.YELLOW);
            } catch (WrongColourException e) { //TODO: needed?
                e.printStackTrace();
            }

            currentSchool.addStudentDiningRoom(toBeMoved);
            checkCoinNeed(currentSchool, numRed,numBlue, numGreen,numPink,numYellow);
            this.conquerProfessor(player, colour); //has the movement of the Student caused the conquering of the Professor?
        } catch (StudentNotFoundException e) {
            e.printStackTrace();
        } catch (ExceededMaxStudentsDiningRoomException e) {
            e.printStackTrace();
        }
    }

    public void moveStudentBagToSchool(int numStudents) {
        this.board.moveStudentBagToSchool(numStudents);
    }

    public void moveMotherNature(int archipelagoIndex) {
        this.board.moveMotherNature(archipelagoIndex);
    }

    public void moveProfessor(Player destinationPlayer, SPColour colour) {
        this.board.moveProfessor(destinationPlayer, colour);
    }

    public boolean isProfessorInSchool(SPColour colour) {
        return this.board.isProfessorInSchool(colour);
    }

    public School whereIsProfessor(SPColour colour) {
        return this.board.whereIsProfessor(colour);
    }

    public void conquerProfessor(Player currentPlayer, SPColour colour) {
        this.board.conquerProfessor(currentPlayer, colour);
    }

    public int whereIsMotherNature() {
        return this.board.whereIsMotherNature();
    }

    public School getPlayerSchool(Player player) {
        return this.board.getPlayerSchool(player);
    }

    public void tryToConquer(Player currentPlayer){
        int currPosMotherNature = this.board.whereIsMotherNature();
        boolean archipelagoConquerable = this.checkIfConquerable(currentPlayer);
        if(archipelagoConquerable){
            this.board.conquerArchipelago(currentPlayer, this.board.archipelagos.get(currPosMotherNature));

            //let's merge Archipelagos
            this.board.mergeArchipelagos();
        }
        else { //the Archipelago remains to the owner
        }
    }


    public boolean checkIfConquerable(Player currentPlayer){
        int currPosMotherNature = this.board.whereIsMotherNature();
        Archipelago currentArchipelago = this.board.archipelagos.get(currPosMotherNature);
        //if the owner of the Archipelago is the current Player, he conquers nothing
        if(currentArchipelago.getOwner() == currentPlayer){
            return false;
        }
        else if(currentArchipelago.getOwner() == null){ //archipelago never conquered before
            return true;
        }
        else if(currentArchipelago.getForbidFlag()){ //This is an advanced function => see comment above(*)
            currentArchipelago.setForbidFlag(false);
        }
        else if(currentArchipelago.getTowerNoValueFlag()){ //This is an advanced function => see comment above(*)
            currentArchipelago.setTowerNoValueFlag(false);
        }
        //the current Player is not the owner: can he conquer the Archipelago?
        else{
            //who has higher influence according to rules?
            Player winner = this.computeWinner(currentArchipelago.getOwner(), currentPlayer, currentArchipelago, twoExtraPointsFlag, colourToExclude);
            twoExtraPointsFlag = false;

            if(winner == currentPlayer){
                return true;
            }
            else{
                return false;
            }
        }
        return false;
    }

    public Player computeWinner(Player owner, Player challenger, Archipelago archipelago) {
        return null;
    }

    public int computeInfluenceOfPlayer(Player player, Archipelago archipelago) {
        return 0;
    }

    public void useAssistantCard(Player player, int turnPriority) throws AssistantCardAlreadyPlayedTurnException {
        this.board.useAssistantCard(player, turnPriority);
    }

    protected Player computeWinner(Player owner, Player challenger, Archipelago archipelago, boolean twoExtraPointsFlag, SPColour colourToExclude){
        int ownerInfluence = this.computeInfluenceOfPlayer(owner, archipelago, colourToExclude);
        int challengerInfluence = this.computeInfluenceOfPlayer(challenger, archipelago, colourToExclude);

        if(twoExtraPointsFlag) {
            challengerInfluence += 2;
        }

        if(ownerInfluence > challengerInfluence){
            return owner;
        }
        else{
            return challenger;
        }
    }


    // Returns the influence of a Player on a Archipelago
    private int computeInfluenceOfPlayer(Player player, Archipelago archipelago, SPColour colourToExclude){
        int influence = 0;

        //if the player owns the Archipelago, the number of Towers (= number of Islands) counts
        if(player == archipelago.getOwner()){
            influence += archipelago.getNumIslands();
        }

        Map<SPColour, Integer> archipelagoStudentsData = archipelago.howManyStudents(); //data about Students on the Archipelago
        List<Professor> playerProfessors = this.board.playerSchool.get(player).getProfessors(); //Professors of the player
        for(Professor p : playerProfessors){
            if(colourToExclude==null || !p.getColour().equals(colourToExclude))
                influence += archipelagoStudentsData.get(p.getColour());
        }

        return influence;
    }

    private void checkCoinNeed(School currentSchool, int numRed, int numBlue, int numGreen, int numPink,int numYellow){
        try {
            if(numRed!=currentSchool.getNumStudentColour(SPColour.RED)){
                if(currentSchool.getNumStudentColour(SPColour.RED)==3 || currentSchool.getNumStudentColour(SPColour.RED)==6 || currentSchool.getNumStudentColour(SPColour.RED)==9){
                    try {
                        ((SchoolAdvanced)currentSchool).addCoin(bank.getCoin());
                    } catch (EmptyCaveauExcepion e) {
                        e.printStackTrace();
                    }
                }
            }
            else if(numBlue!=currentSchool.getNumStudentColour(SPColour.BLUE)){
                if(currentSchool.getNumStudentColour(SPColour.BLUE)==3 || currentSchool.getNumStudentColour(SPColour.BLUE)==6 || currentSchool.getNumStudentColour(SPColour.BLUE)==9){
                    try {
                        ((SchoolAdvanced)currentSchool).addCoin(bank.getCoin());
                    } catch (EmptyCaveauExcepion e) {
                        e.printStackTrace();
                    }
                }
            }
            else if(numGreen!=currentSchool.getNumStudentColour(SPColour.GREEN)){
                if(currentSchool.getNumStudentColour(SPColour.GREEN)==3 || currentSchool.getNumStudentColour(SPColour.GREEN)==6 || currentSchool.getNumStudentColour(SPColour.GREEN)==9){
                    try {
                        ((SchoolAdvanced)currentSchool).addCoin(bank.getCoin());
                    } catch (EmptyCaveauExcepion e) {
                        e.printStackTrace();
                    }
                }
            }
            else if(numPink!=currentSchool.getNumStudentColour(SPColour.PINK)){
                if(currentSchool.getNumStudentColour(SPColour.PINK)==3 || currentSchool.getNumStudentColour(SPColour.PINK)==6 || currentSchool.getNumStudentColour(SPColour.PINK)==9){
                    try {
                        ((SchoolAdvanced)currentSchool).addCoin(bank.getCoin());
                    } catch (EmptyCaveauExcepion e) {
                        e.printStackTrace();
                    }
                }
            }
            else if(numYellow!=currentSchool.getNumStudentColour(SPColour.YELLOW)){
                if(currentSchool.getNumStudentColour(SPColour.YELLOW)==3 || currentSchool.getNumStudentColour(SPColour.YELLOW)==6 || currentSchool.getNumStudentColour(SPColour.YELLOW)==9){
                    try {
                        ((SchoolAdvanced)currentSchool).addCoin(bank.getCoin());
                    } catch (EmptyCaveauExcepion e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (WrongColourException e) {
            e.printStackTrace();
        }
    }

    public void setColourToExclude(SPColour colourToExclude){
        this.colourToExclude= colourToExclude;
    }

    public void setTwoExtraPointsFlag(boolean twoExtraPointsFlag) {
        this.twoExtraPointsFlag = twoExtraPointsFlag;
    }

    public void usePlaceOneStudent(Player player, SPColour chosen, int archipelago) {
        for(AbstractCharacterCard card: extractedCards) {
            if(card instanceof PlaceOneStudent) {
                try {
                    School currentSchool = this.board.getPlayerSchool(player);
                    for(int i = 0; i < card.getCurrentPrice(); i++) {
                        bank.addCoin(((SchoolAdvanced)currentSchool).removeCoin());
                    }

                    ((PlaceOneStudent)card).useEffect(chosen, archipelago);
                    card.updatePrice(this.bank.getCoin());
                } catch (StudentNotFoundException | EmptyCaveauExcepion | ExceededMaxNumCoinException | CoinNotFoundException e ) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void useTakeProfessorOnEquity(Player player) {
        for(AbstractCharacterCard card: extractedCards) {
            if(card instanceof TakeProfessorOnEquity) {
                try {
                    School currentSchool = this.board.getPlayerSchool(player);
                    for(int i = 0; i < card.getCurrentPrice(); i++) {
                        bank.addCoin(((SchoolAdvanced)currentSchool).removeCoin());
                    }

                    ((TakeProfessorOnEquity)card).useEffect(player);
                    card.updatePrice(this.bank.getCoin());
                } catch (EmptyCaveauExcepion | ExceededMaxNumCoinException | CoinNotFoundException e ) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void useFakeMNMovement(Player player, int fakeMNPosition) {
        for(AbstractCharacterCard card: extractedCards) {
            if(card instanceof FakeMNMovement) {
                try {
                    School currentSchool = this.board.getPlayerSchool(player);
                    for(int i = 0; i < card.getCurrentPrice(); i++) {
                        bank.addCoin(((SchoolAdvanced)currentSchool).removeCoin());
                    }

                    ((FakeMNMovement)card).useEffect(player, fakeMNPosition);
                    card.updatePrice(this.bank.getCoin());
                } catch (EmptyCaveauExcepion | ExceededMaxNumCoinException | CoinNotFoundException e ) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void useTwoExtraIslands(Player player, int archipelago) {
        for(AbstractCharacterCard card: extractedCards) {
            if(card instanceof TwoExtraIslands) {
                try {
                    School currentSchool = this.board.getPlayerSchool(player);
                    for(int i = 0; i < card.getCurrentPrice(); i++) {
                        bank.addCoin(((SchoolAdvanced)currentSchool).removeCoin());
                    }

                    ((TwoExtraIslands)card).useEffect(player, archipelago);
                    card.updatePrice(this.bank.getCoin());
                } catch (EmptyCaveauExcepion | ExceededMaxNumCoinException | CoinNotFoundException | ImpossibleMNMove e ) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void useForbidIsland(Player player, int archipelago) {
        for(AbstractCharacterCard card: extractedCards) {
            if(card instanceof ForbidIsland) {
                try {
                    School currentSchool = this.board.getPlayerSchool(player);
                    for(int i = 0; i < card.getCurrentPrice(); i++) {
                        bank.addCoin(((SchoolAdvanced)currentSchool).removeCoin());
                    }

                    ((ForbidIsland)card).useEffect(archipelago);
                    card.updatePrice(this.bank.getCoin());
                } catch (EmptyCaveauExcepion | ExceededMaxNumCoinException | CoinNotFoundException e ) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void useTowerNoValue(Player player) {
        for(AbstractCharacterCard card: extractedCards) {
            if(card instanceof TowerNoValue) {
                try {
                    School currentSchool = this.board.getPlayerSchool(player);
                    for(int i = 0; i < card.getCurrentPrice(); i++) {
                        bank.addCoin(((SchoolAdvanced)currentSchool).removeCoin());
                    }

                    ((TowerNoValue)card).useEffect(player);
                    card.updatePrice(this.bank.getCoin());
                } catch (EmptyCaveauExcepion | ExceededMaxNumCoinException | CoinNotFoundException e ) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void useExchangeThreeStudents(Player player, List<SPColour> hallStudents, List<SPColour> exchangeStudents) {
        for(AbstractCharacterCard card: extractedCards) {
            if(card instanceof ExchangeThreeStudents) {
                try {
                    School currentSchool = this.board.getPlayerSchool(player);
                    for(int i = 0; i < card.getCurrentPrice(); i++) {
                        bank.addCoin(((SchoolAdvanced)currentSchool).removeCoin());
                    }

                    ((ExchangeThreeStudents)card).useEffect(player, hallStudents, exchangeStudents);
                    card.updatePrice(this.bank.getCoin());
                } catch (EmptyCaveauExcepion | ExceededMaxNumCoinException | CoinNotFoundException | WrongNumberOfStudentsTransferExcpetion e ) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void useTwoExtraPoints(Player player) {
        for(AbstractCharacterCard card: extractedCards) {
            if(card instanceof TwoExtraIslands) {
                try {
                    School currentSchool = this.board.getPlayerSchool(player);
                    for(int i = 0; i < card.getCurrentPrice(); i++) {
                        bank.addCoin(((SchoolAdvanced)currentSchool).removeCoin());
                    }

                    ((TwoExtraPoints)card).useEffect(player);
                    card.updatePrice(this.bank.getCoin());
                } catch (EmptyCaveauExcepion | ExceededMaxNumCoinException | CoinNotFoundException e ) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void useExcludeColourFromCounting(Player player, SPColour colourToExclude) {
        for(AbstractCharacterCard card: extractedCards) {
            if(card instanceof ExcludeColourFromCounting) {
                try {
                    School currentSchool = this.board.getPlayerSchool(player);
                    for(int i = 0; i < card.getCurrentPrice(); i++) {
                        bank.addCoin(((SchoolAdvanced)currentSchool).removeCoin());
                    }

                    ((ExcludeColourFromCounting)card).useEffect(player, colourToExclude);
                    card.updatePrice(this.bank.getCoin());
                } catch (EmptyCaveauExcepion | ExceededMaxNumCoinException | CoinNotFoundException e ) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void useExchangeTwoHallDining(Player player, List<SPColour> hallStudents, List<SPColour> diningStudents) {
        for(AbstractCharacterCard card: extractedCards) {
            if(card instanceof ExchangeTwoHallDining) {
                try {
                    School currentSchool = this.board.getPlayerSchool(player);
                    for(int i = 0; i < card.getCurrentPrice(); i++) {
                        bank.addCoin(((SchoolAdvanced)currentSchool).removeCoin());
                    }

                    ((ExchangeTwoHallDining)card).useEffect(player, hallStudents, diningStudents);
                    card.updatePrice(this.bank.getCoin());
                } catch (EmptyCaveauExcepion | ExceededMaxNumCoinException | CoinNotFoundException | WrongNumberOfStudentsTransferExcpetion e ) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void useExtraStudentInDining(Player player, SPColour cardToDining) {
        for(AbstractCharacterCard card: extractedCards) {
            if(card instanceof ExtraStudentInDining) {
                try {
                    School currentSchool = this.board.getPlayerSchool(player);
                    for(int i = 0; i < card.getCurrentPrice(); i++) {
                        bank.addCoin(((SchoolAdvanced)currentSchool).removeCoin());
                    }

                    ((ExtraStudentInDining)card).useEffect(player, cardToDining);
                    card.updatePrice(this.bank.getCoin());
                } catch (EmptyCaveauExcepion | ExceededMaxNumCoinException | CoinNotFoundException | StudentNotFoundException e ) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void useReduceColourInDining(Player player, SPColour colour) {
        for(AbstractCharacterCard card: extractedCards) {
            if(card instanceof ReduceColourInDining) {
                try {
                    School currentSchool = this.board.getPlayerSchool(player);
                    for(int i = 0; i < card.getCurrentPrice(); i++) {
                        bank.addCoin(((SchoolAdvanced)currentSchool).removeCoin());
                    }

                    ((ReduceColourInDining)card).useEffect(colour);
                    card.updatePrice(this.bank.getCoin());
                } catch (EmptyCaveauExcepion | ExceededMaxNumCoinException | CoinNotFoundException e ) {
                    e.printStackTrace();
                }
            }
        }
    }

}
