package view.ObserverLogic;

public interface Observable {
    /**
     * Új nézet regisztrálása az adott modell-elemhez.
     */
    void addObserver(Observer o);

    /**
     * Végigiterál a regisztrált nézeteken és meghívja azok update() metódusát.
     */
    void notifyObservers();
}