package com.pjt.refresheet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class RefresheetApplication {

    public static void main(String[] args) {
        SpringApplication.run(RefresheetApplication.class, args);
    }

}
