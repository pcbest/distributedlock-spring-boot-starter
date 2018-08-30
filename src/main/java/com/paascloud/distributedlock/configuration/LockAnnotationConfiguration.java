package com.paascloud.distributedlock.configuration;

import com.paascloud.distributedlock.annotation.LockAspect;
import com.paascloud.distributedlock.config.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Lock annotation configuration.
 *
 * @author paascloud.net @gmail.com
 */
@Configuration
@EnableConfigurationProperties(DistributedProperties.class)
public class LockAnnotationConfiguration {
    /**
     * Lock aspect lock aspect.
     *
     * @return the lock aspect
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnExpression("!'${distributedLock.redisson}'.isEmpty() || !'${distributedLock.zk}'.isEmpty()")
    LockAspect lockAspect() {
        return new LockAspect();
    }
}