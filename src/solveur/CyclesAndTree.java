package solveur;

import instance.Instance;
import instance.network.Altruist;
import instance.network.Base;
import instance.network.Pair;
import io.InstanceReader;
import io.SolutionWriter;
import io.exception.ReaderException;
import solution.Node;
import solution.Sequence;
import solution.Solution;
import solution.ValidChain;
import solveur.meilleureTransplantation.MeilleureTransplantation;
import solveur.meilleureTransplantation.MeilleureTransplantationAdaptable;

import java.util.ArrayList;
import java.util.LinkedList;

public class CyclesAndTree implements Solveur {

    private final String name = "best cycles + tree chain";
    private boolean ordre = true;

    public CyclesAndTree(boolean ordre) {
        this.ordre = ordre;
    }

    @Override
    public String getNom() {
        return name;
    }

    @Override
    public Solution solve(Instance i) {
        LinkedList<Pair> pairsAvailables;
        Solution s = new Solution(i);
        Solution s1 = new Solution(i);
        Solution s2 = new Solution(i);


        if(ordre) {
            pairsAvailables = new LinkedList<>(s.restOfPairs());
            NotValidCycle nvc = new NotValidCycle();
            s = nvc.creatCycle(pairsAvailables,s);
            //cycleGeneration(s);
            chainGeneration(s);
            localGeneration(s);
        } else {
            chainGeneration(s);
            pairsAvailables = new LinkedList<>(s.restOfPairs());
            NotValidCycle nvc = new NotValidCycle();
            s = nvc.creatCycle(pairsAvailables,s);
            //cycleGeneration(s);
            localGeneration(s);
        }

/*
        pairsAvailables = new LinkedList<>(s1.restOfPairs());
        NotValidCycle nvc1 = new NotValidCycle();
        s1 = nvc1.creatCycle(pairsAvailables,s1);
        //cycleGeneration(s);

        chainGeneration(s1);

        localGeneration(s1);

        chainGeneration(s2);

        pairsAvailables = new LinkedList<>(s2.restOfPairs());
        NotValidCycle nvc2 = new NotValidCycle();
        s2 = nvc2.creatCycle(pairsAvailables,s2);
        //cycleGeneration(s);

        localGeneration(s2);

        if(s1.getGainMedTotal() > s2.getGainMedTotal()) {
            System.out.println("--------------- "+ i.getName() +" -----------------");
            System.out.println(s1);
            return s1;
        } else {
            System.out.println("--------------- "+ i.getName() +" -----------------");
            System.out.println(s2);
            return s2;
        }
 */
        System.out.println("--------------- "+ i.getName() +" -----------------");
        System.out.println(s);
        return s;
    }

    public void cycleGeneration(Solution s) {
        LinkedList<Pair> pairsAvailables = new LinkedList<>(s.restOfPairs());
        NotValidCycle nvc = new NotValidCycle();
        s = nvc.creatCycle(pairsAvailables,s);
    }

    public void localGeneration(Solution s) {
        LinkedList<Pair> pairsAvailables = new LinkedList<>(s.restOfPairs());
        MeilleureTransplantationAdaptable mta = new MeilleureTransplantationAdaptable();
        mta.finishSolve(pairsAvailables, s);
    }

    public void chainGeneration(Solution s) {
        LinkedList<Altruist> altruistsAvailables = new LinkedList<>(s.restOfAltruists());
        LinkedList<Pair> pairsAvailables = new LinkedList<>(s.restOfPairs());
        int maxSizeChain = s.getInstance().getMaxSizeChain();

        if(maxSizeChain>8 && pairsAvailables.size()>75 && altruistsAvailables.size()>15) maxSizeChain = 3;
        if(maxSizeChain>8 && pairsAvailables.size()>75 && altruistsAvailables.size()>10) maxSizeChain = 4;
        if(maxSizeChain>8 && pairsAvailables.size()>75 && altruistsAvailables.size()>5) maxSizeChain = 5;

        if(altruistsAvailables.size()>27) maxSizeChain = 4;
        if(altruistsAvailables.size() == 13 && s.getInstance().getMaxSizeChain() > 4) maxSizeChain = 4;
        if(altruistsAvailables.size() == 5 && s.getInstance().getMaxSizeChain() == 13) maxSizeChain = 4;

        LinkedList<LinkedList<ValidChain>> listChainsByAltruit =
                Node.getAllValidChainsFromTrees(new ArrayList<>(altruistsAvailables),
                        new ArrayList<>(pairsAvailables) , maxSizeChain);
        Node.addChainsIntoSolution(s, Node.getBestCombo(new ArrayList<>(altruistsAvailables) , listChainsByAltruit));
    }

    public static void main(String[] args) {
        try {
            //InstanceReader reader = new InstanceReader("instances/testInstance.txt"); // mettre le nom du fichier
            //InstanceReader reader = new InstanceReader("instances/KEP_p9_n0_k3_l0.txt"); // mettre le nom du fichier
            InstanceReader reader = new InstanceReader("instances/KEP_p100_n11_k5_l17.txt"); // mettre le nom du fichier
            //InstanceReader reader = new InstanceReader("instances/KEP_p100_n11_k3_l13.txt"); // mettre le nom du fichier
            Instance instance = reader.readInstance();

            CyclesAndTree ct = new CyclesAndTree(true);
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
