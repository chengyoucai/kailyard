package net.kailyard.common.exception;

import org.springframework.core.NestedExceptionUtils;

/**
 */
public class ApplicationCheckedException extends Exception {
    private static final long serialVersionUID = 7642364012381602075L;

    public ApplicationCheckedException(String msg) {
        super(msg);
    }

    public ApplicationCheckedException(String msg, Throwable cause) {
        super(msg, cause);
        if(this.getCause() == null && cause != null) {
            this.initCause(cause);
        }

    }

    public String getMessage() {
        return NestedExceptionUtils.buildMessage(super.getMessage(), this.getCause());
    }

}
