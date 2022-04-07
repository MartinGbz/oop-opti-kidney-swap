package io;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class SolutionWriter {

    private String pathSolution;

    private String tab = "\t";
    private String backLine = "\n";

    public SolutionWriter(String path) {
        this.pathSolution = "testSolution/" + path + "_sol.txt";
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
        List<String> listInstances = new ArrayList<>();
        String path = "instanceA";
        String path1 = "instanceB";
        String path2 = "instanceC";

        listInstances.add(path);
        listInstances.add(path1);
        listInstances.add(path2);

        for(int i=0 ; i<listInstances.size() ; i++) {
            SolutionWriter sw = new SolutionWriter(listInstances.get(i));
            sw.writeSolution();
        }

    }


}
