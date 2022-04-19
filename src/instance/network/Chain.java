package instance.network;

import java.util.LinkedList;

public class Chain extends Sequence {

    public Chain() {
        super();
        this.sequence = new LinkedList<Base>();
    }

    @Override
    public int calculGainMed() {
        return 0;
    }
}
