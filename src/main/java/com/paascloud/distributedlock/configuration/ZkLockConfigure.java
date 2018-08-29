package com.paascloud.distributedlock.configuration;

import com.paascloud.distributedlock.config.DistributedProperties;
import com.paascloud.distributedlock.config.ZookeeperProperties;
import com.paascloud.distributedlock.service.impl.ZkDistributedLocker;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * The type Zk lock configure.
 *
 * @author gsfeng
 */
@Configuration
@EnableConfigurationProperties(DistributedProperties.class)
@ConditionalOnProperty("distributedLock.zk.zkAddressList")
public class ZkLockConfigure {
    @Resource
    private DistributedProperties distributedProperties;

    /**
     * Zk lock distributed locker.
     *
     * @return the distributed locker
     */
    @Bean("zkDistributedLocker")
    @ConditionalOnMissingBean
    public ZkDistributedLocker zkLock() {

        ZookeeperProperties zookeeperProperties = distributedProperties.getZk();
        // 重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(zookeeperProperties.getBaseSleepTimeMilliseconds(), zookeeperProperties.getMaxRetries());
        // 通过工厂创建连接
        CuratorFramework cf = CuratorFrameworkFactory.builder()
                .connectString(zookeeperProperties.getZkAddressList())
                .retryPolicy(retryPolicy).sessionTimeoutMs(zookeeperProperties.getSessionTimeoutMilliseconds())
                .connectionTimeoutMs(zookeeperProperties.getConnectionTimeoutMilliseconds())
                .namespace(zookeeperProperties.getNamespace()).build();

        // 开启连接
        cf.start();
        return new ZkDistributedLocker(cf);
    }

}
