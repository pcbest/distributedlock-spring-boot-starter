package com.paascloud.distributedlock.service.impl;

import com.paascloud.distributedlock.annotation.LockType;
import com.paascloud.distributedlock.exception.DistributedLockException;
import com.paascloud.distributedlock.service.DistributedLocker;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.util.StringUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * The type Redisson distributed locker.
 *
 * @author paascloud.net@gmail.com
 */
@Slf4j
public class RedissonDistributedLocker implements DistributedLocker<RLock> {
    
    private static RedissonClient redissonClient;

    /**
     * The constant PREFIX_KEY.
     */
    private static final String PREFIX_KEY = "lock:";


    private void setClient(RedissonClient redissonClient) {
        RedissonDistributedLocker.redissonClient = redissonClient;
    }

    public RedissonDistributedLocker(RedissonClient redissonClient) {
        this.setClient(redissonClient);
    }

    private RLock getLock(LockType lockType, String lockKey) {
        lockKey = PREFIX_KEY + lockKey;
        switch (lockType) {
            case LOCK:
                redissonClient.getLock(lockKey);
            case READ_LOCK:
                return redissonClient.getReadWriteLock(lockKey).readLock();
            case WRITE_LOCK:
                return redissonClient.getReadWriteLock(lockKey).writeLock();
            default:
                log.error("lockType isn't match " + lockType);
                break;
        }
        throw new DistributedLockException("lockType isn't match " + lockType);
    }

    @Override
    public RLock tryLock(String lockKey, Integer waitTime, Integer leaseTime) {
        return this.tryLock(lockKey, waitTime, leaseTime, false);
    }

    @Override
    public RLock tryLock(String lockKey, Integer waitTime, Integer leaseTime, boolean async) {
        return this.tryLock(lockKey, TimeUnit.SECONDS, waitTime, leaseTime, false);
    }

    @Override
    public RLock tryLock(String lockKey, TimeUnit unit, Integer waitTime, Integer leaseTime, boolean async) {
        return this.tryLock(LockType.LOCK, lockKey, TimeUnit.SECONDS, waitTime, leaseTime, false);
    }

    @Override
    public RLock tryLock(LockType lockType, String lockKey, TimeUnit unit, Integer waitTime, Integer leaseTime, boolean async) {
        if (StringUtils.isEmpty(lockKey)) {
            throw new DistributedLockException("lockKey key is null or empty");
        }

        if (waitTime == null) {
            throw new DistributedLockException("waitTime key is null");
        }

        if (leaseTime == null) {
            throw new DistributedLockException("leaseTime key is null");
        }

        if (redissonClient == null) {
            throw new DistributedLockException("Redisson isn't initialized");
        }
        boolean acquired;
        RLock lock = getLock(lockType, lockKey);

        if (lock == null) {
            log.error("get lock is null lockKey={}", lockKey);
            return null;
        }

        try {
            if (async) {
                acquired = lock.tryLockAsync(waitTime, leaseTime, TimeUnit.MILLISECONDS).get();
            } else {
                acquired = lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException | ExecutionException e) {
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

        RLock lock = getLock(lockType, lockKey);
        lock.unlock();
    }
    
    @Override
    public void unlock(RLock lock) {
        lock.unlock();
    }
}