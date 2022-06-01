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

    public SerializedBoardAbstract(List<Archipelago> archipelagos, List<Cloud> clouds, MotherNature mn, List<School> schools) {
        this.type = "standard";
        this.archipelagos = archipelagos;
        this.clouds = clouds;
        this.mn = mn;
        this.schools = schools;
    }

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

    public String getType() {
        return type;
    }

    public List<Archipelago> getArchipelagos() {
        return this.archipelagos;
    }

    public List<Cloud> getClouds() {
        return this.clouds;
    }

    public MotherNature getMn() {
        return this.mn;
    }

    public List<School> getSchools() {
        return schools;
    }

    public State getCurrentState(){return this.currentState;}

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public String getNicknameWinner() {
        return nicknameWinner;
    }

    public void setCurrentState(State newState){this.currentState = newState;}

    public void setCurrentPlayer(Player currentPlayer){this.currentPlayer = currentPlayer;}

    public List<Player> getSitPlayers() {
        return sitPlayers;
    }

    public void setNicknameWinner(String winner){this.nicknameWinner = winner;}

    public void setSitPlayers(List<Player> orderedPlayers) {
        this.sitPlayers = orderedPlayers;
    }

    @Override
    public void manageMessage(ClientView view) {
        view.showBoard(this);
    }
}
