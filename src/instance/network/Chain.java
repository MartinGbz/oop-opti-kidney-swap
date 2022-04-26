package instance.network;

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
        //a mettre dans un checker
        //if(!(this.sequence.getFirst() instanceof Altruist)) return false;
        if(!b.isCompatible(pairToAdd)) return false;
        this.sequence.addLast(pairToAdd);
        return true;
    }

    @Override
    public int calculGainMed() {
        return 0;
    }
}
