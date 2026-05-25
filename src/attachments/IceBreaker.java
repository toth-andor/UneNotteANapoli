package attachments;

import controller.AttachmentLogic.AttachmentType;
import map.Lane;

/**
 * Egy jégtörőfej típusú hókotró-fejet reprezentál, amely feltöri a jeget, de nem
 * távolítja el. A feltört jég hóvá alakul, amelynek eltávolításához további takarítási
 * műveletre van szükség egy söprő- vagy hányófejjel. Fogyóanyagot nem igényel.
 */
public class IceBreaker extends Attachment {
    static final int PRICE = 3;

    /**
     * Létrehoz egy új jégtörő fejet.
     *
     */
    public IceBreaker() {
        super(PRICE);
    }

    /**
     * Meghívja l cleanWithHead metódusát, amely a LaneState logikája alapján a jeget hóvá alakítja,
     * de nem távolítja el. Mindig true-val tér vissza, mivel fogyóanyagot nem igényel.
     *
     * @param l a takarítandó sáv
     * @param timestamp az aktuális idő
     */
    public boolean cleanLane(Lane l, int timestamp) {
        l.cleanWithIceBreaker();
        return l.getStateWasChanged();
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
        return AttachmentType.ICE_BREAKER;
    }
}
