package com.project.innoutburger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j // Simple Logging Facade for Java
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class InNOutBurgerApplication {
    public static void main(String[] args) {
        SpringApplication.run(InNOutBurgerApplication.class, args);
        log.info("[David] Project Start Successfully...");
    }

}
