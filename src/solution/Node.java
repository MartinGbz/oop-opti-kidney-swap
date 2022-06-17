package solution;

import instance.Instance;
import instance.network.Altruist;
import instance.network.Base;
import instance.network.Pair;
import io.InstanceReader;

import java.util.*;

public class Node {

    private final Base data;
    private final List<Node> children = new ArrayList<>();
    private int gain = 0;
    private int sumId;
    private LinkedList<Integer> idList = new LinkedList<>();

    public Node(Base data) {
        this.data = data;
        this.idList.add(data.getId());
        this.sumId = data.getId();
    }

    /**
     *
     * @param child
     * @param tailleMax
     */
    public void addChild(Node child, int tailleMax) {
        Base baseParent = this.data;
        Base baseChild = child.data;

        if(this.idList.size() >= tailleMax) // Taille max atteinte
            return;

        if(!baseParent.isCompatible(baseChild)) // parent to child non compatible
            return;

        child.idList = new LinkedList<>(this.idList);

        if(child.idList.contains(child.data.getId())) // child deja preesent dans cette branche
            return;

        child.idList.add(child.data.getId());
        child.sumId += this.sumId;
        child.gain = this.gain + baseParent.getGainVers((Pair) baseChild);
        this.children.add(child);
    }

    public List<Node> getChildren() {
        return children;
    }
    public Base getData() {
        return data;
    }

    /**
     * Trie la liste des chaines valides
     * @param validChains La liste des chaines à trier
     * @param sortingDirection "DESC" ou "ASC" ("DESC" par défaut si argument incorrect)
     */
    private static void sortChain(LinkedList<Chain> validChains, String sortingDirection) {
        if(!Objects.equals(sortingDirection, "ASC") || !Objects.equals(sortingDirection, "DESC")) {
            sortingDirection = "DESC";
        }
        String finalSortingDirection = sortingDirection;
        validChains.sort((record1, record2) -> record1.compareTo(record2, finalSortingDirection));
    }

    private static void sortValidChain(LinkedList<ValidChain> validChains, String sortingDirection) {
        if(!Objects.equals(sortingDirection, "ASC") || !Objects.equals(sortingDirection, "DESC")) {
            sortingDirection = "DESC";
        }
        String finalSortingDirection = sortingDirection;
        validChains.sort(new Comparator<>() {
            @Override
            public int compare(final ValidChain record1, final ValidChain record2) {
                    return record1.compareTo(record2, finalSortingDirection);
            }
        });
    }

    private static Chain getChainFromIdList(Altruist a, HashMap<Integer, Pair> pairs, int gain, LinkedList<Integer> idList) {
        Chain c = new Chain(a);
        for(int id : idList) {
            Pair p = pairs.get(id);
            c.addPairToChain(p);
        }
        return c;
    }

    /**
     * RECURSIVE
     * Parcourt l'arbre jusqu'à un noeud, puis ajoute une nouvelle chaine valide à partir de la liste des id du noeud
     * @param n le noeud actuel
     * @param listChain la liste des chaines valides
     */
    private static void extractChainsFromTree(Altruist a, HashMap<Integer, Pair> pairs, Node n, LinkedList<Chain> listChain) {
        if(n.getChildren().isEmpty()) {
            if(n.idList.size()>1)
                listChain.add(getChainFromIdList(a, pairs, n.gain, n.idList));
            return;
        }
        for (Node nBis: n.getChildren()) {
            extractChainsFromTree(a, pairs, nBis, listChain);
        }
    }

    /**
     * RECURSIVE
     * Parcourt l'arbre jusqu'à un noeud, puis ajoute une nouvelle chaine valide à partir de la liste des id du noeud
     * @param n le noeud actuel
     * @param listChain la liste des chaines valides
     */
    private static void extractValidChainsFromTree(Node n, LinkedList<ValidChain> listChain) {
        if(n.getChildren().isEmpty()) {
            if(n.idList.size()>1)
                listChain.add(new ValidChain(n.gain, n.idList));
            return;
        }
        for (Node nBis: n.getChildren()) {
            extractValidChainsFromTree(nBis, listChain);
        }
    }

    /**
     *
     * @param node
     * @param appender
     */
    private static void printTree(Node node, String appender) {
        System.out.print(appender + node.getData());
        System.out.println(" - gain : " + node.gain);
        node.getChildren().forEach(each ->  printTree(each, appender + appender));
    }

    /**
     * Fonction récursive de création d'un arbre
     * @param racine le noeud contenant un altruist
     * @param maxSizeChain la taille maximale qu'une chaine peut prendre
     * @param pairs la liste des pairs utilisée pour créer l'arbre
     * @param lStartTime
     */
    private static void createTreeRecursive(Node racine, int maxSizeChain, ArrayList<Pair> pairs, long lStartTime, long maxTime) {
        for (Pair p : pairs) {
            racine.addChild(new Node(p), maxSizeChain);
        }
        if(racine.getChildren().size() != 0) {
            for(Node nBis : racine.getChildren()) {
                long lEndTime = System.nanoTime();
                // System.out.println((lEndTime - lStartTime) / 1000000);
                if(((lEndTime - lStartTime) / 1000000) > maxTime) {
                    return;
                }
                else {
                    createTreeRecursive(nBis, maxSizeChain, pairs, lStartTime, maxTime);
                }
            }
        }
    }

    /**
     *
     * @param racine
     * @param maxSizeChain
     * @param pairs
     * @param maxTime temps max pour la création d'un arbre en millisecondes
     */
    private static void createTree(Node racine, int maxSizeChain, ArrayList<Pair> pairs, long maxTime) {
        long lStartTime = System.nanoTime();
        createTreeRecursive(racine, maxSizeChain, pairs, lStartTime, maxTime);

        long lEndTime = System.nanoTime();
        System.out.println("*** temps total pour l'abre: " + (lEndTime - lStartTime) / 1000000 + " (altruiste: " + racine.data.getId() + ") ***");
    }

    /**
     *
     */
    private static class BestCombo {
        LinkedList<Chain> list;
        int gainTot;

        public BestCombo() {
            this.list = new LinkedList<>();
            this.gainTot = 0;
        }

        public BestCombo(LinkedList<Chain> list, int gainTot) {
            this.list = list;
            this.gainTot = gainTot;
        }

        public int getGainTot() {
            return gainTot;
        }
    }

    private static class BestComboValidChain {
        LinkedList<ValidChain> list;
        int gainTot;

        public BestComboValidChain() {
            this.list = new LinkedList<>();
            this.gainTot = 0;
        }

        public BestComboValidChain(LinkedList<ValidChain> list, int gainTot) {
            this.list = list;
            this.gainTot = gainTot;
        }

        public int getGainTot() {
            return gainTot;
        }
    }

    /**
     *
     * @param listListChain
     * @return
     */
    private static LinkedList<Chain> getBestComboAmongAltruists(LinkedList<LinkedList<Chain>>listListChain) {
        BestCombo bestComboCur;
        BestCombo bestComboFinal = new BestCombo();
        for (int i = 0; i < listListChain.size(); i++) {
            LinkedList<Chain> chains1 = listListChain.get(i);
            LinkedList<Chain> chains2;
            for (int j = i+1; j < listListChain.size(); j++) {
                chains2 = listListChain.get(j);
                bestComboCur = getBestComboBetweenListChains(chains1, chains2);
                if (bestComboCur.getGainTot() > bestComboFinal.getGainTot()) {
                    bestComboFinal = bestComboCur;
                }
            }
        }
        return bestComboFinal.list;
    }

    private static LinkedList<ValidChain> getBestComboAmongAltruistsValidChain(LinkedList<LinkedList<ValidChain>> listListValidChain) {
        BestComboValidChain bestComboCur;
        BestComboValidChain bestComboFinal = new BestComboValidChain();
        for (int i = 0; i < listListValidChain.size(); i++) {
            LinkedList<ValidChain> validChains1 = listListValidChain.get(i);
            LinkedList<ValidChain> chains2;
            for (int j = i+1; j < listListValidChain.size(); j++) {
                chains2 = listListValidChain.get(j);
                bestComboCur = getBestComboBetweenListValidChains(validChains1, chains2);
                if (bestComboCur.getGainTot() > bestComboFinal.getGainTot()) {
                    bestComboFinal = bestComboCur;
                }
            }
        }
        return bestComboFinal.list;
    }

    /**
     *
     * @param chains1
     * @param chains2
     * @return
     */
    private static BestCombo getBestComboBetweenListChains(LinkedList<Chain> chains1, LinkedList<Chain> chains2) {
        int gainTot = 0;

        // triage des listes de chaines
        sortChain(chains1, "DESC");
        sortChain(chains2, "DESC");

        Chain chainChoisie1;
        Chain chainChoisie2 = new Chain();

        if(chains1.get(0).getGainMedSequence() >= chains2.get(0).getGainMedSequence()) {
            chainChoisie1 = chains1.get(0);
        }
        else {
            chainChoisie1 = chains2.get(0);
        }
        for(Chain v1 : chains1) {
            if( (v1.getGainMedSequence() + (chains2.get(0).getGainMedSequence())) < gainTot) {
                break;
            }
            for(Chain v2 : chains2) {
                if(v1.canBeCombined(v2)) {
                    int gainTemp = v1.getGainMedSequence() + v2.getGainMedSequence();
                    if(gainTemp > gainTot) {
                        gainTot = gainTemp;
                        chainChoisie1 = v1;
                        chainChoisie2 = v2;
                    }
                    break;
                }
            }
        }
        LinkedList<Chain> liste = new LinkedList<>();
        liste.add(chainChoisie1);
        if(chainChoisie2.getGainMedSequence() != 0) liste.add(chainChoisie2);
        return new BestCombo(liste, gainTot);
    }

    private static BestComboValidChain getBestComboBetweenListValidChains(LinkedList<ValidChain> chains1, LinkedList<ValidChain> chains2) {
        int gainTot = 0;

        // triage des listes de chaines
        sortValidChain(chains1, "DESC");
        sortValidChain(chains2, "DESC");

        ValidChain chainChoisie1;
        ValidChain chainChoisie2 = new ValidChain();

        if(chains1.get(0).getGain() >= chains2.get(0).getGain()) {
            chainChoisie1 = chains1.get(0);
        }
        else {
            chainChoisie1 = chains2.get(0);
        }
        for(ValidChain v1 : chains1) {
            if( (v1.getGain() + (chains2.get(0).getGain())) < gainTot) {
                break;
            }
            for(ValidChain v2 : chains2) {
                if(v1.canBeCombined(v2)) {
                    int gainTemp = v1.getGain() + v2.getGain();
                    if(gainTemp > gainTot) {
                        gainTot = gainTemp;
                        chainChoisie1 = v1;
                        chainChoisie2 = v2;
                    }
                    break;
                }
            }
        }
        LinkedList<ValidChain> liste = new LinkedList<>();
        liste.add(chainChoisie1);
        if(chainChoisie2.getGain() != 0) liste.add(chainChoisie2);
        return new BestComboValidChain(liste, gainTot);
    }

    /**
     * Retourne le meilleur combo entre les listes de chaines par altruiste
     * Ou la meilleure chaine de la liste s'il n'y en a qu'une seule
     * Ou une liste vide sinon
     * @param listChainsByAltruit la liste des listes de chaines valides par altruiste
     * @return une liste contenant (maximum) deux chaines valides
     */
    public static LinkedList<Chain> getBestCombo(LinkedList<LinkedList<Chain>> listChainsByAltruit) {
        LinkedList<Chain> liste = new LinkedList<>();

        if(listChainsByAltruit.size()>1) {
            liste = getBestComboAmongAltruists(listChainsByAltruit);
        }
        else if(listChainsByAltruit.size()==1) {
            liste.add(listChainsByAltruit.get(0).getFirst());
        }
        System.out.println(liste);
        return liste;
    }

    public static LinkedList<ValidChain> getBestComboValidChain(LinkedList<LinkedList<ValidChain>> listChainsByAltruit) {
        LinkedList<ValidChain> liste = new LinkedList<>();

        if(listChainsByAltruit.size()>1) {
            liste = getBestComboAmongAltruistsValidChain(listChainsByAltruit);
        }
        else if(listChainsByAltruit.size()==1) {
            liste.add(listChainsByAltruit.get(0).getFirst());
        }
        System.out.println(liste);
        return liste;
    }

    /**
     *
     * @param i
     * @param altruistsValid
     * @param pairsValid
     * @param maxDepth
     * @return
     */
    public static LinkedList<LinkedList<Chain>> getAllChainsFromTrees(Instance i, ArrayList<Altruist> altruistsValid, ArrayList<Pair> pairsValid, int maxDepth) {
        LinkedList<LinkedList<Chain>> listChainsByAltruit = new LinkedList<>();
        LinkedList<Chain> chains;
        if(maxDepth>8 && pairsValid.size()>99 && altruistsValid.size()>15) maxDepth = 3;
        if(maxDepth>8 && pairsValid.size()>99 && altruistsValid.size()>10) maxDepth = 4;
        if(maxDepth>8 && pairsValid.size()>99 && altruistsValid.size()>5) maxDepth = 5;
        System.out.println("maxDepth =" + maxDepth);
        for(Altruist a : altruistsValid) {
            Node n1 = new Node(a);
            // createTree(n1, maxDepth, pairsValid); // TODO
            chains = new LinkedList<>();
            extractChainsFromTree(a, i.getPairs(), n1, chains);
            if(!chains.isEmpty()) {
                sortChain(chains, "DESC");
                listChainsByAltruit.add(chains);
            }
        }
        return listChainsByAltruit;
    }

    public static LinkedList<LinkedList<ValidChain>> getAllValidChainsFromTrees(Instance i, ArrayList<Altruist> altruistsValid, ArrayList<Pair> pairsValid, int maxDepth) {

        LinkedList<LinkedList<ValidChain>> listValidChainsByAltruit = new LinkedList<>();
        LinkedList<ValidChain> validChains;

        if(altruistsValid.size() != 0) {
            //ceci est a adapter ! et modifier pour avoir des résultats concluants
            long tpsByTree = 7000 / altruistsValid.size();

            for(Altruist a : altruistsValid) {
                Node n1 = new Node(a);
                createTree(n1, maxDepth, pairsValid, tpsByTree);
                validChains = new LinkedList<>();
                extractValidChainsFromTree(n1, validChains);

                if(!validChains.isEmpty()) {
                    sortValidChain(validChains, "DESC");
                    listValidChainsByAltruit.add(validChains);
                }
            }
        }
        return listValidChainsByAltruit;
    }

    /**
     * Ajout des chaines dans la solution avec mise a jour du gain total de celle ci
     * @param solution la solution cible
     * @param chainsToAdd la liste des chaines à ajouter
     */
    public static void addChainsIntoSolution(Solution solution, LinkedList<Chain> chainsToAdd) {
        for(Chain chain : chainsToAdd) {
            System.out.println(chain);
            solution.getChains().addLast(chain);
        }
        solution.calculGainSolution();
    }

    public static void addValidChainsIntoSolution(Solution solution, LinkedList<ValidChain> validChainsToAdd) {
        for(ValidChain validChain : validChainsToAdd) {
            System.out.println(validChain);
            Altruist a = solution.getInstance().getAltruists().get(validChain.getIdList().getFirst());
            Chain c = new Chain(a);
            boolean add = false;
            for(int i : validChain.getIdList()) {
                if(add) {
                    Pair p = solution.getInstance().getPairs().get(i);
                    c.addPairToChain(p);
                }
                add = true;
            }
            solution.getChains().addLast(c);
        }
        solution.calculGainSolution();
    }

    /**
     * Création des arbres pour les 2 premiers altruistes, avec extraction des chaines
     * Et lancement de la fonction getBestComboBetweenListChains
     * @param instance l'instance
     * @param maxDepth la profondeur max (typiquement : instance.getMaxSizeChain())
     */
    private static void testBasicCreationTree(Instance instance, int maxDepth) {

        if(instance.getAltruists().size() < 2) return;
        Node n1 = new Node(instance.getAltruists().get(1));
        Node n2 = new Node(instance.getAltruists().get(2));

        System.out.println(n1);

        System.out.println("\n ------------ ARBRE... ");
        ArrayList<Pair> pairs = new ArrayList<>(instance.getPairs().values());
        createTree(n1, maxDepth, pairs, 100000);
        createTree(n2, maxDepth, pairs, 100000);

        System.out.println("\n ------------ CHAINES VALIDES... ");
        LinkedList<Chain> validChains1 = new LinkedList<>();
        extractChainsFromTree(instance.getAltruists().get(1), instance.getPairs(), n1, validChains1);
        System.out.println("validChains1.size() : " + validChains1.size());
        sortChain(validChains1, "DESC");
        System.out.println(validChains1.get(0));

        LinkedList<Chain> validChains2 = new LinkedList<>();
        extractChainsFromTree(instance.getAltruists().get(2), instance.getPairs(), n2, validChains2);
        System.out.println("validChains2.size() : " + validChains2.size());
        sortChain(validChains2, "DESC");
        System.out.println(validChains2.get(0));

        System.out.println("\n ------------ COMBO CHAINES... ");
        System.out.println("Les deux chaines peuvent elles se combiner ? " + validChains1.get(0).canBeCombined(validChains1.get(1)));
        System.out.println("Meilleur combo entre les deux listes de chaines valides :");
        System.out.println(getBestComboBetweenListChains(validChains1, validChains2));
    }

    /**
     * Création de tous les arbres (pour chaque altruiste) avec affichage de statistiques
     * @param instance l'instance
     * @param maxDepth la profondeur max (typiquement : instance.getMaxSizeChain())
     */
    private static void testGetAllChainWithTree(Instance instance, int maxDepth) {
        long lStartTime, lEndTime, output;
        LinkedList<Chain> validChains;
        ArrayList<Long> times = new ArrayList<>();
        ArrayList<Integer> maxGains = new ArrayList<>();
        ArrayList<Integer> sizes = new ArrayList<>();
        long totalTime = 0L;
        int totalGain = 0, totalSize = 0;


        ArrayList<Pair> pairs = new ArrayList<>(instance.getPairs().values());
        int i = 0;

        /*
        System.out.println("(before) pairs.size: " + instance.getPairs().size());
        ArrayList<Pair> pairs = new ArrayList<>();
        int middle = instance.getPairs().size() / 2;

        System.out.println("i: " + i + " - middle:" + middle);

        for(Pair p : instance.getPairs().values()) {
            if(i==middle) break;
            pairs.add(p);
            i++;
        }


        int max = instance.getPairs().size() / 2;
        int debut = (int) (Math.random() * max);
        System.out.println("debut: " + debut);

        for(Pair p : instance.getPairs().values()) {
            if(i>debut && i <= (debut+max)) pairs.add(p);
            i++;
        }

        System.out.println("(after) pairs.size: " + pairs.size());
        System.out.println(pairs);
        */

        i = 0;

        int nbAltruists = instance.getAltruists().size();
        // double tpsMaxMs = 0.6 * 180000; // 60% du temps total souhaité par arbre
                                // tps max total en ms pour la création de tous les arbres
                                // 180 000 <=> 3min
        // long tpsByTree =  (long) tpsMaxMs / nbAltruists; // tps par arbre
        long tpsByTree = 500;
        System.out.println("** tpsByTree: " + tpsByTree);

        for(Altruist a : instance.getAltruists().values()) {
            //i++;

            lStartTime = System.nanoTime();
            Node n1 = new Node(a);
            createTree(n1, maxDepth, pairs, tpsByTree);
            validChains = new LinkedList<>();
            // System.out.println("before extractChainsFromTree");
            extractChainsFromTree(a, instance.getPairs(), n1, validChains);
            sizes.add(validChains.size());
            totalSize += validChains.size();
            sortChain(validChains, "DESC");
            maxGains.add(validChains.get(0).getGainMedSequence());
            totalGain += validChains.get(0).getGainMedSequence();
            lEndTime = System.nanoTime();
            output = (lEndTime - lStartTime) / 1000000; // en ms
            times.add(output);
            totalTime += output;

            // if(i==2) break;
        }

        System.out.println("NbAltruists: " + instance.getNbAltruists());

        System.out.println("\n");
        System.out.println(instance.getName());
        System.out.println("MAX SIZE: " + maxDepth);
        System.out.println("-- Size --");
        System.out.println(sizes);
        System.out.println("Moyenne: " + totalSize / sizes.size());
        System.out.println("-- Max Gain --");
        System.out.println(maxGains);
        System.out.println("Moyenne: " + totalGain / maxGains.size());
        System.out.println("-- Temps --");
        System.out.println(times);
        System.out.println("Total : " + totalTime);
    }

    private static void testGetAllValidChainWithTree(Instance instance, int maxDepth, int timeByTree) {
        long lStartTime, lEndTime, output;
        LinkedList<ValidChain> validChains;
        ArrayList<Long> times = new ArrayList<>();
        ArrayList<Integer> maxGains = new ArrayList<>();
        ArrayList<Integer> sizes = new ArrayList<>();
        long totalTime = 0L;
        int totalGain = 0, totalSize = 0;

        ArrayList<Pair> pairs = new ArrayList<>(instance.getPairs().values());

        System.out.println("** tpsByTree: " + timeByTree);

        for(Altruist a : instance.getAltruists().values()) {

            lStartTime = System.nanoTime();
            Node n1 = new Node(a);
            createTree(n1, maxDepth, pairs, timeByTree);
            validChains = new LinkedList<>();
            // System.out.println("before extractChainsFromTree");
            extractValidChainsFromTree(n1, validChains);
            sizes.add(validChains.size());
            totalSize += validChains.size();
            sortValidChain(validChains, "DESC");
            maxGains.add(validChains.get(0).getGain());
            totalGain += validChains.get(0).getGain();
            lEndTime = System.nanoTime();
            output = (lEndTime - lStartTime) / 1000000; // en ms
            times.add(output);
            totalTime += output;
        }

        System.out.println("NbAltruists: " + instance.getNbAltruists());

        System.out.println("\n");
        System.out.println(instance.getName());
        System.out.println("MAX SIZE: " + maxDepth);
        System.out.println("-- Size --");
        System.out.println(sizes);
        System.out.println("Moyenne: " + totalSize / sizes.size());
        System.out.println("-- Max Gain --");
        System.out.println(maxGains);
        System.out.println("Moyenne: " + totalGain / maxGains.size());
        System.out.println("-- Temps --");
        System.out.println(times);
        System.out.println("Total : " + totalTime);
    }

    public static void main(String[] args) {
        try {
            InstanceReader reader = new InstanceReader("instances/KEP_p250_n83_k5_l17.txt");
            Instance instance = reader.readInstance();
            System.out.println(instance);

            // testBasicCreationTree(instance, instance.getMaxSizeChain());

            testGetAllValidChainWithTree(instance, instance.getMaxSizeChain(), 1000);
            // testGetAllValidChainWithTree(instance,6);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Node{" +
                "data=" + data +
                ", children=" + children +
                ", gain=" + gain +
                ", idSum=" + sumId +
                ", idList=" + idList +
                '}';
    }
}
