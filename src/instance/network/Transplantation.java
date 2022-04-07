package instance.network;

public class Transplantation {
    private int medicalGain;
    private Base startPerson;
    private Pair endPerson;

    public Transplantation(Base startPerson, Pair endPerson) {
        this.startPerson = startPerson;
        this.endPerson = endPerson;
    }
}
