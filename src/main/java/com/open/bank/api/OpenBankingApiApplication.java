package com.open.bank.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class OpenBankingApiApplication {
	private static final Logger logger = LoggerFactory.getLogger(OpenBankingApiApplication.class);
	
	@Bean public BCryptPasswordEncoder bCryptPasswordEncoder() {
	    return new BCryptPasswordEncoder(); 
	}

	public static void main(String[] args) {
		SpringApplication.run(OpenBankingApiApplication.class, args);
		
		logger.info(
	      "\n----------------------------------------------------------------------\n\t" + 
	      "Application '{}' is running!\n" + 
	      "----------------------------------------------------------------------",
	      OpenBankingApiApplication.class.getSimpleName()
	    );
	}

}
