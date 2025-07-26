package com.app.readreplica.config.database;

//import com.app.readreplica.config.ApplicationProperties;

import com.app.readreplica.config.ApplicationProperties;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.List;

/**
 * @Author saurabh vaish
 * @Date 23-07-2025
 */

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.app.readreplica.repositories",
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager"
)
public class DataSourceConfig {

    private final ApplicationProperties applicationProperties;

    @Bean(name = "masterDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSourceProperties masterDataSourceProperties() {
        return new DataSourceProperties();
    }
//
    @Bean(name = "masterDataSource")
    public DataSource masterDataSource(@Qualifier("masterDataSourceProperties") DataSourceProperties properties) {
        var ds= properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
        populateHikariConfig((HikariDataSource) ds);
        ds.setPoolName("MasterCP");
        return ds;
    }

    @Bean(name = "replica1DataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.replica1")
    public DataSourceProperties replica1DataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "replica1DataSource")
    public DataSource replica1DataSource(@Qualifier("replica1DataSourceProperties") DataSourceProperties properties) {
        var ds= properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
        populateHikariConfig((HikariDataSource) ds);
        ds.setPoolName("Replica1CP");
        return ds;
    }

    @Bean(name = "replica2DataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.replica2")
    public DataSourceProperties replica2DataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "replica2DataSource")
    public DataSource replica2DataSource(@Qualifier("replica2DataSourceProperties") DataSourceProperties properties) {
        var ds= properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
        populateHikariConfig((HikariDataSource) ds);
        ds.setPoolName("Replica2");
        return ds;
    }

    @Bean(name = "replica3DataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.replica3")
    public DataSourceProperties replica3DataSourceProperties() {
        return new DataSourceProperties();
    }
//
    @Bean(name = "replica3DataSource")
    public DataSource replica3DataSource(@Qualifier("replica3DataSourceProperties") DataSourceProperties properties) {
        var ds= properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
        populateHikariConfig((HikariDataSource) ds);
        ds.setPoolName("MasterCP");
        return ds;
    }


    private void populateHikariConfig(HikariDataSource ds) {
        var hikari = applicationProperties.getHikari();
        ds.setAutoCommit(hikari.isAutoCommit());
        ds.setConnectionTimeout(hikari.getConnectionTimeout());
        ds.setIdleTimeout(hikari.getIdleTimeout());
        ds.setMaxLifetime(hikari.getMaxLifetime());
        ds.setMaximumPoolSize(hikari.getMaximumPoolSize());
        ds.setMinimumIdle(hikari.getMinimumIdle());
    }

//    @Bean(name = "masterDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.master")
//    public DataSource masterDataSource() {
//        var ds = DataSourceBuilder.create().type(HikariDataSource.class).build();
////        populateHikariConfig(ds);
////        ds.setPoolName("MasterCP");
//        return ds;
//    }

//    @Bean(name = "replica1DataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.replica1")
//    public DataSource replica1DataSource() {
//        var ds = DataSourceBuilder.create().type(HikariDataSource.class).build();
////        populateHikariConfig(ds);
////        ds.setPoolName("Replica1CP");
//        return ds;
//    }
//
//    @Bean(name = "replica2DataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.replica2")
//    public DataSource replica2DataSource() {
//        var ds = DataSourceBuilder.create().type(HikariDataSource.class).build();
////        populateHikariConfig(ds);
////        ds.setPoolName("Replica2CP");
//        return ds;
//    }
//
//    @Bean(name = "replica3DataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.replica3")
//    public DataSource replica3DataSource() {
//        var ds = DataSourceBuilder.create().type(HikariDataSource.class).build();
////        populateHikariConfig(ds);
////        ds.setPoolName("Replica3CP");
//        return ds;
//    }


    @Bean(name = "replicaDataSources")
    public List<DataSource> replicaDataSources(
            @Qualifier("replica1DataSource") DataSource replica1,
            @Qualifier("replica2DataSource") DataSource replica2,
            @Qualifier("replica3DataSource") DataSource replica3) {
        return List.of(replica1, replica2, replica3);
    }

    @Bean(name = "routingDataSource")
    public DataSource routingDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
            @Qualifier("replicaDataSources") List<DataSource> replicaDataSources) {

        ReadWriteRoutingDataSource routingDataSource = new ReadWriteRoutingDataSource();
        routingDataSource.setMasterDataSource(masterDataSource);
        routingDataSource.setReplicaDataSources(replicaDataSources);
        routingDataSource.afterPropertiesSet();

        return routingDataSource;
    }

    @Primary
    @Bean(name = "dataSource")
    public DataSource dataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
        // LazyConnectionDataSourceProxy ensures connection is obtained only when needed
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }


//    @Primary
//    @Bean(name = "transactionManager")
//    public PlatformTransactionManager transactionManager(@Qualifier("routingDataSource") DataSource dataSource) {
//        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
//        transactionManager.setDataSource(dataSource);
//        transactionManager.setEnforceReadOnly(true); // Enforce read-only transactions
//        return transactionManager;
//    }

}
