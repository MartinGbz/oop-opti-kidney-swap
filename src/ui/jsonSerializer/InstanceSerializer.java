package ui.jsonSerializer;

import com.google.gson.*;
import instance.Instance;
import instance.network.Altruist;
import instance.network.Pair;

import java.lang.reflect.Type;

public class InstanceSerializer implements JsonSerializer<Instance> {
    @Override
    public JsonElement serialize(Instance instance, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonElement resultat;

        if (instance == null) {
            resultat = JsonNull.INSTANCE;
        } else {

            JsonObject jObjectInstance = new JsonObject();
            JsonObject jObjectProperties = new JsonObject();
            JsonArray listAltruists = new JsonArray();
            JsonArray listPairs = new JsonArray();

            for(Altruist altruist : instance.getAltruists()){
                JsonObject jObjectAltruist = new JsonObject();
                jObjectAltruist.addProperty("id", altruist.getId());
                listAltruists.add(jObjectAltruist);
            }
            for(Pair pair : instance.getPairs()){
                JsonObject jObjectPair= new JsonObject();
                jObjectPair.addProperty("id", pair.getId());
                listPairs.add(jObjectPair);
            }

            jObjectInstance.addProperty("name", instance.getName());
            jObjectProperties.addProperty("nbPairs", instance.getNbPairs());
            jObjectProperties.addProperty("maxSizeCycle", instance.getMaxSizeCycle());
            jObjectProperties.addProperty("maxSizeChain", instance.getMaxSizeChain());
            jObjectInstance.add("properties", jObjectProperties);

            jObjectInstance.add("altruists", listAltruists);
            jObjectInstance.add("pairs", listPairs);

            resultat = jObjectInstance;
        }
        return resultat;
    }
}
