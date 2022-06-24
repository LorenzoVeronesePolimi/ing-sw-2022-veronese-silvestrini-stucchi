package it.polimi.ingsw.Model.Board;

import it.polimi.ingsw.Controller.Enumerations.State;
import it.polimi.ingsw.Messages.ActiveMessageView;
import it.polimi.ingsw.Model.Pawns.MotherNature;
import it.polimi.ingsw.Model.Places.Archipelago;
import it.polimi.ingsw.Model.Places.Cloud;
import it.polimi.ingsw.Model.Places.School.School;
import it.polimi.ingsw.Model.Places.School.SchoolAdvanced;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.View.ClientView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * class for the serialization of the board abstract
 */
public class SerializedBoardAbstract implements Serializable, ActiveMessageView {
    private static final long serialVersionUID = 1L;
    protected String type;
    private List<Archipelago> archipelagos;
    private List<Cloud> clouds;
    private MotherNature mn;
    private List<School> schools;
    private State currentState;
    private Player currentPlayer;
    private String nicknameWinner;
    private List<Player> sitPlayers;

    /**
     * constructor of the serialized board abstract (complete)
     * @param archipelagos list of archipelagos with all the information
     * @param clouds list of clouds with all the information
     * @param mn mother nature
     * @param schools list of schools with all the information
     */
    public SerializedBoardAbstract(List<Archipelago> archipelagos, List<Cloud> clouds, MotherNature mn, List<School> schools) {
        this.type = "standard";
        this.archipelagos = archipelagos;
        this.clouds = clouds;
        this.mn = mn;
        this.schools = schools;
    }

    /**
     * constructor of the serialized board abstract with hidden information about the opponent hand of usable assistant cards
     * @param archipelagos list of archipelagos with all the information
     * @param clouds list of clouds with all the information
     * @param mn mother nature
     * @param schoolList list of opponent schools
     * @param nickname nick of the player to which the serialized board will arrive
     */
    public SerializedBoardAbstract(List<Archipelago> archipelagos, List<Cloud> clouds, MotherNature mn, List<School> schoolList, String nickname) {
        this.type = "standard";
        this.archipelagos = archipelagos;
        this.clouds = clouds;
        this.mn = mn;
        this.schools = new ArrayList<>();

        // Hiding opponent information
        // Here we have to clone the schools -> clone the player (removing the playerHand)
        for(School school : schoolList) {
            if(!school.getPlayer().getNickname().equals(nickname)) {
                if(school instanceof SchoolAdvanced) {
                    this.schools.add(new SchoolAdvanced((SchoolAdvanced) school));
                }
                else{
                    this.schools.add(new School(school));
                }
            }
            else{
                this.schools.add(school);
            }
        }
    }

    /**
     * getter of tthe type of board
     * @return type (standard/advanced)
     */
    public String getType() {
        return type;
    }

    /**
     * getter of archipelagos
     * @return list of archipelagos
     */
    public List<Archipelago> getArchipelagos() {
        return this.archipelagos;
    }

    /**
     * getter of clouds
     * @return list of clouds
     */
    public List<Cloud> getClouds() {
        return this.clouds;
    }

    /**
     * getter of mother nature
     * @return mother nature
     */
    public MotherNature getMn() {
        return this.mn;
    }

    /**
     * getter of schools
     * @return list of schools
     */
    public List<School> getSchools() {
        return schools;
    }

    /**
     * getter of current state
     * @return current state
     */
    public State getCurrentState(){return this.currentState;}

    /**
     * getter of current player
     * @return current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * getter of the nickname of the winner
     * @return nickname of the winner
     */
    public String getNicknameWinner() {
        return nicknameWinner;
    }

    /**
     * setter of current state
     * @param newState state to be setted
     */
    public void setCurrentState(State newState){this.currentState = newState;}

    /**
     * setter of current player
     * @param currentPlayer current player to be setted
     */
    public void setCurrentPlayer(Player currentPlayer){this.currentPlayer = currentPlayer;}

    /**
     * getter of list of players in the order in which they are "sit" (order of entrance in the game)
     * @return list of players in the order in which they are "sit" (order of entrance in the game)
     */
    public List<Player> getSitPlayers() {
        return sitPlayers;
    }

    /**
     * setter of the nickname of the winner
     * @param winner nickname of the winner
     */
    public void setNicknameWinner(String winner){this.nicknameWinner = winner;}

    /**
     * setter of list of players in the order in which they are "sit" (order of entrance in the game)
     * @param orderedPlayers list of players in the order in which they are "sit" (order of entrance in the game)
     */
    public void setSitPlayers(List<Player> orderedPlayers) {
        this.sitPlayers = orderedPlayers;
    }

    /**
     * method that sends to a given view in a message this serialized board abstract
     * @param view view from which the message arrive
     */
    @Override
    public void manageMessage(ClientView view) {
        view.showBoard(this);
    }
}
