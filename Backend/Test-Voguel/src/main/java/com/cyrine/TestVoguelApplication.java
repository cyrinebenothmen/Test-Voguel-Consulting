package com.cyrine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.cyrine.configuration.FilesProperties;

@SpringBootApplication
@EnableConfigurationProperties({FilesProperties.class})
public class TestVoguelApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestVoguelApplication.class, args);
	}

}
