package com.paascloud.distributedlock.configuration;

import com.paascloud.distributedlock.config.*;
import com.paascloud.distributedlock.service.DistributedLocker;
import com.paascloud.distributedlock.service.impl.RedissonDistributedLocker;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * The type Redisson auto configuration.
 *
 * @author paascloud.net@gmail.com
 */
@Configuration
@EnableConfigurationProperties(DistributedProperties.class)
@ConditionalOnProperty("distributedLock.redisson.redisType")
public class RedissonConfiguration {

    @Resource
    private DistributedProperties distributedProperties;

    @Bean
    @ConditionalOnProperty(prefix = "distributedLock.redisson", name = "redisType", havingValue = "cluster")
    RedissonClient redissonCluster() {
        RedissonProperties redisson = distributedProperties.getRedisson();
        RedissonClusterProperties cluster = redisson.getCluster();
        Config config = this.getConfig(distributedProperties);

        ClusterServersConfig serverConfig = config.useClusterServers()

                .setIdleConnectionTimeout(cluster.getIdleConnectionTimeout())
                .setConnectTimeout(cluster.getConnectTimeout())
                .setTimeout(cluster.getTimeout())
                .setRetryAttempts(cluster.getRetryAttempts())
                .setRetryInterval(cluster.getRetryInterval())
                .setSubscriptionsPerConnection(cluster.getSubscriptionsPerConnection())
                .setClientName(cluster.getClientName())

                .setSlaveConnectionMinimumIdleSize(cluster.getSlaveConnectionMinimumIdleSize())
                .setSlaveConnectionPoolSize(cluster.getSlaveConnectionPoolSize())
                .setMasterConnectionMinimumIdleSize(cluster.getMasterConnectionMinimumIdleSize())
                .setMasterConnectionPoolSize(cluster.getMasterConnectionPoolSize())

                .addNodeAddress(cluster.getNodeAddresses())
                .setReadMode(StringUtils.isEmpty(ReadMode.valueOf(cluster.getReadMode())) ? ReadMode.SLAVE : ReadMode.valueOf(cluster.getReadMode()));


        if (!StringUtils.isEmpty(cluster.getPassword())) {
            serverConfig.setPassword(cluster.getPassword());
        }
        return Redisson.create(config);
    }

    /**
     * 哨兵模式自动装配
     *
     * @return the redisson client
     */
    @Bean
    @ConditionalOnProperty(prefix = "distributedLock.redisson", name = "redisType", havingValue = "sentinel")
    RedissonClient redissonSentinel() {
        RedissonSentinelProperties sentinel = distributedProperties.getRedisson().getSentinel();
        Config config = getConfig(distributedProperties);
        SentinelServersConfig serverConfig = config.useSentinelServers()

                .setIdleConnectionTimeout(sentinel.getIdleConnectionTimeout())
                .setConnectTimeout(sentinel.getConnectTimeout())
                .setTimeout(sentinel.getTimeout())
                .setRetryAttempts(sentinel.getRetryAttempts())
                .setRetryInterval(sentinel.getRetryInterval())
                .setSubscriptionsPerConnection(sentinel.getSubscriptionsPerConnection())
                .setClientName(sentinel.getClientName())

                .setSlaveConnectionMinimumIdleSize(sentinel.getSlaveConnectionMinimumIdleSize())
                .setSlaveConnectionPoolSize(sentinel.getSlaveConnectionPoolSize())
                .setMasterConnectionMinimumIdleSize(sentinel.getMasterConnectionMinimumIdleSize())
                .setMasterConnectionPoolSize(sentinel.getMasterConnectionPoolSize())

                .addSentinelAddress(sentinel.getSentinelAddresses())

                .setReadMode(StringUtils.isEmpty(ReadMode.valueOf(sentinel.getReadMode())) ? ReadMode.SLAVE : ReadMode.valueOf(sentinel.getReadMode()))
                .setMasterName(sentinel.getMasterName())
                .setDatabase(sentinel.getDatabase());


        if (!StringUtils.isEmpty(sentinel.getPassword())) {
            serverConfig.setPassword(sentinel.getPassword());
        }
        return Redisson.create(config);
    }

    /**
     * 单机模式自动装配
     *
     * @return the redisson client
     */
    @Bean
    @ConditionalOnProperty(prefix = "distributedLock.redisson", name = "redisType", havingValue = "single")
    RedissonClient redissonSingle() {
        RedissonSingleProperties single = distributedProperties.getRedisson().getSingle();

        Config config = this.getConfig(distributedProperties);

        SingleServerConfig serverConfig = config.useSingleServer()
                .setIdleConnectionTimeout(single.getIdleConnectionTimeout())
                .setConnectTimeout(single.getConnectTimeout())
                .setTimeout(single.getTimeout())
                .setRetryAttempts(single.getRetryAttempts())
                .setRetryInterval(single.getRetryInterval())
                .setSubscriptionsPerConnection(single.getSubscriptionsPerConnection())
                .setClientName(single.getClientName())
                .setAddress(single.getAddress())
                .setSubscriptionConnectionMinimumIdleSize(single.getSubscriptionConnectionMinimumIdleSize())
                .setSubscriptionConnectionPoolSize(single.getSubscriptionConnectionPoolSize())
                .setConnectionMinimumIdleSize(single.getConnectionMinimumIdleSize())
                .setConnectionPoolSize(single.getConnectionPoolSize())
                .setDatabase(single.getDatabase())
                .setDnsMonitoringInterval(single.getDnsMonitoringInterval());


        if (!StringUtils.isEmpty(single.getPassword())) {
            serverConfig.setPassword(single.getPassword());
        }

        return Redisson.create(config);
    }

    /**
     * 装配locker类，并将实例注入到RedissLockUtil中
     *
     * @param redissonClient the redisson client
     * @return the distributed locker
     */
    @Bean("redissonDistributedLocker")
    @ConditionalOnMissingBean
    @ConditionalOnClass(RedissonClient.class)
    DistributedLocker distributedLocker(RedissonClient redissonClient) {
        return new RedissonDistributedLocker(redissonClient);
    }

    private Config getConfig(DistributedProperties distributedProperties) {
        return new Config()
                .setLockWatchdogTimeout(distributedProperties.getRedisson().getLockWatchdogTimeout())
                .setThreads(distributedProperties.getRedisson().getThreads())
                .setNettyThreads(distributedProperties.getRedisson().getNettyThreads());
    }
}