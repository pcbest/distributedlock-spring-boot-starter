package com.paascloud.distributedlock.annotation;

import com.paascloud.distributedlock.configuration.ZkLockConfigure;
import org.springframework.context.annotation.Import;
import java.lang.annotation.*;

/**
 * The interface Enable zk lock.
 *
 * @author paascloud.net@gmail.com
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ZkLockConfigure.class)
@Documented
public @interface EnableZkLock {
}