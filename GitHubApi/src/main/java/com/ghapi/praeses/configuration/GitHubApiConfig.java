package com.ghapi.praeses.configuration;

import org.kohsuke.github.GitHub;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.ghapi.praeses.controllers.GitHubEventController;
import com.ghapi.praeses.controllers.GitHubUserController;


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

	public static void updateClientForControllers(GitHub ghClient) {
		//Not the best, would have different solution in production
		GitHubUserController.setGhClient(ghClient);
		GitHubEventController.setGhClient(ghClient);
	}

}
