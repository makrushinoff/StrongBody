package ua.com.sportfood;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.sportfood.dao.CustomerDAO;

@SpringBootApplication
public class SportfoodApplication {

    public static void main(String[] args) {
        SpringApplication.run(SportfoodApplication.class, args);
    }

}
