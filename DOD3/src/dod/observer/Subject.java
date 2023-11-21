package dod.observer;

import java.util.ArrayList;
import java.util.List;

public class Subject {
    private List<GameObserver> observers = new ArrayList<>();

    public void registerObserver(GameObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers(String message) {
        for (GameObserver observer : observers) {
            observer.update(message);
        }
    }
}
