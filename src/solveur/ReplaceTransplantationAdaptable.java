package solveur;

import instance.network.Base;
import instance.network.Pair;
import operateur.ReplacementPair;
import solution.Sequence;
import solution.Solution;

import java.util.LinkedList;

public class ReplaceTransplantationAdaptable {

    public void localReplacement(LinkedList<Pair> lastPairs, Solution solution) {
        LinkedList<Pair> copyPair = new LinkedList<>(lastPairs);

        initCopyPair(solution, copyPair);
        while(!copyPair.isEmpty()) {
            ReplacementPair insMeilleur;
            insMeilleur = getMeilleurOperateurReplacement(solution, copyPair);
            if(insMeilleur.getDeltaCout() < Integer.MAX_VALUE) {
                solution.doReplacement((insMeilleur));
                copyPair.remove(insMeilleur.getPairToReplace());
            } else {
                copyPair.remove(0);
            }
        }
    }


    private ReplacementPair getMeilleurOperateurReplacement(Solution s, LinkedList<Pair> pairs) {
        ReplacementPair insMeilleur = s.getMeilleureReplacement(pairs.getFirst());

        ReplacementPair insActu;
        for(Pair pair : pairs) {
            insActu = s.getMeilleureReplacement(pair);
            if(insActu.getDeltaCout() > insMeilleur.getDeltaCout() && (insActu.getDeltaCout() != Integer.MAX_VALUE && insActu.getDeltaCout() != Integer.MIN_VALUE))
                insMeilleur = insActu;
        }
        return insMeilleur;
    }

    private void initCopyPair(Solution s, LinkedList<Pair> copyPair) {
        for(Sequence seq : s.getCycles()) {
            for(Base b : seq.getSequence()) {
                copyPair.remove(b);
            }
        }
        for(Sequence seq : s.getChains()) {
            for(Base b : seq.getSequence()) {
                copyPair.remove(b);
            }
        }
    }
}
