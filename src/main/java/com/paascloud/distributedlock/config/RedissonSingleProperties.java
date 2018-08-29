package com.paascloud.distributedlock.config;

import lombok.Getter;
import lombok.Setter;

/**
 * Redisson single properties.
 *
 * @author paascloud.net@gmail.com
 */
@Setter
@Getter
public class RedissonSingleProperties {


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
     * 每个连接的最大订阅数量
     */
    private int subscriptionsPerConnection = 5;

    /**
     * 在Redis节点里显示的客户端名称
     */
    private String clientName;

    /**
     * 节点地址
     */
    private String address = "redis://127.0.0.1:6379";

    /**
     * 从节点发布和订阅连接的最小空闲连接数
     */
    private int subscriptionConnectionMinimumIdleSize = 1;
    /**
     * 从节点发布和订阅连接池大小
     */
    private int subscriptionConnectionPoolSize = 50;
    /**
     * 最小空闲连接数
     */
    private int connectionMinimumIdleSize = 32;
    /**
     * 连接池大小
     */
    private int connectionPoolSize = 64;
    /**
     * 数据库编号
     */
    private int database = 0;

    /**
     * DNS监测时间间隔，单位：毫秒
     */
    private int dnsMonitoringInterval = 5000;
}
