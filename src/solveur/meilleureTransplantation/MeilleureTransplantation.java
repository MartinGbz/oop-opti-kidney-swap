package solveur.meilleureTransplantation;

import operateur.InsertionPair;
import solution.Solution;
import solveur.Solveur;
import instance.Instance;
import instance.network.Pair;
import io.InstanceReader;
import io.SolutionWriter;
import io.exception.ReaderException;

import java.util.*;

public class MeilleureTransplantation implements Solveur {

    private final String name = "Meilleure Transplantation";
    private boolean activeSort;
    private boolean sortOrder;

    public MeilleureTransplantation(boolean activeSort) {
        this.activeSort = activeSort;
    }

    public MeilleureTransplantation(boolean activeSort, boolean sortOrder) {
        this(activeSort);
        this.sortOrder = sortOrder;
    }

    @Override
    public String getNom() {
        return name;
    }

    @Override
    public Solution solve(Instance i) {
        if(i == null) return null;
        Solution s = new Solution(i);
        LinkedList<Pair> copyPair = new LinkedList<>(s.getInstance().getPairs().values());

        if(activeSort) {
            copyPair = sortInReverseOrder(copyPair);
        }

        while(!copyPair.isEmpty()) {
            traitment(s, copyPair);
        }

        s.deleteSequenceNotUsed();
        return s;
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
        if(this.sortOrder)
            Collections.sort(ratio);
        else
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

    public static void main(String[] args) {
        try {
            InstanceReader reader = new InstanceReader("instances/KEP_p9_n1_k3_l3.txt");
            Instance instance = reader.readInstance();

            MeilleureTransplantation mt = new MeilleureTransplantation(true, true);
            Solution s = mt.solve(instance);

            System.out.println(mt);
            System.out.println(s);
            System.out.println("Etat du check : " + s.check());

            SolutionWriter sw = new SolutionWriter(s, "testSolution/");

        }
        catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
}