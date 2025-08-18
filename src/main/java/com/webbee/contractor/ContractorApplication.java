package com.webbee.contractor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ContractorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContractorApplication.class, args);
    }

}
