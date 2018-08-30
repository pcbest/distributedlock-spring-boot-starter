package com.paascloud.distributedlock.service;

import java.util.concurrent.TimeUnit;

import com.paascloud.distributedlock.annotation.LockTypeEnum;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * The interface Distributed locker.
 *
 * @param <T> the type parameter
 * @author paascloud.net @gmail.com
 */
public interface DistributedLocker<T> {

    /**
     * 尝试加锁，最多等待${waitTime}秒，上锁以后${leaseTime}秒自动解锁.
     *
     * @param lockKey   key
     * @param waitTime  等待时间(秒)
     * @param leaseTime 加锁时间(秒)
     * @return the boolean
     */
    T tryLock(String lockKey, Integer waitTime, Integer leaseTime);

    /**
     * 尝试加锁，最多等待${waitTime}秒，上锁以后${leaseTime}秒自动解锁.
     *
     * @param lockKey   key
     * @param waitTime  等待时间(秒)
     * @param leaseTime 加锁时间(秒)
     * @param async     是否异步
     * @return the boolean
     */
    T tryLock(String lockKey, Integer waitTime, Integer leaseTime, boolean async);

    /**
     * 尝试加锁，最多等待${waitTime}秒，上锁以后${leaseTime}秒自动解锁.
     *
     * @param lockKey   key
     * @param unit      超时单位
     * @param waitTime  等待时间
     * @param leaseTime 加锁时间
     * @param async     the async
     * @return the boolean
     */
    T tryLock(String lockKey, TimeUnit unit, Integer waitTime, Integer leaseTime, boolean async);

    /**
     * 尝试加锁，最多等待${waitTime}秒，上锁以后${leaseTime}秒自动解锁.
     *
     * @param lockType  锁类型
     * @param lockKey   key
     * @param waitTime  等待时间
     * @param leaseTime 加锁时间
     * @param async     是否异步
     * @return the boolean
     */
    T tryLock(LockTypeEnum lockType, String lockKey, Integer waitTime, Integer leaseTime, boolean async);


    /**
     * 尝试加锁，最多等待${waitTime}秒，上锁以后${leaseTime}秒自动解锁.
     *
     * @param lockType  锁类型
     * @param lockKey   key
     * @param unit      超时单位
     * @param waitTime  等待时间
     * @param leaseTime 加锁时间
     * @param async     是否异步
     * @return the boolean
     */
    T tryLock(LockTypeEnum lockType, String lockKey, TimeUnit unit, Integer waitTime, Integer leaseTime, boolean async);

    /**
     * 解锁.
     *
     * @param lockType the lock type
     * @param lockKey  key
     */
    void unlock(LockTypeEnum lockType, String lockKey);

    /**
     * 解锁.
     *
     * @param lock 锁
     */
    void unlock(T lock);


    /**
     * Invoke object.
     *
     * @param joinPoint 切点
     * @param lockType  锁类型
     * @param lockKey   key
     * @param waitTime  锁等待时间
     * @param leaseTime 加锁时间
     * @param async     是否异步
     * @return the object
     * @throws Throwable the throwable
     */
    Object invoke(ProceedingJoinPoint joinPoint, LockTypeEnum lockType, String lockKey, Integer waitTime, Integer leaseTime, boolean async) throws Throwable;
}