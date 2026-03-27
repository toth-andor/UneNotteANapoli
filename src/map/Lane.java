package map;

import Vehicle.Vehicle;
import attachments.Attachment;
import java.util.ArrayList;
import java.util.List;

/**
 * Egy útszakaszon belül egy sávot reprezentáló absztrakt osztály.
 * A sávon tartózkodó járműveket és a sáv végpontjait tartja számon.
 * Nem példányosítható — leszármazottjai az {@link OutdoorLane} és a {@link TunnelLane}.
 */
public abstract class Lane implements ILane {

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

    /**
     * A sávon tartózkodó járművek listája.
     */
    private List<Vehicle> vehicles;

    public Lane() {
        this.vehicles = new ArrayList<>();
    }

    /**
     * Regisztrálja a járművet a sávon. Alosztályokban felüldefiniálható,
     * hogy a sáv állapotával is interakcióba lépjen.
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
     * Eltávolítja a járművet a sávról.
     *
     * @param v az eltávolítandó jármű
     */
    @Override
    public void popVehicle(Vehicle v) {
        vehicles.remove(v);
    }

    /**
     * Hóesés hatását kezeli a sávon.
     *
     * @param snow a lehullott hó mennyisége
     */
    @Override
    public abstract void snowFall(int snow);

    /**
     * A hókotró aktív fejének hatását alkalmazza a sávra.
     *
     * @param head az aktív fej
     */
    @Override
    public abstract void cleanWithHead(Attachment head);

    /**
     * A sávon áthaladó jármű és a sáv állapotának interakcióját kezeli.
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
