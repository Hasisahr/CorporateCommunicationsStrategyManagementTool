package hr.javafx.project.csmt.model;

/**
 * Generic class for representing a pair of two related values.
 *Each pair consists of a first and second element, which can be of different types.
 *
 * @param <T> a generic type of the first element
 * @param <P> a generic type of the second element
 * This class is used for bundling two objects together into a single object.
 */


public class Pair<T, P>{
    private T first;
    private P second;

    public Pair(T first, P second) {
        this.first = first;
        this.second = second;
    }
    public T getFirst() {
        return first;
    }
    public void setFirst(T first) {
        this.first = first;
    }
    public P getSecond() {
        return second;
    }
    public void setSecond(P second) {
        this.second = second;
    }

}
