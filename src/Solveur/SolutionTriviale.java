package Solveur;

import Solution.*;
import instance.*;
import instance.network.Altruist;
import instance.network.Pair;
import io.InstanceReader;
import io.SolutionWriter;
import io.exception.ReaderException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class SolutionTriviale implements Solveur {

    private final String name = "Insersion simple";

    @Override
    public String toString() {
        return "InsertionSimple{" +
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
        boolean status;

        for(Map.Entry altruistEntry : s.getInstance().getAltruists().entrySet()) {
            Chain ch = new Chain((Altruist) altruistEntry.getValue());
            s.getChains().addLast(ch);
        }
        for(Map.Entry pairEntry : s.getInstance().getPairs().entrySet()) {
            status = false;
            for(Chain chain : s.getChains()) {
                if(chain.getSequence().size() < s.getInstance().getMaxSizeChain())
                    status = chain.addPairToChain((Pair) pairEntry.getValue());
                if(status) break;
            }
        }
        for(Map.Entry pairEntry : s.getInstance().getPairs().entrySet()) {
            status = false;
            Pair p = (Pair) pairEntry.getValue();
            if(!isUsedInChain(p,s)) {
                for(Cycle cycle : s.getCycles()) {
                    if(cycle.getSequence().size() < s.getInstance().getMaxSizeCycle()) {
                        status = cycle.addPairToCycleEnd(p);
                        if(status) break;
                    }
                }
                if(!status) {
                    s.addPairNewCycle(p);
                }
            }
        }
        s.deleteSequenceNotUsed();
        s.calculGainSolution();
        return s;
    }

    public boolean isUsedInChain(Pair pair, Solution solution) {
        for(Chain c : solution.getChains()) {
            if(c.getSequence().contains(pair)) {
                return true;
            }
        }
        return false;
    }

    public void solveBySolutionTriviale(Instance instance, String directorySolution) {
        Solution s = this.solve(instance);
        System.out.println(this);
        System.out.println(s);
        // s.check();
        System.out.println("Etat du check : " + s.check());
        SolutionWriter sw = new SolutionWriter(s, directorySolution);
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
                    is.solveBySolutionTriviale(instance, directorySolution);
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
