package com.app.readreplica.config.database.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to force using replica datasource
 *
 * @Author saurabh vaish
 * @Date 23-07-2025
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ForceReplica {
    /**
     * Force using replica datasource
     */
    int replicaIndex() default -1; // -1 means load balanced
}