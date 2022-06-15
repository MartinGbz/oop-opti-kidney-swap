package ui.jsonSerializer;

import com.google.gson.*;
import solution.Chain;
import solution.Cycle;
import solution.Sequence;
import solution.Solution;

import java.lang.reflect.Type;

/**
 * Classe de sérialisation de la classe Solution implémentant la classe JsonSerializer de la librairie Gson
 */
public class SolutionSerializer implements JsonSerializer<Solution> {

    /**
     * Produire le json correspondant à notre solution composé d'un ensemble de chaines et cycles
     * @param solution : Objet solution contenant une liste de cycles et de chaines
     * @param type
     * @param jsonSerializationContext : Contexte de serialisation de Gson
     * @return JsonElement : objet Json généré pour l'ensemble de la solution (liste des chaines et cycles)
     */
    @Override
    public JsonElement serialize(Solution solution, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonElement resultat = null;

        if (solution == null) {
            resultat = JsonNull.INSTANCE;
        } else { ;
            JsonObject jObjectSolution = new JsonObject();
            JsonElement jsonSubscription =  null;

            JsonArray cycles = new JsonArray();
            JsonArray chains = new JsonArray();

            jObjectSolution.addProperty("gainMedTotal", solution.getGainMedTotal());

            for (Chain chain : solution.getChains()){
                jsonSubscription = fillJson(jsonSerializationContext, chain);
                chains.add(jsonSubscription);
            }
            for (Cycle cycle : solution.getCycles()){
                jsonSubscription = fillJson(jsonSerializationContext, cycle);
                cycles.add(jsonSubscription);
            }
            jObjectSolution.add("chains", chains);
            jObjectSolution.add("cycles", cycles);

            resultat = jObjectSolution;
        }
        return resultat;

    }

    /**
     * Fonction appelant le Serializer de la séquence
     * @see ./jsonSerializer/SequenceSerializer
     * @param jsonSerializationContext
     * @param seq Sequence à serializer
     * @return JsonElement : objet json représentant la séquence (combinaison altruists/pairs ou pairs)
     */
    private JsonElement fillJson(JsonSerializationContext jsonSerializationContext, Sequence seq) {
        JsonElement jsonSubscription;
        JsonObject jObjectChain = new JsonObject();
        jObjectChain.addProperty("gainMedical", seq.getGainMedSequence());
        jsonSubscription = jsonSerializationContext.serialize((Sequence) seq, Sequence.class);
        return jsonSubscription;
    }
}
