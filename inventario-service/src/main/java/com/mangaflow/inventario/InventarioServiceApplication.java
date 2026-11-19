package com.mangaflow.inventario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class InventarioServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventarioServiceApplication.class, args);
		System.out.println("=================================================");
		System.out.println(" Inventario Service corriendo en: http://localhost:8084");
		System.out.println(" Swagger UI: http://localhost:8084/swagger-ui.html");
		System.out.println("=================================================");
	}
}
