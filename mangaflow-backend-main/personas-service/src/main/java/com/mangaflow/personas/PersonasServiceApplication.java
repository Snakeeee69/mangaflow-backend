package com.mangaflow.personas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PersonasServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PersonasServiceApplication.class, args);
		System.out.println("================================================");
        System.out.println(" Personas Service corriendo en: http://localhost:8081");
        System.out.println(" Swagger UI: http://localhost:8081/swagger-ui/index.html");
        System.out.println("================================================");
	}

}
