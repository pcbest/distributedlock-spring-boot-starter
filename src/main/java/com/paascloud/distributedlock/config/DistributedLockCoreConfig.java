package com.paascloud.distributedlock.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Distributedlock core config.
 *
 * @author paascloud.net@gmail.com
 */
@Configuration
@EnableConfigurationProperties(DistributedProperties.class)
public class DistributedLockCoreConfig {
}
