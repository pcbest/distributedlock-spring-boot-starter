package com.paascloud.distributedlock.annotation;

/**
 * The enum LockAnnotation type enum.
 *
 * @author paascloud.net @gmail.com
 */
public enum LockTypeEnum {
    /**
     * 普通锁
     */
    LOCK("LockAnnotation"),

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

    LockTypeEnum(String value) {
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