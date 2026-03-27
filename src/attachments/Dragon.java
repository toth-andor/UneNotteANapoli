package attachments;

import map.Lane;

/**
 * Egy sárkányfej típusú hókotró-fejet reprezentál, amely biokerozin elégetésével azonnal
 * eltávolítja a havat és a jeget a sávról. Fogyóanyagot igényel, és ha az elfogy,
 * hatástalanná válik, amíg nem töltik fel.
 */
public class Dragon extends Attachment {

    int fuelLevel;

    /**
     * Egy egységnyi biokerozin-utántöltés ára.
     */
    private int priceOfFuel;

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
        l.cleanWithSaltVomitter(timestamp);
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
        return budget - priceOfFuel;
    }
}
