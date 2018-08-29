package com.paascloud.distributedlock.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Distributed properties.
 *
 * @author paascloud.net@gmail.com
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "distributedLock")
public class DistributedProperties {
    private RedissonProperties redisson;

    private ZookeeperProperties zk;
}
