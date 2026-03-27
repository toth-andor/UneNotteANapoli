package map;

import Vehicle.Vehicle;
import attachments.Attachment;

/**
 * A {@link Lane} osztály funkcionalitását egységesítő interfész.
 * A {@link Vehicle} osztály a {@code currentLane} kapcsolaton keresztül
 * hivatkozik erre az interfészre.
 */
public interface ILane {

    /**
     * A paraméterként átadott járművet befogadja és regisztrálja a sávon.
     *
     * @param v         a befogadandó jármű
     * @param timestamp az aktuális idő
     * @return true, ha a befogadás sikerült
     */
    boolean pushVehicle(Vehicle v, int timestamp);

    /**
     * A paraméterként átadott járművet eltávolítja és kiregisztrálja a sávból.
     *
     * @param v az eltávolítandó jármű
     */
    void popVehicle(Vehicle v);

    /**
     * A sávra megadott mennyiségű hó hull.
     *
     * @param snow a lehullott hó mennyisége
     */
    void snowFall(int snow);

    /**
     * A hókotró aktív fejének hatását alkalmazza a sávra.
     * Az {@link Attachment#cleanLane} metódus hatására hívódik.
     *
     * @param head az aktív fej
     */
    void cleanWithHead(Attachment head);
}
