package com.paascloud.distributedlock.service.impl;

import com.paascloud.distributedlock.annotation.LockType;
import com.paascloud.distributedlock.exception.DistributedLockException;
import com.paascloud.distributedlock.service.DistributedLocker;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * The type Redisson distributed locker.
 *
 * @author paascloud.net@gmail.com
 */
@Slf4j
public class ZkDistributedLocker implements DistributedLocker<InterProcessMutex> {

    private static CuratorFramework curatorFramework;
    private static final String PREFIX_KEY = "/lock";

    private static void setClient(CuratorFramework cf) {
        ZkDistributedLocker.curatorFramework = cf;
    }

    public ZkDistributedLocker(CuratorFramework curatorFramework) {
        setClient(curatorFramework);
    }

    private InterProcessMutex getLock(LockType lockType, String lockKey) {
        lockKey = PREFIX_KEY + lockKey;
        switch (lockType) {
            case LOCK:
                return new InterProcessMutex(curatorFramework, lockKey);
            case READ_LOCK:
                return new InterProcessReadWriteLock(curatorFramework, lockKey).readLock();
            case WRITE_LOCK:
                return new InterProcessReadWriteLock(curatorFramework, lockKey).writeLock();
            default:
                log.error("lockType isn't match " + lockType);
                break;
        }
        throw new DistributedLockException("lockType isn't match " + lockType);
    }

    @Override
    public InterProcessMutex tryLock(String lockKey, Integer waitTime, Integer leaseTime) {
        return this.tryLock(lockKey, waitTime, leaseTime, false);
    }

    @Override
    public InterProcessMutex tryLock(String lockKey, Integer waitTime, Integer leaseTime, boolean async) {
        return this.tryLock(lockKey, TimeUnit.SECONDS, waitTime, leaseTime, false);
    }

    @Override
    public InterProcessMutex tryLock(String lockKey, TimeUnit unit, Integer waitTime, Integer leaseTime, boolean async) {
        return this.tryLock(LockType.LOCK, lockKey, TimeUnit.SECONDS, waitTime, leaseTime, false);
    }

    @Override
    public InterProcessMutex tryLock(LockType lockType, String lockKey, TimeUnit unit, Integer waitTime, Integer leaseTime, boolean async) {
        if (curatorFramework == null) {
            throw new DistributedLockException("ZkLock isn't initialized");
        }

        if (StringUtils.isEmpty(lockKey)) {
            throw new DistributedLockException("lockKey key is null or empty");
        }

        if (waitTime == null) {
            throw new DistributedLockException("waitTime key is null");
        }

        if (leaseTime != null) {
            log.warn("zkLock isn't support for leaseTime, lockKey={}", lockKey);
        }

        if (async) {
            throw new DistributedLockException("Async lock of ZkLock isn't support, lockKey={}" + lockKey);
        }

        if (unit == null) {
            unit = TimeUnit.SECONDS;
        }

        boolean acquired;
        InterProcessMutex lock = getLock(lockType, lockKey);

        if (lock == null) {
            log.error("get lock is null lockKey={}", lockKey);
            return null;
        }

        try {
            acquired = lock.acquire(waitTime, unit);
        } catch (Exception e) {
            log.error("加锁出现异常, e={}", e.getMessage(), e);
            throw new DistributedLockException("加锁出现异常, " + e.getMessage());
        }

        return acquired ? lock : null;
    }

    @Override
    public void unlock(LockType lockType, String lockKey) {
        if (StringUtils.isEmpty(lockKey)) {
            throw new DistributedLockException("lockKey key is null or empty");
        }

        throw new DistributedLockException("unlock by lockKey, ZkLock isn't support");
    }
    
    @Override
    public void unlock(InterProcessMutex lock) {
        if (lock != null && lock.isAcquiredInThisProcess()) {
            try {
                lock.release();
            } catch (Exception e) {
                log.error("zkLock unlock is error. ex={}", e.getMessage(), e);
            }
        }
    }
}