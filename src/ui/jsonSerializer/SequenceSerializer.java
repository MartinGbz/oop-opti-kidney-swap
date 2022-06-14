package ui.jsonSerializer;

import com.google.gson.*;
import instance.network.Altruist;
import instance.network.Base;
import instance.network.Pair;
import solution.Cycle;
import solution.Sequence;

import java.lang.reflect.Type;

/**
 * Classe de sérialisation de la classe Séquence implémentant la classe JsonSerializer de la librairie Gson
 */
public class SequenceSerializer implements JsonSerializer<Sequence> {

    /**
     * Produire le json correspondant à notre combinaison altruistes/paires générant une chaine ou paires générant un cycle
     * @param sequence : Objet séquence contenant la combinaison altruistes/paires (chaine) ou paires (cycle)
     * @param type
     * @param jsonSerializationContext : Contexte de serialisation de Gson
     * @return JsonElement : objet Json généré pour chaque élément de la  instance associée à la solution
     */
    @Override
    public JsonElement serialize(Sequence sequence, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonElement resultat = null;
        int i=0;

        if (sequence == null) {
            resultat = JsonNull.INSTANCE;
        } else {

            JsonObject jObjectSequence = new JsonObject();
            JsonArray listInSequence = new JsonArray();

            //Parcours de tous les altruistes/paires de la séquence (un cycle ou une chaine)
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

            jObjectSequence.addProperty("gainMedSequence", sequence.getGainMedSequence());
            jObjectSequence.add("sequence", listInSequence);

            resultat = jObjectSequence;
        }
        return resultat;
    }
}
