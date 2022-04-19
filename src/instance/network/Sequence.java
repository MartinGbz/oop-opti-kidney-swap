package instance.network;

import java.util.List;

public abstract class Sequence {

    private int gainMedSequence;
    protected List sequence;

    public Sequence() {
        this.gainMedSequence = 0;
    }

    public abstract int calculGainMed();

}
