package com.paascloud.distributedlock.service.impl;

import com.paascloud.distributedlock.annotation.LockTypeEnum;
import com.paascloud.distributedlock.exception.DistributedLockException;
import com.paascloud.distributedlock.service.DistributedLocker;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
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

    private RLock getLock(LockTypeEnum lockType, String lockKey) {
        lockKey = PREFIX_KEY + lockKey;
        switch (lockType) {
            case LOCK:
                return redissonClient.getLock(lockKey);
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
        return this.tryLock(LockTypeEnum.LOCK, lockKey, TimeUnit.SECONDS, waitTime, leaseTime, false);
    }

    @Override
    public RLock tryLock(LockTypeEnum lockType, String lockKey, Integer waitTime, Integer leaseTime, boolean async) {
        return this.tryLock(LockTypeEnum.LOCK, lockKey, TimeUnit.SECONDS, waitTime, leaseTime, false);
    }

    @Override
    public RLock tryLock(LockTypeEnum lockType, String lockKey, TimeUnit unit, Integer waitTime, Integer leaseTime, boolean async) {
        if (redissonClient == null) {
            throw new DistributedLockException("Redisson isn't initialized");
        }

        if (StringUtils.isEmpty(lockKey)) {
            throw new DistributedLockException("lockKey key is null or empty");
        }

        if (waitTime == null) {
           waitTime = 0;
        }

        if (leaseTime == null) {
            throw new DistributedLockException("leaseTime key is null");
        }

        if (unit == null) {
            unit = TimeUnit.SECONDS;
        }

        boolean acquired;
        RLock lock = getLock(lockType, lockKey);

        if (lock == null) {
            log.error("get lock is null lockKey={}", lockKey);
            return null;
        }

        log.info("获取到锁 lock={}", lock);

        try {
            if (async) {
                acquired = lock.tryLockAsync(waitTime, leaseTime, unit).get();
            } else {
                acquired = lock.tryLock(waitTime, leaseTime, unit);
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("加锁出现异常, e={}", e.getMessage(), e);
            throw new DistributedLockException("加锁出现异常, " + e.getMessage());
        }

        return acquired ? lock : null;
    }

    @Override
    public void unlock(LockTypeEnum lockType, String lockKey) {
        if (StringUtils.isEmpty(lockKey)) {
            throw new DistributedLockException("lockKey key is null or empty");
        }

        RLock lock = getLock(lockType, lockKey);
        this.unlock(lock);
    }
    
    @Override
    public void unlock(RLock lock) {
        if (lock != null) {
            try {
                lock.unlock();
            } catch (Exception e) {
                log.error("redisson unlock is error. ex={}", e.getMessage(), e);
            }
        }

    }

    @Override
    public Object invoke(ProceedingJoinPoint joinPoint, LockTypeEnum lockType, String lockKey, TimeUnit unit, Integer waitTime, Integer leaseTime, boolean async) throws Throwable {
        RLock lock = null;
        Object result;
        try {
            lock = this.tryLock(lockType, lockKey, leaseTime, waitTime, async);
            if (lock == null) {
                log.error("加锁失败 for [key={}, leaseTime={}, waitTime={}, async={}]", lockType, lockKey, unit, leaseTime, waitTime, async);
                throw new DistributedLockException("获取redisson锁失败" + "[lockKey=" + lockType.getValue() + "lockKey=" + lockKey + "]" );
            }
            log.info("加锁成功 for [key={}, leaseTime={}, waitTime={}, async={}]", lockType, lockKey, leaseTime, waitTime, async);
            result = joinPoint.proceed();
        } finally {
            if (lock != null) {
                this.unlock(lock);
            }
        }

        return result;
    }
}