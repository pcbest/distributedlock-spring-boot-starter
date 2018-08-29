package com.paascloud.distributedlock.annotation;

/**
 * The enum Lock type.
 *
 * @author paascloud.net@gmail.com
 */
public enum LockType {
    /**
     * 普通锁
     */
    LOCK("Lock"),

    /**
     * 读锁
     */
    READ_LOCK("ReadLock"),

    /**
     * 写锁
     */
    WRITE_LOCK("WriteLock"),
    ;

    private String value;

    LockType(String value) {
        this.value = value;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }
}