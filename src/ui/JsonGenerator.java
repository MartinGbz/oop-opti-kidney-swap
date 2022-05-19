package ui;

import com.google.gson.*;
import instance.Instance;
import solution.Sequence;
import solution.Solution;
import solveur.Solveur;
import test.TestAllSolveur;
import ui.jsonSerializer.InstanceSerializer;
import ui.jsonSerializer.SequenceSerializer;
import ui.jsonSerializer.SolutionSerializer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

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

    public JsonElement generateSolutionForInstance(Solution solution, Instance instance, Solveur solveur){

        JsonObject jObject = new JsonObject();
        JsonObject jObjectProperties = new JsonObject();
        jObjectProperties.addProperty("name", solveur.getNom());
        jObject.add("algorithm", jObjectProperties);
        jObject.add("instance", gson.toJsonTree(instance));
        jObject.add("solution", gson.toJsonTree(solution));

        return gson.toJsonTree(jObject);
    }
}

