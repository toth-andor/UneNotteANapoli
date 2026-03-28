package attachments;

import map.Lane;
import skeleton.Skeleton;
import skeleton.Skeleton.CallChainLogger;

/**
 * Egy hányófej típusú hókotró-fejet reprezentál, amely a havat és a feltört jeget
 * messzebbre szórja, így az nem rakódik le egyik szomszédos sávra sem — a szél elfújja.
 * Feltöretlen jeget nem képes eltávolítani, fogyóanyagot nem igényel.
 */
public class VomitingHead extends Attachment {

    /**
     * Létrehoz egy új hányó fejet.
     *
     * @param price a fej vételára
     */
    public VomitingHead(int price) {
        super(price);
    }

    /**
     * Meghívja l cleanWithHead metódusát. A LaneState logikája alapján az eltávolított hó és
     * feltört jég egyik sávra sem kerül át, hanem eltűnik. Feltöretlen jeget nem távolít el.
     * Mindig true-val tér vissza, mivel fogyóanyagot nem igényel.
     *
     * @param l a takarítandó sáv
     * @param timestamp az aktuális idő
     * @return mindig true, mivel fogyóanyagot nem igényel
     */
    public boolean cleanLane(Lane l, int timestamp) {
        CallChainLogger.printCall(
            this,
            "cleanLane(" + Skeleton.getEntityByRef(l) + ", " + timestamp + ")"
        );
        l.cleanWithVomittingHead();
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
