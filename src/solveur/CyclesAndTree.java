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

    @Override
    public String getNom() {
        return name;
    }

    @Override
    public Solution solve(Instance i) {
        //DangerousCycle dc = new DangerousCycle();
        //Solution s = dc.solve(i);

        Solution s = new Solution(i);
        NotValidCycle nvc = new NotValidCycle();
        s = nvc.creatCycle(new LinkedList<>(i.getPairs()),s);

/*
        LinkedList<Altruist> altruitToChain = new LinkedList<>(s.getInstance().getAltruists());
        int maxSizeChain = s.getInstance().getMaxSizeChain();
        LinkedList<Pair> pairToChain = new LinkedList<>(s.getInstance().getPairs());
        for(Sequence seq : s.getCycles()) {
            for(Base b : seq.getSequence()) {
                pairToChain.remove(b);
            }
        }

        System.out.println("--------------- "+ i.getName() +" -----------------");
        //System.out.println(s);
        System.out.println("nombre de paires début : <" + s.getInstance().getPairs().size() + ">");
        System.out.println("nombre de paires restantes : <" + pairToChain.size() + ">");
*/

        ArrayList<Altruist> altruitToChain = new ArrayList<>(s.getInstance().getAltruists());
        int maxSizeChain = s.getInstance().getMaxSizeChain();
        ArrayList<Pair> pairToChain = new ArrayList<>(s.getInstance().getPairs());
        for(Sequence seq : s.getCycles()) {
            for(Base b : seq.getSequence()) {
                pairToChain.remove(b);
            }
        }
        if(maxSizeChain>8 && pairToChain.size()>75 && altruitToChain.size()>15) maxSizeChain = 3;
        if(maxSizeChain>8 && pairToChain.size()>75 && altruitToChain.size()>10) maxSizeChain = 4;
        if(maxSizeChain>8 && pairToChain.size()>75 && altruitToChain.size()>5) maxSizeChain = 5;

        LinkedList<LinkedList<ValidChain>> listChainsByAltruit = Node.getAllValidChainsFromTrees(altruitToChain, pairToChain, maxSizeChain);
        Node.addChainsIntoSolution(s, Node.getBestCombo(altruitToChain, listChainsByAltruit));

        System.out.println("--------------- "+ i.getName() +" -----------------");
        System.out.println(s);
        System.out.println("nombre de paires début : <" + s.getInstance().getPairs().size() + ">");
/*
        MeilleureTransplantationAdaptable mta = new MeilleureTransplantationAdaptable();
        LinkedList<Pair> lastPairs = new LinkedList<>(s.getInstance().getPairs());
        System.out.println("nombre de paires : <" + lastPairs.size() + ">\n");
        for(Sequence seq : s.getCycles()) {
            for(Base b : seq.getSequence()) {
                lastPairs.remove(b);
            }
        }
        for(Sequence seq : s.getChains()) {
            for(Base b : seq.getSequence()) {
                lastPairs.remove(b);
            }
        }
        System.out.println("nombre de paires : <" + lastPairs.size() + ">\n");
        System.out.println(lastPairs);
        mta.finishSolve(lastPairs, s);

        System.out.println(s);
*/
        //System.out.println("nombre de paires : <" + s.getInstance().getPairs().size() + ">\n");
        //System.out.println("nombre de paires : <" + pairToChain.size() + ">\n");
        //System.out.println("nombre de paires : <" + lastPairs.size() + ">\n");
        return s;
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
