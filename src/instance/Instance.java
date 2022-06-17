package instance;

import instance.network.Altruist;
import instance.network.Base;
import instance.network.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class Instance {

    private final String name;
    private final int nbPairs;
    private final int nbAltruists;
    private final int maxSizeCycle;
    private final int maxSizeChain;

    private final HashMap<Integer, Altruist> altruists;
    private final HashMap<Integer, Pair> pairs;

    public Instance(String name, int nbPairs, int nbAltruists, int maxSizeCycle, int maxSizeChain) {
        this.name = name;
        this.nbPairs = nbPairs;
        this.nbAltruists = nbAltruists;
        this.maxSizeCycle = maxSizeCycle;
        this.maxSizeChain = maxSizeChain;
        this.altruists = new HashMap<>();
        this.pairs = new HashMap<>();
    }

    public HashMap<Integer, Altruist> getAltruists() {
        return altruists;
    }
    public HashMap<Integer, Pair> getPairs() {
        return pairs;
    }
    public String getName() {
        return name;
    }
    public int getMaxSizeCycle() {
        return maxSizeCycle;
    }
    public int getMaxSizeChain() {
        return maxSizeChain;
    }
    public int getNbPairs() { return nbPairs; }
    public int getNbAltruists() { return nbAltruists; }


    /**
     * Ajout d'un altruiste dans le tableau correspondant (altruists)
     * @param altruist
     * @return True/False
     */
    public boolean addAltruist(Altruist altruist) {
        if(altruist == null) return false;
        int id = altruist.getId();
        if(this.altruists.containsKey(id)) return false;
        this.altruists.put(id, altruist);
        return true;
    }

    /**
     * Ajout d'un altruiste dans le HashMap des pairs
     * @param pair
     * @return True/False
     */
    public boolean addPair(Pair pair) {
        if(pair == null) return false;
        int id = pair.getId();
        if(this.pairs.containsKey(id)) return false;
        this.pairs.put(id, pair);
        return true;
    }

    /**
     * Association d'objets Transplantation pour chaque valeur dans le tableau "gain"
     * entre "donneur" et la paire correspondante à la valeur
     * @param donneur le donneur (une paire ou un altruiste)
     * @param gains le tableau des gains médicaux de ce donneur
     * @return True/false
     */
    public Boolean addTranspantations(Base donneur, ArrayList<Integer> gains) {
        if(donneur==null) return false;
        for(int i=0; i< nbPairs; i++) {
            donneur.addTransplantation(pairs.get(i + nbAltruists+1), gains.get(i));
        }
        return true;
    }

    /**
     * Retourne l'objet Altruist ou Pair dans le tableau correspondant, en fonction de l'id
     * @param id l'id de la paire ou de l'altruiste
     * @return Base/null
     */
    public Base getBaseById(Integer id) {
        if(id >= 1 && id <= this.nbAltruists) {
            return this.altruists.get(id);
        }
        else if(id > this.nbAltruists && id <= (this.nbPairs+this.nbAltruists)) {
            return this.pairs.get(id);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Instance{" +
                "name='" + name + '\'' +
                ", nbPairs=" + nbPairs +
                ", nbAltruists:" + nbAltruists +
                ", maxSizeCycle=" + maxSizeCycle +
                ", maxSizeChain=" + maxSizeChain +
                ", altruists=" + altruists +
                ", pairs=" + pairs +
                '}';
    }
}

