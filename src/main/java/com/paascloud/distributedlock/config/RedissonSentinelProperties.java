package com.paascloud.distributedlock.config;

import lombok.Getter;
import lombok.Setter;

/**
 * Redisson sentinel properties.
 *
 * @author paascloud.net@gmail.com
 */
@Getter
@Setter
public class RedissonSentinelProperties {

    /**
     * 连接空闲超时，单位：毫秒
     */
    private int idleConnectionTimeout = 10000;

    /**
     * 连接超时，单位：毫秒
     */
    private int connectTimeout = 10000;

    /**
     * 命令等待超时，单位：毫秒
     */
    private int timeout = 3000;

    /**
     * 命令失败重试次数
     */
    private int retryAttempts = 3;

    /**
     * 命令重试发送时间间隔，单位：毫秒
     */
    private int retryInterval = 1500;

    /**
     * 执行失败最大次数
     */
    private int failedAttempts = 3;

    /**
     * redis密码
     */
    private String password;

    /**
     * 在Redis节点里显示的客户端名称
     */
    private String clientName;

    /**
     * 数据库编号
     */
    private int database = 0;

    /**
     * 每个连接的最大订阅数量
     */
    private int subscriptionsPerConnection = 5;

    /**
     * 从节点最小空闲连接数
     */
    private int slaveConnectionMinimumIdleSize = 32;

    /**
     * 从节点连接池大小
     */
    private int slaveConnectionPoolSize = 64;

    /**
     * 主节点最小空闲连接数
     */
    private int masterConnectionMinimumIdleSize = 32;

    /**
     * 主节点连接池大小
     */
    private int masterConnectionPoolSize = 64;

    /**
     * 读取操作的负载均衡模式
     * SLAVE - 只在从服务节点里读取。
     * MASTER - 只在主服务节点里读取。
     * MASTER_SLAVE - 在主从服务节点里都可以读取
     */
    private String readMode = "SLAVE";

    /**
     * 哨兵地址
     * - "redis://127.0.0.1:26379"
     * - "redis://127.0.0.1:26389"
     */
    private String[] sentinelAddresses;

    /**
     * 主服务器的名称, 是哨兵进程中用来监测主从服务切换情况的
     */
    private String masterName;

}
