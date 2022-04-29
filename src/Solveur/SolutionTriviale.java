package Solveur;

import Solution.*;
import instance.*;
import instance.network.Altruist;
import instance.network.Pair;
import io.InstanceReader;
import io.SolutionWriter;
import io.exception.ReaderException;

import java.util.Map;

public class SolutionTriviale implements Solveur {

    private final String name = "Insersion simple";

    @Override
    public String toString() {
        return "InsertionSimple{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public String getNom() {
        return this.name;
    }

    @Override
    public Solution solve(Instance i) {
        Solution s = new Solution(i);
        boolean status;

        for(Map.Entry altruistEntry : s.getInstance().getAltruists().entrySet()) {
            Chain ch = new Chain((Altruist) altruistEntry.getValue());
            s.getChains().addLast(ch);
        }
        for(Map.Entry pairEntry : s.getInstance().getPairs().entrySet()) {
            status = false;
            for(Chain chain : s.getChains()) {
                if(chain.getSequence().size() < s.getInstance().getMaxSizeChain())
                    status = chain.addPairToChain((Pair) pairEntry.getValue());
                if(status) break;
            }
        }

        status = false;

        for(Map.Entry pairEntry : s.getInstance().getPairs().entrySet()) {
            status = false;
            Pair p = (Pair) pairEntry.getValue();
            if(!isUsedInChain(p,s)) {
                for(Cycle cycle : s.getCycles()) {
                    if(cycle.getSequence().size() < s.getInstance().getMaxSizeCycle()) {
                        status = cycle.addPairToCycle(p);
                        if(status) break;
                    }
                }
                if(!status) {
                    s.addPairNewCycle(p);
                }
            }
        }
        s.calculGainSolution();
        return s;
    }


    public boolean isUsedInChain(Pair pair, Solution solution) {
        for(Chain c : solution.getChains()) {
            if(c.getSequence().contains(pair)) {
                return true;
            }
        }
        return false;
    }

/*
    private void calculGainSolution() {
        for(Cycle cycle : this.cycles) {
            this.gainMedTotal += cycle.getGainMedSequence();
        }
        for(Chain chain : this.chains) {
            this.gainMedTotal += chain.getGainMedSequence();
        }
    }
    */


    public void solveBySolutionTriviale(Instance instance) {
        Solution s = this.solve(instance);
        System.out.println(this);
        System.out.println(s);
        // s.check();
        SolutionWriter sw = new SolutionWriter(s);
    }

    public static void main(String[] args) {
        try {
            InstanceReader reader = new InstanceReader("instances/testInstance.txt"); //mettre le nom du fichier
            Instance instance = reader.readInstance();

            SolutionTriviale is = new SolutionTriviale();
            is.solveBySolutionTriviale(instance);


        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }


}
