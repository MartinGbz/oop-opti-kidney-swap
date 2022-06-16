package instance.network;

/**
 * Classe Altruist héritée de Base
 */
public class Altruist extends Base {

    //Constructor
    public Altruist(int id) {
        super(id);
    }

    @Override
    public String toString() {
        return "Altruist{id=" + this.getId() + "}";
    }
}
