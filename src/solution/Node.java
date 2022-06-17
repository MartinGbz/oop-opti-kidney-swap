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
     * TODO
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
     * TODO
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

        @Override
        public String toString() {
            return "BestComboValidChain{" +
                    "list=" + list +
                    ", gainTot=" + gainTot +
                    '}';
        }
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

    private static BestComboValidChain getBestComboBetweenListValidChains(LinkedList<ValidChain> chains1, LinkedList<ValidChain> chains2) {
        int gainTot = 0;

        ValidChain chainChoisie1;
        ValidChain chainChoisie2 = new ValidChain();

        double percentage = 0.4;
        double limite = Integer.MAX_VALUE; // à modifier si besoin

        double limit1 = chains1.size();
        if(limit1 > limite) {
            limit1 = limite;
        }

        double limit2 = chains2.size();
        if(limit2 > limite) {
            limit2 = limite;
        }

        if(chains1.get(0).getGain() >= chains2.get(0).getGain()) {
            chainChoisie1 = chains1.get(0);
        }
        else {
            chainChoisie1 = chains2.get(0);
        }

        gainTot = chainChoisie1.getGain();

        for(int i = 0; i < limit1; i++) {
            ValidChain vC1 = chains1.get(i);
            if( (chains1.get(i).getGain() + (chains2.get(0).getGain())) < gainTot) {
                break;
            }
            for(int j = 0; j < limit2; j++) {
                ValidChain vC2 = chains2.get(j);
                if(vC1.canBeCombined(vC2)) {
                    int gainTemp = vC1.getGain() + vC2.getGain();
                    if(gainTemp > gainTot) {
                        gainTot = gainTemp;
                        chainChoisie1 = vC1;
                        chainChoisie2 = vC2;
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


    public static LinkedList<ValidChain> getBestComboValidChain(LinkedList<LinkedList<ValidChain>> listChainsByAltruit) {
        LinkedList<ValidChain> liste = new LinkedList<>();
        int numberOfList = listChainsByAltruit.size();

        if(numberOfList>1) {
            liste = getBestComboAmongAltruistsValidChain(listChainsByAltruit);
        }
        else if(numberOfList==1) {
            liste.add(listChainsByAltruit.get(0).getFirst());
        }
        System.out.println(liste);
        return liste;
    }

    /**
     * TODO
     * @param altruistsValid
     * @param pairsValid
     * @param maxDepth
     * @return
     */
    public static LinkedList<LinkedList<ValidChain>> getAllValidChainsFromTrees(ArrayList<Altruist> altruistsValid, ArrayList<Pair> pairsValid, int maxDepth) {

        LinkedList<LinkedList<ValidChain>> listValidChainsByAltruit = new LinkedList<>();
        LinkedList<ValidChain> validChains;

        if(altruistsValid.size() != 0) {

            long tpsByTree = 7000 / altruistsValid.size(); // à adapter ! et modifier pour avoir des résultats concluants

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
     * Converti les objets ValidChain en Chain et les insère dans la solution
     * @param solution la solution cible
     * @param validChainsToAdd la liste des ValidChain
     */
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
     * FONCTION DE TEST
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
        createTree(n1, maxDepth, pairs, 10000);
        createTree(n2, maxDepth, pairs, 10000);

        System.out.println("\n ------------ CHAINES VALIDES... ");
        LinkedList<ValidChain> validChains1 = new LinkedList<>();
        extractValidChainsFromTree(n1, validChains1);
        System.out.println("validChains1.size() : " + validChains1.size());
        sortValidChain(validChains1, "DESC");
        System.out.println(validChains1.get(0));

        LinkedList<ValidChain> validChains2 = new LinkedList<>();
        extractValidChainsFromTree(n2, validChains2);
        System.out.println("validChains2.size() : " + validChains2.size());
        sortValidChain(validChains2, "DESC");
        System.out.println(validChains2.get(0));

        System.out.println("\n ------------ COMBO CHAINES... ");
        System.out.println("Les deux chaines peuvent elles se combiner ? " + validChains1.get(0).canBeCombined(validChains1.get(1)));
        System.out.println("Meilleur combo entre les deux listes de chaines valides :");
        System.out.println(getBestComboBetweenListValidChains(validChains1, validChains2));
    }

    /**
     * FONCTION DE TEST
     * Pour chaque altruiste de l'instance & chaque paire, lance la création de l'arbre
     * TODO
     * @param instance l'objet instance
     * @param maxDepth la taille maximale des chaines (= profondeur maximale des arbres)
     * @param timeByTree le temps accordé pour la création de chaque arbre
     */
    private static void testGetAllValidChainWithTree(Instance instance, int maxDepth, int timeByTree) {
        long lStartTime, lEndTime, output;
        LinkedList<ValidChain> validChains;
        ArrayList<Long> times = new ArrayList<>();
        ArrayList<Integer> maxGains = new ArrayList<>();
        ArrayList<Integer> sizes = new ArrayList<>();
        long totalTime = 0L;
        int totalGain = 0, totalSize = 0;

        ArrayList<Pair> pairs = new ArrayList<>(instance.getPairs().values());
        LinkedList<LinkedList<ValidChain>> listValidChainsByAltruit = new LinkedList<>();

        System.out.println("** tpsByTree: " + timeByTree);

        for(Altruist a : instance.getAltruists().values()) {

            lStartTime = System.nanoTime();
            Node n1 = new Node(a);
            createTree(n1, maxDepth, pairs, timeByTree);
            validChains = new LinkedList<>();
            extractValidChainsFromTree(n1, validChains);
            sizes.add(validChains.size());
            totalSize += validChains.size();
            sortValidChain(validChains, "DESC");
            maxGains.add(validChains.get(0).getGain());
            totalGain += validChains.get(0).getGain();
            lEndTime = System.nanoTime();
            listValidChainsByAltruit.add(validChains);
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

        lStartTime = System.nanoTime();
        System.out.println(Node.getBestComboValidChain(listValidChainsByAltruit));
        lEndTime = System.nanoTime();
        output = (lEndTime - lStartTime) / 1000000; // en ms
        System.out.println("Temps getBestComboValidChain: " + output);

    }

    public static void main(String[] args) {
        try {
            InstanceReader reader = new InstanceReader("instances/KEP_p50_n3_k3_l4.txt");
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
