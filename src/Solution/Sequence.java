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
        this.sequence = new LinkedList<>();
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
        if( position < isAltruist || position > this.sequence.size())
                return false;
        return true;
    }

    public int deltaCoutInsertion(int position, Pair pairToAdd) {
        if(!isPositionInsertionValide(position) || pairToAdd == null)
            return Integer.MAX_VALUE;

        int deltaCout = Integer.MAX_VALUE;
        if(this.sequence.isEmpty()) return deltaCout;

        Pair pCurrent;
        Base bPrec;

        if(this.sequence.size() == position) {
            if(this.sequence.getFirst() instanceof Altruist) // dernière position d'une chaine
            {
                bPrec = this.sequence.getLast();
                deltaCout = bPrec.getGainVers(pairToAdd);
                return deltaCout;
            }
            else {
                pCurrent = (Pair) this.sequence.getFirst(); // dernière position d'un cycle
            }
        }
        else {
            pCurrent = (Pair) this.sequence.get(position); // tout sauf dernière position
        }

        if(this.sequence.getFirst() instanceof Pair && position == 0) {
            bPrec = this.sequence.getLast(); // 1ere position d'un cycle
        } else {
            bPrec = this.sequence.get(position-1); // 1ere position d'une chaine
        }

        if(!bPrec.isCompatible(pairToAdd) || !pairToAdd.isCompatible(pCurrent)) return Integer.MAX_VALUE;

        if(this.sequence.size() == 1)
            deltaCout -= bPrec.getGainVers(pCurrent);

        deltaCout += pairToAdd.getGainVers(pCurrent);
        deltaCout += bPrec.getGainVers(pairToAdd);

        return deltaCout;
    }

    public InsertionPair getMeilleureInsertion(Pair pairToInsert) {
        InsertionPair insMeilleur = new InsertionPair();

        InsertionPair insPair;
        for(int position=0 ; position<this.sequence.size()+1 ; position++) {
            if(position != 0 || !(this.sequence.getFirst() instanceof Altruist) ) {
                insPair = new InsertionPair(this, pairToInsert, position);
                if (insPair.isBest(insMeilleur))
                    insMeilleur = insPair;
            }
        }
        return insMeilleur;
    }

    public boolean doInsertion(InsertionPair infos) {
        if(infos == null) return false;
        if(!infos.isMouvementRealisable()) return false;

        Pair pair = infos.getPairToAdd();

        this.gainMedSequence += infos.getDeltaCout();
        this.sequence.add(infos.getPosition(), pair);
/*
        if(!this.check()) {
            System.out.println("Erreur : doInsertion");
            System.out.println(infos);
            System.exit(-1); //termine le programme en cas d'erreur
        }
        */
        return true;
    }


}
