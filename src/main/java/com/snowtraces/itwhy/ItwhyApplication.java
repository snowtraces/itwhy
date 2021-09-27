package com.snowtraces.itwhy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.snowtraces.itwhy.repository"})
public class ItwhyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItwhyApplication.class, args);
    }

}
