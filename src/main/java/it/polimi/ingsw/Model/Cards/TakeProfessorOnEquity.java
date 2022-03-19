package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Places.School;

import java.util.List;

public class TakeProfessorOnEquity extends AbstractCharacterCard{
    public  TakeProfessorOnEquity(){
        super(2);
    }
    public void useEffect(List<School> schools){
        updatePrice();

    }
}
