package cn.ictt.zhanghui.springboot_test.aop;

import cn.ictt.zhanghui.springboot_test.enums.LimitType;
import cn.ictt.zhanghui.springboot_test.exception.MyException;
import cn.ictt.zhanghui.springboot_test.util.http.IPUtils;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author ZhangHui
 * @version 1.0
 * @className LimitAspect
 * @description 限流的切面
 * @date 2020/3/17
 */
@Aspect
@Configuration
public class LimitAspect {

    private static LoadingCache<String, RateLimiter> caches = CacheBuilder.newBuilder()
            .maximumSize(1000)
            //每天定时清理缓存
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build(new CacheLoader<String, RateLimiter>() {
                @Override
                public RateLimiter load(String key) throws Exception {
                    //每秒只发出5个令牌
                    return RateLimiter.create(5);
                }
            });

    @Pointcut("@annotation(cn.ictt.zhanghui.springboot_test.aop.ServiceLimit)")
    public void method() {
    }

    @Around("method()")
    public Object limitMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ServiceLimit serviceLimit = method.getAnnotation(ServiceLimit.class);
        LimitType limitType = serviceLimit.limitType();
        String key = serviceLimit.key();
        Object obj;
        if (limitType.equals(LimitType.IP)) {
            key = IPUtils.getIpAddr();
        }
        try {
            boolean notLimit = caches.get(key).tryAcquire();
            if(notLimit){
                obj = joinPoint.proceed();
            }else{
                throw new MyException("慢一点，慢一点");
            }
        } catch (Throwable e){
            throw  new MyException("慢一点，慢一点，都报错了");
        }
        return obj;
    }
}
