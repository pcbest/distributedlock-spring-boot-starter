package com.paascloud.distributedlock.autoconfigure;

import com.paascloud.distributedlock.ZookeeperProperties;
import com.paascloud.distributedlock.zk.ZkLock;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gsfeng
 */
@Configuration
@ConditionalOnClass(ZkLock.class)
@EnableConfigurationProperties(ZookeeperProperties.class)
public class ZkLockAutoConfigure {
    @Autowired
    private ZookeeperProperties zookeeperProperties;

    @Bean
    @ConditionalOnMissingBean
    public ZkLock zkLock() {
        // 重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(zookeeperProperties.getBaseSleepTimeMilliseconds(),
                zookeeperProperties.getMaxRetries());
        // 通过工厂创建连接
        CuratorFramework cf = CuratorFrameworkFactory.builder().connectString(zookeeperProperties.getZkAddressList()).
                retryPolicy(retryPolicy).sessionTimeoutMs(zookeeperProperties.getSessionTimeoutMilliseconds())
                .connectionTimeoutMs(zookeeperProperties.getConnectionTimeoutMilliseconds())
                .namespace(zookeeperProperties.getNamespace()).build();
        // 开启连接
        cf.start();
        return new ZkLock(cf);
    }

}
