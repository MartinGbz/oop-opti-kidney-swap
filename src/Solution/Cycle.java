package Solution;

import instance.network.Altruist;
import instance.network.Base;
import instance.network.Pair;

import java.util.LinkedList;

public class Cycle extends Sequence {

    public Cycle() {
        super();
        this.sequence = new LinkedList<>();
    }

    /**
     * Ajoute une paire dans un cycle (si le cycle en cours n'est pas vide = ajout si compatible,
     * sinon le cycle est vide, ajout dans un nouveau cycle)
     * @param pairToAdd
     * @return
     * @note !WARNING! : NE MARCHE QU'AVEC DES CYCLES DE TAILLE 2
     */
    public boolean addPairToCycle(Pair pairToAdd) {
        if(pairToAdd == null) return false;
        if(!pairToAdd.asCompatibility()) return false;

        if(!this.sequence.isEmpty()) {
            Pair p = (Pair) this.sequence.getLast();
            if(!p.isCompatible(pairToAdd) || !pairToAdd.isCompatible(p)) return false;
        }
        this.sequence.addLast(pairToAdd);
        return true;
    }

    @Override
    public int calculGainMed() {
        return 0;
    }

    public boolean check(int maxSize) {
        if(this.sequence.isEmpty()) return false;
        int sizeSeq = this.sequence.size();
        if(sizeSeq > maxSize || sizeSeq == 1) {
            System.out.println("Check Cycle False (cycle trop grande ou de taille 1)");
            return false;
        }
        // if(sizeSeq == 1) return true; // si on considère qu'un cycle avec 1 paire est valide
        // sinon ajouter une condition dans le "if" du dessus

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
