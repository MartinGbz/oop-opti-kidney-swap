package solution;

import instance.Instance;
import instance.network.Altruist;
import instance.network.Base;
import instance.network.Pair;
import io.InstanceReader;

import java.util.*;

public class Node {

    private Base data = null;
    private final List<Node> children = new ArrayList<>();
    private Node parent = null;
    private int gain = 0;
    private int sumId;
    private ArrayList<Integer> idList = new ArrayList<Integer>();

    public Node(Base data) {
        this.data = data;
        this.idList.add(data.getId());
        this.sumId = data.getId();
    }

    public void addChild(Node child, int tailleMax) {
        Base baseParent = this.data;
        Base baseChild = child.data;

        if(this.idList.size() >= tailleMax) {
            // System.out.println("taille max atteinte");
            return;
        }

        if(!baseParent.isCompatible(baseChild)) {
            // System.out.println("parent to child non compatible");
            return;
        }

        child.setParent(this);
        child.idList = new ArrayList<>(this.idList);

        if(child.idList.contains(child.data.getId())) {
            // System.out.println("child deja preesent dans cette branche");
            return;
        }

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
    private void setParent(Node parent) {
        this.parent = parent;
    }

    /**
     * Trie la liste des chaines valides
     * @param validChains La liste des chaines à trier
     * @param sortingDirection "DESC" ou "ASC" ("DESC" par défaut si argument incorrect)
     */
    private static void sortChain(LinkedList<ValidChain> validChains, String sortingDirection) {
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

    private static void extractChainsFromTree(Node n, LinkedList<ValidChain> listChain) {
        if(n.getChildren().isEmpty()) {
            listChain.add(new ValidChain(n.gain, n.idList));
            return;
        }
        for (Node nBis: n.getChildren()) {
            extractChainsFromTree(nBis, listChain);
        }
    }

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
     */
    private static void createTree(Node racine, int maxSizeChain, ArrayList<Pair> pairs) {
        for (Pair p: pairs) {
            racine.addChild(new Node(p), maxSizeChain);
        }
        if(racine.getChildren().size() != 0) {
            for(Node nBis : racine.getChildren()) {
                createTree(nBis, maxSizeChain, pairs);
            }
        }
    }

    private static LinkedList<ValidChain> getComboChains(LinkedList<ValidChain> chains1, LinkedList<ValidChain> chains2) {
        int gainTot = 0;

        // triage des listes de chaines
        sortChain(chains1, "DESC");
        sortChain(chains2, "DESC");

        ValidChain chainChoisie1;
        ValidChain chainChoisie2 = new ValidChain();

        if(chains1.get(0).getGain() >= chains2.get(0).getGain()) {
            chainChoisie1 = chains1.get(0);
        }
        else {
            chainChoisie1 = chains2.get(0);
        }

        System.out.println("\n-- debut fct -- ");
        // System.out.println(chains1);
        // System.out.println(chains2);

        System.out.println("\n-- début boucles");

        for(ValidChain v1 : chains1) {
            for(ValidChain v2 : chains2) {
                if(v1.canBeCombined(v2)) {
                    int gainTemp = v1.getGain() + v2.getGain();
                    if(gainTemp > gainTot) {
                        gainTot = gainTemp;
                        chainChoisie1 = v1;
                        chainChoisie2 = v2;
                    }
                }
            }
        }

        LinkedList<ValidChain> liste = new LinkedList<>();
        liste.add(chainChoisie1);
        if(chainChoisie2.getGain() != 0) liste.add(chainChoisie2);
        return liste;
    }

    public static void main(String[] args) {
        try {

            // args
            // -inst instances/KEP_p9_n1_k3_l3.txt -dSol testSolution

            InstanceReader reader = new InstanceReader("instances/KEP_p100_n5_k3_l13.txt");
            Instance instance = reader.readInstance();
            System.out.println(instance);

            // int maxSize = instance.getMaxSizeChain();
            int maxSize = 11;
            long lStartTime, lEndTime, output;
            LinkedList<ValidChain> validChains;
            ArrayList<Long> times = new ArrayList<>();
            ArrayList<Integer> maxGains = new ArrayList<>();
            ArrayList<Integer> sizes = new ArrayList<>();
            Long totalTime = 0L;
            int totalGain = 0, totalSize = 0;

            for(Altruist a : instance.getAltruists()) {
                lStartTime = System.nanoTime();
                // System.out.println("\n--- Altruist id : " + a.getId());
                Node n1 = new Node(a);
                createTree(n1, maxSize, instance.getPairs());
                validChains = new LinkedList<>();
                extractChainsFromTree(n1, validChains);
                // System.out.println("size: " + validChains.size());
                sizes.add(validChains.size());
                totalSize += validChains.size();
                sortChain(validChains, "DESC");
                // System.out.println("gain max: " + validChains.get(0).getGain());
                maxGains.add(validChains.get(0).getGain());
                totalGain += validChains.get(0).getGain();
                lEndTime = System.nanoTime();
                output = lEndTime - lStartTime;
                // System.out.println("elapsed time in milliseconds: " + output / 1000000);
                times.add(output / 1000000);
                totalTime += output / 1000000;
            }

            System.out.println("\n");
            System.out.println(instance.getName());
            System.out.println("MAX SIZE: " + maxSize);
            System.out.println("-- Size --");
            System.out.println(sizes);
            System.out.println("Moyenne: " + totalSize / instance.getNbAltruists());
            System.out.println("-- Max Gain --");
            System.out.println(maxGains);
            System.out.println("Moyenne: " + totalGain / instance.getNbAltruists());
            System.out.println("-- Temps --");
            System.out.println(times);
            System.out.println("Total : " + totalTime);

            /*
            Node n1 = new Node(instance.getAltruists().get(0));
            Node n2 = new Node(instance.getAltruists().get(1));

            System.out.println(n1);

            System.out.println("\n ------------ ARBRE... ");
            // createTree(n1, instance.getMaxSizeChain(), instance.getPairs());
            // createTree(n2, instance.getMaxSizeChain(), instance.getPairs());
            createTree(n1, 7, instance.getPairs());
            createTree(n2, 7, instance.getPairs());

            // printTree(n1, " ");

            System.out.println("\n ------------ CHAINES VALIDES... ");

            LinkedList<ValidChain> validChains1 = new LinkedList<>();
            extractChainsFromTree(n1, validChains1);
            System.out.println("validChains1.size() : " + validChains1.size());
            sortChain(validChains1, "DESC");
            System.out.println(validChains1.get(0));

            LinkedList<ValidChain> validChains2 = new LinkedList<>();
            extractChainsFromTree(n2, validChains2);
            System.out.println("validChains2.size() : " + validChains2.size());
            sortChain(validChains2, "DESC");
            System.out.println(validChains2.get(0));


            System.out.println("\n ------------ COMBO CHAINES... ");

            // System.out.println(validChains1.get(0).canBeCombined(validChains1.get(1)));

            // System.out.println(getComboChains(validChains1, validChains2));
            */

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
