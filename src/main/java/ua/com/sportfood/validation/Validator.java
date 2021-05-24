package ua.com.sportfood.validation;

import ua.com.sportfood.exceptions.ValidationException;

public interface Validator<T, V> {
    T validate(V v) throws ValidationException;
}
