package solution;

import instance.network.Base;
import instance.network.Pair;

//ceci est un commentaire

public class Cycle extends Sequence {

    public Cycle() {
        super();
    }

    public Cycle(Cycle c) {
        super(c.sequence,c.gainMedSequence);
    }
    /**
     * Ajoute une paire dans un cycle (si le cycle en cours n'est pas vide = ajout si compatible,
     * sinon le cycle est vide, ajout dans un nouveau cycle)
     */
    public boolean addPairToCycleEnd(Pair pairToAdd) {
        if(pairToAdd == null) return false;
        if(!pairToAdd.asCompatibility()) return false;

        int delta, pLastToPairToAdd, pairToAddToPFirst;

        if(!this.sequence.isEmpty()) {
            Pair pLast = (Pair) this.sequence.getLast();
            Pair pFirst = (Pair) this.sequence.getFirst();

            if(!pLast.isCompatible(pairToAdd) || !pairToAdd.isCompatible(pFirst)) return false;

            pLastToPairToAdd = pLast.getTransplantations().get(pairToAdd).getMedicalGain();
            pairToAddToPFirst = pairToAdd.getTransplantations().get(pFirst).getMedicalGain();
            int pLastToPFirst = 0;
            if(!pLast.equals(pFirst)) {
                pLastToPFirst = pLast.getTransplantations().get(pFirst).getMedicalGain();
            }
            delta = (pLastToPairToAdd + pairToAddToPFirst) - pLastToPFirst;
            System.out.println("gain ajouté cycle <" + delta + ">");
            this.gainMedSequence += delta;
        }
        this.sequence.addLast(pairToAdd);
        return true;
    }

    public boolean addPairToInvalideCycle(Pair pairToAdd) {
        if(pairToAdd == null) return false;
        if(!pairToAdd.asCompatibility()) return false;
        int delta;
        if(!this.sequence.isEmpty()) {
            Pair pLast = (Pair) this.sequence.getLast();
            if(!pLast.isCompatible(pairToAdd)) return false;
            delta = pLast.getGainVers(pairToAdd);
            this.gainMedSequence += delta;
        }
        this.sequence.addLast(pairToAdd);
        return true;
    }

    public boolean standardisation(int maxSizeCycle) {
        int posBegin, posEnd = maxSizeCycle-1;
        int sizeCycle = this.sequence.size(), sizeValideCycle=maxSizeCycle;
        Cycle c = new Cycle(this);
        Cycle cSol = new Cycle();
        Base bBegin, bEnd;

        if(sizeCycle < maxSizeCycle) {
            sizeValideCycle=sizeCycle;
            posEnd=sizeCycle-1;
        }

        while(sizeValideCycle != 1) {
            for(posBegin=0 ; posBegin < sizeCycle ; posBegin++) {
                if(posEnd == sizeCycle) posEnd=0;

                bBegin = c.sequence.get(posBegin);
                bEnd = c.sequence.get(posEnd);

                if(bEnd.isCompatible(bBegin)) {
                    Cycle cBis = new Cycle(c);
                    int posBefore, posAfter;
                    if(posBegin < posEnd) {
                        posBefore = posBegin;
                        posAfter = posEnd;
                    } else {
                        posBefore = posEnd;
                        posAfter = posBegin;
                    }
                    if(posAfter != sizeCycle-1)
                        for(int i=sizeCycle-1 ; i > posAfter ; i--)
                            cBis.sequence.remove(i);
                    if(posBefore !=0)
                        for(int i=0 ; i < posBefore ; i++)
                            cBis.sequence.removeFirst();

                    if(cSol.calculGainMed() < cBis.calculGainMed()) {
                        cSol = cBis;
                        cSol.gainMedSequence = cBis.calculGainMed();
                    }
                }
                posEnd++;
            }
            sizeValideCycle--;
        }
        if(cSol.gainMedSequence != 0 && cSol.gainMedSequence < Integer.MAX_VALUE) {
            this.sequence = cSol.sequence;
            this.gainMedSequence = cSol.gainMedSequence;
            return true;
        }
        return false;
    }

    @Override
    public int calculGainMed() {
        if(this.sequence.size() < 2) return 0;

        int gainMed=0;
        Pair pCurrent, pNext;

        int posCurrent=0, posNext=1;
        for(posCurrent=0 ; posCurrent < this.sequence.size() ; posCurrent++) {
            if(posNext == this.sequence.size()) {
                posNext = 0;
            }
            pCurrent = (Pair) this.sequence.get(posCurrent);
            pNext = (Pair) this.sequence.get(posNext);
            gainMed += pCurrent.getGainVers(pNext);
            posNext++;
        }
        return gainMed;
    }

    public boolean check(int maxSize) {
        if(this.sequence.isEmpty()) return false;
        int sizeSeq = this.sequence.size();
        if(sizeSeq > maxSize || sizeSeq == 1) {
            System.out.println("Check Cycle False (cycle trop grande ou de taille 1)");
            return false;
        }
        for(int i=0; i<sizeSeq-1; i++) {
            if(!this.sequence.get(i).isCompatible(this.sequence.get(i+1))) {
                System.out.println("Check Cycle False (un des donneurs ne peut pas donner au suivant)");
                return false;
            }
        }
        if(!this.sequence.get(sizeSeq-1).isCompatible(this.sequence.get(0))) {
            System.out.println("Check Cycle False (le dernier donneur ne peut pas donner au 1er patient)");
            return false;
        }
        return true;
    }
}
