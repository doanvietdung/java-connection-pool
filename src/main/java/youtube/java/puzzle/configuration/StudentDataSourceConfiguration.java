package youtube.java.puzzle.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "youtube.java.puzzle.student.repository",
        entityManagerFactoryRef = "studentEntityManagerFactory",
        transactionManagerRef = "studentTransactionManager")
public class StudentDataSourceConfiguration {
    @Autowired
    private Environment env;
//    Cách 1
//    @Bean
//    @ConfigurationProperties("spring.datasource.student")
//    public DataSourceProperties studentDataSourceProperties() {
//        return new DataSourceProperties();
//    }

    //    Cách 2
    @Value("${spring.datasource.student.url}")
    private String url;

    @Value("${spring.datasource.student.username}")
    private String username;

    @Value("${spring.datasource.student.password}")
    private String password;

    @Bean
    @ConfigurationProperties("spring.datasource.student.configuration")
    public DataSource studentDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));

        // Các thông số cấu hình cho HikariCP
        config.setMinimumIdle(Integer.parseInt(Objects.requireNonNull(env.getProperty("spring.datasource.hikari.minimum-idle"))));
        config.setMaximumPoolSize(Integer.parseInt(Objects.requireNonNull(env.getProperty("spring.datasource.hikari.maximum-pool-size"))));
        config.setIdleTimeout(Long.parseLong(Objects.requireNonNull(env.getProperty("spring.datasource.hikari.idle-timeout"))));
        config.setPoolName(env.getProperty("spring.datasource.hikari.pool-name"));

        return new HikariDataSource(config);
    }

    @Bean(name = "studentEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean studentEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(studentDataSource())
                .packages("youtube.java.puzzle.student.entity")
                .build();
    }

    @Bean(name = "studentTransactionManager")
    public PlatformTransactionManager studentTransactionManager(
            final @Qualifier("studentEntityManagerFactory") LocalContainerEntityManagerFactoryBean studentEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(studentEntityManagerFactory.getObject()));
    }
}