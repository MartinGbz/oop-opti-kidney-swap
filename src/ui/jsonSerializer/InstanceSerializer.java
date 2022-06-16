package ui.jsonSerializer;

import com.google.gson.*;
import instance.Instance;
import instance.network.Altruist;
import instance.network.Pair;
import java.lang.reflect.Type;

/**
 *  Classe de sérialisation de la classe Instance implémentant la classe JsonSerializer de la librairie Gson
 */
public class InstanceSerializer implements JsonSerializer<Instance> {

    /**
     * Produire le json contenant l'ensemble des propriétés de l'instance
     * @param instance : Objet instance contenant les propriétés de l'instance
     * @param type
     * @param jsonSerializationContext : Contexte de serialisation de Gson
     * @return JsonElement : objet Json généré pour la classe instance associée à la solution
     */
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

            //Parcours de tous les altruists
            for(Altruist altruist : instance.getAltruists().values()){
                JsonObject jObjectAltruist = new JsonObject();
                jObjectAltruist.addProperty("id", altruist.getId());
                listAltruists.add(jObjectAltruist);
            }
            //Parcours de toutes les paires
            for(Pair pair : instance.getPairs().values()){
                JsonObject jObjectPair= new JsonObject();
                jObjectPair.addProperty("id", pair.getId());
                listPairs.add(jObjectPair);
            }

            //Ajout des différentes propriétés à l'objet Json
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
