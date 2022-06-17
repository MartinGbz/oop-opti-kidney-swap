package solveur;

import instance.network.Base;
import instance.network.Pair;
import operateur.CycleNotValide;
import solution.Cycle;
import solution.Solution;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public class NotValidCycle {

    public Solution creatCycle(LinkedList<Pair> lastPairs, Solution solution) {
        boolean endLoop;
        int notFirstLoop, sizeSol =0, maxLoop;
        Random r = new Random(0);
        Solution solBase = new Solution(solution);
        Solution s;
        Solution ss = new Solution(solution);
        if(solution.getInstance().getPairs().size() < 51) maxLoop = 2000;
        else if(solution.getInstance().getPairs().size() < 101) maxLoop = 1000;
        else maxLoop = 250;

        for(int k=0 ; k<maxLoop ; k++) {
            notFirstLoop = 0;
            s = new Solution(solBase);
            endLoop = true;
            while (endLoop) {
                LinkedList<Pair> copyPair = new LinkedList<>(lastPairs);
                if (notFirstLoop != 0) {
                    for (Cycle c : s.getCycles()) {
                        for (Base b : c.getSequence()) {
                            copyPair.remove(b);
                        }
                    }
                }
                Collections.shuffle(copyPair,r);
                for (int j = 0; j < 2; j++) {
                    while (!copyPair.isEmpty()) {
                        traitment(s, copyPair);
                    }
                    copyPair = s.recoverCyclesOfOne();
                }
                standardisation(s);
                s.deleteSequenceNotUsed();
                if (sizeSol < s.getCycles().size()) {
                    sizeSol = s.getCycles().size();
                } else {
                    endLoop = false;
                }
                notFirstLoop = 1;
            }
            s.calculGainSolution();
            if(s.getGainMedTotal() > ss.getGainMedTotal() && s.getGainMedTotal() < Integer.MAX_VALUE) {
                ss = new Solution(s);
            }
        }
        return ss;
    }

    private void traitment(Solution s, LinkedList<Pair> copyPair) {
        CycleNotValide insMeilleur;
        insMeilleur = getMeilleurCycleNotValide(s, copyPair);

        if(s.doAddLast(insMeilleur)) {
            copyPair.remove(insMeilleur.getPairToAdd());
        }
        else {
            s.addPairNewCycle(copyPair.getFirst());
            copyPair.removeFirst();
        }
    }

    private boolean standardisation(Solution s) {
        boolean addCycle = false;
        for(Cycle c : s.getCycles()) {
            if(c.standardisation(s.getInstance().getMaxSizeCycle())) {
                addCycle = true;
            }
        }
        return addCycle;
    }

    private CycleNotValide getMeilleurCycleNotValide(Solution s, LinkedList<Pair> pairs) {
        CycleNotValide insMeilleur = s.getMeilleurCycleNotValide(pairs.getFirst());

        CycleNotValide insActu;
        for(Pair pair : pairs) {
            insActu = s.getMeilleurCycleNotValide(pair);
            if(insActu.getDeltaCout() > insMeilleur.getDeltaCout() && (insActu.getDeltaCout() != Integer.MAX_VALUE && insActu.getDeltaCout() != Integer.MIN_VALUE))
                insMeilleur = insActu;
        }
        return insMeilleur;
    }

}
