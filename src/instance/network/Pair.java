package instance.network;

public class Pair extends Base{
    public Pair(int id) {
        super(id);
    }

    @Override
    public String toString() {
        /*String s = "Pair{" +
                "id=" + id +
                "/ transplantations=";
        for(Transplantation t : this.getTransplantations().values()) {
            s += "\n\t" + t.toString();
        }
        s+= "\n}";
        return s;*/

        return "Pair{id=" + id + "}";

    }
}
