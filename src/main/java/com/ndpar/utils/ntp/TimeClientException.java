package com.ndpar.utils.ntp;

/**
 * Unchecked wrapper exception for all checked exception related to NTP.
 */
public class TimeClientException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TimeClientException() {
        super();
    }

    public TimeClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeClientException(String message) {
        super(message);
    }

    public TimeClientException(Throwable cause) {
        super(cause);
    }
}
