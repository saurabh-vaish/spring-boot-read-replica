package com.app.readreplica.config.database.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to force using master datasource even for read operations
 *
 * @Author saurabh vaish
 * @Date 23-07-2025
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ForceMaster {
    /**
     * Force using master datasource even for read operations
     */
}