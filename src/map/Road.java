package map;

import java.util.ArrayList;
import java.util.List;
import skeleton.Skeleton.CallChainLogger;

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
     * Az út neve (pl. road_1), a MapModel rendeli hozzá.
     */
    private String name;

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
        CallChainLogger.printCall(this, "snowFall(" + amount + ")");
        for (Lane lane : lanes) {
            lane.snowFall(amount);
        }
        CallChainLogger.printReturn(null);
    }

    /**
     * Egy game tick elteltét jelzi az úthoz tartozó összes sávnak,
     * hogy azok lejárt állapotukat (pl. só) frissíthessék.
     *
     * @param timestamp az aktuális idő
     */
    public void tick(int timestamp) {
        CallChainLogger.printCall(this, "tick(" + timestamp + ")");
        for (Lane lane : lanes) {
            lane.tick(timestamp);
        }
        CallChainLogger.printReturn(null);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
