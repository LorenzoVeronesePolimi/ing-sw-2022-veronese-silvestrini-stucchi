package it.polimi.ingsw.Model.Pawns;

public class MotherNature { //Singleton
    private static MotherNature instance;

    private MotherNature() { }

    public static MotherNature instance() {
        if (instance == null) {
            instance = new MotherNature();
        }

         return instance;
    }
}
