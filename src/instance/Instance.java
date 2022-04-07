package instance;

public class Instance {
    String name;
    int nbPairs;
    int nbAltruists;
    int maxSizeCycle;
    int maxSizeChain;

    public Instance(String name, int nbPairs, int nbAltruists, int maxSizeCycle, int maxSizeChain) {
        this.name = name;
        this.nbPairs = nbPairs;
        this.nbAltruists = nbAltruists;
        this.maxSizeCycle = maxSizeCycle;
        this.maxSizeChain = maxSizeChain;
    }
}


