package solution;

import instance.network.Altruist;
import instance.network.Base;
import instance.network.Pair;

public class Chain extends Sequence {

    public Chain() {
        super();
    }

    public Chain(Altruist altruist) {
        this();
        this.sequence.addFirst(altruist);
    }

    public boolean addPairToChain(Pair pairToAdd) {
        if(pairToAdd == null) return false;
        Base b = this.sequence.getLast();

        if(!b.isCompatible(pairToAdd)) return false;

        this.sequence.addLast(pairToAdd);

        int delta = b.getTransplantations().get(pairToAdd).getMedicalGain();
        this.gainMedSequence += delta;

        return true;
    }

    @Override
    public int calculGainMed() {
        return 0;
    }

    public boolean check(int maxSize) {
        if(this.sequence.isEmpty()) return false;
        if(!(this.sequence.getFirst() instanceof Altruist)) return false;
        int sizeSeq = this.sequence.size();
        if(sizeSeq > maxSize || sizeSeq == 1) {
            System.out.println("Check Chain False (chaine trop grande ou de taille 1)");
            return false;
        }
        for(int i=0; i<sizeSeq-1; i++) {
            if(!this.sequence.get(i).isCompatible(this.sequence.get(i+1))) {
                System.out.println("Check Chain False (un des donneurs ne peut pas donner au suivant)");
                return false;
            }
        }
        return true;
    }

}
