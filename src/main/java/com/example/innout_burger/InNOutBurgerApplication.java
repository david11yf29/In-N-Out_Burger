package com.example.innout_burger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j // Simple Logging Facade for Java
@SpringBootApplication
public class InNOutBurgerApplication {
    public static void main(String[] args) {
        SpringApplication.run(InNOutBurgerApplication.class, args);
        log.info("[David] Project Start Successfully...");
    }

}
