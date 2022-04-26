package instance.network;

public class Transplantation {
    private int medicalGain;
    private Base donneur;
    private Pair patient;

    public Transplantation(int medicalGain, Base donneur, Pair patient) {
        this.medicalGain = medicalGain;
        this.donneur = donneur;
        this.patient = patient;
    }

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
