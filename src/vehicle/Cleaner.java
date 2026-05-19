package vehicle;

import java.util.ArrayList;

import map.Lane;
import view.ObserverLogic.Observable;
import view.ObserverLogic.Observer;

/**
 * Egy takarító játékost reprezentál.
 * Kezeli a játékoshoz tartozó egy vagy több hókotró működését, nyilvántartja a rendelkezésre
 * álló pénzösszeget, amelyből fejek vásárlása, fogyóanyag-utántöltés és új hókotró vásárlása
 * finanszírozható. Pontszámát a megtisztított útszakaszok után kapott bevétel adja.
 */
public class Cleaner implements IScoreOwner, Observable {

    private final ArrayList<Observer> observers = new ArrayList<>();

    /**
     * A takarító rendelkezésére álló pénzösszeg.
     */
    private int balance;
    private ArrayList<SnowPlow> snowPlows;

    public void registerSnowPlow(SnowPlow snowPlow) {
        snowPlows.add(snowPlow);
    }

    public int buySnowPlow(Lane lane) {
        if (balance < 100) // TODO: move price to static var
            return balance;

        balance -= 100;
        new SnowPlow(this, lane, null); //TODO resolve name
        return balance;
    }

    public ArrayList<SnowPlow> getSnowPlows() {
        return snowPlows;
    }

    /**
     * Létrehoz egy új takarítót.
     *
     * @param initialBalance a kezdeti pénzösszeg
     */
    public Cleaner(int initialBalance) {
        this.balance = initialBalance;
        this.snowPlows = new ArrayList<>();
    }

    /**
     * Növeli a rendelkezésre álló pénzösszeget a megadott összeggel.
     * Megtisztított útszakasz után hívódik meg.
     *
     * @param amount a hozzáadandó összeg
     */
    public void addIncome(int amount) {
        balance += amount;
        notifyObservers();
    }

    /**
     * Beállítja a rendelkezésre álló pénzösszeget a megadott értékre.
     * Vásárlás után hívódik meg.
     *
     * @param score az új pénzösszeg
     */
    public void setScore(int score) {
        balance = score;
        notifyObservers();
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

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }
}
