package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Messages.INMessages.Message;

import java.io.Serializable;
import java.util.List;

/**
 * Class that performs input checking. It checks if the message received from the client is valid int it's input form.
 */
public class ControllerInput implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int MAX_NUM_ARCHIPELAGOS = 12;


    public ControllerInput(){}

    /**
     * Call the right method to check the message received
     * @param arg message received
     * @return true if the format is ok, false other-ways
     */
    public boolean checkFormat(Object arg){
        if(!(arg instanceof Message)){
            return false;
        }

        Message message = (Message)arg;

        return message.checkInput(this);
    }

    /**
     * @param nickname nickname to check
     * @return true if it's not void, false other-ways
     */
    public boolean checkNickname(String nickname){
        return !(nickname.equals("")) && !(nickname.contains("*"));
    }

    /**
     * @param c colour to check
     * @return true if it's one of the given colours, false other-ways
     */
    public boolean checkPlayerColour(String c){
        String[] possibleColours = {"white", "black", "gray"};
        for(String s : possibleColours){
            if(c.toLowerCase().equals(s)){return true;}
        }
        return false;
    }

    /**
     * @param m number of moves requested
     * @return true if the number is possible, false other-ways
     */
    public boolean checkMotherNatureMovement(int m){
        return (m >= 0 && m <= 7);
    }

    /**
     * @param p priority of the AssistantCard played
     * @return true if the number is possible, false other-ways
     */
    public boolean checkTurnPriority(int p){
        return (p >= 1 && p <= 10);
    }

    /**
     * @param c colour to check
     * @return true if it's one of the given colours, false other-ways
     */
    public boolean checkStudentColour(String c){
        String[] possibleColours = {"red", "pink", "blue", "yellow", "green"};
        for(String s : possibleColours){
            if(c.toLowerCase().equals(s)){return true;}
        }
        return false;
    }

    /**
     * @param colours colours to check
     * @return true if all colours are ok in format, false other-ways
     */
    public boolean checkMultipleStudentColour(List<String> colours){
        for(String c : colours){
            if(!(this.checkStudentColour(c) || c.equals("-"))){return false;} //I accept "-" for Character Cards
        }
        //at least one student should be not "-"
        for(String c : colours){
            if(this.checkStudentColour(c)){
                return true;
            }
        }
        return false;
    }

    /**
     * @param i index of the destination archipelago
     * @return true if the index is possible, false other-ways
     */
    public boolean checkDestinationArchipelagoIndex(int i){
        return (i >= 0 && i <= MAX_NUM_ARCHIPELAGOS);
    }

    /**
     * @param i index of the cloud
     * @return true if the index is possible, false other-ways
     */
    public boolean checkCloudIndex(int i){
        return (i >= 0 && i <=3);
    }

    /**
     * @param i index of the CharacterCard
     * @return true if the index is possible, false other-ways
     */
    public boolean checkIndexCard(int i){
        return (i >= 0 && i <= 2);
    }
}