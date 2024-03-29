package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Model.Bag;
import it.polimi.ingsw.Model.Bank;
import it.polimi.ingsw.Model.Cards.*;
import it.polimi.ingsw.Model.Enumerations.CharacterCardEnumeration;
import it.polimi.ingsw.Model.Enumerations.PlayerColour;
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
    private boolean fakeMNMovementFlag = false;
    private boolean takeProfessorOnEquityFlag = false;
    private boolean towerNoValueFlag = false;
    private final List<AbstractCharacterCard> extractedCards; //is final... temporarily removed just for testing card usage
    private final Bank bank;
    private String nameCardUsed = "";


    /**
     * constructor of the board: takes an instance of BoardTwo/Three/Four and adds all the advanced features of the game, such as the bank,
     * the coins caveau in every school, and the available character cards.
     * @param boardToExtend instance of the board that is wanted to be extended
     * @throws ExceededMaxStudentsHallException
     * @throws StudentNotFoundException
     * @throws TowerNotFoundException
     * @throws EmptyCaveauException
     */
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

    /**
     * constructor of the board: builds a copy of a given board abstract
     * @param boardToCopy BoardTwo/Three/Four that has to be copied (with which toCopy has been built)
     * @param toCopy boardAdvanced that has to be copied
     */
    public BoardAdvanced(BoardAbstract boardToCopy, BoardAdvanced toCopy){
        this.board = boardToCopy;
        this.twoExtraPointsFlag = toCopy.twoExtraPointsFlag;
        this.colourToExclude = toCopy.colourToExclude;
        this.extractedCards = toCopy.extractedCards; //is final... temporarily removed just for testing card usage
        this.bank = new Bank(toCopy.bank);
    }

    //TODO: remove this method and set final the extractedCard list...added just for testing

    /**
     * method added for testing: sets a given character card as the extracted one
     * @param c card that is tested
     */
    public void setExtractedCards(AbstractCharacterCard c){
        extractedCards.clear();
        extractedCards.add(c);
    }

    /**
     * method added for testing: sets two given character cards as extracted
     * @param c1 first card that is tested
     * @param c2 second card that is tested
     */
    public void setExtractedCardsTwo(AbstractCharacterCard c1, AbstractCharacterCard c2){
        extractedCards.clear();
        extractedCards.add(c1);
        extractedCards.add(c2);
    }

    /**
     * setter of fakeMNMovementFlag. It's true when the fake conquer is running, so that the notify is not made by
     * the board, but by the character card
     */
    public void setFakeMNMovementFlag(boolean fakeMNMovementFlag) {
        this.fakeMNMovementFlag = fakeMNMovementFlag;
    }

    /**
     * setter of takeProfessorOnEquityFlag. It's true during the turn in which the CharacterCard TakeProfessorOnEquity was used.
     * While it's true, it's effect is activated for each movement of a Student to the DiningRoom
     */
    public void setTakeProfessorOnEquityFlag(boolean takeProfessorOnEquityFlag) {
        this.takeProfessorOnEquityFlag = takeProfessorOnEquityFlag;
    }

    public void setTowerNoValueFlag(boolean towerNoValueFlag) {
        this.towerNoValueFlag = towerNoValueFlag;
    }

    /**
     * Method to assing to nameCardUsed the value ""
     */
    public void resetNameCardUsed() {
        this.nameCardUsed = "";
    }

    /**
     * Setter of nameCardUsed
     * @param name the new value of nameCardUsed
     */
    public void setNameCardUsed(String name) {
        this.nameCardUsed = name;
    }

    /**
     * Getter of name card used
     * @return the value of nameCardUsed
     */
    public String getNameCardUsed() {
        return this.nameCardUsed;
    }

    /**
     * getter of the bag
     * @return the bag
     */
    public Bag getBag(){return this.board.bag;}

    /**
     * getter of the bank
     * @return the bank
     */
    public Bank getBank() {
        return bank;
    }

    /**
     * getter of the color to exclude from counting for the computation of the dominance on an archipelago
     * @return the colour to exclude
     */
    public SPColour getColourToExclude() {
        return colourToExclude;
    }

    /**
     * getter of the list of archipelagos
     * @return the list of archipelagos
     */
    public List<Archipelago> getArchiList(){
        return new ArrayList<>(this.board.archipelagos);
    }

    /**
     * getter of the list of schools
     * @return the list of schools
     */
    public List<School> getSchools(){return new ArrayList<>(this.board.schools);}

    /**
     * getter of the list of clouds
     * @return the list of clouds
     */
    public List<Cloud> getClouds(){return new ArrayList<>(this.board.clouds);}

    /**
     * getter of a given archipelago
     * @param archipelagoIndex index of the archipelago that is wanted to be returned
     * @return the archipelago of the given index
     */
    public Archipelago getArchipelago(int archipelagoIndex) {
        return this.board.getArchipelago(archipelagoIndex);
    }

    /**
     * getter of the number of students for each colour that are placed on a given archipelago
     * @param archipelagoIndex index of the archipelago of which information is required
     * @return the map of the number of students for each colour that are placed on a given archipelago
     */
    public Map<SPColour, Integer> getNumStudentsInArchipelago(int archipelagoIndex) {
        return this.board.getNumStudentsInArchipelago(archipelagoIndex);
    }

    /**
     * getter of the extracted cards
     * @return the list of extracted cards
     */
    public List<AbstractCharacterCard> getExtractedCards(){
        return new ArrayList<>(this.extractedCards);
    }

    /**
     * getter of takeProfessorOnEquityFlag. It's true during the turn in which the CharacterCard TakeProfessorOnEquity was used.
     * While it's true, it's effect is activated for each movement of a Student to the DiningRoom
     */
    public boolean getTakeProfessorOnEquityFlag() {
        return this.takeProfessorOnEquityFlag;
    }

    /**
     * method that says if in a given player's hall there is a student of a given colour
     * @param player is the owner of the school where the student of c colour is supposed to be
     * @param c colour of student that is being looked for
     * @return true if in a given player's hall there is a student of a given colour, false otherwise
     */
    public boolean isStudentInSchoolHall(Player player, SPColour c){
        return this.board.isStudentInSchoolHall(player, c);
    }

    /**
     * method that moves a student from the bag to a cloud
     * @throws StudentNotFoundException if the bag has no students anymore
     * @throws ExceededMaxStudentsCloudException if the cloud is already full
     */
    @Override
    public void moveStudentBagToCloud() throws StudentNotFoundException, ExceededMaxStudentsCloudException {
        this.board.moveStudentBagToCloud();
        this.notifyPlayers();
    }

    /**
     * method that moves a student from a player' s hall to a given archipelago
     * @param player owner of the school from which I want to move the student
     * @param colour of the student that I want to move
     * @param archipelagoIndex index of the archipelago where I want to move the student to
     * @throws StudentNotFoundException if in the hall there is no such student
     */
    public void moveStudentSchoolToArchipelagos(Player player, SPColour colour, int archipelagoIndex) throws StudentNotFoundException {
        System.out.println("[BoardAdvanced, moveStudentSchoolToArchipelagos]: arriva in adv");
        this.board.moveStudentSchoolToArchipelagos(player, colour, archipelagoIndex);
        System.out.println("[BoardAdvanced, moveStudentSchoolToArchipelagos]: notify advanced");
        this.notifyPlayers();
    }

    /**
     * method that moves a student from a cloud to a player's hall
     * @param player
     * @param cloudIndex
     * @throws ExceededMaxStudentsHallException
     */
    public void moveStudentCloudToSchool(Player player, int cloudIndex) throws ExceededMaxStudentsHallException {
        this.board.moveStudentCloudToSchool(player, cloudIndex);
        this.notifyPlayers();
    }

    /**
     * method that moves a student from the player's hall to his dining room, and moves a coin from the bank to the school caveau if
     * the student is placed in a spot that is a multiple of 3
     * @param player owner of the school from which I want to move the student
     * @param colour of the student that I want to move to the dining room
     * @throws StudentNotFoundException
     * @throws ExceededMaxStudentsDiningRoomException
     * @throws EmptyCaveauException
     * @throws ProfessorNotFoundException
     * @throws NoProfessorBagException
     */
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

    /**
     * method that moves a given number of students from the bag to a school
     * @param numStudents number of student that I want to move
     * @throws ExceededMaxStudentsHallException
     * @throws StudentNotFoundException
     */
    public void moveStudentBagToSchool(int numStudents) throws ExceededMaxStudentsHallException, StudentNotFoundException {
        this.board.moveStudentBagToSchool(numStudents);
    }

    /**
     * method that place mother nature into archipelago 0 during the construction of the board
     */
    @Override
    public void placeMotherNatureInitialBoard() {
        this.board.placeMotherNatureInitialBoard();
    }

    /**
     * method that moves mother nature of a given number of positions
     * @param mnMoves number of archipelagos that I want mother nature to move through
     */
    public void moveMotherNature(int mnMoves) {
        this.board.moveMotherNature(mnMoves);
    }

    /**
     * method that moves mother nature on a given archipelago
     * @param index of the archipelago on which mother nature has to moved
     */
    public void moveMotherNatureInArchipelagoIndex(int index){
        this.board.moveMotherNatureInArchipelagoIndex(index);
    }

    /**
     * method that moves the professor of a given colour from its current position to the given destination
     * @param destinationPlayer owner of the school that I want to move mother the professor to
     * @param colour of the professor I want to move
     * @throws ProfessorNotFoundException
     * @throws NoProfessorBagException
     */
    public void moveProfessor(Player destinationPlayer, SPColour colour) throws ProfessorNotFoundException, NoProfessorBagException {
        this.board.moveProfessor(destinationPlayer, colour);
        this.notifyPlayers();
    }

    /**
     * method that says if the professor of a given colour is placed in any school
     * @param colour of the professor
     * @return true if the professor is placed in a school, false otherwise
     */
    public boolean isProfessorInSchool(SPColour colour) {
        return this.board.isProfessorInSchool(colour);
    }

    /**
     * method that returns the position of the professor of a given colour
     * @param colour of the professor
     * @return the school where the professor is
     */
    public School whereIsProfessor(SPColour colour) {
        return this.board.whereIsProfessor(colour);
    }

    /**
     * method that moves the professor of a given color from its current position into the school of the current player
     * @param currentPlayer is the current player
     * @param colour of the professor to conquer
     * @throws ProfessorNotFoundException
     * @throws NoProfessorBagException
     */
    public void conquerProfessor(Player currentPlayer, SPColour colour) throws ProfessorNotFoundException, NoProfessorBagException {
        this.board.conquerProfessor(currentPlayer, colour);
    }

    /**
     * method that gives the current position of mother nature (its archipelago index)
     * @return the current position of mother nature
     */
    public int whereIsMotherNature() {
        return this.board.whereIsMotherNature();
    }

    /**
     * getter of the school of a given player
     * @param player owner of the school that I want to get
     * @return the school of a given player
     */
    public School getPlayerSchool(Player player) {
        return this.board.getPlayerSchool(player);
    }

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
        System.out.println("[BoardAdvanced, tryToConquer]: entering conquer ");
        int currPosMotherNature = this.whereIsMotherNature();

        if (this.board.getArchipelago(currPosMotherNature).getForbidFlag()>0) { // => block conquering
            this.board.getArchipelago(currPosMotherNature).removeForbidFlag();
            for(AbstractCharacterCard c : this.extractedCards){ // re-put the icon on the card (which has max 4 icons, min 0)
                if(c.getType() == CharacterCardEnumeration.FORBID_ISLAND){
                    ((ForbidIsland)c).addForbidIconsRemained();
                }
            }
            System.out.println("[BoardAdvanced, tryToConquer]: forbid ");
        }

        else {
            if (this.board instanceof BoardTwo || this.board instanceof BoardThree) {
                System.out.println("[BoardAdvanced, tryToConquer]: checking influences ");
                boolean parity = false; //true if there is parity: no one conquer
                int pInfluence; // influence of the player in the actual cycle
                int maxInfluence = 0; // max influence found
                Player maxInfluencePlayer = null; // player with max influence found
                for (Player p : this.board.players) {
                    pInfluence = computeInfluenceOfPlayerAdvanced(p, this.board.archipelagos.get(currPosMotherNature), p == currentPlayer);

                    if(this.towerNoValueFlag && this.board.getArchipelago(currPosMotherNature).getOwner() == p){
                        pInfluence -= this.board.getArchipelago(currPosMotherNature).getNumIslands();
                    }

                    System.out.println("[BoardAdvanced, tryToConquer]: influence of " + p.getNickname() + " is " + pInfluence);
                    if (pInfluence > maxInfluence) {
                        maxInfluence = pInfluence;
                        maxInfluencePlayer = p;
                        parity = false;
                    } else if (pInfluence == maxInfluence) {
                        parity = true;
                    }

                }
                if (maxInfluencePlayer != null && !parity) { //if someone wins the winner computation, he can conquer
                    System.out.println("[BoardAdvanced, tryToConquer]: conquerable by " + maxInfluencePlayer.getNickname());
                    this.board.conquerArchipelago(maxInfluencePlayer, this.board.archipelagos.get(currPosMotherNature));

                    this.board.mergeArchipelagos();
                }
            } else if (this.board instanceof BoardFour) {
                int pInfluence; // influence of the player in the actual cycle
                Player representativeWhite = null;
                Player representativeBlack = null;
                int influeceWhite = 0;
                int influenceBlack = 0;
                for (Player p : this.board.players) {
                    pInfluence = computeInfluenceOfPlayerAdvanced(p, this.board.archipelagos.get(currPosMotherNature),p == currentPlayer);

                    if(this.towerNoValueFlag && this.board.getArchipelago(currPosMotherNature).getOwner() == p){
                        pInfluence -= this.board.getArchipelago(currPosMotherNature).getNumIslands();
                    }

                    if (p.getColour() == PlayerColour.WHITE) {
                        influeceWhite += pInfluence;
                        representativeWhite = p;
                    } else {
                        influenceBlack += pInfluence;
                        representativeBlack = p;
                    }
                }

                if (influeceWhite > influenceBlack) {
                    this.board.conquerArchipelago(representativeWhite, this.board.archipelagos.get(currPosMotherNature));

                    this.board.mergeArchipelagos();
                } else if (influeceWhite < influenceBlack) {
                    this.board.conquerArchipelago(representativeBlack, this.board.archipelagos.get(currPosMotherNature));

                    this.board.mergeArchipelagos();
                }
            }
        }

        if(!this.fakeMNMovementFlag){ //if conquer is fake, notify will be by the character card
            System.out.println("[BoardAdvanced, tryToConquer]: normal notify ");
            notifyPlayers();
        }

    }
    /*OLD RULES
    public void tryToConquer(Player currentPlayer) throws
            InvalidTowerNumberException, AnotherTowerException, ExceededMaxTowersException, TowerNotFoundException {
        System.out.println("[BoardAdvanced, tryToConquer]: entering conquer ");
        int currPosMotherNature = this.board.whereIsMotherNature();
        boolean archipelagoConquerable = this.checkIfConquerable(currentPlayer);
        if(archipelagoConquerable){
            System.out.println("[BoardAdvanced, tryToConquer]: conquerable ");
            this.board.conquerArchipelago(currentPlayer, this.board.archipelagos.get(currPosMotherNature));

            //let's merge Archipelagos
            this.board.mergeArchipelagos();
        }

        if(!this.fakeMNMovementFlag){ //if conquer is fake, notify will be by the character card
            System.out.println("[BoardAdvanced, tryToConquer]: normal notify ");
            notifyPlayers();
        }
    }*/

    /**
     * method that verifies if a player can conquer the archipelago on which is placed mother nature, takes into account also if one of the
     * cards that modify the influence criteria has been used
     * @param currentPlayer player that should conquer the archipelago
     * @return true if the current Player (who moved MotherNature) will conquer the Archipelago, false otherwise
     */
    /*
    public boolean checkIfConquerable(Player currentPlayer){
        Archipelago currentArchipelago = this.board.archipelagos.get(this.board.whereIsMotherNature());
        if(this.board instanceof BoardTwo || this.board instanceof BoardThree) {
            //if the owner of the Archipelago is the current Player, he conquers nothing
            if (currentArchipelago.getForbidFlag()>0) { //This is an advanced function => see comment above(*)
                currentArchipelago.removeForbidFlag();
                for(AbstractCharacterCard c : this.extractedCards){ // re-put the icon on the card (which has max 4 icons, min 0)
                    if(c.getType() == CharacterCardEnumeration.FORBID_ISLAND){
                        ((ForbidIsland)c).addForbidIconsRemained();
                    }
                }
                System.out.println("[BoardAdvanced, tryToConquer]: forbid ");
                return false;
            }

            if (currentArchipelago.getOwner() == currentPlayer) {
                System.out.println("[BoardAdvanced, tryToConquer]: already owner ");
                return false;
            } else if (currentArchipelago.getOwner() == null) { //archipelago never conquered before
                List<Professor> conquerorProfessors = this.board.playerSchool.get(currentPlayer).getProfessors();
                System.out.println("[BoardAdvanced, tryToConquer]: no owner ");
                return setConquerable(currentArchipelago, conquerorProfessors);
            }
            //the current Player is not the owner: can he conquer the Archipelago?
            else {
                System.out.println("[BoardAdvanced, tryToConquer]: battle ");
                //who has higher influence according to rules?
                Player winner = this.computeWinner(currentArchipelago.getOwner(), currentPlayer, currentArchipelago, twoExtraPointsFlag, colourToExclude);
                currentArchipelago.setTowerNoValueFlag(false);
                twoExtraPointsFlag = false;

                return winner == currentPlayer;
            }
        } else if(this.board instanceof BoardFour){
            if (currentArchipelago.getForbidFlag()>0) { //This is an advanced function => see comment above(*)
                currentArchipelago.removeForbidFlag();
                for(AbstractCharacterCard c : this.extractedCards){ // re-put the icon on the card (which has max 4 icons, min 0)
                    if(c.getType() == CharacterCardEnumeration.FORBID_ISLAND){
                        ((ForbidIsland)c).addForbidIconsRemained();
                    }
                }
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
    }*/

    /*
    /**
     * method that says if a given archipelago is conquerable, given a list o professors. takes into account the colourToExclude character card
     * @param currentArchipelago
     * @param conquerorProfessors
     * @return true if it's conquerable, false otherwise
     */
    /*
    private boolean setConquerable(Archipelago currentArchipelago, List<Professor> conquerorProfessors) {
        boolean conquerable = false;
        System.out.println("[BoardAdvanced, setConquerable]: archi " + this.getArchiList().indexOf(currentArchipelago));
        for(Professor p : conquerorProfessors){
            System.out.println("[BoardAdvanced, setConquerable]: loop ");
            //can't conquer an Island without Students coloured without the Colour of a Professor of mine, even if no one has conquered it before
            if(!conquerable && !p.getColour().equals(colourToExclude)) {
                System.out.println("[BoardAdvanced, tryToConquer]: conquerable for " + p.getColour() + " = " + (currentArchipelago.howManyStudents().get(p.getColour()) > 0));
                conquerable = currentArchipelago.howManyStudents().get(p.getColour()) > 0;
            }
        }
        this.colourToExclude = null;
        return conquerable;
    }*/

    /**
     * method that must be here to correctly implement the interface, the one with the correct parameters is below
     * @param owner player that owned the archipelago
     * @param challenger player that wants to conquer the archipelago
     * @param archipelago archipelago on which I want to compute the winner
     * @return null
     */
    public Player computeWinner(Player owner, Player challenger, Archipelago archipelago) {
        return null;
    }

    /**
     * method that must be here to correctly implement the interface
     * @param player of whom I want to compute the influence on an archipelago
     * @param archipelago on which I want to compute the influence of the player
     * @return influence of the player
     */
    public int computeInfluenceOfPlayer(Player player, Archipelago archipelago) {
        return this.board.computeInfluenceOfPlayer(player, archipelago);
    }
    /*OLD RULES
    public int computeInfluenceOfPlayer(Player player, Archipelago archipelago) {

        return 0;
    }
    */

    /**
     * Computes the influence of the player, considering effects of CharacterCards
     * @param player of whom I want to compute the influence on an archipelago
     * @param archipelago on which I want to compute the influence of the player
     * @param isCurrentPlayer true if player is the on who is leading the turn
     * @return influence of player
     */
    public int computeInfluenceOfPlayerAdvanced(Player player, Archipelago archipelago, boolean isCurrentPlayer){
        int influence = this.computeInfluenceOfPlayer(player, archipelago);

        if(this.colourToExclude != null){ // subtract students of the excluded colour from the count
            for(Professor p : this.board.getPlayerSchool(player).getProfessors()){
                if(p.getColour() == this.colourToExclude){
                    influence -= archipelago.getStudentsData().get(this.colourToExclude);
                    break;
                }
            }
        }


        if(isCurrentPlayer){ // apply modifications to the score so that I consider advanced functions
            if(this.twoExtraPointsFlag){
                influence += 2;
            }
        }

        return influence;
    }

    /**
     * method that activate the choice of an assistant card
     * @param usedCards list of assistant cards already used
     * @param player player that wants to use the assistant card
     * @param turnPriority number of turn priority that the player has chosen
     * @throws AssistantCardAlreadyPlayedTurnException if a card with the same priority has been already chosen by one of the opponent during
     * the same turn
     * @throws NoAssistantCardException if doesn't exist any assistant card whith such turn priority
     */
    public void useAssistantCard(List<Integer> usedCards, Player player, int turnPriority) throws AssistantCardAlreadyPlayedTurnException,
            NoAssistantCardException {
        this.board.useAssistantCard(usedCards, player, turnPriority);
        notifyPlayers();
    }

    /*
    /**
     * method that computes which of two players has most influence on an Archipelago
     * @param owner player that owned the archipelago
     * @param challenger player that wants to conquer the archipelago
     * @param archipelago archipelago on which I want to compute the winner
     * @param twoExtraPointsFlag boolean that signals that the twoExtraPoints character card has been used
     * @param colourToExclude colour that will not be counted in the computation of the winner, chosen during the usage of the colourToExclude
     *                        character card
     * @return the player who has most influence
     */
    /*
    protected Player computeWinner(Player owner, Player challenger, Archipelago archipelago, boolean twoExtraPointsFlag, SPColour colourToExclude){
        System.out.println("[BoardAdvanced, computeWinner]: entering compute ");
        if(this.board instanceof BoardTwo || this.board instanceof BoardThree) {
            int ownerInfluence = this.computeInfluenceOfPlayer(owner, archipelago, colourToExclude);
            int challengerInfluence = this.computeInfluenceOfPlayer(challenger, archipelago, colourToExclude);
            this.colourToExclude = null;

            if (twoExtraPointsFlag) {
                challengerInfluence += 2;
            }

            if (ownerInfluence >= challengerInfluence) {
                System.out.println("[BoardAdvanced, tryToConquer]: owner ");
                return owner;
            } else {
                System.out.println("[BoardAdvanced, tryToConquer]: challenger ");
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
    }*/

    /*
    /**
     * method that computes the influence of a given player on a given archipelago (takes into account if the colourToExclude character card
     * has been used).
     * @param player of which is wanted to compute the influence
     * @param archipelago on which the influence is wanted to be calculated on
     * @param colourToExclude colour that will be excluded from the computation (can be null)
     * @return influence of a Player on an Archipelago
     */
    /*
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
    }*/

    /**
     * method that manages the gain of coins when a student is added to a dining room
     * @param currentSchool school that has placed a student in the dining room
     * @param numRed number of red students in the dining room
     * @param numBlue number of blue students in the dining room
     * @param numGreen number of green students in the dining room
     * @param numPink number of pink students in the dining room
     * @param numYellow number of yellow students in the dining room
     * @throws EmptyCaveauException is there are no more money in the bank
     */
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

    /**
     * setter of the colour to exclude
     * @param colourToExclude colour that is wanted to be excluded
     */
    public void setColourToExclude(SPColour colourToExclude){
        this.colourToExclude = colourToExclude;
    }

    /**
     * setter of the twoExtraPoints flag (usage of the twoExtraPoints character card)
     * @param twoExtraPointsFlag true or false
     */
    public void setTwoExtraPointsFlag(boolean twoExtraPointsFlag) {
        this.twoExtraPointsFlag = twoExtraPointsFlag;
    }

    /**
     * method that activates the effect of the placeOneStudent character card
     * @param player player who uses the card
     * @param chosen student tha has been chosen to be placed in an archipelago
     * @param archipelago archipelago where the chosen student will be placed
     * @param indexCard index of the card
     * @throws StudentNotFoundException
     * @throws EmptyCaveauException
     * @throws ExceededMaxNumCoinException
     * @throws CoinNotFoundException
     */
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

        this.nameCardUsed = "Place one student";
        notifyPlayers();
    }

    /**
     * method that activates the effect of the takeProfessorOnEquity character card
     * @param player player who uses the card
     * @param indexCard index of the card
     * @throws EmptyCaveauException
     * @throws ExceededMaxNumCoinException
     * @throws CoinNotFoundException
     * @throws InvalidTowerNumberException
     * @throws AnotherTowerException
     * @throws ProfessorNotFoundException
     * @throws NoProfessorBagException
     * @throws ExceededMaxTowersException
     * @throws TowerNotFoundException
     */
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

        this.nameCardUsed = "Take prof. on equity";
        notifyPlayers();
    }

    /**
     * method that activates the effect of the fakeMNMovement character card
     * @param player player who uses the card
     * @param fakeMNPosition index of the archipelago on which mother nature has to do the fake movement
     * @param indexCard index of the card
     * @throws EmptyCaveauException
     * @throws ExceededMaxNumCoinException
     * @throws CoinNotFoundException
     * @throws InvalidTowerNumberException
     * @throws AnotherTowerException
     * @throws ExceededMaxTowersException
     * @throws TowerNotFoundException
     */
    public void useFakeMNMovement(Player player, int fakeMNPosition, int indexCard) throws
            EmptyCaveauException, ExceededMaxNumCoinException, CoinNotFoundException, InvalidTowerNumberException,
            AnotherTowerException, ExceededMaxTowersException, TowerNotFoundException {
        System.out.println("[BoardAdvanced, useFakeMNMovement]: fake " + this.extractedCards.get(indexCard).getCurrentPrice());
        System.out.println("[BoardAdvanced, useFakeMNMovement]: type " + this.extractedCards.get(indexCard).getType());
        if(this.makePayment(player, this.extractedCards.get(indexCard))) {
            ((FakeMNMovement) this.extractedCards.get(indexCard)).useEffect(player, fakeMNPosition);
            this.extractedCards.get(indexCard).updatePrice(this.bank.getCoin());
        } else {
            throw new CoinNotFoundException();
        }

        System.out.println("[BoardAdvanced, tryToConquer]: fake notify ");
        this.nameCardUsed = "Fake MN movement";
        notifyPlayers();
    }

    /**
     * method that activates the effect of the twoExtraIslands character card
     * @param player player who uses the card
     * @param indexCard index of the card
     * @throws EmptyCaveauException
     * @throws ExceededMaxNumCoinException
     * @throws CoinNotFoundException
     */
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

        this.nameCardUsed = "Two extra islands";
        notifyPlayers();
    }

    /**
     * method that activates the effect of the forbidIsland character card
     * @param player player that uses the card
     * @param archipelago archipelago on which a forbid tile will be placed
     * @param indexCard index of the card
     * @throws EmptyCaveauException
     * @throws ExceededMaxNumCoinException
     * @throws CoinNotFoundException
     * @throws ExceededNumberForbidFlagException
     */
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

        this.nameCardUsed = "Forbid island";
        notifyPlayers();
    }

    /**
     * method that activates the effect of the towerNoValue character card
     * @param player player that uses the card
     * @param indexCard index of the card
     * @throws EmptyCaveauException
     * @throws ExceededMaxNumCoinException
     * @throws CoinNotFoundException
     */
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

        this.nameCardUsed = "Tower no value";
        notifyPlayers();
    }

    /**
     * method that activates the effect of the ExchangeThreeStudents character card
     * @param player player that uses the card
     * @param hallStudents list of students of the hall that will be exchanged
     * @param exchangeStudents list of students of the card that will be exchanged
     * @param indexCard index of tha card
     * @throws EmptyCaveauException
     * @throws ExceededMaxNumCoinException
     * @throws CoinNotFoundException
     * @throws WrongNumberOfStudentsTransferException
     * @throws StudentNotFoundException
     * @throws ExceededMaxStudentsHallException
     */
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

        this.nameCardUsed = "Exchange three students";
        notifyPlayers();
    }

    /**
     * method that activates the effect of the twoExtraPoints character card
     * @param player player that uses the card
     * @param indexCard index of the card
     * @throws EmptyCaveauException
     * @throws ExceededMaxNumCoinException
     * @throws CoinNotFoundException
     */
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

        this.nameCardUsed = "Two extra points";
        notifyPlayers();
    }

    /**
     * method that activates the effect of the excludeColourFromCounting character card
     * @param player player that uses the card
     * @param colourToExclude colour that will be excluded from counting
     * @param indexCard index of the card
     * @throws EmptyCaveauException
     * @throws ExceededMaxNumCoinException
     * @throws CoinNotFoundException
     * @throws InvalidTowerNumberException
     * @throws AnotherTowerException
     * @throws ExceededMaxTowersException
     * @throws TowerNotFoundException
     */
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

        this.nameCardUsed = "Exclude col. from count.";
        notifyPlayers();
    }

    /**
     * method that activates the effect of the ExchangeTwoHallDining character card
     * @param player player that uses the card
     * @param hallStudents list of students in the hall that will be exchanged
     * @param diningStudents list of students in dining room that will be excluded
     * @param indexCard index of the card
     * @throws EmptyCaveauException
     * @throws ExceededMaxNumCoinException
     * @throws CoinNotFoundException
     * @throws WrongNumberOfStudentsTransferException
     * @throws ExceededMaxStudentsDiningRoomException
     * @throws ExceededMaxStudentsHallException
     * @throws StudentNotFoundException
     * @throws ProfessorNotFoundException
     * @throws NoProfessorBagException
     */
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

        this.nameCardUsed = "Exchange two hall dining";
        notifyPlayers();
    }

    /**
     * method that activates the effect of the extraStudentInDining character card
     * @param player player that uses the card
     * @param cardToDining colour of the student to place in the dining room
     * @param indexCard index of the card
     * @throws EmptyCaveauException
     * @throws ExceededMaxNumCoinException
     * @throws CoinNotFoundException
     * @throws StudentNotFoundException
     * @throws ExceededMaxStudentsDiningRoomException
     */
    public void useExtraStudentInDining(Player player, SPColour cardToDining, int indexCard) throws
            EmptyCaveauException, ExceededMaxNumCoinException, CoinNotFoundException, StudentNotFoundException,
            ExceededMaxStudentsDiningRoomException, ProfessorNotFoundException, NoProfessorBagException {
        System.out.println("[BoardAdvanced, useExtraStudentInDining]: extradin" + this.extractedCards.get(indexCard).getCurrentPrice());
        System.out.println("[BoardAdvanced, useExtraStudentInDining]: type" + this.extractedCards.get(indexCard).getType());
        if(this.makePayment(player, this.extractedCards.get(indexCard))) {
            ((ExtraStudentInDining) this.extractedCards.get(indexCard)).useEffect(player, cardToDining);
            this.extractedCards.get(indexCard).updatePrice(this.bank.getCoin());
        } else {
            throw new CoinNotFoundException();
        }

        this.nameCardUsed = "Extra stud. in dining";
        notifyPlayers();
    }

    /**
     * method that activates the effect of the reduceColourInDining character card
     * @param player player that uses the card
     * @param colour colour that will be reduced
     * @param indexCard index of the card
     * @throws EmptyCaveauException
     * @throws ExceededMaxNumCoinException
     * @throws CoinNotFoundException
     * @throws StudentNotFoundException
     */
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

        this.nameCardUsed = "Reduce col. in dining";
        notifyPlayers();
    }

    /**
     * method that verifies if a student has enough money to buy the usage of a given card, and if possible makes the payment
     * @param player player that uses the card
     * @param card chosen card
     * @return true if the payment has been done successfully, false otherwise
     * @throws ExceededMaxNumCoinException
     * @throws EmptyCaveauException
     */
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

    /**
     * method that notifies every change of the board to all the players connected
     */
    @Override
    public void notifyPlayers() {
        System.out.println("[BoardAdvanced, notifyPlayers]: advanced");
        SerializedBoardAdvanced serializedBoardAdvanced =
                new SerializedBoardAdvanced(this.board.archipelagos, this.board.clouds, this.board.mn,
                        this.board.schools, this.colourToExclude, this.nameCardUsed, this.extractedCards);

        notify(serializedBoardAdvanced);
    }
}
