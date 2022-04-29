package instance;

import instance.network.Altruist;
import instance.network.Base;
import instance.network.Pair;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Instance {
    private String name;
    private int nbPairs;
    private int nbAltruists;
    private int maxSizeCycle;
    private int maxSizeChain;

    private final LinkedHashMap<Integer, Altruist> altruists;
    private final LinkedHashMap<Integer, Pair> pairs;

    public Instance(String name, int nbPairs, int nbAltruists, int maxSizeCycle, int maxSizeChain) {
        this.name = name;
        this.nbPairs = nbPairs;
        this.nbAltruists = nbAltruists;
        this.maxSizeCycle = maxSizeCycle;
        this.maxSizeChain = maxSizeChain;
        this.altruists = new LinkedHashMap<>();
        this.pairs = new LinkedHashMap<>();
    }

    public int getMaxSizeChain() {
        return maxSizeChain;
    }

    public int getMaxSizeCycle() {
        return maxSizeCycle;
    }

    public LinkedHashMap<Integer, Pair> getPairs() {
        return new LinkedHashMap<>(pairs);
    }
    public LinkedHashMap<Integer, Altruist> getAltruists() {
        return new LinkedHashMap<>(altruists);
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

    public int getMaxSizeCycle() { return maxSizeCycle; }

    public int getMaxSizeChain() { return maxSizeChain; }

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
     * Ajout d'un altruiste dans le tableau correspondant (pairs)
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
     * @param donneur
     * @param gains
     * @return True/false
     */
    public Boolean addTranspantations(Base donneur, ArrayList<Integer> gains) {
        if(donneur==null) return false; //le code dit que 5 et 6 sont null
        for(int i=0; i<nbPairs; i++) {
            donneur.addTransplantation(pairs.get(i + nbAltruists), gains.get(i));
        }
        return true;
    }

    /**
     * Retourne l'objet Altruist ou Pair dans le tableau correspondant, en fonction de l'id
     * @param id
     * @return Base/null
     */
    public Base getBaseById(Integer id) {
        if(id >= 0 && id < this.nbAltruists) {
            return this.altruists.get(id);
        }
        else if(id >= this.nbAltruists && id < (this.nbPairs+this.nbAltruists)) {
            return this.pairs.get(id);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Instance{" +
                "name='" + name + '\'' +
                ", nbPairs=" + nbPairs +
                ", nbAltruists=" + nbAltruists +
                ", maxSizeCycle=" + maxSizeCycle +
                ", maxSizeChain=" + maxSizeChain +
                ", altruists=" + altruists +
                ", pairs=" + pairs +
                '}';
    }

}


