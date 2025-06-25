package com.priyanathbhukta.notenest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAware")
@EnableScheduling
public class NoteNestApiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NoteNestApiServiceApplication.class, args);
	}

}
