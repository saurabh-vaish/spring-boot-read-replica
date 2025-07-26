package com.app.readreplica.config.database;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.NonNullApi;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class ReadWriteRoutingDataSource extends AbstractRoutingDataSource {
    
    private static final Logger logger = LoggerFactory.getLogger(ReadWriteRoutingDataSource.class);
    
    private DataSource masterDataSource;
    private List<DataSource> replicaDataSources;
    private final AtomicInteger replicaCounter = new AtomicInteger(0);
    private final ThreadLocal<String> forceDataSource = new ThreadLocal<>();


    @Override
    public void afterPropertiesSet() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        
        // Add master datasource
        targetDataSources.put(DataSourceType.MASTER, masterDataSource);
        
        // Add replica data-sources
        for (int i = 0; i < replicaDataSources.size(); i++) {
            targetDataSources.put(DataSourceType.REPLICA.name() + "_" + i, replicaDataSources.get(i));
        }
        
        setTargetDataSources(targetDataSources);
        setDefaultTargetDataSource(masterDataSource);
        super.afterPropertiesSet();
    }
    
    @Override
    protected Object determineCurrentLookupKey() {

        // Check if datasource is explicitly set
        String forcedKey = forceDataSource.get();
        if (forcedKey != null) {
            logger.debug("Using forced datasource: {}", forcedKey);
            return forcedKey;
        }

        if(DataSourceContextHolder.isMasterRequired()){
            logger.debug("Using master datasource for write transaction");
            return DataSourceType.MASTER;
        }

        if(DataSourceContextHolder.isReplicaRequired() || TransactionSynchronizationManager.isCurrentTransactionReadOnly()){
            String replicaKey = selectReadReplica();
            logger.debug("Using read replica: {}", replicaKey);
            return replicaKey;
        }

        if(TransactionSynchronizationManager.isActualTransactionActive() ){
            logger.debug("Using master datasource for write transaction");
            return DataSourceType.MASTER;
        }

        // 3. Default to master
        return DataSourceType.MASTER;
//        return selectReadReplica();
    }
    
    private String selectReadReplica() {
        if (replicaDataSources.isEmpty()) {
            logger.warn("No replica datasources available, falling back to master");
            return DataSourceType.MASTER.name();
        }
        
        // Round-robin load balancing with failover based on health check
        for (int i = 0; i < replicaDataSources.size(); i++) {
            int index = replicaCounter.getAndIncrement() % replicaDataSources.size();
            String replicaKey = DataSourceType.REPLICA.name() + "_" + index;

            // Check if replica is healthy
            if (isDataSourceHealthy(replicaDataSources.get(index))) {
                return replicaKey;
            }

            logger.warn("Replica {} is unhealthy, trying next replica", replicaKey);
        }

        // All replicas failed, fallback to master
        logger.error("All replica datasources are unhealthy, falling back to master");
        return DataSourceType.MASTER.name();


        // Random load balancing
//        int index = Math.abs(replicaCounter.getAndIncrement() % replicaDataSources.size());
//        Object key = index;
//        logger.debug("Routing to REPLICA index: {}, key: {}", index, DataSourceType.REPLICA.name() + "_" + key);
//        return DataSourceType.REPLICA.name() + "_" + index;

    }
    
    private boolean isDataSourceHealthy(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(1); // 1 second timeout
        } catch (SQLException e) {
            logger.error("Health check failed for replica datasource", e);
            return false;
        }
    }
    
    public void forceDataSource(String key) {
        forceDataSource.set(key);
    }
    
    public void clearForcedDataSource() {
        forceDataSource.remove();
    }
    
    public static void forceMaster() {
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        threadLocal.set(DataSourceType.MASTER.name());
    }
    
    public static void forceReplica() {
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        threadLocal.set("replica");
    }


//    /**
//     * Get connection from the target datasource, if connection is not available then fallback to master
//     * @return Connection
//     * @throws SQLException
//     */
//    @Override
//    public Connection getConnection() throws SQLException {
//        try {
//            return determineTargetDataSource().getConnection();
//        } catch (SQLException ex) {
//            // log fallback
//            if (DataSourceContextHolder.getDataSourceType() == DataSourceType.REPLICA) {
//                logger.warn("Replica DB unavailable, falling back to MASTER");
//                DataSourceContextHolder.set(DataSourceType.MASTER);
//                return determineTargetDataSource().getConnection();
//            } else {
//                throw ex;
//            }
//        }
//    }
}