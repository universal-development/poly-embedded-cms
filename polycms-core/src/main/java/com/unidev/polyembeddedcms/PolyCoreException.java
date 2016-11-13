package com.unidev.polyembeddedcms;

import com.unidev.platform.common.exception.CommonRuntimeException;

/**
 * Exception thrown on poly core error
 */
public class PolyCoreException extends CommonRuntimeException {

    public PolyCoreException() {
    }

    public PolyCoreException(String message) {
        super(message);
    }

    public PolyCoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public PolyCoreException(Throwable cause) {
        super(cause);
    }
}
