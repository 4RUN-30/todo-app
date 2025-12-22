package com.arun.pwa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PwaApplication {

    private static final Logger logger = LoggerFactory.getLogger(PwaApplication.class);

	public static void main(String[] args) {
		logger.info("Starting PwaApplication");
		SpringApplication.run(PwaApplication.class, args);
		logger.info("PwaApplication started");
	}

}
