package operateur;

import solution.Sequence;

/**
 * Classe reprsentant l'opérateur
 */
public abstract class Operator {

    protected Sequence processedSequence;
    protected int deltaCoutOperation;

    public Operator() {
        this.deltaCoutOperation = Integer.MAX_VALUE;
    }

    public Operator(Sequence sequence) {
        this();
        this.processedSequence = sequence;
    }

    public int getDeltaCout() {
        return this.deltaCoutOperation;
    }
    public Sequence getProcessedSequence() {
        return processedSequence;
    }

    /**
     * Réalise le mouvement de l'opérateur si réalisable
     * @return état de succès de l'opération de mouvement (boolean)
     */
    public boolean doMouvementIfRealisable() {
        if(!this.isMouvementRealisable()) return false;
        this.doMouvement();
        return true;
    }

    /**
     * Vérifie si le mouvement de l'opérateur est réalisable
     * @return état de la réalisibilité (boolean)
     */
    public boolean isMouvementRealisable() {
        if(getDeltaCout() < Integer.MAX_VALUE && getDeltaCout() > 0) //avant Integer.MIN_VALUE
            return true;
        return false;
    }

    /**
     * Compare 1 opérateur  pour détecter le meilleur
     * @param op Opérateur de comparaison
     * @return l'etat indiquant si l'opérateur en paramètre est le meilleur (boolean)
     */
    public boolean isBest(Operator op) {
        if(op == null) return true;
        if(this.getDeltaCout() >= Integer.MAX_VALUE) return false;
        if(this.getDeltaCout() > op.getDeltaCout() || op.getDeltaCout() >= Integer.MAX_VALUE) return true;
        return false;
    }

    protected abstract int evalDeltaCout();
    protected abstract boolean doMouvement();

    @Override
    public String toString() {
        return "Operator{" +
                "Sequence=" + processedSequence +
                ", \ndeltaCoutOperation=" + deltaCoutOperation +
                '}';
    }
}
