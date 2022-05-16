package Solution;

import instance.Instance;
import instance.network.Base;
import io.InstanceReader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Node {

    private Base data = null;

    private List<Node> children = new ArrayList<>();

    private Node parent = null;

    private int gain = -1;

    private int idSum = -1;

    private ArrayList<Integer> idList = new ArrayList<Integer>();

    public Node(Base data) {
        this.data = data;
        // TODO : verifier ajout possible
        if(this.parent != null){
            this.idList = new ArrayList<>(this.parent.idList);
            this.parent.idList = null;
        }
        this.idList.add(data.getId());
    }

    public Node addChild(Node child) {
        child.setParent(this);
        this.children.add(child);
        return child;
    }

    public void addChildren(List<Node> children) {
        children.forEach(each -> each.setParent(this));
        this.children.addAll(children);
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

    private static <T> void printTree(Node node, String appender) {
        System.out.println(appender + node.getData());
        node.getChildren().forEach(each ->  printTree(each, appender + appender));
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

                    Node n1 = new Node(instance.getPairs().get(0));
                    System.out.println(n1);
                    n1.addChild(new Node(instance.getPairs().get(1)));
                    System.out.println(n1);

//                    for (Pair p: instance.getPairs()) {
//
//                    }

//                    SolutionTriviale is = new SolutionTriviale();
//                    is.solveBySolutionTriviale(instance, directorySolution);
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

    @Override
    public String toString() {
        return "Node{" +
                "data=" + data +
                ", children=" + children +
                ", gain=" + gain +
                ", idSum=" + idSum +
                ", idList=" + idList +
                '}';
    }
}
