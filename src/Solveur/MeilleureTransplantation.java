package Solveur;

import Operateur.InsertionPair;
import Solution.Solution;
import instance.Instance;
import instance.network.Pair;
import io.InstanceReader;
import io.SolutionWriter;
import io.exception.ReaderException;

import java.util.ArrayList;

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
        ArrayList<Pair> copyPair = new ArrayList<>(s.getInstance().getPairs());
        InsertionPair insMeilleur;

        while(!copyPair.isEmpty()) {
            insMeilleur = getMeilleurOperateurInsertion(s, copyPair);

            if(s.doInsertion(insMeilleur)) {
                System.out.println("MeilleureTransplantation - solve - if");
                copyPair.remove(insMeilleur.getPairToAdd());
            }
            else {
                System.out.println("MeilleureTransplantation - solve - else");
                int firstPairId = getFirstId(copyPair);
                s.addPairNewCycle(copyPair.get(firstPairId));
                copyPair.remove(firstPairId);
            }
        }
        return s;
    }

    private InsertionPair getMeilleurOperateurInsertion(Solution s, ArrayList<Pair> pairs) {
        int firstPairId = getFirstId(pairs);
        InsertionPair insMeilleur = s.getMeilleureInsertion(pairs.get(firstPairId));

        InsertionPair insActu;
        System.out.println("MeilleureTransplantation - getMeilleurOperateurInsertion - for");
        for(Pair pair : s.getInstance().getPairs()) {
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
