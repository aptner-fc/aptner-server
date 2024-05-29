package com.fc8.platform.config;//package com.fc8.aptner.config;
//
//import com.fc8.aptner.common.utils.KmsUtils;
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//import java.util.Properties;
//
//@Slf4j
//@Configuration
//@RequiredArgsConstructor
//public class DatabaseConfig {
//
//    private final KmsUtils kmsUtils;
//
//    @Value("${spring.datasource.driver-class-name}")
//    private String driverClassName;
//
//    @Value("${spring.datasource.url}")
//    private String databaseUrl;
//
//    @Value("${spring.datasource.username}")
//    private String databaseUsername;
//
//    @Value("${spring.datasource.password}")
//    private String databasePassword;
//
//    @Bean
//    public DataSource dataSource() {
//
//        Properties properties = new Properties();
//        properties.setProperty("driverClassName", driverClassName);
////        properties.setProperty("jdbcUrl", kmsUtils.decrypt(databaseUrl));
//        properties.setProperty("jdbcUrl", (databaseUrl));
//        properties.setProperty("maxLifetime", "179000");
//        properties.setProperty("idleTimeout", "185000");
////        properties.setProperty("username", kmsUtils.decrypt(databaseUsername));
////        properties.setProperty("password", kmsUtils.decrypt(databasePassword));
//        properties.setProperty("username", (databaseUsername));
//        properties.setProperty("password", (databasePassword));
//        properties.setProperty("leakDetectionThreshold", "60000");
//        properties.setProperty("maximumPoolSize", "20");
//        properties.setProperty("minimumIdle", "20");
//
//        return new HikariDataSource(new HikariConfig(properties));
//    }
//}
