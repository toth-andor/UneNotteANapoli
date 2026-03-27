package map;

import java.util.ArrayList;
import java.util.List;

/**
 * Több sávot összefogó útszakaszt reprezentáló osztály, amely a környezeti eseményekért felel.
 * Ez a végcélja a Commuter járműveknek (Bus, Car), valamint rajta keresztül egységesen
 * lehet hóesést véghezvinni a hozzá tartozó Lane-eken.
 * Számon tartja a hozzátartozó Lane-eket és az út két végén elhelyezkedő Junction-öket.
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
     * Hozzáad egy sávot az úthoz és beállítja a sáv visszamutató referenciáját.
     *
     * @param lane a hozzáadandó sáv
     */
    public void addLane(Lane lane) {
        lanes.add(lane);
        lane.setRoad(this);
    }

    /**
     * amount mennyiségű hóesés minden a Road-hoz tartozó sávra.
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
