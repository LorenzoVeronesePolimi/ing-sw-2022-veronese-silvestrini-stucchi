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

import java.util.List;
import java.util.Map;

public abstract class Board {
    List<School> schools;   //list of all school in the game (one for each player)
    List<Player> players;   //list of all players in the game (in order)
    Map<Player, School> playerSchool;   //map of players and their relative school
    List<Archipelago> archipelagos;     //list of all the archipelagos in the game (in order)
    List<Cloud> clouds;     //list of all clouds in the game
    MotherNature mn;    //reference to MotherNature(Singleton)
    Bag bag;   //reference to the Bag

    public Board(List<Player> players) {
        for(int i = 0; i < 12; i++) {
            Archipelago a = new Archipelago(i);

            archipelagos.add(a);
        }

        this.players.addAll(players);
        mn = MotherNature.instance();
        bag = Bag.instance();
    }

    public void moveStudentSchoolToArchipelagos(Player player, SPColour colour, int archipelagoIndex) {
        //school related to the player that made the move
        School currentSchool = playerSchool.get(player);

        if(currentSchool != null) {
            try {
                Student toBeMoved = currentSchool.removeStudentHall(colour);
                archi.addStudent(toBeMoved);
            } catch (StudentNotFoundException e) {
                e.printStackTrace();
            }
        }
    };

    public void moveStudentCloudToSchool(Player player, int cloudIndex){
        //remove all the students from one particular cloud
        List<Student> toBeMoved = cloud.empty();

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

    public abstract void moveStudentBagToCloud();

    public abstract void moveStudentBagToSchool();

    public int getMotherNaturePosition(){
        return archipelagos.indexOf(mn.getCurrentPosition());
    };

    //TODO: we need also to consider the initial placing of MN made by the players
    public void moveMotherNature(int numPos){
        //Archipelago newMNPosition = archipelagos.get(numPos);
        //mn.putInPosition(newMNPosition);

    };

    public void moveProfessor(Player destPlayer, SPColour colour){
        //school related to the player that gets the professor
        School destSchool = playerSchool.get(destPlayer);
        School mittSchool = null;
        Professor toBeMoved = null;

        if(isProfessorInSchool(colour)) {
            mittSchool = whereIsProfessor(colour);

            try {
                toBeMoved = mittSchool.removeProfessor(colour);
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

        destSchool.addProfessor(toBeMoved);
    };

    private boolean isProfessorInSchool(SPColour colour) {
        for(School s: schools) {
            for(Professor p: s.getProfessors()) {
                if(p.getColour().equals(colour)) {
                    return true;
                }
            }
        }

        return false;
    }

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

    //TODO: archipelago parameter(?)
    public void conquerArchipelago(Player player, int archipelagoIndex) {
        School currentSchool = playerSchool.get(player);

        archipelagos.get(idArchi)

        Tower toBeMoved = currentSchool.removeTower();
        archipelagos.get(idArchi).addTower(toBeMoved);

    }
}
