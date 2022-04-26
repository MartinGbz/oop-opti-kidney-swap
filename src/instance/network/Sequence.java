package instance.network;

import java.util.LinkedList;
import java.util.List;

public abstract class Sequence {

    private int gainMedSequence;
    protected LinkedList sequence;

    public Sequence() {
        this.gainMedSequence = 0;
    }

    public int getGainMedSequence() {
        return gainMedSequence;
    }

    public LinkedList getSequence() {
        return sequence;
    }

    public abstract int calculGainMed();

    @Override
    public String toString() {
        return "Sequence{" +
                "gainMedSequence=" + gainMedSequence +
                ", sequence=" + sequence +
                '}';
    }
}
