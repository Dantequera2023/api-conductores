package com.sopra.flotas.flotasbackend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(exclude = { UserDetailsServiceAutoConfiguration.class })
@EntityScan("com.sopra.albia.persistence.entity")
@Slf4j
public class App extends SpringBootServletInitializer {

	@Value("${app.messages.cors.init}")
	private String corsStarting;
	@Value("${app.origin.allowed.first}")
	private String firstOrigin;
	@Value("${app.origin.allowed.second}")
	private String secondOrigin;


	public static void main(String[] args) {
		final SpringApplication application = new SpringApplication(App.class);
		application.run(args);
	}

}
