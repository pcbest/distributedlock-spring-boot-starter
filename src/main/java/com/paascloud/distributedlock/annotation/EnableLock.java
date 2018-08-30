package com.paascloud.distributedlock.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableRedissonLock
@EnableZkLock
@EnableAnnotationLock
public @interface EnableLock {
}