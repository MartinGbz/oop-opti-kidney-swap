package solveur;

import instance.Instance;
import instance.network.Altruist;
import instance.network.Base;
import instance.network.Pair;
import io.InstanceReader;
import io.SolutionWriter;
import io.exception.ReaderException;
import solution.*;
import solveur.meilleureTransplantation.MeilleureTransplantation;
import solveur.meilleureTransplantation.MeilleureTransplantationAdaptable;

import java.util.ArrayList;
import java.util.LinkedList;

public class CyclesAndTree implements Solveur {

    private final String name = "best cycles + tree chain";

    @Override
    public String getNom() {
        return name;
    }

    @Override
    public Solution solve(Instance i) {
        LinkedList<Pair> pairsAvailables;
        Solution s = new Solution(i);

        // generation de cycle
        pairsAvailables = new LinkedList<>(s.restOfPairs());
        NotValidCycle nvc = new NotValidCycle();
        s = nvc.creatCycle(pairsAvailables,s);
        // ----

        chainGeneration(s);
        localGeneration(s);

        System.out.println("--------------- "+ i.getName() +" -----------------");
        System.out.println(s);
        return s;
    }

    public void localGeneration(Solution s) {
        LinkedList<Pair> pairsAvailables = new LinkedList<>(s.restOfPairs());
        MeilleureTransplantationAdaptable mta = new MeilleureTransplantationAdaptable();
        mta.finishSolve(pairsAvailables, s);

        pairsAvailables = new LinkedList<>(s.restOfPairs());
        ReplaceTransplantationAdaptable rta = new ReplaceTransplantationAdaptable();
        rta.localReplacement(pairsAvailables, s);
        s.deleteSequenceNotUsed();
    }

    public void chainGeneration(Solution s) {
        LinkedList<Altruist> altruistsAvailables = new LinkedList<>(s.restOfAltruists());
        LinkedList<Pair> pairsAvailables = new LinkedList<>(s.restOfPairs());
        int maxSizeChain = s.getInstance().getMaxSizeChain();
        LinkedList<LinkedList<ValidChain>> listValidChainsByAltruit;

        if(maxSizeChain > 9) {
            if (altruistsAvailables.size()>82) maxSizeChain = 5;
            else if (altruistsAvailables.size()>27) maxSizeChain = 6;
            else if (altruistsAvailables.size()>12) maxSizeChain = 7;
        }

        listValidChainsByAltruit = Node.getAllValidChainsFromTrees(new ArrayList<>(altruistsAvailables),
                new ArrayList<>(pairsAvailables), maxSizeChain);
        Node.addValidChainsIntoSolution(s, Node.getBestComboValidChain(listValidChainsByAltruit));
    }

    public static void main(String[] args) {
        try {
            //InstanceReader reader = new InstanceReader("instances/testInstance.txt"); // mettre le nom du fichier
            //InstanceReader reader = new InstanceReader("instances/KEP_p9_n0_k3_l0.txt"); // mettre le nom du fichier
            InstanceReader reader = new InstanceReader("instances/KEP_p100_n11_k5_l17.txt"); // mettre le nom du fichier
            //InstanceReader reader = new InstanceReader("instances/KEP_p100_n11_k3_l13.txt"); // mettre le nom du fichier
            Instance instance = reader.readInstance();

            CyclesAndTree ct = new CyclesAndTree();
            Solution s = ct.solve(instance);

            System.out.println(ct);
            System.out.println(s);
            System.out.println("Etat du check : " + s.check());

            SolutionWriter sw = new SolutionWriter(s, "testSolution/");

        }
        catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
