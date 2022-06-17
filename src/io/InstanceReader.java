package io;

import instance.Instance;
import instance.network.Altruist;
import instance.network.Pair;
import io.exception.FileExistException;
import io.exception.FormatFileException;
import io.exception.OpenFileException;
import io.exception.ReaderException;

import java.io.*;
import java.util.ArrayList;

public class InstanceReader {

    private File instanceFile;

    //Constructor
    public InstanceReader(String inputPath) throws ReaderException {
        if (inputPath == null) {
            throw new OpenFileException();
        }
        if (!inputPath.endsWith(".txt")) {
            throw new FormatFileException("txt", "txt");
        }
        String instanceName = inputPath;
        this.instanceFile = new File(instanceName);
    }

    /**
     * Lecture du fichier d'instance ligne par ligne
     * et création d'un object instance correspondant aux datas
     * @return l'objet instance avec les données lues dans le fichier ouvert
     * @throws ReaderException
     * @throws NumberFormatException
     */
    public Instance readInstance() throws ReaderException, NumberFormatException {
        try {
            FileReader f = new FileReader(this.instanceFile.getAbsolutePath());
            BufferedReader br = new BufferedReader(f);

            int pair = Integer.parseInt(readData(br, "patient-donneur"));
            int altruist = Integer.parseInt(readData(br, "altruistes"));
            int cycle = Integer.parseInt(readData(br, "cycles"));
            int chain = Integer.parseInt(readData(br, "chaines"));

            String fileName = this.instanceFile.getName();
            Instance instance = new Instance(fileName, pair, altruist, cycle, chain);
            ArrayList<ArrayList<Integer>> matrice = readMatrice(br, pair, altruist);

            // Ajout des Altruists & des Pairs dans les tableaux de l'instance
            Altruist a;
            Pair p;
            for(int i=1; i <= altruist; i++) { //
                a = new Altruist(i);
                instance.addAltruist(a);
            }
            for(int i=(altruist+1) ; i <= (pair+altruist); i++) { //
                p = new Pair(i);
                instance.addPair(p);
            }

            // Création des transplantations pour chaque ligne de la matrice
            for(int i=1; i <= (altruist+pair); i++) { //
                instance.addTranspantations(instance.getBaseById(i), matrice.get(i-1));
            }

            br.close();
            f.close();
            return instance;
        } catch (FileNotFoundException ex) {
            throw new FileExistException(instanceFile.getName());
        } catch (IOException ex) {
            throw new ReaderException("IO exception", ex.getMessage());
        }
    }

    /**
     * Retourne la ligne située en dessous de celle contenant la valeur "toFind"
     * @param br buffer de lecture du fichier d'instance
     * @param toFind chaine à rechercher
     * @return la ligne suivant la chaine à rechercher (String)
     * @throws IOException
     */
    private String readData(BufferedReader br, String toFind) throws IOException {
        String ligne = br.readLine();
        while(!ligne.contains(toFind)) {
            ligne = br.readLine();
        }
        ligne = br.readLine();
        return ligne;
    }

    /**
     * Lecture de la matrice (tableaux de transplantations)
     * @param br buffer de lecture du fichier d'instance
     * @param nbPair Integer : nombre de paires dans l'instance
     * @param nbAltruist Integer : nombre d'altruistes dans l'instance
     * @return un tableau deux dimensions contenant les valeurs de transplantations pour chaque altruiste et chaque paire
     */
    private ArrayList<ArrayList<Integer>> readMatrice(BufferedReader br, Integer nbPair, Integer nbAltruist) throws IOException {
        String ligne = br.readLine();
        while(!ligne.contains("// Une valeur -1 signifie que la transplantation n'est pas realisable")) {
            ligne = br.readLine();
        }
        ligne = br.readLine();

        ArrayList<ArrayList<Integer>> matrice = new ArrayList<>();
        for(int i=1; i<= nbAltruist+nbPair; i++) { //
            matrice.add(getLine(ligne, nbPair));
            ligne = br.readLine();
        }
        return matrice;
    }

    /**
     * Place nbCol valeurs de la chaine (séparées par des tabulations) dans un tableau
     * @param ligne ligne de fichier texte à lire
     * @param nbCol nombre de valeurs à stocker dans le tableau
     * @return le tableau créé
     */
    private ArrayList<Integer> getLine(String ligne, Integer nbCol) throws NumberFormatException {
        String[] values = ligne.split(" |\t");
        ArrayList<Integer> valuesSplit = new ArrayList<>();
        int value;
        for (int i=0; i<nbCol; i++) {
            value = Integer.parseInt(values[i]);
            valuesSplit.add(value);
        }
        return valuesSplit;
    }

    public static void main(String[] args) {
        try {
            InstanceReader reader = new InstanceReader("instances/testInstance.txt");
            System.out.println(reader.readInstance());
            System.out.println("Instance lue avec success !");
        }
        catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
