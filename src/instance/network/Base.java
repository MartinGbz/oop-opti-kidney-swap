package instance.network;

import java.util.HashMap;
import java.util.Map;

public abstract class Base {
    int id;
    private Map<Pair, Transplantation> transplantations;

    public Base(int id) {
        this.id = id;
        this.transplantations = new HashMap<>();
    }

    public void addTransplantation(Pair patient, int gain) {
        //TODO : à dé commenter si souhait de retirer les gains -1 dans les transplantations
        //if(gain != -1) {
            Transplantation t = new Transplantation(gain, this, patient);
            this.transplantations.put(patient, t);
        //}
    }

    public int getId() {
        return id;
    }
    public Map<Pair, Transplantation> getTransplantations() {
        return transplantations;
    }

}
