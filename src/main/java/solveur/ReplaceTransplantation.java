package solveur;

import operateur.ReplacementPair;
import solution.Solution;
import solution.Sequence;
import solveur.meilleureTransplantation.MeilleureTransplantation;
import instance.Instance;
import instance.network.Base;
import instance.network.Pair;
import io.InstanceReader;
import io.SolutionWriter;
import io.exception.ReaderException;

import java.util.LinkedList;

public class ReplaceTransplantation implements Solveur {

    private final String name = "Remplacement Transplantation";


    @Override
    public String getNom() {
        return name;
    }

    @Override
    public Solution solve(Instance i) {
        MeilleureTransplantation mt = new MeilleureTransplantation(true, true);
        Solution s = mt.solve(i);
        LinkedList<Pair> copyPair = new LinkedList<>(s.getInstance().getPairs());

        initCopyPair(s, copyPair);

        while(!copyPair.isEmpty()) {
            ReplacementPair insMeilleur;
            insMeilleur = getMeilleurOperateurReplacement(s, copyPair);
            if(insMeilleur.getDeltaCout() < Integer.MAX_VALUE) {
                s.doReplacement((insMeilleur));
                copyPair.remove(insMeilleur.getPairToReplace());
            } else {
                copyPair.remove(0);
            }


            /*
            if(s.doReplacement(insMeilleur)) {
                copyPair.remove(insMeilleur.getPairToReplace());
            }
             */
            /*
            else {
                s.addPairNewCycle(copyPair.getFirst());
                copyPair.removeFirst();
            }
            */
        }

        return s;
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
                if(copyPair.contains(b))
                    copyPair.remove(b);
            }
        }
        for(Sequence seq : s.getChains()) {
            for(Base b : seq.getSequence()) {
                if(copyPair.contains(b))
                    copyPair.remove(b);
            }
        }
    }

    public static void main(String[] args) {
        try {
            //InstanceReader reader = new InstanceReader("instances/testInstance.txt"); // mettre le nom du fichier
            //InstanceReader reader = new InstanceReader("instances/KEP_p9_n0_k3_l0.txt"); // mettre le nom du fichier
            //InstanceReader reader = new InstanceReader("instances/KEP_p9_n1_k3_l3.txt"); // mettre le nom du fichier
            InstanceReader reader = new InstanceReader("instances/KEP_p100_n5_k3_l7.txt"); // mettre le nom du fichier
            Instance instance = reader.readInstance();

            //MeilleureTransplantation mt = new MeilleureTransplantation(true, true);
            //Solution s = mt.solve(instance);

            ReplaceTransplantation rt = new ReplaceTransplantation();
            Solution s = rt.solve(instance);

            System.out.println(rt);
            System.out.println(s);
            System.out.println("Etat du check : " + s.check());

            SolutionWriter sw = new SolutionWriter(s, "testSolution/");

        }
        catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
