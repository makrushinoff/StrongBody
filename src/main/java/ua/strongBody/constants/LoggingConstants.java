package ua.strongBody.constants;

public final class LoggingConstants {
    private LoggingConstants() {
    }

    public static final String LOG_DEBUG_EMPTY_PATTERN = "Method was called.";
    public static final String LOG_DEBUG_ONE_ARG_PATTERN = "Method was called with argument: '{}'.";
    public static final String LOG_DEBUG_TWO_ARG_PATTERN = "Method was called with argument(s): Customer: '{}', id: '{}'.";

    public static final String GENERAL_PRODUCT_NOT_FOUND_PATTERN = "Product with %s: '%s' not found!";
    public static final String GENERAL_CUSTOMER_NOT_FOUND_PATTERN = "Customer with %s: '%s' not found!";
    public static final String GENERAL_CART_NOT_FOUND_PATTERN = "Cart with %s: '%s' not found!";
    public static final String GENERAL_BOOKING_NOT_FOUND_PATTERN = "Booking with %s: '%s' not found!";
    public static final String GENERAL_ORDER_NOT_FOUND_PATTERN = "Booking with %s: '%s' not found!";

    public static final String VALIDATION_FAILED_PATTERN = "Validation stage is failed. Message: '%s'";
    public static final String SAVE_FAILED_PATTERN = "Customer save process is failed. Exception: '%s'";
    public static final String REGISTRATION_SUCCESS_PATTERN = "Customer with username '%s' was successfully registered!";

}
