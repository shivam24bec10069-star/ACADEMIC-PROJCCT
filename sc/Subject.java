package com.logiflow.pattern;

/**
 * Subject interface in the Observer design pattern.
 */
public interface Subject {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
}
