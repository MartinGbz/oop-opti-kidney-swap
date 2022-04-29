package Solution;

import instance.network.Altruist;
import instance.network.Base;
import instance.network.Pair;

import java.util.LinkedList;

public class Chain extends Sequence {

    public Chain() {
        super();
        this.sequence = new LinkedList<Base>();
    }

    public Chain(Altruist altruist) {
        this();
        this.sequence.addFirst(altruist);
    }

    public boolean addPairToChain(Pair pairToAdd) {
        if(pairToAdd == null) return false;
        Base b = (Base) this.sequence.getLast();

        if(!b.isCompatible(pairToAdd)) return false;

        this.sequence.addLast(pairToAdd);

        int delta = b.getTransplantations().get(pairToAdd).getMedicalGain();
        System.out.println("gain ajouté chain <" + delta + ">");
        this.gainMedSequence += delta;

        return true;
    }

    @Override
    public int calculGainMed() {
        return 0;
    }

    public boolean check() {
        if(this.sequence.isEmpty()) return false;
        if(!(this.sequence.getFirst() instanceof Altruist)) return false;

        // if(this.sequence.size() == 1) return true;
        /*for(Object base : this.sequence) {

        }*/
        return true;
    }

}
