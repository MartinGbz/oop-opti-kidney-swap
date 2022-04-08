package instance.network;

public class Transplantation {
    private int medicalGain;
    private Base startPerson;
    private Pair endPerson;

    public Transplantation(int medicalGain, Base startPerson, Pair endPerson) {
        this.medicalGain = medicalGain;
        this.startPerson = startPerson;
        this.endPerson = endPerson;
    }
}
