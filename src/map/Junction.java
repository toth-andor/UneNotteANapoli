package map;

import java.util.ArrayList;
import java.util.List;

/**
 * Az úthálózat navigációs pontja, csomópont, ahol a sávok találkoznak.
 * Felelőssége a járművek útvonalának irányítása: a hozzá érkező autóknak és buszoknak
 * megmutatja a céljuk felé vezető következő sávot. Ütközés nem fordulhat elő benne,
 * csak áthaladás. Számon tartja a benne végződő utakat.
 */
public class Junction {

    /**
     * A csomópontok pozíciója a [0, 1] intervallumon.
     * A [0.5, 0.5] pont a képernyő közepe **/
    private float posX;
    private float posY;

    public float getPosX() { return posX; }
    public float getPosY() { return posY; }

    public void setPosX(float posX) { this.posX = posX; }
    public void setPosY(float posY) { this.posY = posY; }

    /**
     * A csomópontban végződő utak listája.
     */
    private List<Road> roads;

    /**
     * A csomópont neve.
     */
    private String name;

    public Junction(String name) {
        this.roads = new ArrayList<>();
        this.name = name;
    }

    public String getName() {
        return name;
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
