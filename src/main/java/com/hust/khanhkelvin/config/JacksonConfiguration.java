package com.hust.khanhkelvin.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.validation.ConstraintViolationProblemModule;

@Configuration
public class JacksonConfiguration {
    // ******* REQUIRED ********

    // Problem is a library that implements application/problem+json.
    // It comes with an extensible set of interfaces/implementations as well as convenient functions for every day use.
    // It's decoupled from any JSON library, but contains a separate module for Jackson.
    // Cau hinh problem module de custom response of exeption handler duoi dang json
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModules(
                new ProblemModule(),
                new JavaTimeModule(),
                new ConstraintViolationProblemModule());
    }

    @Bean
    public Module dateTimeModule(){
        return new JavaTimeModule();
    }

    @Bean
    public JavaTimeModule javaTimeModule() {
        return new JavaTimeModule();
    }

    @Bean
    public Jdk8Module jdk8TimeModule() {
        return new Jdk8Module();
    }

}
