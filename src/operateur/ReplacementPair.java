package operateur;

import solution.Sequence;
import instance.network.Pair;

/**
 * Objet regroupant les données et méthodes nécessaires pour remplacer une paire
 * à une position données dans une séquence
 */
public class ReplacementPair extends Operator {

    private int position;
    private Pair pairToReplace;

    //Constructor
    public ReplacementPair() {
        super();
    }

    public ReplacementPair(Sequence sequence, Pair pairToReplace, int position) {
        super(sequence);
        this.position = position;
        this.pairToReplace = pairToReplace;
        this.deltaCoutOperation = this.evalDeltaCout();
    }

    //Getters
    public Pair getPairToReplace() {
        return pairToReplace;
    }
    public int getPosition() {
        return position;
    }

    /**
     *  Calcul du delta cout du cycle/chaine après remplacement
     *  @return delta cout du cycle après remplacement
     */
    @Override
    protected int evalDeltaCout() {
        if(processedSequence != null)
            return this.processedSequence.deltaCoutReplacement(position,pairToReplace);
        return 0;
    }

    /**
     * Opération de remplacement dans un cycle/chaine
     * @return état du succès du remplacement (boolean)
     */
    @Override
    protected boolean doMouvement() {
        return this.processedSequence.doReplacement(this);
    }
}
