package attachments;

import map.Lane;
import skeleton.Skeleton;
import skeleton.Skeleton.CallChainLogger;

/**
 * Egy zúzalékszóró típusú hókotró-fejet reprezentál, amely zúzott követ juttat az útra,
 * ezzel megszünteti a jég csúszósságát. Fogyóanyagot igényel; ha az elfogy, hatástalanná
 * válik, amíg nem töltik fel. A zúzalék nem tömörödik, de hóesés hatására befedődhet.
 */
public class StoneVommiter extends Attachment {
    static final int PRICE = 5, PRICE_OF_FUEL = 1, FULL_TANK = 20;

    private int fuelLevel;
    private int priceOfFuel;

    /**
     * Létrehoz egy új zúzalékszóró fejet.
     *
     * price a fej vételára
     * priceOfFuel a fogyóanyag utántöltés ára
     */
    public StoneVommiter() {
        super(PRICE);
        this.priceOfFuel = PRICE_OF_FUEL;
        this.fuelLevel = 0;
    }

    /**
     * Csak akkor hívja meg l cleanWithStoneVomitter metódusát, ha van elegendő zúzalék.
     * Ha nincs, false-szal tér vissza és takarítás nem történik.
     *
     * @param l a takarítandó sáv
     * @param timestamp az aktuális idő
     */
    @Override
    public boolean cleanLane(Lane l, int timestamp) {
        CallChainLogger.printCall(
            this,
            "cleanLane(" + Skeleton.getEntityByRef(l) + ", " + timestamp + ")"
        );
        if (fuelLevel <= 0) {
            CallChainLogger.printReturn("false");
            return false;
        }
        l.cleanWithStoneVomitter(timestamp);
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
    @Override
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
