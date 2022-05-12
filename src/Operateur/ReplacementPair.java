package Operateur;

import Solution.Sequence;
import instance.network.Pair;

public class ReplacementPair extends Operator {

    private int position;
    private Pair pairToReplace;

    public ReplacementPair() {
        super();
    }

    public ReplacementPair(Sequence sequence, Pair pairToReplace, int position) {
        super(sequence);
        this.position = position;
        this.pairToReplace = pairToReplace;
        this.deltaCoutOperation = this.evalDeltaCout();
    }

    public Pair getPairToReplace() {
        return pairToReplace;
    }

    public int getPosition() {
        return position;
    }

    @Override
    protected int evalDeltaCout() {
        if(processedSequence != null)
            return this.processedSequence.deltaCoutReplacement(position,pairToReplace);
        return 0;
    }

    @Override
    protected boolean doMouvement() {
        return this.processedSequence.doReplacement(this);
    }
}
