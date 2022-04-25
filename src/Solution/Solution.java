package Solution;

import instance.Instance;
import instance.network.Chain;
import instance.network.Cycle;
import instance.network.Pair;

import java.util.LinkedList;
import java.util.List;
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
        if(c.addPairCycle(pair)) {
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
            if(c.addPairCycle(pair)) {
                this.gainMedTotal += (c.getGainMedSequence() - deltaGain);
                return true;
            }
        }

        return false;
    }

    /**
     * EN TRAVAUX
     */
    private void solutionInstance() {
        Cycle c = new Cycle();
        boolean status = false;
        int i =0;

        for(Map.Entry mapentry : this.instance.getPairs().entrySet()) {
            if(this.cycles.get(i).getSequence().size() <= 2) {
                status = this.addPairExistingCycle((Pair)mapentry.getValue());
                if(status == true) break;
            }
        }
    }



    public static void main(String[] args) {

    }

}

