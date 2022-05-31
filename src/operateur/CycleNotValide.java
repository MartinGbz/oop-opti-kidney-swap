package operateur;

import operateur.Operator;
import solution.Sequence;
import instance.network.Pair;

public class CycleNotValide extends Operator {

    private Pair pairToAdd;

    public CycleNotValide() {
        super();
    }

    public CycleNotValide(Pair pairToAdd) {
        this();
        this.pairToAdd = pairToAdd;
    }

    public CycleNotValide(Sequence sequence, Pair pairToAdd) {
        super(sequence);
        this.pairToAdd = pairToAdd;
        this.deltaCoutOperation = this.evalDeltaCout();
    }

    public Pair getPairToAdd() {
        return pairToAdd;
    }

    @Override
    protected int evalDeltaCout() {
        if(processedSequence != null)
            return this.processedSequence.deltaCoutInsertionInvalideCycle(this.pairToAdd);
        return 0;
    }

    @Override
    protected boolean doMouvement() {
        return this.processedSequence.doInsertionEnd(this);
    }
}
