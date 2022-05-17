package solveur;

import solution.Solution;
import instance.Instance;

public interface Solveur {

    String getNom();

    Solution solve(Instance i);
}
