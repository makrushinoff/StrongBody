package ua.strongBody.processors.post;

import java.util.List;

public interface PostProcessor<T> {
    void postProcess(List<T> tList);

    void postProcess(T t);
}
