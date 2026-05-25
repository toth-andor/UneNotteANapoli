package attachments;

import controller.AttachmentLogic.AttachmentType;
import map.Lane;

/**
 * Egy sárkányfej típusú hókotró-fejet reprezentál, amely biokerozin elégetésével azonnal
 * eltávolítja a havat és a jeget a sávról. Fogyóanyagot igényel, és ha az elfogy,
 * hatástalanná válik, amíg nem töltik fel.
 */
public class Dragon extends Attachment {
    static final int PRICE = 10, PRICE_OF_FUEL = 2, FULL_TANK = 20;

    int fuelLevel;

    /**
     * Egy egységnyi biokerozin-utántöltés ára.
     */
    private int priceOfFuel;

    /**
     * Létrehoz egy új sárkányfejet.
     *
     * price a fej vételára
     * priceOfFuel a fogyóanyag utántöltés ára
     */
    public Dragon() {
        super(PRICE);
        this.priceOfFuel = PRICE_OF_FUEL;
        this.fuelLevel = 0;
    }

    /**
     * Csak akkor hívja meg l cleanWithHead metódusát, ha van elegendő fogyóanyag.
     * Ha nincs, false-szal tér vissza és takarítás nem történik.
     *
     * @param l a takarítandó sáv
     * @param timestamp az aktuális idő
     */
    public boolean cleanLane(Lane l, int timestamp) {
        if (fuelLevel <= 0) {
            return false;
        }
        l.cleanWithDragon();
        fuelLevel--;
        return true;
    }

    /**
     * Ha budget >= priceOfFuel, levonja a feltöltés árát, feltölti a fejet, majd visszaadja
     * a maradék budgetet. Ha nincs elegendő fedezet, változatlanul visszaadja a budget értékét.
     *
     * @param budget a feltöltésre rendelkezésre álló keret
     * @return a felhasználás utáni maradék budget
     */
    public int refill(int budget) {
        if (budget < priceOfFuel) {
            return budget;
        }
        int result = budget - priceOfFuel;
        fuelLevel = FULL_TANK;
        return result;
    }
    @Override
    public controller.AttachmentLogic.AttachmentType getType() {
        return AttachmentType.DRAGON;
    }
}
