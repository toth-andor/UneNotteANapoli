package view.ObserverLogic;

public interface Observable {
    void addObserver(Observer o);
    void notifyObservers();
}