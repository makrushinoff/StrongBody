package ua.strongBody;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "ua.strongBody.config")
public class StrongBodyApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(StrongBodyApplication.class, args);
    }

}
