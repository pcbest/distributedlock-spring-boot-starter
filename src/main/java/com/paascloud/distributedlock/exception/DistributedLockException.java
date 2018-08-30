package com.paascloud.distributedlock.exception;

/**
 * The type LockAnnotation exception.
 *
 * @author paascloud.net@gmail.com
 */
public class DistributedLockException extends RuntimeException {

    private static final long serialVersionUID = 8328954568921180023L;

    /**
     * Instantiates a new LockAnnotation exception.
     */
    public DistributedLockException() {
        super();
    }

    /**
     * Instantiates a new LockAnnotation exception.
     *
     * @param message the message
     */
    public DistributedLockException(String message) {
        super(message);
    }

    /**
     * Instantiates a new LockAnnotation exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public DistributedLockException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new LockAnnotation exception.
     *
     * @param cause the cause
     */
    public DistributedLockException(Throwable cause) {
        super(cause);
    }
}