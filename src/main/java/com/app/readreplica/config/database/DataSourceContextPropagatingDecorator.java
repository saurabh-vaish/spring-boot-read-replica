package com.app.readreplica.config.database;

import org.springframework.core.task.TaskDecorator;
import org.springframework.stereotype.Component;

/**
 * Thread-local values like DataSourceContextHolder do not propagate to new threads.
 * So we must use InheritableThreadLocal or a custom TaskDecorator.
 *
 * @Author saurabh vaish
 * @Date 23-07-2025
 */
@Component
public class DataSourceContextPropagatingDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        var context = DataSourceContextHolder.isReplicaRequired(); // get the current context
        return () -> {
            try {
                DataSourceContextHolder.setReplicaRequired(context); // set the context in the new thread
                runnable.run();
            } finally {
                DataSourceContextHolder.clearReplica(); // clear the context after the task is done
            }
        };
    }
}
