package com.paascloud.distributedlock.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.concurrent.TimeUnit;

/**
 * <p>Title:	  ZkLock. </p>
 * <p>Description 分布式锁 </p>
 *
 * @author gsfeng
 */
public class ZkLock {

    private static CuratorFramework client;

    public static CuratorFramework getClient() {
        return client;
    }

    public static void setClient(CuratorFramework client) {
        ZkLock.client = client;
    }

    public ZkLock(CuratorFramework client) {
        setClient(client);
        getClient().start();
    }

    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例
     * 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载
     * 针对一件商品实现，多件商品同时秒杀建议实现一个map
     */
    private static class SingletonHolder {
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static InterProcessMutex mutex = new InterProcessMutex(client, "/curator/lock");
    }

    public static InterProcessMutex getMutex() {
        return SingletonHolder.mutex;
    }

    /**
     * 获得了锁
     *
     * @param time 锁定时长
     * @param unit 时间单位
     * @return boolean
     */
    public boolean lock(long time, TimeUnit unit) {
        try {
            return getMutex().acquire(time, unit);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 释放锁
     */
    public void unlock() {
        try {
            getMutex().release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}  
