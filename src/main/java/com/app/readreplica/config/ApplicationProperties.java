package com.app.readreplica.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Application configuration properties for read-replica setup
 *
 * @Author saurabh vaish
 * @Date 23-07-2025
 */
@Data
@Component
@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {

    private Database database = new Database();
    private ReadReplica readReplica = new ReadReplica();
    private ConnectionPool connectionPool = new ConnectionPool();
    private Monitoring monitoring = new Monitoring();
    private Security security = new Security();
    private Hikari hikari = new Hikari();

    /**
     * Master/Write Database Configuration
     */
    @Data
    public static class Database {
        private String url;
        private String username;
        private String password;
        private String driverClassName = "com.postgresql.Driver";
        private int connectionTimeout = 30000;
        private int idleTimeout = 600000;
        private int maxLifetime = 1800000;
        private boolean autoCommit = true;
    }

    /**
     * Read Replica Configuration
     */
    @Data
    public static class ReadReplica {
        private boolean enabled = true;
        private List<ReplicaNode> nodes;
        private LoadBalancing loadBalancing = new LoadBalancing();
        private FailoverStrategy failoverStrategy = new FailoverStrategy();
        private int healthCheckInterval = 30000; // milliseconds

        /**
         * Individual Replica Node Configuration
         */
        @Data
        public static class ReplicaNode {
            private String name;
            private String url;
            private String username;
            private String password;
            private int weight = 1;
            private boolean active = true;
            private int priority = 1;
        }

        /**
         * Load Balancing Strategy Configuration
         */
        @Data
        public static class LoadBalancing {
            private Strategy strategy = Strategy.ROUND_ROBIN;
            private boolean stickySession = false;
            private int sessionTimeout = 300000; // 5 minutes

            public enum Strategy {
                ROUND_ROBIN,
                WEIGHTED_ROUND_ROBIN,
                LEAST_CONNECTIONS,
                RANDOM,
                LEAST_RESPONSE_TIME
            }
        }

        /**
         * Failover Strategy Configuration
         */
        @Data
        public static class FailoverStrategy {
            private boolean enabled = true;
            private int maxRetries = 3;
            private int retryDelay = 1000; // milliseconds
            private boolean fallbackToMaster = true;
            private int circuitBreakerThreshold = 5;
            private int circuitBreakerTimeout = 60000; // 1 minute
        }
    }

    /**
     * Connection Pool Configuration
     */
    @Data
    public static class ConnectionPool {
        private int maximumPoolSize = 20;
        private int minimumIdle = 5;
        private int connectionTimeout = 30000;
        private int idleTimeout = 600000;
        private int maxLifetime = 1800000;
        private String poolName = "ReadReplicaPool";
        private boolean registerMbeans = true;
    }

    /**
     * Monitoring Configuration
     */
    @Data
    public static class Monitoring {
        private boolean enabled = true;
        private int metricsInterval = 60000; // 1 minute
        private boolean logSlowQueries = true;
        private int slowQueryThreshold = 2000; // 2 seconds
        private boolean healthChecks = true;
        private Map<String, String> customMetrics;
    }

    /**
     * Security Configuration
     */
    @Data
    public static class Security {
        private boolean sslEnabled = false;
        private String sslMode = "PREFERRED";
        private String trustStore;
        private String trustStorePassword;
        private String keyStore;
        private String keyStorePassword;
        private boolean verifyServerCertificate = true;
        private boolean useSSL = false;
    }


    @Getter
    @Setter
    public static class Hikari{
        private boolean autoCommit;
        private int connectionTimeout;
        private int idleTimeout;
        private int maxLifetime;
        private int maximumPoolSize;
        private int minimumIdle;
    }
}
