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

@Configuration
@ConditionalOnClass(ZkLock.class)
@EnableConfigurationProperties(ZookeeperProperties.class)
public class ZkLockAutoConfigure {
    @Autowired
    private ZookeeperProperties zookeeperProperties;


    @Bean
    @ConditionalOnMissingBean
    public ZkLock zkLock() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(zookeeperProperties.getBaseSleepTimeMilliseconds(), zookeeperProperties.getMaxRetries());
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString(zookeeperProperties.getZkAddressList()).retryPolicy(retryPolicy)
                .sessionTimeoutMs(zookeeperProperties.getSessionTimeoutMilliseconds())
                .connectionTimeoutMs(zookeeperProperties.getConnectionTimeoutMilliseconds())
                .namespace(zookeeperProperties.getNamespace()).build();
        return new ZkLock(client);
    }

}
