package solveur.meilleureTransplantation;

import solution.Solution;
import solveur.Solveur;
import instance.Instance;

public class MTwithSortOrder implements Solveur {

    private final String name = "Meilleure transplantation (trie croissant)";

    @Override
    public String getNom() {
        return name;
    }

    @Override
    public Solution solve(Instance i) {
        MeilleureTransplantation mt = new MeilleureTransplantation(true, true);
        Solution s = mt.solve(i);
        return s;
    }
}
