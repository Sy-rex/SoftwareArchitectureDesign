package com.sobolev.spring.springlab1;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringLab1Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringLab1Application.class, args);
    }

    @Bean
    public ModelMapper moddelMapper(){
        return new ModelMapper();
    }
}
