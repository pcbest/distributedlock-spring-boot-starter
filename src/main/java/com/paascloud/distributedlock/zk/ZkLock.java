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

    private static CuratorFramework curatorFramework;

    private static void setClient(CuratorFramework cf) {
        ZkLock.curatorFramework = cf;
    }

    public ZkLock(CuratorFramework curatorFramework) {
        setClient(curatorFramework);
    }

    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例
     * 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载
     */
    private static class SingletonHolder {
        static String path = "";
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static InterProcessMutex mutex = new InterProcessMutex(curatorFramework, "/curator-lock" + path);
    }

    private static InterProcessMutex getMutex(String lockPath) {
        SingletonHolder.path = lockPath;
        return SingletonHolder.mutex;
    }


    /**
     * 获得了锁
     *
     * @param lockPath 锁路径
     * @param time     锁定时长
     * @param unit     时间单位
     * @return boolean
     */
    public boolean lock(String lockPath, long time, TimeUnit unit) {
        try {
            return getMutex(lockPath).acquire(time, unit);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 释放锁
     *
     * @param lockPath 锁路径
     */
    public void unlock(String lockPath) {
        try {
            getMutex(lockPath).release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}  
