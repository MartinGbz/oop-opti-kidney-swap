package Solveur;

import Solution.Solution;
        import instance.Instance;
        import io.InstanceReader;
        import io.exception.ReaderException;

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
        return null;
    }

    public void solveBySolutionTriviale(Instance instance) {
        Solution s = this.solve(instance);
        System.out.println(this);
        System.out.println(s);
        // s.check();
    }

    public static void main(String[] args) {
        try {
            InstanceReader reader = new InstanceReader("instances/testInstance.txt"); //mettre le nom du fichier
            Instance instance = reader.readInstance();

            SolutionTriviale is = new SolutionTriviale();
            is.solveBySolutionTriviale(instance);

        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }


}
