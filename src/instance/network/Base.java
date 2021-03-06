package instance.network;

import java.util.*;

public abstract class Base {

    protected int id;
    private final Map<Pair, Transplantation> transplantations;

    public Base(int id) {
        this.id = id;
        this.transplantations = new HashMap<>();
    }

    public int getId() {
        return id;
    }
    public Map<Pair, Transplantation> getTransplantations() {
        return new HashMap<>(transplantations);
    }

    /**
     * Création d'un objet Transplantation "t" et ajout dans le HashMap "transplantations"
     * Clé -> valeur : patient -> t
     * @param patient le patient recevant le don
     * @param gain le gain associé à l'échange
     */
    public void addTransplantation(Pair patient, int gain) {
        Transplantation t = new Transplantation(gain, this, patient);
        this.transplantations.put(patient, t);
    }

    /**
     * Test si l'objet actuel (Base) est compatible avec le patient (gain != -1)
     * @param patient le patient recevant le don
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

    public int getGainVers(Pair dest) {
        int gain = this.transplantations.get(dest).getMedicalGain();
        if(gain != -1)
            return gain;
        else
            return Integer.MAX_VALUE;
    }

    public int ratioGain() {
        int ratioTemp, ratio=0, div=0;
        Transplantation t;
        if(this == null) return -1;
        if(!this.asCompatibility()) return -1;

        for(Map.Entry entry : this.transplantations.entrySet()) {
            t = (Transplantation) entry.getValue();
            ratioTemp = t.getMedicalGain();
            if(ratioTemp != -1) {
                ratio += ratioTemp;
                div++;
            }
        }
        if(ratio != 0)
            ratio = ratio / div ;
        else
            ratio = -1;
        return ratio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Base base = (Base) o;
        return id == base.id && Objects.equals(transplantations, base.transplantations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
