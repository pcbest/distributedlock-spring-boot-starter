package com.paascloud.distributedlock.annotation;

/**
 * The enum Distributed lock type enum.
 *
 * @author paascloud.net@gmail.com
 */
public enum DistributedLockTypeEnum {
    /**
     * zookeeper锁
     */
    ZK,

    /**
     * redisson锁
     */
    REDISSON;

    public static String getBeanName(DistributedLockTypeEnum typeEnum) {
        if (typeEnum == ZK) {
            return "zkDistributedLocker";
        } else {
            return "redissonDistributedLocker";
        }
    }
}