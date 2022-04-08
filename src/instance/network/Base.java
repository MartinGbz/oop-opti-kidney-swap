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

    public void addTransplantation(Pair pair, int gain) {
        Transplantation t = new Transplantation(gain, this, pair);
        this.transplantations.put(pair, t);
    }

}
