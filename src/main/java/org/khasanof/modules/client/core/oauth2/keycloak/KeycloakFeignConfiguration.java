package org.khasanof.modules.client.core.oauth2.keycloak;

import feign.RequestInterceptor;
import org.khasanof.modules.client.core.config.ModulesClientCoreProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

/**
 * @author Nurislom
 * @see org.khasanof.modules.client.core.oauth2.keycloak
 * @since 5/7/2024 2:53 PM
 */
public class KeycloakFeignConfiguration {

    /**
     *
     * @param restTemplateBuilder
     * @param clientCoreProperties
     * @return
     */
    @Bean
    public RequestInterceptor requestInterceptor(RestTemplateBuilder restTemplateBuilder, ModulesClientCoreProperties clientCoreProperties) {
        return new KeycloakInterceptor(restTemplateBuilder, clientCoreProperties);
    }
}
