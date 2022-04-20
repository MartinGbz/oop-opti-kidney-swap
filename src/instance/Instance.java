package instance;

import instance.network.Altruist;
import instance.network.Base;
import instance.network.Pair;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Instance {
    String name;
    int nbPairs;
    int nbAltruists;
    int maxSizeCycle;
    int maxSizeChain;

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

    public boolean addAltruist(Altruist altruist) {
        if(altruist == null) return false;
        int id = altruist.getId();
        if(this.altruists.containsKey(id)) return false;
        this.altruists.put(id, altruist);
        return true;
    }

    public boolean addPair(Pair pair) {
        if(pair == null) return false;
        int id = pair.getId();
        if(this.pairs.containsKey(id)) return false;
        this.pairs.put(id, pair);
        System.out.println("ajout pair id <" + id + ">");
        return true;
    }

    public Boolean addTranspantations(Base donneur, ArrayList<Integer> gains) {
        if(donneur==null) return false; //le code dit que 5 et 6 sont null

        for(int i=0; i<nbPairs; i++) {
            donneur.addTransplantation(pairs.get(i + nbAltruists), gains.get(i));
        }
        return true;
    }

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


