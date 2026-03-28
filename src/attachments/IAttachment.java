package attachments;

import map.Lane;

/**
 * Az összes hókotró-fej egységes interfésze. Biztosítja, hogy a SnowPlow és más
 * komponensek típustól függetlenül kezelhessék a fejeket.
 */
public interface IAttachment {

    /**
     * Meghívja l cleanWithHead metódusát, ha a fej működőképes. A tényleges takarítási
     * logikát a LaneState végzi a cleanWithHead-en belül. Visszatérési értéke jelzi, hogy
     * a fej működőképes volt-e.
     *
     * @param l a takarítandó sáv
     * @param timestamp az aktuális idő
     * @return true, ha a fej működőképes volt, egyébként false
     */
    boolean cleanLane(Lane l, int timestamp);

    /**
     * Visszaadja a fej vételárát.
     *
     * @return a fej ára
     */
    int getPrice();

    /**
     * Megpróbálja feltölteni a fejet a megadott keretből.
     * Visszatérési értéke a felhasználás utáni maradék budget. Ha ret < budget, a
     * feltöltés sikeres volt.
     *
     * @param budget a feltöltésre rendelkezésre álló keret
     * @return a felhasználás utáni maradék budget
     */
    int refill(int budget);
}
