# distributedlock-spring-boot-starter
简化分布式锁的使用，提供基于spring-boot自动配置的starter

# QuickStart
下载编译
```
git clone git@github.com:pcbest/distributedlock-spring-boot-starter.git
cd distributedlock-spring-boot-starter; 
mvn clean install -Dmaven.test.skip -Denv=release
```

# 使用
添加依赖
```
<dependency>
    <groupId>com.liuzm.paascloud</groupId>
    <artifactId>distributedlock-spring-boot-starter</artifactId>
    <version>${bulid.version}</version>
</dependency>
```

配置

```
application.properties或者application.yml
# 必须配置
distributedlock.zk.zkAddressList=127.0.0.1:2181
或者
distributedlock.zk.zkAddressList=ip1:2181,ip1,ip1:2181

# 可不配置
distributedlock.zk.namespace=lock
```
获取锁释放锁
```
    final String lockPath = "/testlok";
    boolean res = false;
    try {
        res = zkLock.lock(lockPath, 3, TimeUnit.SECONDS);
        if (res) {
            // 获取到了锁 do something
        } else {
            System.out.println(Thread.currentThread().getName() + "未获取到锁");
            // 未获取到锁 do something
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        //释放锁
        if (res) {
            zkLock.unlock(lockPath);
        }
    }
```

