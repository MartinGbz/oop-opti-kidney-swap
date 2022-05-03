package Solveur;

import Operateur.InsertionPair;
import Solution.Solution;
import instance.Instance;
import instance.network.Pair;
import io.InstanceReader;
import io.SolutionWriter;
import io.exception.ReaderException;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

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
        LinkedHashMap<Integer, Pair> copyPair = new LinkedHashMap<Integer, Pair>(s.getInstance().getPairs());
        InsertionPair insMeilleur;

        while(!copyPair.isEmpty()) {
            insMeilleur = getMeilleurOperateurInsertion(s, copyPair);

            if(s.doInsertion(insMeilleur)) {
                System.out.println("MeilleureTransplantation - solve - if");
                copyPair.remove(insMeilleur.getPairToAdd());
            }
            else {
                System.out.println("MeilleureTransplantation - solve - else");
                int firstPairId = getFirst(copyPair);
                s.addPairNewCycle(copyPair.get(firstPairId));
                copyPair.remove(firstPairId);
            }
        }
        return s;
    }

    private InsertionPair getMeilleurOperateurInsertion(Solution s, LinkedHashMap<Integer, Pair> pairs) {
        int firstPairId = getFirst(pairs);
        InsertionPair insMeilleur = s.getMeilleureInsertion(pairs.get(firstPairId));

        InsertionPair insActu;
        System.out.println("MeilleureTransplantation - getMeilleurOperateurInsertion - for");
        for(Map.Entry pairEntry : s.getInstance().getPairs().entrySet()) {
            insActu = s.getMeilleureInsertion((Pair)pairEntry.getValue());
            if(insActu.getDeltaCout() > insMeilleur.getDeltaCout() && insActu.getDeltaCout() != Integer.MAX_VALUE)
                insMeilleur = insActu;
        }
        return insMeilleur;
    }

    public static int getFirst(LinkedHashMap<Integer, Pair> lhm) {
        int count = 1;

        for (Map.Entry<Integer, Pair> it : lhm.entrySet()) {
            if (count == 1) {
                System.out.println("First Key-> "+it.getKey());
                System.out.println("First Value-> "+it.getValue());
                return it.getKey();
            }
            count++;
        }
        return Integer.MAX_VALUE;
    }

    public static void main(String[] args) {
        try {
            InstanceReader reader = new InstanceReader("instances/testInstance.txt"); //mettre le nom du fichier
            Instance instance = reader.readInstance();

            MeilleureTransplantation mt = new MeilleureTransplantation();

            Solution s = mt.solve(instance);
            System.out.println(mt);
            System.out.println(s);
            // s.check();
            System.out.println("Etat du check : " + s.check());
            SolutionWriter sw = new SolutionWriter(s);



        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }
}