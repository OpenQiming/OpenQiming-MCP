package cn.com.ctsi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.net.UnknownHostException;


/**
 * Web Application
 *
 */
@SpringBootApplication(scanBasePackages = { "cn.com.ctsi.*"})
@EnableConfigurationProperties({LiquibaseProperties.class})
@EnableTransactionManagement
public class WebApplication {

    private static final Logger log = LoggerFactory.getLogger(WebApplication.class);

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments
     * @throws UnknownHostException if the local host name could not be resolved into an address
     */
    public static void main(String[] args){
        SpringApplication app = new SpringApplication(WebApplication.class);
        Environment env = app.run(args).getEnvironment();
        log.info(env.getProperty("spring.application.name") + " start success！！！");
    }
}
