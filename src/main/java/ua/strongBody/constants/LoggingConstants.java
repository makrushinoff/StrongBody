package ua.strongBody.constants;

public enum LoggingConstants {

    LOG_DEBUG_EMPTY_PATTERN("Method was called."),
    LOG_DEBUG_ONE_ARG_PATTERN("Method was called with argument: '{}'."),
    LOG_DEBUG_TWO_ARG_PATTERN("Method was called with argument(s): Customer: '{}', id: '{}'."),

    GENERAL_PRODUCT_NOT_FOUND_PATTERN("Product with %s: '%s' not found!"),
    GENERAL_CUSTOMER_NOT_FOUND_PATTERN("Customer with %s: '%s' not found!"),
    GENERAL_CART_NOT_FOUND_PATTERN("Cart with %s: '%s' not found!"),
    GENERAL_BOOKING_NOT_FOUND_PATTERN("Booking with %s: '%s' not found!"),
    GENERAL_ORDER_NOT_FOUND_PATTERN("Booking with %s: '%s' not found!"),

    VALIDATION_FAILED_PATTERN("Validation stage is failed. Message: '%s'"),
    SAVE_FAILED_PATTERN("Customer save process is failed. Exception: '%s'"),
    REGISTRATION_SUCCESS_PATTERN("Customer with username '%s' was successfully registered!");

    private final String message;

    LoggingConstants(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
