package com.example.cinemamanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@SpringBootApplication
@EnableCaching
@EntityScan(basePackages = "com.example.cinemamanagementsystem.models")
public class CinemaManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CinemaManagementSystemApplication.class, args);
    }

}
