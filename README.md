\# MangaFlow - Sistema de Microservicios



MangaFlow es un sistema de gestión y venta de mangas desarrollado con arquitectura de microservicios utilizando Spring Boot.



El sistema permite gestionar personas, catálogo de mangas, inventario, pedidos, pagos, puntos, descuentos, notificaciones, reportes y autenticación de usuarios.



\## Tecnologías utilizadas



\- Java 21

\- Spring Boot

\- Spring Web

\- Spring Data JPA

\- Spring Reactive Web / WebClient

\- Spring Security

\- MySQL

\- Laragon

\- Postman

\- GitHub



\## Arquitectura



El proyecto está compuesto por 10 microservicios independientes. Cada servicio posee su propia responsabilidad, endpoints REST y base de datos independiente.



\## Microservicios



| Microservicio | Puerto | Responsabilidad |

|----------------------------------------------

| personas-service | 8081 | Gestión de personas y roles de usuario |

| catalogo-service | 8083 | Gestión del catálogo de mangas |

| inventario-service | 8084 | Gestión de stock e inventario |

| pedidos-service | 8085 | Gestión de pedidos |

| pagos-service | 8086 | Registro y validación de pagos |

| puntos-service | 8087 | Acumulación y canje de puntos |

| descuentos-service | 8088 | Cálculo de descuentos |

| notificaciones-service | 8089 | Gestión de notificaciones |

| reportes-service | 8090 | Generación de reportes |

| auth-service | 8091 | Registro, login, roles y autenticación |



\## Bases de datos



Cada microservicio utiliza una base de datos propia en MySQL:



\- mangaflow\_personas\_db

\- mangaflow\_catalogo\_db

\- mangaflow\_inventario\_db

\- mangaflow\_pedidos\_db

\- mangaflow\_pagos\_db

\- mangaflow\_puntos\_db

\- mangaflow\_descuentos\_db

\- mangaflow\_notificaciones\_db

\- mangaflow\_reportes\_db

\- mangaflow\_auth\_db



\## Ejecución



Cada microservicio debe ejecutarse desde su propia carpeta:



```powershell

cd nombre-del-servicio

.\\mvnw.cmd spring-boot:run
```



\## Service Discovery (Eureka)



El proyecto incluye un servidor de registro Eureka (`eureka-server`, puerto 8761). Debe iniciarse antes que los microservicios para que estos se registren automáticamente.



```powershell

cd eureka-server

.\\mvnw.cmd spring-boot:run
```



Panel de Eureka: http://localhost:8761



\## Documentación de la API (Swagger)



Cada microservicio expone su documentación interactiva con springdoc-openapi:



\- Swagger UI: `http://localhost:{puerto}/swagger-ui.html`

\- OpenAPI JSON: `http://localhost:{puerto}/v3/api-docs`



\## Pruebas



Cada microservicio incluye pruebas unitarias con JUnit 5 y Mockito. Para ejecutarlas:



```powershell

cd nombre-del-servicio

.\\mvnw.cmd test
```

