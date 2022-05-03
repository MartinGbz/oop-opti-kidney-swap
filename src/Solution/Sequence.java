package Solution;

import Operateur.InsertionPair;
import instance.network.Altruist;
import instance.network.Base;
import instance.network.Pair;
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

    private boolean isPositionInsertionValide(int position) {
        int isAltruist = 0;
        if(this.sequence.getFirst() instanceof Altruist)
            isAltruist = 1;
        if( position < 0+isAltruist || position > this.sequence.size()) //pb
                return false;
        return true;
    }

    public int deltaCoutInsertion(int position, Pair pairToAdd) {
        if(!isPositionInsertionValide(position) || pairToAdd == null)
            return Integer.MIN_VALUE;

        int deltaCout = Integer.MIN_VALUE;

        if(!this.sequence.isEmpty()) {
            Pair pCurrent = (Pair) this.sequence.get(position);
            Base bPrec;

            if(this.sequence.getFirst() instanceof Pair && position ==0) {
                bPrec = (Base) this.sequence.getLast();
            } else {
                bPrec = (Base) this.sequence.get(position-1);
            }

            if(!bPrec.isCompatible(pairToAdd) || !pairToAdd.isCompatible(pCurrent)) return Integer.MIN_VALUE;

            deltaCout -= bPrec.getGainVers(pCurrent);
            deltaCout += pairToAdd.getGainVers(pCurrent);
            deltaCout += bPrec.getGainVers(pairToAdd);
        }
        return deltaCout;
    }

    public InsertionPair getMeilleureInsertion(Pair pairToInsert) {
        InsertionPair insMeilleur = new InsertionPair();

        //if(!this.isClientAddedByDemande(pairToInsert)) return insMeilleur; //défaut
        System.out.println("Sequence - getMeilleureInsertion - for");
        InsertionPair insPair;
        for(int position=0 ; position<this.sequence.size()+1 ; position++) {
            if(position != 0 && !(this.sequence.getFirst() instanceof Altruist) ) {
                insPair = new InsertionPair(this, pairToInsert, position);
                if (insPair.isBest(insMeilleur))
                    insMeilleur = insPair;
            }
        }
        return insMeilleur;
    }



}
