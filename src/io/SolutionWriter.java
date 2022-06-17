package io;

import solution.Solution;
import instance.Instance;
import solution.*;
import instance.network.Base;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe d'écriture de la solution dans un fichier
 */
public class SolutionWriter {

    private final String pathSolution;

    private final String directorySolution;
    private final String tab = "\t";
    private final String backLine = "\n";

    //Constructor
    public SolutionWriter(Solution solution, String directorySolution) {
        String nomFicInst = stripExtension(solution.getInstance().getName());
        this.directorySolution = directorySolution;
        this.pathSolution = directorySolution + "/" + nomFicInst + "_sol";
        this.writeSolution(solution);
    }

    /**
     * Obtenir le nom du fichier sans l'extension (ici .txt)
     * @param str nom du fichier texte
     * @return String nom du fichier texte sans l'extension de format
     */
    static String stripExtension (String str) {
        if (str == null) return null;
        int pos = str.lastIndexOf(".");
        if (pos == -1) return str;
        return str.substring(0, pos);
    }

    /**
     * Création et écriture de la Solution obtenue dans un fichier de solution txt
     * @param solution Solution à écrire dans le fichier
     */
    public void writeSolution(Solution solution) {
        try {
            createFile();
            writeSolutionCost(solution.getGainMedTotal());
            writeDescriptionSolution(solution);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * Création du fichier de solution txt et répertoires si les répertoires contenant les solutions n'existent pas
     * @throws Exception
     */
    public void createFile() throws Exception {
        // Récupération du nom du fichier + "création"
        File f = new File(pathSolution + ".txt");

        File d = new File(directorySolution);
        if(!d.exists() || !d.isDirectory()){
            d.mkdirs();
        }
        if(f.isFile()) { // si le fichier existe deja
            f.delete(); // on le supprime
        }
        f.createNewFile(); //on créé le fichier dans le répertoire
    }

    /**
     * Ecriture du coût total de la solution dans le fichier de solution txt
     * @param gainMedicalSolution gain médical de l'ensemble de la solution
     * @throws Exception
     */
    public void writeSolutionCost(int gainMedicalSolution) throws Exception {
        FileWriter fw = new FileWriter(pathSolution +".txt",true);
        fw.write("// Cout total de la solution" + backLine);
        //TODO : aller chercher le vrai cout de la solution
        String gainMedSol = String.valueOf(gainMedicalSolution);
        fw.write(gainMedSol);
        fw.write(backLine + backLine);
        fw.close();
    }

    /**
     * Ecriture de l'ensemble de la solution (chaines+cycles)
     * @param solution Solution ()
     * @throws Exception
     */
    public void writeDescriptionSolution(Solution solution) throws Exception {
        FileWriter fw = new FileWriter(pathSolution + ".txt",true);
        fw.write("// Description de la solution" + backLine);
        fw.close();
        this.writeCycleSolution(solution.getCycles());
        this.writeChaineSolution(solution.getChains());
    }

    /**
     * Ecriture de la séquence d'un cycle/chaine dans le fichier de solution txt
     * @param fw instance du fichier de solution
     * @param c Sequence : éléments de la chaine ou du cycle à écrire dans le fichier de solution
     * @throws Exception
     */
    public void writeSequenceSolution(FileWriter fw, Sequence c) throws Exception {
        for(int i = 0 ; i < c.getSequence().size() ; i++) {
            String id = String.valueOf((c.getSequence().get(i)).getId());
            fw.write(id);
            if(i != (c.getSequence().size()-1))
                fw.write(tab);
        }
    }

    /**
     * Ecriture des cycles dans le fichier de solution txt
     * @param cycles liste des cycles à écrire dans le fichier de solution
     * @throws Exception
     */
    public void writeCycleSolution(LinkedList<Cycle> cycles) throws Exception {
        FileWriter fw = new FileWriter(pathSolution + ".txt",true);
        fw.write("// Cycles" + backLine);
        for(Sequence c : cycles) {
            this.writeSequenceSolution(fw, c);
            fw.write(backLine);
        }
        fw.write(backLine);
        fw.close();
    }

    /**
     * Ecriture des chaines dans le fichier de solution txt
     * @param chains liste des chaines à écrire dans le fichier de solution
     * @throws Exception
     */
    public void writeChaineSolution(LinkedList<Chain> chains) throws Exception {
        FileWriter fw = new FileWriter(pathSolution + ".txt",true);
        fw.write("// Chaines" + backLine);
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
