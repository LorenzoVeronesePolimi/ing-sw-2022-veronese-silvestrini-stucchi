package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Messages.INMessage.Message;

import java.util.List;


public class ControllerInput {
    private static final int MAX_NUM_ARCHIPELAGOS = 12;


    public ControllerInput(){}

    public boolean checkFormat(Object arg){
        if(!(arg instanceof Message)){
            return false;
        }

        Message message = (Message)arg;

        return message.checkInput(this);
    }

    public boolean checkNickname(String nickname){
        return !(nickname.equals(""));
    }

    //This checks ONLY the format of the word
    public boolean checkPlayerColour(String c){
        String[] possibleColours = {"white", "black", "gray"};
        for(String s : possibleColours){
            if(c.toLowerCase().equals(s)){return true;}
        }
        return false;
    }

    public boolean checkMotherNatureMovement(int m){
        return (m >= 0 && m <= 7);
    }

    public boolean checkTurnPriority(int p){
        return (p >= 1 && p <= 10);
    }

    // Check if the colour in the message (a String) is possible
    public boolean checkStudentColour(String c){
        String[] possibleColours = {"red", "pink", "blue", "yellow", "green"};
        for(String s : possibleColours){
            if(c.toLowerCase().equals(s)){return true;}
        }
        return false;
    }

    public boolean checkMultipleStudentColour(List<String> colours){
        for(String c : colours){
            if(!(this.checkStudentColour(c) || c.equals("-"))){return false;} //I accept "-" for Character Cards
        }
        return true;
    }

    // Check if the destination Archipelago is possible
    public boolean checkDestinationArchipelagoIndex(int i){
        return (i >= 0 && i <= MAX_NUM_ARCHIPELAGOS);
    }

    public boolean checkCloudIndex(int i){
        return (i >= 0 && i <=3);
    }

    public boolean checkIndexCard(int i){
        return (i >= 0 && i <= 2);
    }
}