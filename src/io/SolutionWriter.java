package io;

import Solution.Solution;
import instance.Instance;
import Solution.*;
import instance.network.Base;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SolutionWriter {

    private String pathSolution;

    private String tab = "\t";
    private String backLine = "\n";

    public SolutionWriter(Solution solution) {
        String nomFicInst = stripExtension(solution.getInstance().getName());
        this.pathSolution = "testSolution/" + nomFicInst + "_sol";
        this.writeSolution(solution);
    }

    static String stripExtension (String str) {
        if (str == null) return null;
        int pos = str.lastIndexOf(".");
        if (pos == -1) return str;
        return str.substring(0, pos);
    }

    public void writeSolution(Solution solution) {
        try {
            createFile();
            writeSolutionCost(solution.getGainMedTotal());
            writeDescriptionSolution(solution);
        } catch (Exception e) {
            System.err.println(e);
        }
    }


    public void createFile() throws Exception {
        // Récupération du nom du fichier + "création"
        File f = new File(pathSolution + ".txt");

        if(f.isFile()) { //si le fichier existe deja
            f.delete(); //on le supprime
        }
        f.createNewFile(); //on créé le fichier dans le répertoire
    }

    public void writeSolutionCost(int gainMedicalSolution) throws Exception {
        FileWriter fw = new FileWriter(pathSolution +".txt",true);
        fw.write("// Cout total de la solution" + backLine);
        //TODO : aller chercher le vrai cout de la solution
        String gainMedSol = String.valueOf(gainMedicalSolution);
        fw.write(gainMedSol);
        fw.write(backLine + backLine);
        fw.close();
    }

    public void writeDescriptionSolution(Solution solution) throws Exception {
        FileWriter fw = new FileWriter(pathSolution + ".txt",true);
        fw.write("// Description de la solution" + backLine);
        fw.close();
        this.writeCycleSolution(solution.getCycles());
        this.writeChaineSolution(solution.getChains());
        //this.writeSequenceSolution(solution);
    }

    public void writeSequenceSolution(FileWriter fw, Sequence c) throws Exception {
        for(int i = 0 ; i < c.getSequence().size() ; i++) {
            String id = String.valueOf(((Base) c.getSequence().get(i)).getId());
            fw.write(id);
            if(i != (c.getSequence().size()-1))
                fw.write(tab);
        }
    }

    public void writeCycleSolution(LinkedList<Cycle> cycles) throws Exception {
        FileWriter fw = new FileWriter(pathSolution + ".txt",true);
        fw.write("// Cycles" + backLine);
        //TODO : faire une boucle sur les vrais cycles
        for(Sequence c : cycles) {
            this.writeSequenceSolution(fw, c);
            fw.write(backLine);
        }
        fw.write(backLine);
        fw.close();
    }

    public void writeChaineSolution(LinkedList<Chain> chains) throws Exception {
        FileWriter fw = new FileWriter(pathSolution + ".txt",true);
        fw.write("// Chaines" + backLine);
        //TODO : faire une boucle sur les vraies chaines
        for(Chain c : chains) {
            this.writeSequenceSolution(fw, c);
            if(!c.equals(chains.getLast()))
                fw.write(backLine);
        }
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
        /*
        for(int i=0 ; i<listInstances.size() ; i++) {
            SolutionWriter sw = new SolutionWriter();
            sw.writeSolution();
        }
        */


    }


}
