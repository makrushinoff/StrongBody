package ua.strongBody.populator;

public interface Populator<T, V> {
    void populate(T t, V v);
}
