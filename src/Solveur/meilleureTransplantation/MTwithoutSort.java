package Solveur.meilleureTransplantation;

import Solution.Solution;
import Solveur.Solveur;
import instance.Instance;

public class MTwithoutSort implements Solveur {

    private final String name = "Meilleure transplantation (sans trie)";

    @Override
    public String getNom() {
        return name;
    }

    @Override
    public Solution solve(Instance i) {
        MeilleureTransplantation mt = new MeilleureTransplantation(false);
        Solution s = mt.solve(i);
        return s;
    }
}
