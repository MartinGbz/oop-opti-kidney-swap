package Solution;

import instance.Instance;
import instance.network.Chain;
import instance.network.Cycle;

import java.util.LinkedList;
import java.util.List;

public class Solution {

    int gainMedTotal;
    private Instance instance;
    private List<Cycle> cycles;
    private List<Chain> chains;

    public Solution(Instance instance) {
        this.gainMedTotal = 0;
        this.instance = instance;
        this.cycles = new LinkedList<>();
        this.chains = new LinkedList<>();
    }

    @Override
    public String toString() {
        return "Solution{" +
                "gainMedTotal=" + gainMedTotal +
                ", instance=" + instance +
                ", cycles=" + cycles +
                ", chains=" + chains +
                '}';
    }

    public static void main(String[] args) {

    }

}

