package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Messages.*;

import java.util.List;


public class ControllerInput {
    private static final int MAX_NUM_ARCHIPELAGOS = 12;


    public ControllerInput(){}

    public boolean checkFormat(Object arg){
        if(!(arg instanceof Message)){
            return false;
        }

        Message message = (Message)arg;
        switch(message.getType()){
            case CREATE_MATCH:
                return (this.checkNickname(((MessageCreateMatch)message).getNicknameFirstPlayer()) &&
                        this.checkPlayerColour(((MessageCreateMatch)message).getColourFirstPlayer()));
            case ADD_PLAYER:
                return (this.checkNickname(((MessageAddPlayer)message).getNickname()) &&
                        this.checkPlayerColour(((MessageAddPlayer)message).getColour()));
            case ASSISTANT_CARD:
                return(this.checkNickname(((MessageAssistantCard)message).getNicknamePlayer()) &&
                        this.checkMotherNatureMovement(((MessageAssistantCard)message).getMotherNatureMovement()) &&
                        this.checkTurnPriority(((MessageAssistantCard)message).getTurnPriority()));
            case STUDENT_HALL_TO_DINING_ROOM:
                return(this.checkNickname(((MessageStudentHallToDiningRoom)message).getNicknamePlayer())) &&
                        this.checkStudentColour(((MessageStudentHallToDiningRoom)message).getColour());
            case STUDENT_TO_ARCHIPELAGO:
                return (this.checkNickname(((MessageStudentToArchipelago)message).getNicknamePlayer()) &&
                        this.checkStudentColour(((MessageStudentToArchipelago)message).getColour()) &&
                        this.checkDestArchipelagoIndex(((MessageStudentToArchipelago)message).getDestArchipelagoIndex()));
            case MOVE_MOTHER_NATURE:
                return (this.checkNickname(((MessageMoveMotherNature)message).getNicknamePlayer()) &&
                        this.checkMotherNatureMovement(((MessageMoveMotherNature)message).getMoves()));
            case STUDENT_CLOUD_TO_SCHOOL:
                return (this.checkNickname(((MessageStudentCloudToSchool)message).getNicknamePlayer()) &&
                        this.checkCloudIndex(((MessageStudentCloudToSchool)message).getIndexCloud()));
            case CC_EXCHANGE_THREE_STUDENTS:
                return (this.checkIndexCard(((MessageCC)message).getIndexCard()) &&
                        this.checkNickname(((MessageCCExchangeThreeStudents)message).getNicknamePlayer()) &&
                        this.checkMultipleStudentColour(((MessageCCExchangeThreeStudents)message).getColoursCard()) &&
                        this.checkMultipleStudentColour(((MessageCCExchangeThreeStudents)message).getColoursHall()));
            case CC_EXCHANGE_TWO_HALL_DINING:
                return (this.checkIndexCard(((MessageCC)message).getIndexCard()) &&
                        this.checkNickname(((MessageCCExchangeTwoHallDining)message).getNicknamePlayer()) &&
                        this.checkMultipleStudentColour(((MessageCCExchangeTwoHallDining)message).getColoursHall()) &&
                        this.checkMultipleStudentColour(((MessageCCExchangeTwoHallDining)message).getColoursDiningRoom()));
        }

        return false;

    }

    private boolean checkNickname(String nickname){
        return !(nickname.equals(""));
    }

    //This checks ONLY the format of the word
    private boolean checkPlayerColour(String c){
        String[] possibleColours = {"white", "black", "gray"};
        for(String s : possibleColours){
            if(c.toLowerCase().equals(s)){return true;}
        }
        return false;
    }

    private boolean checkMotherNatureMovement(int m){
        return (m > 1 && m <= 6);
    }

    private boolean checkTurnPriority(int p){
        return (p >= 1 && p <= 10);
    }

    // Check if the colour in the message (a String) is possible
    private boolean checkStudentColour(String c){
        String[] possibleColours = {"red", "pink", "blue", "yellow", "green"};
        for(String s : possibleColours){
            if(c.toLowerCase().equals(s)){return true;}
        }
        return false;
    }

    private boolean checkMultipleStudentColour(List<String> colours){
        for(String c : colours){
            if(!this.checkStudentColour(c) || c.equals("-")){return false;} //I accept "-" for Character Cards
        }
        return true;
    }

    // Check if the destination Archipelago is possible
    private boolean checkDestArchipelagoIndex(int i){
        return i <= MAX_NUM_ARCHIPELAGOS;
    }

    private boolean checkCloudIndex(int i){
        return (i >= 0 && i <=4);
    }

    private boolean checkIndexCard(int i){
        return (i>=0 && i <= 2);
    }
}