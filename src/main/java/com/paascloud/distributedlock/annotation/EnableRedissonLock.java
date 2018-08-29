package com.paascloud.distributedlock.annotation;

import com.paascloud.distributedlock.configuration.RedissonConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * The interface Enable redisson lock.
 *
 * @author paascloud.net@gmail.com
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RedissonConfiguration.class)
@Documented
public @interface EnableRedissonLock {
}