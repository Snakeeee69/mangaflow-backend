# MangaFlow - Sistema de Microservicios

MangaFlow es un sistema de gestión y venta de mangas (físicos y digitales) desarrollado con arquitectura de microservicios utilizando Spring Boot.

El sistema permite gestionar personas, catálogo de mangas, inventario, pedidos, pagos, puntos de fidelización, descuentos y autenticación de usuarios.

* **Casa Matriz y Punto de Retiro:** Ahumada 85, Eurocentro, Santiago (Planta -1, Local 12).

## Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring Cloud (Netflix Eureka & API Gateway)
- Spring Web
- Spring Data JPA
- Spring Reactive Web / WebClient
- Spring Security
- MySQL / Laragon
- Docker & Docker Compose
- Postman
- GitHub

## Arquitectura

El proyecto está compuesto por **8 microservicios de negocio independientes** y **2 servicios de infraestructura** (`eureka-server` y `api-gateway`). Cada microservicio posee su propia responsabilidad, endpoints REST y base de datos independiente en MySQL.

## Microservicios

| Microservicio | Puerto | Responsabilidad |
|---|---:|---|
| eureka-server | 8761 | Servidor de descubrimiento y registro de microservicios |
| api-gateway | 8090 | Punto único de entrada y enrutamiento hacia los microservicios |
| personas-service | 8081 | Gestión de personas y roles de usuario (`ADMINISTRADOR`, `OPERADOR`, `CLIENTE`) |
| catalogo-service | 8083 | Gestión del catálogo de mangas |
| inventario-service | 8084 | Gestión de stock e inventario |
| pedidos-service | 8085 | Gestión de pedidos |
| pagos-service | 8086 | Registro y validación de pagos |
| puntos-service | 8087 | Acumulación y canje de puntos |
| descuentos-service | 8088 | Cálculo y aplicación de descuentos (`DIGITAL_A_FISICO`) |
| auth-service | 8091 | Registro, login, roles y autenticación |

## Bases de datos

Cada microservicio de negocio utiliza una base de datos propia en MySQL (Laragon):

- `mangaflow_personas_db`
- `mangaflow_catalogo_db`
- `mangaflow_inventario_db`
- `mangaflow_pedidos_db`
- `mangaflow_pagos_db`
- `mangaflow_puntos_db`
- `mangaflow_descuentos_db`
- `mangaflow_auth_db`

## Ejecución

### Opción 1: Ejecución completa con Docker Compose (recomendada)

Con MySQL (Laragon) activo en el puerto `3306` y las 8 bases de datos creadas, ejecuta desde la raíz del proyecto backend:

```powershell
docker compose up -d --build