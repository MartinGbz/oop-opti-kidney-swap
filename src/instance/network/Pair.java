package instance.network;

/**
 * Classe définissant les paires, héritage de la classe mère Base
 */
public class Pair extends Base{

    //Constructor
    public Pair(int id) {
        super(id);
    }

    @Override
    public String toString() {
       return "Pair{id=" + this.getId() + "}";
    }
}
