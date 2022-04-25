package Solveur;

import Solution.Solution;
import instance.Instance;

public interface Solveur {

    String getNom();

    Solution solve(Instance i);
}
