package org.khasanof.modules.client.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Nurislom
 * @see org.khasanof.modules.client.core.config
 * @since 8/24/2024 6:38 PM
 */
@Data
@ConfigurationProperties(prefix = "modules.client.core")
public class ModulesClientCoreProperties {

    /**
     *
     */
    private final KeycloakProperties keycloak = new KeycloakProperties();

    /**
     *
     */
    @Data
    public static class KeycloakProperties {
        private String url;
        private String clientId;
        private String clientSecret;
        private String scope;
        private String username;
        private String password;
        private String grantType;
    }
}
