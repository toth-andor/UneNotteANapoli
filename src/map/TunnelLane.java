package map;

import Vehicle.Vehicle;
import attachments.Attachment;

/**
 * A Lane osztály leszármazottja, az alagúttal fedett sávokat valósítja meg.
 * Az alagút védi a sávot a hóeséstől és a jegesedéstől, ezért a környezeti hatások
 * és takarítási műveletek nem értelmezhetők — minden metódus üres implementáció.
 */
public class TunnelLane extends Lane {


    public TunnelLane(Junction source, Junction destination, String name) {
        super(name);
        this.source = source;
        this.destination = destination;
    }

    @Override
    public boolean pushVehicle(Vehicle v, int timestamp) {
        super.pushVehicle(v, timestamp);
        return true;
    }

    /**
     * Alagútban nem esik hó, a hóesésnek nincs hatása.
     *
     * @param snow a lehullott hó mennyisége (figyelmen kívül hagyva)
     */
    @Override
    public void snowFall(int snow) {
    }

    /**
     * Alagútban takarítás nem szükséges, a fejnek nincs hatása.
     *
     * @param head az aktív fej (figyelmen kívül hagyva)
     */
    @Override
    public void cleanWithHead(Attachment head) {
    }

    /**
     * Alagútban a forgalomnak nincs különleges hatása a sáv állapotára.
     *
     * @param v         az áthaladó jármű
     * @param timestamp az aktuális idő
     */
    @Override
    public void handleTraffic(Vehicle v, int timestamp) {
    }


    /**
     * Alagútban sárkányfejnek nincs hatása.
     */
    @Override
    public void cleanWithDragon() {
    }

    /**
     * Alagútban sószórónak nincs hatása.
     *
     * @param timestamp a takarítás időbélyege (figyelmen kívül hagyva)
     */
    @Override
    public void cleanWithSaltVomitter(int timestamp) {
    }

    /**
     * Alagútban söprőfejnek nincs hatása.
     */
    @Override
    public void cleanWithSweeper() {
    }

    /**
     * Alagútban jégtörőfejnek nincs hatása.
     */
    @Override
    public void cleanWithIceBreaker() {
    }


    /**
     * Alagútban hányófejnek nincs hatása.
     */
    @Override
    public void cleanWithVomittingHead() {
    }

    /**
     * Alagútban zúzalékszórónak nincs hatása.
     *
     * @param timestamp a takarítás időbélyege (figyelmen kívül hagyva)
     */
    @Override
    public void cleanWithStoneVomitter(int timestamp) {
    }

    /**
     * Alagútban a ticknek nincs hatása, az állapot nem változik.
     *
     * @param timestamp az aktuális idő
     */
    @Override
    public void tick(int timestamp) {
    }
}
