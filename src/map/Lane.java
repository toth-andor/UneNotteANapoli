package map;

import vehicle.Vehicle;
import attachments.Attachment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Egy útszakaszon belül a sávokat reprezentáló absztrakt osztály.
 * A sávok állapotát és a rajta közlekedő járműveket tartja számon.
 * Nem példányosítható — leszármazottjai az OutdoorLane és a TunnelLane.
 */
public abstract class Lane implements ILane {

    private String direction;

    public String getDirection() {
        return direction;
    }
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * Jelzi, hogy a sáv állapota megváltozott-e az aktuális játék során
     */
    private boolean stateWasChanged = false;

    /**
     * Beállítja, hogy a sáv állapota megváltozott-e az aktuális játék során.
     *
     * @param stateWasChanged true, ha az állapot megváltozott
     */
    public void setStateWasChanged(boolean stateWasChanged) {
        this.stateWasChanged = stateWasChanged;
    }

    /**
     * @return true, ha a sáv állapota megváltozott 
     */
    public boolean getStateWasChanged() {
        return stateWasChanged;
    }

    /**
     * A sáv kiindulópontját jelölő csomópont.
     */
    protected Junction source;

    /**
     * A sáv végpontját jelölő csomópont.
     */
    protected Junction destination;

    /**
     * Az az út, amelyhez ez a sáv tartozik.
     */
    private Road road;

    private String name;

    /**
     * A sávon tartózkodó járművek listája.
     */
    private List<Vehicle> vehicles;

    public Lane(String name) {
        this.vehicles = new ArrayList<>();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Lane() {
        this.vehicles = new ArrayList<>();
    }

    /**
     * A paraméterként átadott jármű befogadásáért felel, regisztrálja a sávon.
     * Alosztályokban felüldefiniálható, hogy a sáv állapotával is interakcióba lépjen.
     *
     * @param v         a befogadandó jármű
     * @param timestamp az aktuális idő
     * @return true, ha a befogadás sikerült
     */
    @Override
    public boolean pushVehicle(Vehicle v, int timestamp) {
        vehicles.add(v);
        return true;
    }

    /**
     * A paraméterként átadott jármű eltávolításáért felel, kiregisztrálja a sávból.
     *
     * @param v az eltávolítandó jármű
     */
    /**
     * @return a sávon tartózkodó járművek csak olvasható listája
     */
    public List<Vehicle> getVehicles() {
        return Collections.unmodifiableList(vehicles);
    }

    @Override
    public void popVehicle(Vehicle v) {
        vehicles.remove(v);
    }

    /**
     * A függvény hívásának hatására az adott sávon paraméterként átadott snow mennyiségű hó esik.
     *
     * @param snow a lehullott hó mennyisége
     */
    @Override
    public abstract void snowFall(int snow);

    /**
     * Hattatja a sávra a hókotró aktív fejét, az Attachment osztály cleanLane metódusának
     * hatására hívódik.
     *
     * @param head az aktív fej
     */
    @Override
    public abstract void cleanWithHead(Attachment head);

    /**
     * Ez a függvény kezeli a sáv állapotának és a rajta átmenő járműveknek az interakcióját.
     * Kültéri sávon a LaneState logikája fut le; alagútsávon nincs hatás.
     *
     * @param v         az áthaladó jármű
     * @param timestamp az aktuális idő
     */
    public abstract void handleTraffic(Vehicle v, int timestamp);

    /**
     * Sárkányfejjel végzett takarítás hatása a sávra.
     */
    public abstract void cleanWithDragon();

    /**
     * Sószóróval végzett takarítás hatása a sávra.
     *
     * @param timestamp a takarítás időbélyege
     */
    public abstract void cleanWithSaltVomitter(int timestamp);

    /**
     * Söprőfejjel végzett takarítás hatása a sávra.
     */
    public abstract void cleanWithSweeper();

    /**
     * Jégtörőfejjel végzett takarítás hatása a sávra.
     */
    public abstract void cleanWithIceBreaker();

    /**
     * Hányófejjel végzett takarítás hatása a sávra.
     */
    public abstract void cleanWithVomittingHead();

    /**
     * Zúzalékszóróval végzett takarítás hatása a sávra.
     *
     * @param timestamp a takarítás időbélyege
     */
    public abstract void cleanWithStoneVomitter(int timestamp);

    /**
     * Egy game tick elteltét jelzi a sávnak. A sáv továbbítja az állapotának,
     * amely szükség esetén megváltoztatja azt (pl. só lejáratakor DryState-be vált).
     *
     * @param timestamp az aktuális idő
     */
    public abstract void tick(int timestamp);

    /**
     * @return az az út, amelyhez ez a sáv tartozik
     */
    public Road getRoad() {
        return road;
    }

    /**
     * Beállítja az utat, amelyhez ez a sáv tartozik.
     *
     * @param road az út
     */
    public void setRoad(Road road) {
        this.road = road;
    }

    /**
     * @return a sáv kiindulópontja
     */
    public Junction getSource() {
        return source;
    }

    /**
     * @return a sáv végpontja
     */
    public Junction getDestination() {
        return destination;
    }
}
