package ua.strongBody.populator;

public interface Populator<T, V> {
    void convert(T t, V v);
}
