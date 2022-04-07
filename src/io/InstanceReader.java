package io;

import io.exception.FileExistException;
import io.exception.FormatFileException;
import io.exception.OpenFileException;
import io.exception.ReaderException;

import java.io.*;

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

    public Boolean readInstance() throws ReaderException, NumberFormatException {
        try{
            FileReader f = new FileReader(this.instanceFile.getAbsolutePath());
            BufferedReader br = new BufferedReader(f);

            Integer pair = Integer.valueOf(readData(br, "patient-donneur"));
            Integer altruist = Integer.valueOf(readData(br, "altruistes"));
            Integer cycle = Integer.valueOf(readData(br, "cycles"));
            Integer chain = Integer.valueOf(readData(br, "chaines"));

            System.out.println("pair: " + pair + " - " + "altruist: " + altruist + " - " +
                    "cycle: " + cycle + " - " + "chain: " + chain);

            readMatrice(br, pair, altruist);

            br.close();
            f.close();
            return true;
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

    private void readMatrice(BufferedReader br, Integer nbPair, Integer nbAltruist) throws IOException {
        String ligne = br.readLine();
        while(!ligne.contains("// Une valeur -1 signifie que la transplantation n'est pas realisable")) {
            ligne = br.readLine();
        }
        ligne = br.readLine();

        System.out.println("Lignes des altruistes:");
        for(int i=0; i<nbAltruist; i++) {
            readLine(ligne, nbPair);
            ligne = br.readLine();
        }

        System.out.println("Lignes des paires:");
        for(int i=0; i<nbPair; i++) {
            readLine(ligne, nbPair);
            ligne = br.readLine();
        }
    }

    private void readLine(String ligne, Integer nbCol) throws NumberFormatException {
        String[] values = ligne.split(" |\t");
        int value;
        for (int i=0; i<nbCol; i++) {
            value = Integer.parseInt(values[i]);
            System.out.print(value + " ");
        }
        System.out.print("\n");
    }

    public static void main(String[] args) {
        try {
            InstanceReader reader = new InstanceReader("instances/testInstance.txt");

            reader.readInstance();
            System.out.println("Instance lue avec success !");

        } catch (ReaderException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
