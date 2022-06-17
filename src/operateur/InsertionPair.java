package operateur;

import solution.Sequence;
import instance.network.Pair;

/**
 * Objet regroupant les données et méthodes nécessaires pour insérer une paire
 * à une position données dans une séquence
 */
public class InsertionPair extends Operator {

    private int position;
    private Pair pairToAdd;

    public InsertionPair() {
        super();
    }

    public InsertionPair(int position, Pair pairToAdd) {
        this();
        this.position = position;
        this.pairToAdd = pairToAdd;
    }

    public InsertionPair(Sequence sequence, Pair pairToAdd, int position) {
        super(sequence);
        this.position = position;
        this.pairToAdd = pairToAdd;
        this.deltaCoutOperation = this.evalDeltaCout();
    }

    public int getPosition() {
        return position;
    }
    public Pair getPairToAdd() {
        return pairToAdd;
    }

    /**
     *  Calcul du delta cout du cycle/chaine après insertion
     *  @return delta cout du cycle invalide
     */
    @Override
    protected int evalDeltaCout() {
        if(processedSequence != null)
            return this.processedSequence.deltaCoutInsertion(position, this.pairToAdd);
        return 0;
    }

    /**
     * Opération d'insertion dans un cycle/chaine
     * @return état du succès de l'insertion (boolean)
     */
    @Override
    protected boolean doMouvement() {
        return this.processedSequence.doInsertion(this);
    }

    @Override
    public String toString() {
        return "InsertionPair{" +
                "position=" + position +
                ", \n\tpairToAdd=" + pairToAdd +
                ", \n\tprocessedSequence=" + processedSequence +
                ", \n\tdeltaCoutOperation=" + deltaCoutOperation +
                '}';
    }
}
