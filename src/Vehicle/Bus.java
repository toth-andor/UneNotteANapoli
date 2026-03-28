package Vehicle;

import map.Lane;
import map.Road;
import skeleton.Skeleton.CallChainLogger;

/**
 * Egy buszvezető által irányított buszt reprezentál.
 * Két végállomás között közlekedik, és minden sikeres forduló után bevételt szerez.
 * A megtett fordulók száma alapján pontszámot tart nyilván.
 */
public class Bus extends Commuter implements IScoreOwner {

    /**
     * A sikeres forduló után járó bevétel.
     */
    static final int TURN_AROUND_BONUS = 1;

    /**
     * Az eddig megszerzett bevétel.
     */
    private int balance;

    /**
     * Létrehoz egy új buszt.
     *
     * @param destination1 az első végállomás
     * @param destination2 a második végállomás
     * @param currentLane a busz kezdeti sáva
     * @param balance az induláskor megadott kezdeti bevétel
     */
    public Bus(
        Road destination1,
        Road destination2,
        Lane currentLane,
        int balance
    ) {
        super(destination1, destination2, currentLane);
        this.balance = balance;
    }

    /**
     * Növeli a bevételt a megadott összeggel.
     * Sikeres forduló után hívódik meg.
     *
     * @param amount a bevételhez hozzáadandó összeg
     */
    public void addIncome(int amount) {
        CallChainLogger.printCall(this, "addIncome(" + amount + ")");
        balance += amount;
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

    /**
     * Frissíti a bevételt a forduló után járó bónusz hozzáadásával.
     * A busz sikeres fordulója után hívódik meg.
     */
    @Override
    protected void updateIncome() {
        CallChainLogger.printCall(this, "updateIncome()");
        CallChainLogger.printReturn(balance + "");
        balance += TURN_AROUND_BONUS;
    }
}
