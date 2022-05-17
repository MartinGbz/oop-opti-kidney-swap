package solution;

import operateur.InsertionPair;
import operateur.ReplacementPair;
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
    public LinkedList<Base> getSequence() {
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

    private boolean isPositionValideInsertion(int position) {
        int isAltruist = 0;
        if(this.sequence.getFirst() instanceof Altruist)
            isAltruist = 1;
        if( position < isAltruist || position > this.sequence.size())
                return false;
        return true;
    }

    private boolean isPositionValideReplacement(int position) {
        int isAltruist = 0;
        if(this.sequence.getFirst() instanceof Altruist)
            isAltruist = 1;
        if( position < isAltruist || position >= this.sequence.size())
            return false;
        return true;
    }

    public int deltaCoutReplacement(int position, Pair pairToReplace) {
        System.out.println("I am HERE");
        if(!isPositionValideReplacement(position) || pairToReplace == null)
            return Integer.MAX_VALUE;

        int deltaCout = 0;
        if(this.sequence.isEmpty()) return Integer.MAX_VALUE;

        Base bCurrent = this.sequence.get(position);
        Base bPrec;
        Base bNext;

        if(this.sequence.size()-1 == position) {
            if(this.sequence.getFirst() instanceof Altruist) // dernière position d'une chaine
            {
                bPrec = this.sequence.get(position-1);
                if(bPrec.isCompatible(pairToReplace)) {
                    int oldWay = bPrec.getGainVers((Pair)this.sequence.getLast());
                    int newWay = bPrec.getGainVers(pairToReplace);
                    deltaCout = newWay - oldWay;
                    return deltaCout;
                }
                return Integer.MAX_VALUE; // si deltaCout > 0 => AMELIORANT
            }
            else {
                //bPrec = this.sequence.get(position-1);
                //bCurrent = this.sequence.getLast();
                bNext = this.sequence.getFirst();
            }
        } else {
            //bPrec = this.sequence.get(position-1);
            //bCurrent = this.sequence.get(position);
            bNext = this.sequence.get(position+1);
        }

        if(this.sequence.getFirst() instanceof Pair && position == 0) {
            bPrec = this.sequence.getLast();
        } else {
            bPrec = this.sequence.get(position-1);
        }

        if(!bPrec.isCompatible(pairToReplace) || !pairToReplace.isCompatible(bNext)) return Integer.MAX_VALUE;

        int oldWay = bPrec.getGainVers((Pair)bCurrent) + bCurrent.getGainVers((Pair)bNext);
        int newWay = bPrec.getGainVers(pairToReplace) + pairToReplace.getGainVers((Pair)bNext);
        deltaCout = newWay - oldWay;

        return deltaCout; // si deltaCout > 0 => AMELIORANT
    }

    public int deltaCoutInsertion(int position, Pair pairToAdd) {
        if(!isPositionValideInsertion(position) || pairToAdd == null)
            return Integer.MAX_VALUE;

        int deltaCout = 0;
        if(this.sequence.isEmpty()) return Integer.MAX_VALUE;

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
            bPrec = this.sequence.get(position-1);
        }

        if(!bPrec.isCompatible(pairToAdd) || !pairToAdd.isCompatible(pCurrent)) return Integer.MAX_VALUE;

        if(this.sequence.size() != 1)
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

    public ReplacementPair getMeilleureReplacement(Pair pairToReplace) {
        ReplacementPair insMeilleur = new ReplacementPair();

        ReplacementPair insPair;
        for(int position=0 ; position<this.sequence.size()+1 ; position++) {
            if(position != 0 || !(this.sequence.getFirst() instanceof Altruist) ) {
                insPair = new ReplacementPair(this, pairToReplace, position);
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

    public boolean doReplacement(ReplacementPair infos) {
        if(infos == null) return false;
        if(!infos.isMouvementRealisable()) return false;

        Pair pair = infos.getPairToReplace();

        this.gainMedSequence += infos.getDeltaCout();
        //this.sequence.remove(infos.getPosition());
        //this.sequence.add(infos.getPosition(), pair);
        this.sequence.set(infos.getPosition(), pair);

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
