package operateur;

import solution.Sequence;
import instance.network.Pair;

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

    @Override
    protected int evalDeltaCout() {
        if(processedSequence != null)
            return this.processedSequence.deltaCoutInsertion(position, this.pairToAdd);
        return 0;
    }

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
