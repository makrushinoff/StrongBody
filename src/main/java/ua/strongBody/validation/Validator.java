package ua.strongBody.validation;

import ua.strongBody.exceptions.ValidationException;

public interface Validator<V> {
    void validate(V v) throws ValidationException;
}
