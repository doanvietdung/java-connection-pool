package youtube.java.puzzle.configuration;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;
import youtube.java.puzzle.college.entity.College;

import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "youtube.java.puzzle.college.repository",
        entityManagerFactoryRef = "collegeEntityManagerFactory",
        transactionManagerRef = "collegeTransactionManager")
public class CollegeDataSourceConfiguration {

    @Autowired
    private Environment env;
    @Value("${spring.datasource.college.url}")
    private String url;

    @Value("${spring.datasource.college.username}")
    private String username;

    @Value("${spring.datasource.college.password}")
    private String password;
//    @Bean
//    @Primary
//    @ConfigurationProperties("spring.datasource.college")
//    public DataSourceProperties collegeDataSourceProperties() {
//        return new DataSourceProperties();
//    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.college.configuration")
    public DataSource collegeDataSource() {
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

    @Primary
    @Bean(name = "collegeEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean collegeEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(collegeDataSource())
                .packages("youtube.java.puzzle.college.entity")
                .build();
    }

    @Primary
    @Bean(name = "collegeTransactionManager")
    public PlatformTransactionManager collegeTransactionManager(
            final @Qualifier("collegeEntityManagerFactory") LocalContainerEntityManagerFactoryBean collegeEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(collegeEntityManagerFactory.getObject()));
    }
}