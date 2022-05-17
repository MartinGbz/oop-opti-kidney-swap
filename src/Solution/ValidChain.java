package Solution;

import java.util.ArrayList;

public class ValidChain {

    private int gain;
    private ArrayList<Integer> idList = new ArrayList<Integer>();

    public ValidChain(int gain, ArrayList<Integer> idList) {
        this.gain = gain;
        this.idList = idList;
    }

    public int getGain() {
        return gain;
    }

    public ArrayList<Integer> getIdList() {
        return idList;
    }

    @Override
    public String toString() {
        return "\n ValidChain{" +
                "gain=" + gain +
                ", idList=" + idList +
                '}';
    }

    public int compareTo(ValidChain v2) {
        if(this.gain < v2.gain) {
            return -1;
        }
        else if(this.gain > v2.gain) {
            return 1;
        }
        return 0;
    }
}
