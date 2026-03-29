package attachments;

import map.Lane;
import skeleton.Skeleton;
import skeleton.Skeleton.CallChainLogger;

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
        CallChainLogger.printCall(
            this,
            "cleanLane(" + Skeleton.getEntityByRef(l) + ", " + timestamp + ")"
        );
        if (fuelLevel <= 0) {
            CallChainLogger.printReturn("false");
            return false;
        }
        l.cleanWithDragon();
        fuelLevel--;
        CallChainLogger.printReturn("true");
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
        CallChainLogger.printCall(this, "refill(" + budget + ")");
        if (budget < priceOfFuel) {
            CallChainLogger.printReturn(budget + "");
            return budget;
        }
        int result = budget - priceOfFuel;
        fuelLevel = FULL_TANK;
        CallChainLogger.printReturn(result + "");
        return result;
    }
}
