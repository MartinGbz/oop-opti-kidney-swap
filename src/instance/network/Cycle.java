package instance.network;

import java.util.LinkedList;

public class Cycle extends Sequence {

    public Cycle() {
        super();
        this.sequence = new LinkedList<Pair>();
    }

    @Override
    public int calculGainMed() {
        return 0;
    }
}
