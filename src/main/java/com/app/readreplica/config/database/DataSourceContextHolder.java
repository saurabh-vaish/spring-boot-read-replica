package com.app.readreplica.config.database;

/**
 * Uses a ThreadLocal to store the routing decision for the current thread.
 * This allows the AOP aspect to signal the routing data source to use a replica.
 */
public class DataSourceContextHolder {

    private static final ThreadLocal<Boolean> replicaRequestHolder = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> masterRequestHolder = new ThreadLocal<>();

    public static void setReplicaRequired(boolean isReplicaRequired) {
        replicaRequestHolder.set(isReplicaRequired);
    }

    public static boolean isReplicaRequired() {
//        return true;
        return replicaRequestHolder.get() != null && replicaRequestHolder.get();
    }

    public static void clearReplica() {
        replicaRequestHolder.remove();
    }

    public static void setMasterRequired(boolean isMasterRequired) {
        masterRequestHolder.set(isMasterRequired);
    }

    public static boolean isMasterRequired() {
        return masterRequestHolder.get() != null && masterRequestHolder.get();
    }

    public static void clearMaster() {
        masterRequestHolder.remove();
    }
}
