package com.project.gamemarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class GameMarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(GameMarketApplication.class, args);
	}

}
