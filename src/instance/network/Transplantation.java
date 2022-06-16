package instance.network;

/**
 * Classe représentant la transplantation entre un donneur et un patient
 */
public class Transplantation {

    private final int medicalGain;
    private final Base donneur;
    private final Pair patient;

    //Constructor
    public Transplantation(int medicalGain, Base donneur, Pair patient) {
        this.medicalGain = medicalGain;
        this.donneur = donneur;
        this.patient = patient;
    }

    //Getter
    public int getMedicalGain() {
        return medicalGain;
    }

    @Override
    public String toString() {
        if(patient != null) {
            return "{gain=" + medicalGain +
                    "/ patient= " + patient.getId() +
                    '}';
        }
        return "{gain=" + medicalGain +
                '}';
    }
}
