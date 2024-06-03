package com.jtsp.springredistemplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;

@SpringBootApplication(exclude = { RedisRepositoriesAutoConfiguration.class })
//@SpringBootApplication
public class SpringRedisTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringRedisTemplateApplication.class, args);
    }

}
