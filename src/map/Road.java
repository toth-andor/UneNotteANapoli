package map;

import java.util.ArrayList;
import java.util.List;

/**
 * Több sávot összefogó útszakaszt reprezentáló osztály.
 * Egy út pontosan 4 sávból áll, és két {@link Junction} csomópontot köt össze.
 * A Commuter járművek ({@link Vehicle.Bus}, {@link Vehicle.Car}) célpontjaként szolgál,
 * és rajta keresztül egységesen lehet hóesést véghezvinni az összes sávon.
 */
public class Road {

    /**
     * Az úthoz tartozó sávok (pontosan 4 db).
     */
    private List<Lane> lanes;

    /**
     * Az út egyik végén lévő csomópont.
     */
    private Junction end1;

    /**
     * Az út másik végén lévő csomópont.
     */
    private Junction end2;

    public Road(Junction end1, Junction end2) {
        this.end1 = end1;
        this.end2 = end2;
        this.lanes = new ArrayList<>();
    }

    /**
     * Hozzáad egy sávot az úthoz.
     *
     * @param lane a hozzáadandó sáv
     */
    public void addLane(Lane lane) {
        lanes.add(lane);
        lane.setRoad(this);
    }

    /**
     * Megadott mennyiségű hót hullat az úthoz tartozó összes sávra.
     *
     * @param amount a lehullott hó mennyisége
     */
    public void snowFall(int amount) {
        for (Lane lane : lanes) {
            lane.snowFall(amount);
        }
    }

    /**
     * @return az út egyik végpontja
     */
    public Junction getEnd1() {
        return end1;
    }

    /**
     * @return az út másik végpontja
     */
    public Junction getEnd2() {
        return end2;
    }

    /**
     * @return az úthoz tartozó sávok listája
     */
    public List<Lane> getLanes() {
        return lanes;
    }
}
