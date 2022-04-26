package Solution;

import instance.Instance;
import instance.network.Chain;
import instance.network.Cycle;
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

        for(Map.Entry mapentry : this.instance.getPairs().entrySet()) {
            status = false;
            for(Cycle cycle : this.cycles) {
                if(cycle.getSequence().size() < 2) {
                    status = cycle.addPairToCycle((Pair) mapentry.getValue());
                    if(status) break;
                }
            }
            if(!status) {
                c.addPairToCycle((Pair) mapentry.getValue());
                this.cycles.addLast(c);
                c = new Cycle();
            }
        }
    }

    public static void main(String[] args) {
        try {
            InstanceReader reader = new InstanceReader("instances/testInstance.txt");
            Instance i = reader.readInstance();
            System.out.println("Instance lue avec success !");

            Solution s = new Solution(i);
            s.solutionInstanceWithCycles();

            System.out.println(s);

        }
        catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }

}

