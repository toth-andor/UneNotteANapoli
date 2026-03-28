package attachments;

import map.Lane;
import skeleton.Skeleton;
import skeleton.Skeleton.CallChainLogger;

/**
 * Egy jégtörőfej típusú hókotró-fejet reprezentál, amely feltöri a jeget, de nem
 * távolítja el. A feltört jég hóvá alakul, amelynek eltávolításához további takarítási
 * műveletre van szükség egy söprő- vagy hányófejjel. Fogyóanyagot nem igényel.
 */
public class IceBreaker extends Attachment {

    /**
     * Létrehoz egy új jégtörő fejet.
     *
     * @param price a fej vételára
     */
    public IceBreaker(int price) {
        super(price);
    }

    /**
     * Meghívja l cleanWithHead metódusát, amely a LaneState logikája alapján a jeget hóvá alakítja,
     * de nem távolítja el. Mindig true-val tér vissza, mivel fogyóanyagot nem igényel.
     *
     * @param l a takarítandó sáv
     * @param timestamp az aktuális idő
     */
    public boolean cleanLane(Lane l, int timestamp) {
        CallChainLogger.printCall(
            this,
            "cleanLane(" + Skeleton.getEntityByRef(l) + ", " + timestamp + ")"
        );
        l.cleanWithIceBreaker();
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
