package view.ObserverLogic;

public interface Observer {
    /**
     * A modell hívja meg állapotváltozáskor. Hatására a nézet frissíti
     * a belső adatait és kezdeményezi a grafikus felület újrarajzolását.
     */
    void update();
}