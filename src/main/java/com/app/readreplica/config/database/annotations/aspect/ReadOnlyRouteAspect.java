package com.app.readreplica.config.database.annotations.aspect;

import com.app.readreplica.config.database.DataSourceContextHolder;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
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

    @Before("@annotation(com.app.readreplica.config.database.annotations.ReadOnlyReplica)")
    public void setReadDataSourceType() {
        DataSourceContextHolder.setReplicaRequired(true);
    }

    @After("@annotation(com.app.readreplica.config.database.annotations.ReadOnlyReplica)")
    public void clearDataSourceType() {
        DataSourceContextHolder.clearReplica();
    }

    @AfterThrowing(pointcut = "@annotation(com.app.readreplica.config.database.annotations.ReadOnlyReplica)", throwing = "ex")
    public void clearAfterException(Throwable ex) {
        DataSourceContextHolder.clearReplica();
    }

    @Before("@annotation(com.app.readreplica.config.database.annotations.ForceMaster)")
    public void setForceMasterDataSourceType() {
        DataSourceContextHolder.setMasterRequired(true);
    }

    @After("@annotation(com.app.readreplica.config.database.annotations.ForceMaster)")
    public void clearForceMasterDataSourceType() {
        DataSourceContextHolder.clearMaster();
    }

    @AfterThrowing(pointcut = "@annotation(com.app.readreplica.config.database.annotations.ForceMaster)", throwing = "ex")
    public void clearForceMasterAfterException(Throwable ex) {
        DataSourceContextHolder.clearMaster();
    }


}
