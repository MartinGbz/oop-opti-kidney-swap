package instance.network;

public class Altruist extends Base {

    public Altruist(int id) {
        super(id);
    }

    @Override
    public String toString() {
        /*String s = "Altruist{" +
                "id=" + id +
                "/ transplantations=";
        for(Transplantation t : this.getTransplantations().values()) {
            s += "\n\t" + t.toString();
        }
        s+= "\n}";
        return s;*/
        return "Altruist{id=" + this.getId() + "}";
    }
}
