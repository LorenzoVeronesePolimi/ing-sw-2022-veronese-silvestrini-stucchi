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
            Archipelago a = new Archipelago();

            archipelagos.add(a);
        }

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

        for(int i = 1; i < archipelagos.size(); i++) {
            if(i < 6) {
                archipelagos.get(i).addStudent(initialStudents.get(i));
            } else if(i > 6) {
                archipelagos.get(i).addStudent(initialStudents.get(i-1));
            }
        }

    }

    public abstract void moveStudentBagToCloud();

    public abstract void moveStudentBagToSchool();

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
    // TODO
    public void checkIfConquerable(Player currentPlayer){
        Archipelago currentArchipelago = this.archipelagos.get(this.whereIsMotherNature());

        //if(currentPlayer == currentArchipelago.get)
    }
}
