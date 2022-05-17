package operateur;

import solution.Sequence;

public abstract class Operator {

    protected Sequence processedSequence ;
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

    public void setProcessedSequence(Sequence processedSequence) {
        this.processedSequence = processedSequence;
    }

    public boolean doMouvementIfRealisable() {
        if(!this.isMouvementRealisable()) return false;
        this.doMouvement();
        return true;
    }

    public boolean isMouvementRealisable() {
        if(getDeltaCout() < Integer.MAX_VALUE && getDeltaCout() > 0) //avant Integer.MIN_VALUE
            return true;
        return false;
    }

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
