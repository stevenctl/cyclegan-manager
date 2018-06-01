package com.slandow.cycleganmanager

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@SpringBootApplication
class CycleganManagerApplication {

	static void main(String[] args) {
		SpringApplication.run CycleganManagerApplication, args
	}

	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		new BCryptPasswordEncoder()
	}
}
