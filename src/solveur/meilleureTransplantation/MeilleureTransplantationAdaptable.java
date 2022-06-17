package solveur.meilleureTransplantation;

import instance.Instance;
import instance.network.Pair;
import io.InstanceReader;
import io.SolutionWriter;
import io.exception.ReaderException;
import operateur.InsertionPair;
import solution.Solution;
import solveur.Solveur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class MeilleureTransplantationAdaptable  {

    public void finishSolve(LinkedList<Pair> lastPairs, Solution solution) {
        LinkedList<Pair> copyPair = new LinkedList<>(lastPairs);
        copyPair = sortInReverseOrder(copyPair);

        while(!copyPair.isEmpty()) {
            traitment(solution, copyPair);
        }
        solution.deleteSequenceNotUsed();
    }


    private void traitment(Solution s, LinkedList<Pair> copyPair) {
        InsertionPair insMeilleur;
        insMeilleur = getMeilleurOperateurInsertion(s, copyPair);

        if(s.doInsertion(insMeilleur)) {
            copyPair.remove(insMeilleur.getPairToAdd());
        }
        else {
            s.addPairNewCycle(copyPair.getFirst());
            copyPair.removeFirst();
        }
    }

    private LinkedList<Pair> sortInReverseOrder(LinkedList<Pair> copyPair) {
        LinkedList<Pair> copyOfCopyPair = new LinkedList<>();
        ArrayList<Integer> ratio = new ArrayList<>();
        for(Pair p : copyPair) {
            ratio.add(p.ratioGain());
        }
        //Collections.sort(ratio);
        Collections.sort(ratio, Collections.reverseOrder());
        for(Integer r : ratio) {
            for(Pair p : copyPair) {
                if(r == p.ratioGain()) {
                    copyOfCopyPair.addLast(p);
                    copyPair.remove(p);
                    break;
                }
            }
        }
        copyPair = copyOfCopyPair;
        return copyPair;
    }

    private InsertionPair getMeilleurOperateurInsertion(Solution s, LinkedList<Pair> pairs) {
        InsertionPair insMeilleur = s.getMeilleureInsertion(pairs.getFirst());

        InsertionPair insActu;
        for(Pair pair : pairs) {
            insActu = s.getMeilleureInsertion(pair);
            if(insActu.getDeltaCout() > insMeilleur.getDeltaCout() && (insActu.getDeltaCout() != Integer.MAX_VALUE && insActu.getDeltaCout() != Integer.MIN_VALUE))
                insMeilleur = insActu;
        }
        return insMeilleur;
    }
}
