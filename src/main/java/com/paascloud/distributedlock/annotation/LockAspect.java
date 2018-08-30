package com.paascloud.distributedlock.annotation;

import com.paascloud.distributedlock.exception.DistributedLockException;
import com.paascloud.distributedlock.service.DistributedLocker;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * The type LockAnnotation aspect.
 *
 * @author paascloud.net@gmail.com
 */
@Slf4j
@Aspect
public class LockAspect {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Log annotation.
     */
    @Pointcut("@annotation(com.paascloud.distributedlock.annotation.LockAnnotation)")
    public void lockAnnotation() {
    }

    /**
     * Do after.
     */
    @Around(value = "lockAnnotation()")
    public Object invoke(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("已经记录下操作日志@Around 方法执行前");


        String methodName = joinPoint.getSignature().getName();
        Object target = joinPoint.getTarget();
        Class<?> targetClass = target.getClass();
        //得到拦截的方法
        Method method = getMethodByClassAndName(targetClass, methodName);
        //方法的参数
        assert method != null;
        LockAnnotation lockAnnotation = (LockAnnotation) getAnnotationByMethod(method);

        if (lockAnnotation == null) {
            return joinPoint.proceed();
        }


        DistributedLockTypeEnum name = lockAnnotation.name();
        LockTypeEnum lockType = lockAnnotation.lockType();
        String lockKey = lockAnnotation.lockKey();
        int leaseTime = lockAnnotation.leaseTime();
        int waitTime = lockAnnotation.waitTime();
        boolean async = lockAnnotation.async();

        String lockTypeValue = lockType.getValue();

        if (StringUtils.isEmpty(lockKey)) {
            throw new DistributedLockException("Annotation [" + name + "]'s key is null or empty");
        }

        String beanName = DistributedLockTypeEnum.getBeanName(name);

        if (StringUtils.isEmpty(beanName)) {
            throw new DistributedLockException("Annotation [" + beanName + "]'s key is null or empty");
        }

        log.info("Intercepted for [beanName={}, key={}, leaseTime={}, waitTime={}, async={}, proxiedClass={}, method={}]", beanName, lockTypeValue, lockKey, leaseTime, waitTime, async, targetClass.getName(), methodName);

        DistributedLocker locker = applicationContext.getBean(beanName, DistributedLocker.class);

        return locker.invoke(joinPoint, lockType, lockKey, leaseTime, waitTime, async);
    }


    /**
     * 根据目标方法和注解类型  得到该目标方法的指定注解
     */
    private Annotation getAnnotationByMethod(Method method) {
        Annotation[] all = method.getAnnotations();
        for (Annotation annotation : all) {
            if (annotation.annotationType() == LockAnnotation.class) {
                return annotation;
            }
        }
        return null;
    }

    /**
     * 根据类和方法名得到方法
     */
    private Method getMethodByClassAndName(Class c, String methodName) {
        Method[] methods = c.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }
}