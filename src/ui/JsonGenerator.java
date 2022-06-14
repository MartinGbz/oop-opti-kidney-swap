package ui;

import com.google.gson.*;
import instance.Instance;
import solution.Sequence;
import solution.Solution;
import solveur.Solveur;
import ui.jsonSerializer.InstanceSerializer;
import ui.jsonSerializer.SequenceSerializer;
import ui.jsonSerializer.SolutionSerializer;

/**
 * Classe réalisant la génération du json associé à une instance (algorithme et solution associée)
 */
public class JsonGenerator {

    public Gson gson = null;

    public JsonGenerator() {
        gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .enableComplexMapKeySerialization()
                .setPrettyPrinting()
                .registerTypeAdapter(Instance.class, new InstanceSerializer())
                .registerTypeAdapter(Solution.class, new SolutionSerializer())
                .registerTypeAdapter(Sequence.class, new SequenceSerializer())
                .create();
    }

    /**
     * Produire le json complet d'une instance et de sa solution associée avec précision de l'algorithme de génération
     * @param solution Solution : solution de l'instance sélectionnée
     * @param instance Instance : instance associée à la solution générée
     * @param solveur Solveur : solveur utilisé pour générer la solution
     * @param bestExecTime long : temps d'exécution nécessaire pour produire la solution avec le solveur spécifié
     * @return
     */
    public JsonElement generateSolutionForInstance(Solution solution, Instance instance, Solveur solveur, long bestExecTime){

        JsonObject jObject = new JsonObject();
        //Propriétés de l'algorithme
        JsonObject jObjectProperties = new JsonObject();
        jObjectProperties.addProperty("name", solveur.getNom());
        jObjectProperties.addProperty("executionTime", bestExecTime);

        jObject.add("algorithm", jObjectProperties);
        jObject.add("instance", gson.toJsonTree(instance));
        jObject.add("solution", gson.toJsonTree(solution));

        return gson.toJsonTree(jObject);
    }
}

