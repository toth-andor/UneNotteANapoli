package Vehicle;

/**
 * Egy takarító játékost reprezentál.
 * Kezeli a játékoshoz tartozó egy vagy több hókotró működését, nyilvántartja a rendelkezésre
 * álló pénzösszeget, amelyből fejek vásárlása, fogyóanyag-utántöltés és új hókotró vásárlása
 * finanszírozható. Pontszámát a megtisztított útszakaszok után kapott bevétel adja.
 */
public class Cleaner implements IScoreOwner {

    /**
     * A takarító rendelkezésére álló pénzösszeg.
     */
    private int balance;

    /**
     * Létrehoz egy új takarítót.
     *
     * @param initialBalance a kezdeti pénzösszeg
     */
    public Cleaner(int initialBalance) {
        this.balance = initialBalance;
    }

    /**
     * Növeli a rendelkezésre álló pénzösszeget a megadott összeggel.
     * Megtisztított útszakasz után hívódik meg.
     *
     * @param amount a hozzáadandó összeg
     */
    public void addIncome(int amount) {
        balance += amount;
    }

    /**
     * Beállítja a rendelkezésre álló pénzösszeget a megadott értékre.
     * Vásárlás után hívódik meg.
     *
     * @param score az új pénzösszeg
     */
    public void setScore(int score) {
        balance = score;
    }

    /**
     * Visszaadja az aktuális pontszámot, amely a megszerzett bevétel
     * alapján számítódik.
     *
     * @return az aktuális pontszám
     */
    public int getScore() {
        return balance;
    }
}
