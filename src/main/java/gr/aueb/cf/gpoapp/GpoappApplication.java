package gr.aueb.cf.gpoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
public class GpoappApplication {

	public static void main(String[] args) {
		SpringApplication.run(GpoappApplication.class, args);
	}

}
