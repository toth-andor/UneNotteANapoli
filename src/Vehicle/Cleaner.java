package Vehicle;

import java.util.ArrayList;

import map.Lane;
import skeleton.Skeleton;
import skeleton.Skeleton.CallChainLogger;

/**
 * Egy takarító játékost reprezentál.
 * Kezeli a játékoshoz tartozó egy vagy több hókotró működését, nyilvántartja a rendelkezésre
 * álló pénzösszeget, amelyből fejek vásárlása, fogyóanyag-utántöltés és új hókotró vásárlása
 * finanszírozható. Pontszámát a megtisztított útszakaszok után kapott bevétel adja.
 */
public class Cleaner implements IScoreOwner {

    static final int SNOW_PLOW_PRICE = 30;

    ArrayList<SnowPlow> snowplows = new ArrayList<>();

    /**
     * A takarító rendelkezésére álló pénzösszeg.
     */
    private int balance;

    /**
     * Létrehoz egy új takarítót.
     *
     * @param initialBalance a kezdeti pénzösszeg
     */
    public Cleaner(int initialBalance, Lane dest) {
        this.balance = initialBalance;
        snowplows = new ArrayList<>();

        SnowPlow sn = new SnowPlow(this, dest);
        CallChainLogger.printCall(sn, "SnowPlow(" + Skeleton.getEntityByRef(this) + ", " + Skeleton.getEntityByRef(dest) + ")");
        CallChainLogger.printReturn(null);
        Skeleton.pushEntity("NewSnowPlowBought-" + sn.hashCode(), sn);

        sn.buyAttachment(sn.getOwnedTools().getFirst());

        snowplows.add(sn);
    }

    /**
     * Növeli a rendelkezésre álló pénzösszeget a megadott összeggel.
     * Megtisztított útszakasz után hívódik meg.
     *
     * @param amount a hozzáadandó összeg
     */
    public void addIncome(int amount) {
        CallChainLogger.printCall(this, "addIncome(" + amount + ")");
        balance += amount;
        CallChainLogger.printReturn(null);
    }

    /**
     * Beállítja a rendelkezésre álló pénzösszeget a megadott értékre.
     * Vásárlás után hívódik meg.
     *
     * @param score az új pénzösszeg
     */
    public void setScore(int score) {
        CallChainLogger.printCall(this, "setScore(" + score + ")");
        balance = score;
        CallChainLogger.printReturn(null);
    }

    /**
     * Visszaadja az aktuális pontszámot, amely a megszerzett bevétel
     * alapján számítódik.
     *
     * @return az aktuális pontszám
     */
    public int getScore() {
        CallChainLogger.printCall(this, "getScore()");
        CallChainLogger.printReturn(balance + "");
        return balance;
    }

    public boolean buySnowPlow(Lane dest) {
        CallChainLogger.printCall(this, "buySnowPlow(" + Skeleton.getEntityByRef(dest) + ")");
        if (balance < SNOW_PLOW_PRICE) {
            CallChainLogger.printReturn("false");
            return false;
        }
        balance -= SNOW_PLOW_PRICE;

        SnowPlow sn = new SnowPlow(this, dest);
        CallChainLogger.printCall(sn, "SnowPlow(" + Skeleton.getEntityByRef(this) + ", " + Skeleton.getEntityByRef(dest) + ")");
        CallChainLogger.printReturn(null);

        Skeleton.pushEntity("NewSnowPlowBought-" + sn.hashCode(), sn);


        snowplows.add(sn);

        CallChainLogger.printReturn("true");
        return true;
    }
}
