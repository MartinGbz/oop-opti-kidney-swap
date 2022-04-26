package Solution;

import instance.Instance;
import instance.network.Altruist;
import instance.network.Pair;
import io.InstanceReader;
import io.exception.ReaderException;

import java.util.LinkedList;
import java.util.Map;

public class Solution {

    int gainMedTotal;
    private Instance instance;
    private LinkedList<Cycle> cycles;
    private LinkedList<Chain> chains;

    public Solution(Instance instance) {
        this.gainMedTotal = 0;
        this.instance = instance;
        this.cycles = new LinkedList<>();
        this.chains = new LinkedList<>();
    }

    @Override
    public String toString() {
        return "Solution{" +
                "gainMedTotal=" + gainMedTotal +
                ", instance=" + instance +
                ", cycles=" + cycles +
                ", chains=" + chains +
                '}';
    }

    /**
     * Ajoute une pair dans un nouveau cycle
     * @param pair
     * @return
     */
    public boolean addPairNewCycle(Pair pair) {
        Cycle c = new Cycle();
        if(c.addPairToCycle(pair)) {
            this.cycles.addLast(c);
            this.gainMedTotal += c.getGainMedSequence();
            return true;
        }
        return false;
    }


    /**
     * Ajoute une pair dans un cycle existant
     * @param pair
     * @return
     */
    public boolean addPairExistingCycle(Pair pair) {
        int deltaGain = 0;
        for(Cycle c : this.cycles) {
            deltaGain = c.getGainMedSequence();
            if(c.addPairToCycle(pair)) {
                this.gainMedTotal += (c.getGainMedSequence() - deltaGain);
                return true;
            }
        }

        return false;
    }

    /**
     * EN TRAVAUX
     */
    private void solutionInstanceWithCycles() {
        Cycle c = new Cycle();
        boolean status;

        for(Map.Entry pairEntry : this.instance.getPairs().entrySet()) {
            status = false;
            Pair p = (Pair) pairEntry.getValue();
            if(!isUsedInChain(p)) {
                for(Cycle cycle : this.cycles) {
                    if(cycle.getSequence().size() < 2) {
                        status = cycle.addPairToCycle(p);
                        if(status) break;
                    }
                }
                if(!status) {
                    this.addPairNewCycle(p);
                }
            }
        }
    }

    public boolean isUsedInChain(Pair pair) {
        for(Chain c : this.chains) {
            if(c.getSequence().contains(pair)) {
                return true;
            }
        }
        return false;
    }

    private void solutionInstanceWithChain() {
        boolean status = false;
        for(Map.Entry altruistEntry : this.instance.getAltruists().entrySet()) {
            Chain c = new Chain((Altruist) altruistEntry.getValue());
            this.chains.addLast(c);
        }
        for(Map.Entry pairEntry : this.instance.getPairs().entrySet()) {
            status = false;
            for(Chain chain : this.chains) {
                status = chain.addPairToChain((Pair) pairEntry.getValue());
                if(status) break;
            }
        }
    }

    private void SolutionInstance() {
        this.solutionInstanceWithChain();
        this.solutionInstanceWithCycles();
    }

    public static void main(String[] args) {
        try {
            InstanceReader reader = new InstanceReader("instances/testInstance.txt");
            Instance i = reader.readInstance();
            System.out.println("Instance lue avec success !");

            Solution s = new Solution(i);
            //s.solutionInstanceWithCycles();
            //s.solutionInstanceWithChain();
            s.SolutionInstance();
            System.out.println(s);

        }
        catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }

}

