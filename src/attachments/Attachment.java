package attachments;

import map.Lane;

/**
 * Az összes hókotró-fej közös ősét reprezentálja.
 * Tárolja a fej alapárát, és alapértelmezett implementációt biztosít a takarítási és
 * újratöltési műveletekhez. Olyan fejek esetén, amelyek nem igényelnek fogyóanyagot,
 * ezek az alapimplementációk elegendőek.
 */
public abstract class Attachment implements IAttachment {

    /**
     * A fej vételára.
     */
    protected int price;

    /**
     * Létrehoz egy új hókotró fejet.
     *
     * @param price a fej vételára
     */
    public Attachment(int price) {
        this.price = price;
    }

    /**
     * Visszaadja a fej vételárát.
     *
     * @return a fej ára
     */
    public int getPrice() {
        return price;
    }

    /**
     * Meghívja l cleanWithHead metódusát, amely a LaneState logikája alapján dönti el, hogy
     * a takarítás elvégezhető-e (pl. feltöretlen jeget nem lehet söprőfejjel eltávolítani).
     * Az alapimplementáció mindig true-val tér vissza. Fogyóanyagot igénylő leszármazottaknál
     * felül van definiálva.
     *
     * @param l a takarítandó sáv
     * @param timestamp az aktuális idő
     */
    public abstract boolean cleanLane(Lane l, int timestamp);

    /**
     * Az alapimplementáció változatlanul visszaadja a budget értékét, mivel nem fogyóanyagos
     * fejeket nem kell feltölteni. Fogyóanyagos leszármazottaknál felül van definiálva.
     *
     * @param budget a feltöltésre rendelkezésre álló keret
     * @return a felhasználás utáni maradék budget
     */
    public abstract int refill(int budget);
    public abstract controller.AttachmentLogic.AttachmentType getType();

}
