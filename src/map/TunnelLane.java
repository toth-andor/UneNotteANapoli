package map;

import Vehicle.Vehicle;
import attachments.Attachment;
import skeleton.Skeleton;
import skeleton.Skeleton.CallChainLogger;

/**
 * A Lane osztály leszármazottja, az alagúttal fedett sávokat valósítja meg.
 * Az alagút védi a sávot a hóeséstől és a jegesedéstől, ezért a környezeti hatások
 * és takarítási műveletek nem értelmezhetők — minden metódus üres implementáció.
 */
public class TunnelLane extends Lane {

    @Override
    public boolean pushVehicle(Vehicle v, int timestamp) {
        CallChainLogger.printCall(this, "pushVehicle(" + Skeleton.getEntityByRef(v) + ", " + timestamp + ")");
        super.pushVehicle(v, timestamp);
        CallChainLogger.printReturn("true");
        return true;
    }

    /**
     * Alagútban nem esik hó, a hóesésnek nincs hatása.
     *
     * @param snow a lehullott hó mennyisége (figyelmen kívül hagyva)
     */
    @Override
    public void snowFall(int snow) {
        CallChainLogger.printCall(this, "snowFall(" + snow + ")");
        CallChainLogger.printReturn(null);
    }

    /**
     * Alagútban takarítás nem szükséges, a fejnek nincs hatása.
     *
     * @param head az aktív fej (figyelmen kívül hagyva)
     */
    @Override
    public void cleanWithHead(Attachment head) {
        CallChainLogger.printCall(this, "cleanWithHead(" + Skeleton.getEntityByRef(head) + ")");
        CallChainLogger.printReturn(null);
    }

    /**
     * Alagútban a forgalomnak nincs különleges hatása a sáv állapotára.
     *
     * @param v         az áthaladó jármű
     * @param timestamp az aktuális idő
     */
    @Override
    public void handleTraffic(Vehicle v, int timestamp) {
        CallChainLogger.printCall(this, "handleTraffic(" + Skeleton.getEntityByRef(v) + ", " + timestamp + ")");
        CallChainLogger.printReturn(null);
    }

    /**
     * Alagútban sárkányfejnek nincs hatása.
     */
    @Override
    public void cleanWithDragon() {
        CallChainLogger.printCall(this, "cleanWithDragon()");
        CallChainLogger.printReturn(null);
    }

    /**
     * Alagútban sószórónak nincs hatása.
     *
     * @param timestamp a takarítás időbélyege (figyelmen kívül hagyva)
     */
    @Override
    public void cleanWithSaltVomitter(int timestamp) {
        CallChainLogger.printCall(this, "cleanWithSaltVomitter(" + timestamp + ")");
        CallChainLogger.printReturn(null);
    }

    /**
     * Alagútban söprőfejnek nincs hatása.
     */
    @Override
    public void cleanWithSweeper() {
        CallChainLogger.printCall(this, "cleanWithSweeper()");
        CallChainLogger.printReturn(null);
    }

    /**
     * Alagútban jégtörőfejnek nincs hatása.
     */
    @Override
    public void cleanWithIceBreaker() {
        CallChainLogger.printCall(this, "cleanWithIceBreaker()");
        CallChainLogger.printReturn(null);
    }

    /**
     * Alagútban hányófejnek nincs hatása.
     */
    @Override
    public void cleanWithVomittingHead() {
        CallChainLogger.printCall(this, "cleanWithVomittingHead()");
        CallChainLogger.printReturn(null);
    }

    /**
     * Alagútban zúzalékszórónak nincs hatása.
     *
     * @param timestamp a takarítás időbélyege (figyelmen kívül hagyva)
     */
    @Override
    public void cleanWithStoneVomitter(int timestamp) {
        CallChainLogger.printCall(this, "cleanWithStoneVomitter(" + timestamp + ")");
        CallChainLogger.printReturn(null);
    }

    /**
     * Alagútban a ticknek nincs hatása, az állapot nem változik.
     *
     * @param timestamp az aktuális idő
     */
    @Override
    public void tick(int timestamp) {
        CallChainLogger.printCall(this, "tick(" + timestamp + ")");
        CallChainLogger.printReturn(null);
    }
}
