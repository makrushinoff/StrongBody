package ua.strongBody.exceptions;

public class FieldValidationException extends RuntimeException {
    private final String invalidFieldName;
    private final String invalidValue;
    private final String reason;

    public FieldValidationException(String invalidFieldName, String invalidValue, String reason) {
        this.invalidFieldName = invalidFieldName;
        this.invalidValue = invalidValue;
        this.reason = reason;
    }

    public String getInvalidFieldName() {
        return invalidFieldName;
    }

    public String getInvalidValue() {
        return invalidValue;
    }

    public String getReason() {
        return reason;
    }
}
