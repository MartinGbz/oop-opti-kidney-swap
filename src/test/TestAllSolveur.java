package test;

import Solution.Solution;
import Solveur.Solveur;
import instance.Instance;
import io.InstanceReader;
import io.exception.ReaderException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import Solveur.SolutionTriviale;


public class TestAllSolveur{
    /**
     * Tous les solveurs a tester et comparer
     */
    private final List<Solveur> solveurs;
    /**
     * Nom du chemin du repertoire dans lequel se trouvent les instances a tester
     */
    private String pathRepertoire;
    /**
     * Toutes les instances a tester
     */
    private List<Instance> instances;
    /**
     * Resultats obtenus pour chaque couple instance/solveur
     */
    private Map<InstanceSolveur, Resultat> resultats;
    /**
     * Somme (sur les instances) des resultats pour chaque solveur
     */
    private Map<Solveur, Resultat> totalStats;

    /**
     * Constructeur par donnees.
     *
     * @param pathRepertoire le chemin du repertoire ou se trouvent toutes les
     *                       instances a tester
     */
    public TestAllSolveur(String pathRepertoire) {
        this.pathRepertoire = pathRepertoire;
        solveurs = new ArrayList<>();
        instances = new ArrayList<>();
        this.resultats = new HashMap<>();
        this.addSolveurs();
        this.readNomInstances();
        this.totalStats = new HashMap<>();
        for (Solveur solveur : solveurs) {
            totalStats.put(solveur, new Resultat());
        }
    }

    /**
     * Ajout de tous les solveurs que l'on souhaite comparer
     */
    private void addSolveurs() {
        // TO CHECK : constructeur par defaut de la classe InsertionSimple
        solveurs.add(new SolutionTriviale());
    }


    /**
     * Lecture de tous les noms des instances a tester.
     * Ces instances se trouvent dans le repertoire pathRepertoire.
     * Les instances sont lues et chargees en memoire.
     */
    private void readNomInstances(){
        File folder = new File(pathRepertoire);
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                try {
                    // TO CHECK : constructeur de InstanceReader
                    InstanceReader reader = new InstanceReader(file.getAbsolutePath());
                    // TO CHECK : lecture d'une instance avec la classe InstanceReader
                    instances.add(reader.readInstance());
                } catch (ReaderException ex) {
                    System.out.println("L'instance " + file.getAbsolutePath()
                            + " n'a pas pu etre lue correctement");
                }
            }
        }
    }

    /**
     * Affichage de tous les resultats.
     * Les resultats sont affiches dans un fichier csv avec separateur ';'.
     * @param nomFichierResultats nom du fichier de resultats
     */
    public void printAllResultats(String nomFichierResultats){
        PrintWriter ecriture = null;
        try {
            ecriture = new PrintWriter(nomFichierResultats + ".csv");
            printEnTetes(ecriture);
            for (Instance inst : instances) {
                printResultatsInstance(ecriture, inst);
            }
            ecriture.println();
            printSommeResultats(ecriture);
        } catch (IOException ex) {
            System.out.println("Erreur fichier ecriture");
            System.out.println(ex);
        }
        if (ecriture != null) {
            ecriture.close();
        }
    }

    /**
     * Affichage de la somme des resultats par solveur.
     * Les valeurs sont ecrites sur une seule ligne.
     * @param ecriture le writer sur lequel on fait l'ecriture
     */
    private void printSommeResultats(PrintWriter ecriture){
        ecriture.print("SOMME");
        for (Solveur solveur : solveurs) {
            ecriture.print(";" + totalStats.get(solveur).formatCsv());
        }
    }

    /**
     * Ecriture des resultats d'une instance pour tous les solveurs.
     * Pour chque solveur, l'instance est resolue par le solveeur avant que
     * ses resultats ne soient ecrits sur le fichier.
     * @param ecriture le writer sur lequel on fait l'ecriture
     * @param inst l'instane pour laquelle on ecrit les resultats
     */
    private void printResultatsInstance (PrintWriter ecriture, Instance inst){
        // TO CHECK : recuperer le nom de l'instance
        ecriture.print(inst.getName());
        for (Solveur solveur : solveurs) {
            long start = System.currentTimeMillis();
            // TO CHECK : resolution de l'instance avec le solveur
            Solution sol = solveur.solve(inst);
            long time = System.currentTimeMillis() - start;
            // TO CHECK : recperer le cout total de la solution, et savoir si
            // la solution est valide
            System.out.println("Cout total de la solution " + sol.getGainMedTotal());
            Resultat result = new Resultat(sol.getGainMedTotal(), time, sol.check());
            resultats.put(new InstanceSolveur(inst, solveur), result);
            ecriture.print(";" + result.formatCsv());
            totalStats.get(solveur).add(result);
        }
        ecriture.println();
    }

    /**
     * Eriture des en-tetes dans le fichier de resultats.
     * @param ecriture le writer sur lequel on fait l'ecriture
     */
    private void printEnTetes(PrintWriter ecriture){
        for (Solveur solveur : solveurs) {
            // TO CHECK : recuperer le nom du solveur
            ecriture.print(";" + solveur.getNom() + ";;");
        }
        ecriture.println();
        for (Solveur solveur : solveurs) {
            ecriture.print(";cout");
            ecriture.print(";tps(ms)");
            ecriture.print(";valide ?");
        }
        ecriture.println();
    }

    /**
     * Cette classe interne represente le couple instance / solveur
     */
    class InstanceSolveur {
        private Instance instance;
        private Solveur solveur;

        public InstanceSolveur(Instance instance, Solveur solveur) {
            this.instance = instance;
            this.solveur = solveur;
        }

        public Instance getInstance() {
            return instance;
        }

        public Solveur getSolveur() {
            return solveur;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 29 * hash + Objects.hashCode(this.instance);
            hash = 29 * hash + Objects.hashCode(this.solveur.getNom());
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final InstanceSolveur other = (InstanceSolveur) obj;
            if (!Objects.equals(this.instance, other.instance)) {
                return false;
            }
            if (!Objects.equals(this.solveur.getNom(), other.solveur.getNom())) {
                return false;
            }
            return true;
        }
    }

    /**
     * Cette classe interne represente le resultat a ecrire lorsqu'une instance
     * a ete resolue.
     */
    class Resultat {
        /**
         * Le cout de la solution
         */
        private int cout;
        /**
         * Le temps de resolution en millisecondes
         */
        private long timeInMs;
        /**
         * Indique si la solution est valide (true) ou non (false)
         */
        private boolean check;

        /**
         * Construteur par defaut
         */
        public Resultat() {
            this.cout = 0;
            this.timeInMs = 0;
            this.check = true;
        }

        /**
         * Construteur par donnes
         * @param cout le cout de la solution
         * @param timeInMs le temps de resolution en millisecondes
         * @param check vrai si la solution est valide, faux sinon
         */
        public Resultat(int cout, long timeInMs, boolean check) {
            this.cout = cout;
            this.timeInMs = timeInMs;
            this.check = check;
        }

        /**
         * Ajout d'un resultat pour faire la somme
         * @param resultat le resultat a ajouter
         */
        public void add(Resultat resultat) {
            this.cout += resultat.cout;
            this.timeInMs += resultat.timeInMs;
            this.check = this.check && resultat.check;
        }

        /**
         * @return le resultat formatte avec separateur ';' pour erire dans un
         * fichier csv
         */
        public String formatCsv() {
            return formatSepMilliers(this.cout) + ";"
                    + formatSepMilliers(this.timeInMs) + ";"
                    + check;
        }

        /**
         * Formattage d'un entier avec separateur de milliers.
         * @param val nombre entier a formatter
         * @return val formatte avec separateur de milliers
         */
        private String formatSepMilliers(long val) {
            String s = "";
            s += val%1000;
            val = val/1000;
            while(val > 0) {
                s = val%1000 + " " + formatMilliers(s);
                val = val/1000;
            }
            return s;
        }

        /**
         * @param s une chaine de caracteres
         * @return s avec des '0' au ebut si sa taille initiale etait infeieure
         * a 3
         */
        private String formatMilliers(String s) {
            while(s.length() < 3) {
                s = "0"+s;
            }
            return s;
        }
    }

    /**
     * Test de perforances de tous les solveurs sur les instances du repertoire
     * 'instances'.
     * @param args
     */
    public static void main (String[]args){
        TestAllSolveur test = new TestAllSolveur("instances");
        test.printAllResultats("results");
        /*try {
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
        }*/
    }
}

