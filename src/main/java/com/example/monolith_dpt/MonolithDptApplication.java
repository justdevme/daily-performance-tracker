package com.example.monolith_dpt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class MonolithDptApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonolithDptApplication.class, args);
	}

}
