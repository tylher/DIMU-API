package com.dimu.dimuapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity(debug = true)
@EnableJpaAuditing
public class DimuapiApplication implements CommandLineRunner {
	@Value("${spring.datasource.url}")
	private String url;
	public static void main(String[] args) {

		SpringApplication.run(DimuapiApplication.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println(url);
	}

}
