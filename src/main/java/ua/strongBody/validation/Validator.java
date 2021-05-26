package ua.strongBody.validation;

import ua.strongBody.exceptions.ValidationException;

public interface Validator<T, V> {
    T validate(V v) throws ValidationException;
}
