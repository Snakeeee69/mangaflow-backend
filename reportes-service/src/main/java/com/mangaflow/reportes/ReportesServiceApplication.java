package com.mangaflow.reportes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ReportesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReportesServiceApplication.class, args);
		System.out.println("================================================");
		System.out.println("Reportes service corriendo en: http://localhost:8083");
		System.out.println("Swagger UI: http://localhost:8083/swagger-ui.html");
		System.out.println("================================================");
	}

}
