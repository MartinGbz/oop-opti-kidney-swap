package Solveur;

import Operateur.InsertionPair;
import Solution.Solution;
import Solution.Sequence;
import instance.Instance;
import instance.network.Pair;
import io.InstanceReader;
import io.SolutionWriter;
import io.exception.ReaderException;

import java.util.ArrayList;
import java.util.LinkedList;

public class MeilleureTransplantation implements Solveur {

    private final String name = "Meilleure Transplantation";

    @Override
    public String getNom() {
        return null;
    }

    @Override
    public Solution solve(Instance i) {
        if(i == null) return null;
        Solution s = new Solution(i);
        LinkedList<Pair> copyPair = new LinkedList<>(s.getInstance().getPairs());
        InsertionPair insMeilleur;
        LinkedList<Sequence> sequenceOfSolution = new LinkedList<>();

        while(!copyPair.isEmpty()) {
            insMeilleur = getMeilleurOperateurInsertion(s, copyPair);

            if(s.doInsertion(insMeilleur)) {
                copyPair.remove(insMeilleur.getPairToAdd());
            }
            else {
                s.addPairNewCycle(copyPair.getFirst());
                copyPair.removeFirst();
            }
        }
        s.deleteSequenceNotUsed();
        return s;
    }

    private InsertionPair getMeilleurOperateurInsertion(Solution s, LinkedList<Pair> pairs) {
        InsertionPair insMeilleur = s.getMeilleureInsertion(pairs.getFirst());

        InsertionPair insActu;
        for(Pair pair : pairs) {
            insActu = s.getMeilleureInsertion(pair);
            if(insActu.getDeltaCout() > insMeilleur.getDeltaCout() && insActu.getDeltaCout() != Integer.MAX_VALUE)
                insMeilleur = insActu;
        }
        return insMeilleur;
    }

    public static int getFirstId(ArrayList<Pair> pairs) {
        int id = -1;
        if(pairs.size()==0) return id;
        return pairs.get(0).getId();
    }

    public static void main(String[] args) {
        try {
            InstanceReader reader = new InstanceReader("instances/testInstance.txt"); // mettre le nom du fichier
            //InstanceReader reader = new InstanceReader("instances/KEP_p50_n3_k3_l13.txt"); // mettre le nom du fichier
            Instance instance = reader.readInstance();

            MeilleureTransplantation mt = new MeilleureTransplantation();
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
