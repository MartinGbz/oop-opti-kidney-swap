package operateur;

import solution.Sequence;
import instance.network.Pair;

/**
 * Classe représentant les cycles invalides ce sont des cycles ne rebouclant pas sur le premier élément
 */
public class CycleNotValide extends Operator {

    private Pair pairToAdd;

    public CycleNotValide() {
        super();
    }
    public CycleNotValide(Sequence sequence, Pair pairToAdd) {
        super(sequence);
        this.pairToAdd = pairToAdd;
        this.deltaCoutOperation = this.evalDeltaCout();
    }

    public Pair getPairToAdd() {
        return pairToAdd;
    }

    /**
     * Calcul du delta cout du cycle invalide
     * @return delta cout du cycle invalide
     */
    @Override
    protected int evalDeltaCout() {
        if(processedSequence != null)
            return this.processedSequence.deltaCoutInsertionInvalideCycle(this.pairToAdd);
        return 0;
    }

    /**
     * Opération d'insertion dans un cycle invalide
     * @return état du succès de l'insertion (boolean)
     */
    @Override
    protected boolean doMouvement() {
        return this.processedSequence.doInsertionEnd(this);
    }
}
