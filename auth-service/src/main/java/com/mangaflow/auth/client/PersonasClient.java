package com.mangaflow.auth.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PersonasClient {

    private final RestTemplate restTemplate;
    private final String personasServiceUrl;

    public PersonasClient(
            RestTemplate restTemplate,
            @Value("${personas.service.url:http://localhost:8081}") String personasServiceUrl
    ) {
        this.restTemplate = restTemplate;
        this.personasServiceUrl = personasServiceUrl;
    }

    public boolean existePersona(Long personaId) {
        try {
            Boolean existe = restTemplate.getForObject(
                    personasServiceUrl + "/mangas/personas/" + personaId + "/existe",
                    Boolean.class
            );
            return Boolean.TRUE.equals(existe);
        } catch (Exception e) {
            return false;
        }
    }
}