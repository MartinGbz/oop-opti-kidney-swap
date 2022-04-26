package io;

import instance.Instance;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class SolutionWriter {

    private String pathSolution;

    private String tab = "\t";
    private String backLine = "\n";

    public SolutionWriter(Instance instance) {
        this.pathSolution = "testSolution/" + instance.getName() + "_sol.txt";
    }

    public void writeSolution() {
        try {
            createFile();
            writeSolutionCost();
            writeDescriptionSolution();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void createFile() throws Exception {
        // Récupération du nom du fichier + "création"
        File f = new File(pathSolution);

        if(f.isFile()) { //si le fichier existe deja
            f.delete(); //on le supprime
        }
        f.createNewFile(); //on créé le fichier dans le répertoire
    }

    public void writeSolutionCost() throws Exception {
        FileWriter fw = new FileWriter(pathSolution,true);
        fw.write("// Cout total de la solution" + backLine);
        //TODO : aller chercher le vrai cout de la solution
        fw.write("18");
        fw.write(backLine + backLine);
        fw.close();
    }

    public void writeDescriptionSolution() throws Exception {
        FileWriter fw = new FileWriter(pathSolution,true);
        fw.write("// Description de la solution" + backLine);
        fw.close();
        this.writeCycleSolution();
        this.writeChaineSolution();
    }

    public void writeCycleSolution() throws Exception {
        FileWriter fw = new FileWriter(pathSolution,true);
        fw.write("// Cycles" + backLine);
        //TODO : faire une boucle sur les vrais cycles
        fw.write("5" + tab + "7" + tab + "6");
        fw.write(backLine + backLine);
        fw.close();
    }

    public void writeChaineSolution() throws Exception {
        FileWriter fw = new FileWriter(pathSolution,true);
        fw.write("// Chaines" + backLine);
        //TODO : faire une boucle sur les vraies chaines
        fw.write("1" + tab + "3");
        fw.write(backLine + backLine);
        fw.close();
    }

    public static void main(String[] args) {
        List<Instance> listInstances = new ArrayList<>();

        Instance inst1 = new Instance("instance1", 3, 1, 3, 3);
        Instance inst2 = new Instance("instance2", 2, 2, 3, 3);
        Instance inst3 = new Instance("instance3", 4, 1, 3, 3);

        listInstances.add(inst1);
        listInstances.add(inst2);
        listInstances.add(inst3);

        for(int i=0 ; i<listInstances.size() ; i++) {
            SolutionWriter sw = new SolutionWriter(listInstances.get(i));
            sw.writeSolution();
        }

    }


}
