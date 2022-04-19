package instance;

import instance.network.Altruist;
import instance.network.Pair;

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
//        if (clientToAdd == null) return false;
//        int id = clientToAdd.getId();
//        if (this.clients.containsKey(id)) return false;
//        ajouterNouvelleRoute(clientToAdd);
//        this.clients.put(id, clientToAdd);
//        return true;
        return false;
    }

    // TODO : ajouter un 2eme argument (tableau avec la correspondance patient - gain ?)
    private void addNewTranspantation(Pair patient) {
//        this.depot.ajouterRoute(clientToAdd);
//        clientToAdd.ajouterRoute(this.depot);
//
//        for(Client c : clients.values()) {
//            c.ajouterRoute(clientToAdd);
//            clientToAdd.ajouterRoute(c);
//        }
    }

}


