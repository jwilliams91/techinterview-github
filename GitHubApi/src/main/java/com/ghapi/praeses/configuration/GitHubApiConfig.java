package com.ghapi.praeses.configuration;

import java.io.IOException;
import java.util.function.BiFunction;

import org.kohsuke.github.GitHub;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
@ComponentScan("com.ghapi.praeses.controllers")
public class GitHubApiConfig {
	
	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
            	//Set the api to accept request from the UI application
                registry.addMapping("/api/**").allowedOrigins("http://localhost:4200");
            }
        };
    }
	
	public static Object gitHubRequest(Object input, String authHeader, BiFunction<Object, GitHub, Object> function) {
		try {
			GitHub gh = GitHub.connectUsingOAuth(authHeader.substring(7));
			return function.apply(input, gh);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		
	}
	
}
