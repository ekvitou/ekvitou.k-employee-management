package com.ekvitou.employee_management.keycloak;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class KeycloakConfigure {
    @Value("${keycloak.server-url}")
    private String keycloakSeverUrl;
    @Value("${keycloak.main-realm}")
    private String realm;
    @Value("${keycloak.client-id}")
    private String clientId;
    @Value("${keycloak.client-secret}")
    private String clientSecret;
    @Bean
    public Keycloak keycloakInstance(){
        // hard-code
        return KeycloakBuilder.builder()
                .serverUrl(keycloakSeverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
    }
}