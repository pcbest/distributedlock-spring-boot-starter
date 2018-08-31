package com.paascloud.distributedlock.config;

import lombok.Getter;
import lombok.Setter;

/**
 * Redisson properties.
 * https://github.com/redisson/redisson/wiki
 *
 * @author paascloud.net@gmail.com
 */
@Setter
@Getter
public class RedissonProperties {


    /**
     * - cluster 集群, - replicated 云托管模式, - sentinel 哨兵, - single 单点
     */
    private String redisType = "single";

    /**
     * 监控锁的看门狗超时
     */
    private int lockWatchdogTimeout = 30000;

    /**
     * 线程池数量
     */
    private int threads = 2;

    /**
     * Netty线程池数量
     */
    private int nettyThreads = 2;

    /**
     * 集群
     */
    private RedissonClusterProperties cluster;

    /**
     * 云托管模式
     */
    private RedissonClusterProperties replicated;

    /**
     * 单点
     */
    private RedissonSingleProperties single;

    /**
     * 哨兵
     */
    private RedissonSentinelProperties sentinel;
}

