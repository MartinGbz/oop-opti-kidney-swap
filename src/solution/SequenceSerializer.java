package solution;

import com.google.gson.*;
import instance.network.Altruist;
import instance.network.Base;
import java.lang.reflect.Type;

public class SequenceSerializer implements JsonSerializer<Sequence> {

    @Override
    public JsonElement serialize(Sequence sequence, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonElement resultat = null;

        if (sequence == null) {
            resultat = JsonNull.INSTANCE;
        } else {

            JsonObject jObjectSequence = new JsonObject();

            jObjectSequence.addProperty("gainMedSequence", sequence.getGainMedSequence());

            JsonArray listInSequence = new JsonArray();
            for (Base seq : sequence.getSequence()){
                JsonObject jObjectBase = new JsonObject();

                if(seq instanceof Altruist){
                    jObjectBase.addProperty("isAltruist", true);
                }
                jObjectBase.addProperty("id", seq.getId());
                listInSequence.add(jObjectBase);
            }
            jObjectSequence.add("sequence", listInSequence);

            resultat = jObjectSequence;
        }
        return resultat;
    }
}
