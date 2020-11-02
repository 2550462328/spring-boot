package cn.ictt.zhanghui.springboot_test.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Order(5) //定义的i越小，切面的优先级越高
@Component
public class WebLogAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //使用threadLocal定义开始时间 防止同步问题
    ThreadLocal<Long> startTime = new ThreadLocal<Long>();

    @Pointcut("execution(public * cn.ictt.zhanghui.springboot_test.web..*.*(..))")
    public void webLog(){ }

    @Before("webLog()")
    public void deBefore(JoinPoint joinPoint){
        startTime.set(System.currentTimeMillis());
        //接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //记录下请求内容
        logger.info("URL : " + request.getRequestURL().toString());
        logger.info("HTTP_METHOD : " + request.getMethod());
        logger.info("IP : " + request.getRemoteAddr());
        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(returning = "response", pointcut = "webLog()")
    public void doAfterReturn(Object response){
        //处理完请求 返回内容
        logger.info("RESPONSE : " + response);
        logger.info("SPEND TIME : " + (System.currentTimeMillis() - startTime.get()));
    }
}
