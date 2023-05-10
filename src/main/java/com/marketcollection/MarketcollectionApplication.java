package com.marketcollection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MarketcollectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketcollectionApplication.class, args);
	}
}
