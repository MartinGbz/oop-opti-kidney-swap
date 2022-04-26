package instance.network;

import java.util.*;

public abstract class Base {
    private int id;
    private Map<Pair, Transplantation> transplantations;

    public Base(int id) {
        this.id = id;
        this.transplantations = new HashMap<>();
    }

    /**
     * Création d'un objet Transplantation "t" et ajout dans le HashMap "transplantations"
     * Clé -> valeur : patient -> t
     * @param patient
     * @param gain
     */
    public void addTransplantation(Pair patient, int gain) {
        //TODO : à dé commenter si souhait de retirer les gains -1 dans les transplantations
        //if(gain != -1) {
            Transplantation t = new Transplantation(gain, this, patient);
            this.transplantations.put(patient, t);
        //}
    }

    public int getId() {
        return id;
    }
    public Map<Pair, Transplantation> getTransplantations() {
        return new HashMap<>(transplantations);
    }



    /**
     * Y a t il une compatibilité entre this et patient ?
     * a tester
     * @param patient
     * @return false (patient and this are not compatible) else true
     */
    public boolean isCompatible(Base patient) {
        if(patient == null) return false;
        if(patient instanceof Altruist) return false;
        int gain = this.transplantations.get(patient).getMedicalGain();
        if(gain == -1) return false;
        return true;
    }

    /**
     * Recherche si donneur peut donner à quelqu'un
     * A tester
     * @return false (no compatibility) true (min one compatibility)
     */
    public boolean asCompatibility() {
        // if(this == null) return false;
        //Boucle while+iterator
        Iterator iterator = this.transplantations.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry mapentry = (Map.Entry) iterator.next();
            Transplantation t = (Transplantation) mapentry.getValue();
            int gain = t.getMedicalGain();
            if(gain != -1) return true;
        }
        return false;
    }

    /**
     * Cherche la meilleure compatibilité avec le donneur
     * a tester
     * @param donneur
     * @return Integer.MAX_VALUE OU idBestCompatibility
     */
    public int bestCompatibility(Base donneur) {
        if(donneur == null) return Integer.MAX_VALUE;

        int bestCompatibility = -1;
        int idBestCompatibility = Integer.MAX_VALUE;

        Iterator iterator = donneur.transplantations.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry mapentry = (Map.Entry) iterator.next();
            int gain = (int) mapentry.getValue();
            if(gain != -1 && gain > bestCompatibility) {
                idBestCompatibility = (int) mapentry.getKey();
            }
        }
        return idBestCompatibility;
    }

}
