package com.paascloud.distributedlock.annotation;

import com.paascloud.distributedlock.configuration.LockAnnotationConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * The interface Enable zk lock.
 *
 * @author paascloud.net@gmail.com
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(LockAnnotationConfiguration.class)
@Documented
public @interface EnableAnnotationLock {
}