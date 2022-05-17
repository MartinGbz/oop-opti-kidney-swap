package solution;

import com.google.gson.*;
import java.lang.reflect.Type;

public class SolutionSerializer implements JsonSerializer<Solution> {

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

    private JsonElement fillJson(JsonSerializationContext jsonSerializationContext, Sequence seq) {
        JsonElement jsonSubscription;
        JsonObject jObjectChain = new JsonObject();
        jObjectChain.addProperty("gainMedical", seq.getGainMedSequence());
        jsonSubscription = jsonSerializationContext.serialize((Sequence) seq, Sequence.class);
        return jsonSubscription;
    }
}
