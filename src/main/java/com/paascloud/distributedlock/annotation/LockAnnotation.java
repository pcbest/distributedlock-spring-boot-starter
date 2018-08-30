package com.paascloud.distributedlock.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * The interface Elastic job config.
 *
 * @author paascloud.net @gmail.com
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LockAnnotation {


    /**
     * 分布式锁类型.
     *
     * @return the distributed lock type enum
     */
    DistributedLockTypeEnum name() default DistributedLockTypeEnum.REDISSON;

    /**
     * 锁类型.
     * {@link LockTypeEnum}
     *
     * @return the lock type
     */
    LockTypeEnum lockType() default LockTypeEnum.LOCK;

    /**
     * key.
     *
     * @return the string
     */
    String lockKey();

    /**
     * 时间单位(默认秒).
     *
     * @return the time unit
     */
    TimeUnit unit() default TimeUnit.SECONDS;

    /**
     * 锁等待时间(默认0标识不等待).
     *
     * @return the int
     */
    int waitTime() default 0;

    /**
     * 持锁时间，持锁超过此时间则自动丢弃锁
     * 单位毫秒，默认5秒
     */
    int leaseTime() default 30;

    /**
     * 是否异步, zk锁不支持此参数为true.
     *
     * @return the boolean
     */
    boolean async() default false;
}