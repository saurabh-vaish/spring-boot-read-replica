//package com.app.readreplica.config;
//
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import org.springframework.transaction.support.TransactionTemplate;
//
//import javax.sql.DataSource;
//
//@Configuration
//@EnableTransactionManagement
//public class TransactionConfig {
//
//    @Primary
//    @Bean(name = "transactionManager")
//    public PlatformTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
//        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
//        transactionManager.setDataSource(dataSource);
//        transactionManager.setEnforceReadOnly(true); // Enforce read-only transactions
//        return transactionManager;
//    }
//
////    @Bean(name = "readOnlyTransactionTemplate")
////    public TransactionTemplate readOnlyTransactionTemplate(
////            @Qualifier("transactionManager") PlatformTransactionManager transactionManager) {
////        TransactionTemplate template = new TransactionTemplate(transactionManager);
////        template.setReadOnly(true);
////        template.setTimeout(30); // 30 seconds timeout
////        return template;
////    }
////
////    @Bean(name = "writeTransactionTemplate")
////    public TransactionTemplate writeTransactionTemplate(
////            @Qualifier("transactionManager") PlatformTransactionManager transactionManager) {
////        TransactionTemplate template = new TransactionTemplate(transactionManager);
////        template.setReadOnly(false);
////        template.setTimeout(60); // 60 seconds timeout for writes
////        return template;
////    }
////
////    @Bean(name = "readOnlyTransactionManager")
////    public PlatformTransactionManager readOnlyTransactionManager(@Qualifier("dataSource") DataSource dataSource) {
////        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
////        transactionManager.setDataSource(dataSource);
////        transactionManager.setDefaultTimeout(30);
////        return transactionManager;
////    }
////
////    @Bean(name = "writeTransactionManager")
////    public PlatformTransactionManager writeTransactionManager(@Qualifier("dataSource") DataSource dataSource) {
////        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
////        transactionManager.setDataSource(dataSource);
////        transactionManager.setDefaultTimeout(60);
////        return transactionManager;
////    }
//}