package com.app.readreplica.config.database.annotations.aspect;

import com.app.readreplica.config.database.DataSourceContextHolder;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 *
 *
 * @Author saurabh vaish
 * @Date 23-07-2025
 */

@Aspect
@Component
@Order(0)
public class ReadOnlyRouteAspect {


    @Pointcut("@annotation(com.app.readreplica.config.database.annotations.ReadOnlyReplica)")
    private void readOnlyReplicaAnnotation() {}

    @Pointcut("@annotation(com.app.readreplica.config.database.annotations.ForceMaster)")
    private void forceMasterAnnotation() {}

    @Before("readOnlyReplicaAnnotation()")
    public void setReadDataSourceType() {
        DataSourceContextHolder.setReplicaRequired(true);
    }

    @After("readOnlyReplicaAnnotation()")
    public void clearDataSourceType() {
        DataSourceContextHolder.clearReplica();
    }

    @AfterThrowing(pointcut = "readOnlyReplicaAnnotation()", throwing = "ex")
    public void clearAfterException(Throwable ex) {
        DataSourceContextHolder.clearReplica();
    }

    @Before("forceMasterAnnotation()")
    public void setForceMasterDataSourceType() {
        DataSourceContextHolder.setMasterRequired(true);
    }

    @After("forceMasterAnnotation()")
    public void clearForceMasterDataSourceType() {
        DataSourceContextHolder.clearMaster();
    }

    @AfterThrowing(pointcut = "forceMasterAnnotation()", throwing = "ex")
    public void clearForceMasterAfterException(Throwable ex) {
        DataSourceContextHolder.clearMaster();
    }


}
