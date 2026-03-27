package map;

import java.util.ArrayList;
import java.util.List;

/**
 * Az úthálózat egy csomópontját reprezentáló osztály, ahol sávok találkoznak.
 * Felelőssége a járművek útvonalának irányítása: a hozzá érkező járműveknek
 * megmutatja a céljuk felé vezető következő sávot. Ütközés nem fordulhat elő
 * benne, csak áthaladás.
 */
public class Junction {

    /**
     * A csomópontban végződő utak listája.
     */
    private List<Road> roads;

    public Junction() {
        this.roads = new ArrayList<>();
    }

    /**
     * Hozzáad egy utat a csomóponthoz.
     *
     * @param road a hozzáadandó út
     */
    public void addRoad(Road road) {
        roads.add(road);
    }

    /**
     * @return a csomóponthoz kapcsolódó utak listája
     */
    public List<Road> getRoads() {
        return roads;
    }
}
