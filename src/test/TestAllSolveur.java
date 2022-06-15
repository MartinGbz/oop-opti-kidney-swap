package test;

import solution.Solution;
import solveur.CyclesAndTree;
import solveur.Solveur;
import com.google.gson.*;
import instance.Instance;
import io.InstanceReader;
import io.SolutionWriter;
import io.exception.ReaderException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import ui.JsonGenerator;

public class TestAllSolveur{
    /**
     * Tous les solveurs a tester et comparer
     */
    private final List<Solveur> solveurs;
    /**
     * Nom du chemin du repertoire dans lequel se trouvent les instances a tester
     */
    private String path;
    /**
     *
     *
     */
    private String directorySolution;

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


    public TestAllSolveur(String path, String directorySolution) {
        this.path = path;
        this.directorySolution = directorySolution;
        solveurs = new ArrayList<>();
        instances = new ArrayList<>();
        this.resultats = new HashMap<>();
        this.addSolveurs();
        this.readNomInstance();

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
        //solveurs.add(new SolutionTriviale());
        //solveurs.add(new MTwithSortOrder()); //trié croissant
        //solveurs.add(new MTwithReverseOrder()); //trié decroissant
        //solveurs.add(new MTwithoutSort()); //pas trié
        //solveurs.add(new ReplaceTransplantation()); //utilise true,true
        solveurs.add(new CyclesAndTree(true));
        //solveurs.add(new CyclesAndTree(false));
    }


    /**
     * Lecture de tous les noms des instances a tester.
     * Ces instances se trouvent dans le repertoire pathRepertoire.
     * Les instances sont lues et chargees en memoire.
     */
    private void readNomInstance(){
        File filePath = new File(path);

        if(filePath.exists() && filePath.isDirectory()){
            File[] listOfFiles = filePath.listFiles();
            for (File file : listOfFiles) {
                read(file);
            }
        }
        else{
            read(filePath);
        }
    }

    /**
     * Lecture de l'instance
     * @param file
     */
    private void read(File file) {
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

    /**
     * Fonction vérifiant la présence des params -inst & -dSol avec leurs valeurs associées
     * @param params : liste des paramètres
     * @return String[] : Tableau contenant le chemin vers le répertoire/fichier d'instance et le répertoire contenant les solutions
     * @throws Exception : si absence de paramètres ou paramètres incorrects
     */
    public static String[] checkParams(String[] params) throws Exception {

        String pathInst;
        String dirSol;
        if (params.length == 4) {

            if (params[0].equals("-inst")) {
                pathInst = params[1];
            } else if (params[2].equals("-inst")) {
                pathInst = params[3];
            } else {
                throw new Error("Paramètre -inst manquant");
            }

            if (params[0].equals("-dSol")) {
                dirSol = params[1];
            } else if (params[2].equals("-dSol")) {
                dirSol = params[3];
            } else {
                throw new Error("Paramètre -dSol manquant");
            }
        } else {
            throw new Error("Paramètres manquants");
        }

        String [] path = new String[2];
        path[0] = pathInst;
        path[1] = dirSol;

        return path;

    }

    /**
     * Ecriture des resultats d'une instance pour tous les solveurs.
     * Pour chque solveur, l'instance est resolue par le solveeur avant que
     * ses resultats ne soient ecrits sur le fichier.
     * @param ecriture le writer sur lequel on fait l'ecriture
     * @param inst l'instane pour laquelle on ecrit les resultats
     */
    private JsonElement printResultatsInstance(PrintWriter ecriture, Instance inst) {

        Solution best = new Solution(inst);
        Solveur bestSolveur= null;
        JsonElement resultatJson =null;
        long bestExecTime=0;

        //Recuperation du nom de l'instance pour le csv
        String nomInst = inst.getName();
        String [] nom = nomInst.split("\\.");
        ecriture.print(nom[0]);

        for(Solveur solveur : solveurs) {
            long start = System.currentTimeMillis();
            // TO CHECK : resolution de l'instance avec le solveur
            Solution sol = solveur.solve(inst);
            long time = System.currentTimeMillis() - start;

            //Récupération de la meilleure solution valide pour une instance donnée
            if(sol.getGainMedTotal()>=best.getGainMedTotal() && sol.check()){
                bestExecTime = time;
                best = sol;
                bestSolveur=solveur;
            }

            // la solution est valide
            System.out.println("Cout total de la solution "+ sol.getGainMedTotal());
            Resultat result = new Resultat(sol.getGainMedTotal(), time, sol.check());
            resultats.put(new InstanceSolveur(inst, solveur), result);
            ecriture.print(";"+result.formatCsv());
            totalStats.get(solveur).add(result);
        }
        ecriture.println();

        //Production de la trame json de la solution ayant le plus gros gain médical
        if(best.getGainMedTotal()>0) {
            SolutionWriter sw = new SolutionWriter(best, directorySolution);
            JsonGenerator gson = new JsonGenerator();
            resultatJson = gson.generateSolutionForInstance(best, inst, bestSolveur, bestExecTime);
        }
        return resultatJson;
    }

    /**
     * Eriture des en-tetes dans le fichier de resultats.
     * @param ecriture le writer sur lequel on fait l'ecriture
     */
    private void printEnTetes(PrintWriter ecriture) {
        for(Solveur solveur : solveurs) {
            // TO CHECK : recuperer le nom du solveur
            ecriture.print(";"+solveur.getNom()+";;");
        }
        ecriture.println();
        for(Solveur solveur : solveurs) {
            ecriture.print(";cout");
            ecriture.print(";tps(ms)");
            ecriture.print(";valide ?");
        }
        ecriture.println();
    }
    /**
     * Affichage de tous les resultats.
     * Les resultats sont affiches dans un fichier csv avec separateur ';'.
     * L'ensemble des propriétés de la solution (instance, solveur, cycle & chaines générés) sont écrits dans un fichier Json pour l'interface web
     * @param nomFichierResultats nom du fichier de resultats
     */
    public void printAllResultats(String nomFichierResultats) {

        JsonGenerator gson =  new JsonGenerator();
        JsonObject jObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();

        PrintWriter ecriture = null;
        FileWriter ecritureJson = null;
        try {
            ecriture = new PrintWriter(nomFichierResultats+".csv");
            ecritureJson = new FileWriter("../kidney-webapp/src/app/"+nomFichierResultats+".json");
            printEnTetes(ecriture);

            //Fabrication du json pour l'ensemble des instances
            for(Instance inst : instances) {
                jsonArray.add(printResultatsInstance(ecriture, inst));
            }
            jObject.add("results",jsonArray);

            //Ecriture du fichier json
            JsonElement jElement = gson.gson.toJsonTree(jObject);
            ecritureJson.write(jElement.toString());
            ecritureJson.flush();


            ecriture.println();
            printSommeResultats(ecriture);

            /*Desktop desk= Desktop.getDesktop();
            String url="src/ui/webapp/index.html";
            File htmlFile = new File(url);
            Desktop.getDesktop().browse(htmlFile.toURI());*/

        } catch (IOException ex) {
            System.out.println("Erreur fichier ecriture");
            System.out.println(ex);
        }
        if(ecriture != null) {
            ecriture.close();
        }
    }

    /**
     * Affichage de la somme des resultats par solveur.
     * Les valeurs sont ecrites sur une seule ligne.
     * @param ecriture le writer sur lequel on fait l'ecriture
     */
    private void printSommeResultats(PrintWriter ecriture) {
        ecriture.print("SOMME");
        for(Solveur solveur : solveurs) {
            ecriture.print(";"+totalStats.get(solveur).formatCsv());
        }
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
     * Test de perforances de tous les solveurs sur les instances du repertoire/instance spécifique
     * @param args String[] : liste des paramètres à fournir à l'exécution.
     */
    public static  void main(String[] args){

        try {
            String filenameInstance;
            String directorySolution;

            String [] result;
            result=checkParams(args);
            filenameInstance=result[0];
            directorySolution=result[1];

            TestAllSolveur test = new TestAllSolveur(filenameInstance, directorySolution);
            test.printAllResultats("results");

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


}

