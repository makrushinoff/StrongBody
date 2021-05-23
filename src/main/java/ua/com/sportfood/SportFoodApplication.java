package ua.com.sportfood;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "ua.com.sportfood.config")
public class SportFoodApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SportFoodApplication.class, args);
    }

}
