package solveur;

import operateur.CycleNotValide;
import solution.Solution;
import solution.Cycle;
import instance.Instance;
import instance.network.Base;
import instance.network.Pair;
import io.InstanceReader;
import io.SolutionWriter;
import io.exception.ReaderException;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public class DangerousCycle implements Solveur {

    private final String name = "Dangerous Cycle";

    @Override
    public String getNom() {
        return name;
    }

    @Override
    public Solution solve(Instance i) {
        if(i == null) return null;
        Solution s, ss = new Solution(i);
        boolean endLoop = true;
        int notFirstLoop, sizeSol =0, maxLoop=0;
        Random r = new Random(0);

        if(i.getPairs().size() < 51) maxLoop = 1000;
        else if(i.getPairs().size() < 101) maxLoop = 500;
        else maxLoop = 100;

        System.out.println("Itération choisi <" + maxLoop + ">");
        for(int k=0 ; k<maxLoop ; k++) {
            notFirstLoop = 0;
            s = new Solution(i);
            endLoop = true;
            while (endLoop) {
                LinkedList<Pair> copyPair = new LinkedList<>(s.getInstance().getPairs().values());
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
        //System.out.println("insMeilleur <" + insMeilleur + ">\n");

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

    public static void main(String[] args) {
        try {
            //InstanceReader reader = new InstanceReader("instances/testInstance.txt"); // mettre le nom du fichier
            //InstanceReader reader = new InstanceReader("instances/KEP_p9_n0_k3_l0.txt"); // mettre le nom du fichier
            //InstanceReader reader = new InstanceReader("instances/KEP_p9_n1_k3_l3.txt"); // mettre le nom du fichier
            InstanceReader reader = new InstanceReader("instances/KEP_p100_n11_k3_l13.txt"); // mettre le nom du fichier
            Instance instance = reader.readInstance();

            DangerousCycle dc = new DangerousCycle();
            Solution s = dc.solve(instance);

            System.out.println(dc);
            System.out.println(s);
            System.out.println("Etat du check : " + s.check());

            SolutionWriter sw = new SolutionWriter(s, "testSolution/");

        }
        catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
