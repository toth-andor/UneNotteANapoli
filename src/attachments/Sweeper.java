package attachments;

import map.Lane;
import skeleton.Skeleton;
import skeleton.Skeleton.CallChainLogger;

/**
 * Egy söprőfej típusú hókotró-fejet reprezentál, amely a havat és a feltört jeget
 * közvetlenül a hókotró melletti jobb oldali sávra tolja. Ha nem létezik jobb oldali sáv,
 * vagy a takarítás hídon történik, a hó eltűnik. Feltöretlen jeget nem képes eltávolítani,
 * fogyóanyagot nem igényel.
 */
public class Sweeper extends Attachment {

    static final int PRICE = 0;
    /**
     * Létrehoz egy új söprő fejet.
     *
     * @param price a fej vételára
     */
    public Sweeper() {
        super(PRICE);
    }

    /**
     * Meghívja l cleanWithHead metódusát. A LaneState logikája alapján, ha létezik jobb oldali
     * szomszédos sáv és nem hídon történik a takarítás, a havat oda tolja; egyébként eltűnik.
     * Feltöretlen jeget nem távolít el. Mindig true-val tér vissza, mivel fogyóanyagot nem igényel.
     *
     * @param l a takarítandó sáv
     * @param timestamp az aktuális idő
     */
    public boolean cleanLane(Lane l, int timestamp) {
        CallChainLogger.printCall(
            this,
            "cleanLane(" + Skeleton.getEntityByRef(l) + ", " + timestamp + ")"
        );
        l.cleanWithSweeper();
        CallChainLogger.printReturn("true");
        return true;
    }

    /**
     * Az alapimplementáció változatlanul visszaadja a budget értékét, mivel nem fogyóanyagos
     * fejeket nem kell feltölteni.
     *
     * @param budget a feltöltésre rendelkezésre álló keret
     * @return a felhasználás utáni maradék budget
     */
    public int refill(int budget) {
        CallChainLogger.printCall(this, "refill(" + budget + ")");
        CallChainLogger.printReturn(budget + "");
        return budget;
    }
}
