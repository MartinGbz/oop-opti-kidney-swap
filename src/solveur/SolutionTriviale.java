package solveur;

import solution.*;
import instance.*;
import io.InstanceReader;
import io.SolutionWriter;


import java.nio.file.Files;
import java.nio.file.Path;

public class SolutionTriviale implements Solveur {

    private final String name = "Solution Triviale";

    @Override
    public String toString() {
        return "Solution Triviale{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public String getNom() {
        return this.name;
    }

    @Override
    public Solution solve(Instance i) {
        Solution s = new Solution(i);
        s.createChainsWithAltruists(i);
        s.addPairsIntoChains(i);
        s.addPairsIntoCycles(i);
        s.deleteSequenceNotUsed();
        s.calculGainSolution();
        return s;
    }

    public Solution solveBySolutionTriviale(Instance instance, String directorySolution) {
        Solution s = this.solve(instance);
        System.out.println(this);
        System.out.println(s);
        System.out.println("Etat du check interne : " + s.check());
        SolutionWriter sw = new SolutionWriter(s, directorySolution);
        return s;
    }

    public static void main(String[] args) {
        try {
            String filenameInstance;
            String directorySolution;

            if(args.length==4){

                System.out.println(args[0]);
                System.out.println(args[1]);
                System.out.println(args[2]);
                System.out.println(args[3]);

                if(args[0].equals("-inst")){
                    filenameInstance = args[1];
                }
                else if(args[2].equals("-inst")){
                    filenameInstance = args[3];
                }
                else{
                    throw new Error("Paramètre -inst manquant");
                }

                if(args[0].equals("-dSol")){
                    directorySolution = args[1];
                }
                else if(args[2].equals("-dSol")){
                    directorySolution = args[3];
                }
                else{
                    throw new Error("Paramètre -dSol manquant");
                }


                if(Files.isRegularFile(Path.of(filenameInstance))){

                    InstanceReader reader = new InstanceReader(filenameInstance); //mettre le nom du fichier
                    Instance instance = reader.readInstance();

                    SolutionTriviale is = new SolutionTriviale();
                    Solution s = is.solveBySolutionTriviale(instance, directorySolution);
                }
                else{
                    throw new Error("Fichier introuvable");
                }
            }
            else{
                throw new Error("Paramètres manquants");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


}
