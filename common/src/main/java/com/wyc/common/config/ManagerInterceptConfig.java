package com.wyc.common.config;
import com.wyc.common.service.ClientService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class ManagerInterceptConfig {

    @Autowired
    private ClientService clientService;
//    @Around(value="execution (* com.zjmxdz.dao.*.*(..))")
    public Object userCenterAction(ProceedingJoinPoint proceedingJoinPoint)throws Throwable{
       return null;
    }

    //获取控制器方法
    private Method getControllerMethod(ProceedingJoinPoint proceedingJoinPoint){
        Object target = proceedingJoinPoint.getTarget();
        String methodName = proceedingJoinPoint.getSignature().getName();
        Method[] methods = target.getClass().getDeclaredMethods();
        for(Method oneMethod:methods){
            if(oneMethod.getName().equals(methodName)){
                oneMethod.setAccessible(true);
                return oneMethod;
            }
        }
        return null;
    }
}
