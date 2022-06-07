package ui.jsonSerializer;

import com.google.gson.*;
import instance.network.Altruist;
import instance.network.Base;
import instance.network.Pair;
import solution.Cycle;
import solution.Sequence;

import java.lang.reflect.Type;

public class SequenceSerializer implements JsonSerializer<Sequence> {

    @Override
    public JsonElement serialize(Sequence sequence, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonElement resultat = null;
        int i=0;


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
                if(i<sequence.getSequenceSize()-1){
                    jObjectBase.addProperty("coutVersleSuivant", seq.getGainVers((Pair) sequence.getSequence().get(i+1)));
                }
                else if(i==sequence.getSequenceSize()-1 && sequence.getSequence().get(0) instanceof Pair){
                    jObjectBase.addProperty("coutVersleSuivant", seq.getGainVers((Pair) sequence.getSequence().get(0)));
                }

                listInSequence.add(jObjectBase);
                i++;
            }
            jObjectSequence.add("sequence", listInSequence);

            resultat = jObjectSequence;
        }
        return resultat;
    }
}
