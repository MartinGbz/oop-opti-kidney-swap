package solution;

import java.util.LinkedList;

public class ValidChain {

    private int gain;
    private final LinkedList<Integer> idList;

    //Construction
    public ValidChain() {
        this.gain = 0;
        this.idList = new LinkedList<>();
    }

    public ValidChain(int gain, LinkedList<Integer> idList) {
        this.gain = gain;
        this.idList = idList;
    }

    //Getters
    public int getGain() {
        return gain;
    }
    public LinkedList<Integer> getIdList() {
        return idList;
    }

    @Override
    public String toString() {
        return "\n ValidChain{" +
                "gain=" + gain +
                ", idList=" + idList +
                '}';
    }

    /**
     *
     * @param chainToCompare
     * @return
     */
    public boolean canBeCombined(ValidChain chainToCompare) {
        for(int id : chainToCompare.getIdList()) {
            if(this.getIdList().contains(id)) return false;
        }
        return true;
    }

    /**
     * Comparaison afin d'obtenir un tri décroissant lors des tris
     * @param v2 la chaine à comparer avec this
     * @param sortingDirection "ASC" ou "DESC"
     * @return 1 ou -1 en fonction du sens choisi et de la comparaison
     */
    public int compareTo(ValidChain v2, String sortingDirection) {
        if(sortingDirection.equals("ASC")) {
            if(this.gain < v2.gain) {
                return -1;
            }
            else if(this.gain > v2.gain) {
                return 1;
            }
        }
        else if(sortingDirection.equals("DESC")) {
            if (this.gain > v2.gain) {
                return -1;
            } else if (this.gain < v2.gain) {
                return 1;
            }
        }
        return 0;
    }
}
