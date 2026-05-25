package attachments;

import controller.AttachmentLogic.AttachmentType;
import map.Lane;

/**
 * Egy söprőfej típusú hókotró-fejet reprezentál, amely a havat és a feltört jeget
 * közvetlenül a hókotró melletti jobb oldali sávra tolja. Ha nem létezik jobb oldali sáv,
 * vagy a takarítás hídon történik, a hó eltűnik. Feltöretlen jeget nem képes eltávolítani,
 * fogyóanyagot nem igényel.
 */
public class Sweeper extends Attachment {

    static final int PRICE = 1;
    /**
     * Létrehoz egy új söprő fejet.
     *
     * 
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
        l.cleanWithSweeper();
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
        return budget;
    }
    @Override
    public controller.AttachmentLogic.AttachmentType getType() {
        return AttachmentType.SWEEPER;
    }
}
