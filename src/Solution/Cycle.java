package Solution;

import instance.network.Pair;

import java.util.LinkedList;

public class Cycle extends Sequence {

    public Cycle() {
        super();
        this.sequence = new LinkedList<Pair>();
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

    @Override
    public String toString() {
        return "Cycle{" +
                "sequence=" + sequence +
                '}';
    }
}
