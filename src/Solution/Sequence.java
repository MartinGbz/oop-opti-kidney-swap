package Solution;

import instance.network.Base;

import java.util.LinkedList;

public abstract class Sequence {

    protected int gainMedSequence;
    protected LinkedList<Base> sequence;

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
