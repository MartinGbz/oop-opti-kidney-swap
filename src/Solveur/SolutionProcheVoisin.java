package Solveur;

import Solution.Solution;
import instance.Instance;
import io.InstanceReader;
import io.exception.ReaderException;

public class SolutionProcheVoisin  implements Solveur {

    private final String name = "Proche Voisin";

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
        return null;
    }

    public static void main(String[] args) {
        try {
            InstanceReader reader = new InstanceReader("instances/KEP_p50_n3_k3_l13.txt"); //mettre le nom du fichier
            Instance instance = reader.readInstance();

            SolutionProcheVoisin is = new SolutionProcheVoisin();
            //is.solveBySolutionTriviale(instance);


        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
