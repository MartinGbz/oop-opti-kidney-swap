package Solution;

import instance.Instance;
import instance.network.Base;
import instance.network.Pair;
import io.InstanceReader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Node {

    private Base data = null;
    private List<Node> children = new ArrayList<>();
    private Node parent = null;
    private int gain = 0;
    private int sumId;
    private ArrayList<Integer> idList = new ArrayList<Integer>();

    public Node(Base data) {
        this.data = data;
        this.idList.add(data.getId());
        this.sumId = data.getId();
    }

    public boolean addChild(Node child, int tailleMax) {
        Base baseParent = this.data;
        Base baseChild = child.data;

        if(this.idList.size() >= tailleMax) {
            System.out.println("taille max atteinte");
            return false;
        }

        if(!baseParent.isCompatible(baseChild)) {
            System.out.println("parent to child non compatible");
            return false;
        }

        child.setParent(this);
        child.idList = new ArrayList<>(this.idList);

        if(child.idList.contains(child.data.getId())) {
            System.out.println("child deja preesent dans cette branche");
            return false;
        }

        child.idList.add(child.data.getId());
        child.sumId += this.sumId;
        child.gain = this.gain + baseParent.getGainVers((Pair) baseChild);
        // this.idList = null;
        this.children.add(child);

        return true;
    }

    public List<Node> getChildren() {
        return children;
    }

    public Base getData() {
        return data;
    }

    public void setData(Base data) {
        this.data = data;
    }

    private void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }

    private static void printTree(Node node, String appender) {
        System.out.print(appender + node.getData());
        System.out.println(" - gain : " + node.gain);
        node.getChildren().forEach(each ->  printTree(each, appender + appender));
    }

    private static void createTree(Node n, Instance i) {

        // TODO : reduire l'instance à taille max et liste des paires

        for (Pair p: i.getPairs()) {
            n.addChild(new Node(p), i.getMaxSizeChain());
        }
        if(n.getChildren().size() != 0) {
            for(Node nBis : n.getChildren()) {
                createTree(nBis, i);
            }
        }
    }

    public static void main(String[] args) {
        try {
            String filenameInstance;
            String directorySolution;

            if (args.length == 4) {

                System.out.println(args[0]);
                System.out.println(args[1]);
                System.out.println(args[2]);
                System.out.println(args[3]);

                if (args[0].equals("-inst")) {
                    filenameInstance = args[1];
                } else if (args[2].equals("-inst")) {
                    filenameInstance = args[3];
                } else {
                    throw new Error("Paramètre -inst manquant");
                }

                if (args[0].equals("-dSol")) {
                    directorySolution = args[1];
                } else if (args[2].equals("-dSol")) {
                    directorySolution = args[3];
                } else {
                    throw new Error("Paramètre -dSol manquant");
                }


                if (Files.isRegularFile(Path.of(filenameInstance))) {

                    InstanceReader reader = new InstanceReader(filenameInstance); //mettre le nom du fichier
                    Instance instance = reader.readInstance();
                    System.out.println(instance);

                    Node n1 = new Node(instance.getAltruists().get(0));
                    System.out.println(n1);


                    createTree(n1, instance);
                    printTree(n1, " ");

                    LinkedList<ValidChain> validChains = new LinkedList<>();
                    extractChainsFromTree(n1, validChains);
                    System.out.println(validChains);
                    System.out.println(getBetterChain(validChains));
                    System.out.println(validChains);

                } else {
                    throw new Error("Fichier introuvable");
                }
            } else {
                throw new Error("Paramètres manquants");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static ValidChain getBetterChain(LinkedList<ValidChain> validChains) {
        validChains.sort(new Comparator<>() {
            @Override
            public int compare(final ValidChain record1, final ValidChain record2) {
                return record1.compareTo(record2);
            }
        });
        return validChains.getLast();
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
