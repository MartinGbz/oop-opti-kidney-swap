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
}
