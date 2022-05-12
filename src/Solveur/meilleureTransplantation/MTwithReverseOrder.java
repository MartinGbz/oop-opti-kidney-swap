package Solveur.meilleureTransplantation;

import Solution.Solution;
import Solveur.Solveur;
import instance.Instance;

public class MTwithReverseOrder implements Solveur {

    private final String name = "Meilleure transplantation (trie decroissant)";

    @Override
    public String getNom() {
        return name;
    }

    @Override
    public Solution solve(Instance i) {
        MeilleureTransplantation mt = new MeilleureTransplantation(true, false);
        Solution s = mt.solve(i);
        return s;
    }
}
