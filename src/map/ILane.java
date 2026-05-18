package map;

import vehicle.Vehicle;
import attachments.Attachment;

/**
 * A Lane osztály funkcionalitását egységesítő interfész.
 * A Vehicle osztály a currentLane kapcsolaton keresztül tartja számon,
 * hogy a jármű melyik sávban halad.
 */
public interface ILane {

    /**
     * A paraméterként átadott jármű befogadásáért felel, regisztrálja a sávon.
     *
     * @param v         a befogadandó jármű
     * @param timestamp az aktuális idő
     * @return true, ha a befogadás sikerült
     */
    boolean pushVehicle(Vehicle v, int timestamp);

    /**
     * A paraméterként átadott jármű eltávolításáért felel, kiregisztrálja a sávból.
     *
     * @param v az eltávolítandó jármű
     */
    void popVehicle(Vehicle v);

    /**
     * A függvény hívásának hatására az adott sávon paraméterként átadott snow mennyiségű hó esik.
     *
     * @param snow a lehullott hó mennyisége
     */
    void snowFall(int snow);

    /**
     * Hattatja a sávra a hókotró aktív fejét, az Attachment osztály cleanLane metódusának
     * hatására hívódik.
     *
     * @param head az aktív fej
     */
    void cleanWithHead(Attachment head);
}
