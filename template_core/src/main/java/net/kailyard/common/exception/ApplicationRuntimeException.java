package net.kailyard.common.exception;

import org.springframework.core.NestedExceptionUtils;

/**
 * @version 1.0
 */
public class ApplicationRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -4092344129244899656L;

    public ApplicationRuntimeException(String msg) {
        super(msg);
    }

    public ApplicationRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
        if(this.getCause() == null && cause != null) {
            this.initCause(cause);
        }

    }

    public String getMessage() {
        return NestedExceptionUtils.buildMessage(super.getMessage(), this.getCause());
    }
}
