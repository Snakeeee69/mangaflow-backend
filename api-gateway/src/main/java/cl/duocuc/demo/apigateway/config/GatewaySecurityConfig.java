package cl.duocuc.demo.apigateway.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class GatewaySecurityConfig {

    @Value("${jwt.secret:MangaFlowSuperSecretKeyParaJWT2026DuocUCFullStack2!}")
    private String jwtSecret;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r
                        .path("/mangas/auth", "/mangas/auth/**")
                        .uri("lb://auth-service"))
                .route("personas-service", r -> r
                        .path("/mangas/personas", "/mangas/personas/**")
                        .uri("lb://personas-service"))
                .route("catalogo-service", r -> r
                        .path("/mangas/catalogo", "/mangas/catalogo/**")
                        .uri("lb://catalogo-service"))
                .route("inventario-service", r -> r
                        .path("/mangas/inventario", "/mangas/inventario/**")
                        .uri("lb://inventario-service"))
                .route("pedidos-service", r -> r
                        .path("/mangas/pedidos", "/mangas/pedidos/**")
                        .uri("lb://pedidos-service"))
                .route("pagos-service", r -> r
                        .path("/mangas/pagos", "/mangas/pagos/**")
                        .uri("lb://pagos-service"))
                .route("puntos-service", r -> r
                        .path("/mangas/puntos", "/mangas/puntos/**")
                        .uri("lb://puntos-service"))
                .route("descuentos-service", r -> r
                        .path("/mangas/descuentos", "/mangas/descuentos/**")
                        .uri("lb://descuentos-service"))
                .build();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://localhost:5174",
                "http://127.0.0.1:5173",
                "http://127.0.0.1:5174",
                "http://localhost:3000"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }

    @Bean
    @Order(0)
    public GlobalFilter jwtAuthenticationFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();
            HttpMethod method = request.getMethod();

            if (method == HttpMethod.OPTIONS || esRutaPublica(path, method)) {
                return chain.filter(exchange);
            }

            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return responderError(exchange, HttpStatus.UNAUTHORIZED, "Token JWT faltante o invalido");
            }

            String token = authHeader.substring(7);
            try {
                SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
                Claims claims = Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

                String rol = claims.get("rol", String.class);
                String correo = claims.getSubject();

                if (requiereRolAdminOStaff(path, method)
                        && !"ADMINISTRADOR".equals(rol)
                        && !"OPERADOR".equals(rol)) {
                    return responderError(exchange, HttpStatus.FORBIDDEN, "No tienes permisos para acceder a este recurso");
                }

                ServerHttpRequest mutatedRequest = request.mutate()
                        .header("X-User-Email", correo != null ? correo : "")
                        .header("X-User-Role", rol != null ? rol : "")
                        .build();

                return chain.filter(exchange.mutate().request(mutatedRequest).build());
            } catch (Exception e) {
                return responderError(exchange, HttpStatus.UNAUTHORIZED, "Token JWT expirado o manipulado");
            }
        };
    }

    private boolean esRutaPublica(String path, HttpMethod method) {
        if (path.equals("/mangas/auth/login")
                || path.equals("/mangas/auth/registrar")
                || path.equals("/mangas/auth/register")) {
            return true;
        }
        if (path.equals("/mangas/personas") && method == HttpMethod.POST) {
            return true;
        }
        if (path.matches("^/mangas/personas/\\d+/existe$") && method == HttpMethod.GET) {
            return true;
        }
        if (path.startsWith("/mangas/catalogo") && method == HttpMethod.GET) {
            return true;
        }
        if (path.startsWith("/mangas/inventario") && method == HttpMethod.GET) {
            return true;
        }
        return path.contains("/v3/api-docs") || path.contains("/swagger-ui");
    }

    private boolean requiereRolAdminOStaff(String path, HttpMethod method) {
        if (path.startsWith("/mangas/auth")
                && !path.equals("/mangas/auth/login")
                && !path.equals("/mangas/auth/registrar")
                && !path.equals("/mangas/auth/register")) {
            return true;
        }
        if (path.startsWith("/mangas/personas") && method == HttpMethod.DELETE) {
            return true;
        }
        return path.startsWith("/mangas/catalogo") && method != HttpMethod.GET;
    }

    private Mono<Void> responderError(ServerWebExchange exchange, HttpStatus status, String mensaje) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String json = String.format("{\"status\":%d,\"message\":\"%s\"}", status.value(), mensaje);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}

