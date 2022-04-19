package io;

import instance.Instance;
import instance.network.Altruist;
import instance.network.Pair;
import io.exception.FileExistException;
import io.exception.FormatFileException;
import io.exception.OpenFileException;
import io.exception.ReaderException;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class InstanceReader {

    private File instanceFile;

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

    public Instance readInstance() throws ReaderException, NumberFormatException {
        try {
            FileReader f = new FileReader(this.instanceFile.getAbsolutePath());
            BufferedReader br = new BufferedReader(f);

            Integer pair = Integer.valueOf(readData(br, "patient-donneur"));
            Integer altruist = Integer.valueOf(readData(br, "altruistes"));
            Integer cycle = Integer.valueOf(readData(br, "cycles"));
            Integer chain = Integer.valueOf(readData(br, "chaines"));

            String fileName = this.instanceFile.getName();
            Instance instance = new Instance(fileName, pair, altruist, cycle, chain);
            System.out.println(instance);

            System.out.println("pair: " + pair + " - " + "altruist: " + altruist + " - " +
                    "cycle: " + cycle + " - " + "chain: " + chain);

            ArrayList<ArrayList<Integer>> matrice = readMatrice(br, pair, altruist);

            Altruist a;
            Pair p;
            for(int i=0; i<altruist; i++) {
                a = new Altruist(i);
                instance.addAltruist(a);
            }
            for(int i=altruist; i<pair; i++) {
                p = new Pair(i);
                instance.addPair(p);
            }

            System.out.println(instance);

            for(int i=0; i<altruist+pair; i++) {
                instance.addTranspantations(instance.getBaseById(i), matrice.get(i));
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
     *
     * @param br
     * @param toFind
     * @return
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

    private ArrayList<ArrayList<Integer>> readMatrice(BufferedReader br, Integer nbPair, Integer nbAltruist) throws IOException {
        String ligne = br.readLine();
        while(!ligne.contains("// Une valeur -1 signifie que la transplantation n'est pas realisable")) {
            ligne = br.readLine();
        }
        ligne = br.readLine();

        ArrayList<ArrayList<Integer>> matrice = new ArrayList<>();
        for(int i=0; i<nbAltruist+nbPair; i++) {
            matrice.add(getLine(ligne, nbPair));
            ligne = br.readLine();
        }
        return matrice;
    }

    private ArrayList<Integer> getLine(String ligne, Integer nbCol) throws NumberFormatException {
        String[] values = ligne.split(" |\t");
        ArrayList<Integer> valuesSplit = new ArrayList<>();
        int value;
        for (int i=0; i<nbCol; i++) {
            value = Integer.parseInt(values[i]);
            // System.out.print(value + " ");
            valuesSplit.add(value);
        }
        // System.out.print("\n");
        return valuesSplit;
    }

    public static void main(String[] args) {
        try {
            InstanceReader reader = new InstanceReader("instances/testInstance.txt");

            System.out.println(reader.readInstance());

            System.out.println("Instance lue avec success !");

        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
