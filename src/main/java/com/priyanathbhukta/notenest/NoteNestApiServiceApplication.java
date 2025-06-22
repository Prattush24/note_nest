package com.priyanathbhukta.notenest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAware")
public class NoteNestApiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NoteNestApiServiceApplication.class, args);
	}

}
