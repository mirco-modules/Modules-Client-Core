package org.khasanof.modules.client.core.oauth2.keycloak;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.khasanof.modules.client.core.oauth2.OAuthIdpTokenResponseDTO;
import org.khasanof.modules.client.core.config.ModulesClientCoreProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;

/**
 * @author Nurislom
 * @see org.khasanof.modules.client.core.keycloak
 * @since 5/7/2024 2:54 PM
 */
@SuppressWarnings("rawtypes")
public class KeycloakInterceptor implements RequestInterceptor {

    public static final String AUTHORIZATION = "Authorization";
    public static final String CONNECT_TOKEN = "/protocol/openid-connect/token";

    @Value("${spring.security.oauth2.client.provider.oidc.issuer-uri:default}")
    private String issuerUri;

    @Value("${spring.security.oauth2.client.registration.oidc.client-id:default}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.oidc.client-secret:default}")
    private String clientSecret;

    private final RestTemplate restTemplate;
    private final ModulesClientCoreProperties.KeycloakProperties keycloakProperties;

    public KeycloakInterceptor(RestTemplateBuilder restTemplateBuilder, ModulesClientCoreProperties keycloakProperties) {
        this.restTemplate = restTemplate(restTemplateBuilder);
        this.keycloakProperties = keycloakProperties.getKeycloak();
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Optional<String> accessToken = getAccessToken();
        accessToken.ifPresent(token -> requestTemplate.header(AUTHORIZATION, concatToken(token)));
    }

    private RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
            .additionalMessageConverters(new FormHttpMessageConverter(), new OAuth2AccessTokenResponseHttpMessageConverter())
            .errorHandler(new OAuth2ErrorResponseErrorHandler())
            .build();
    }

    private Optional<String> getAccessToken() {
        return Optional
            .of(restTemplate.exchange(getRequestEntity(), OAuthIdpTokenResponseDTO.class))
            .map(HttpEntity::getBody)
            .map(OAuthIdpTokenResponseDTO::getAccessToken);
    }

    private RequestEntity getRequestEntity() {
        return RequestEntity
            .post(URI.create(issuerUri + CONNECT_TOKEN))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(formParameters());
    }

    private MultiValueMap<String, String> formParameters() {
        MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
        formParameters.add(OAuth2ParameterNames.CLIENT_ID, clientId);
        formParameters.add(OAuth2ParameterNames.CLIENT_SECRET, clientSecret);
        formParameters.add(OAuth2ParameterNames.SCOPE, keycloakProperties.getScope());
        formParameters.add(OAuth2ParameterNames.USERNAME, keycloakProperties.getUsername());
        formParameters.add(OAuth2ParameterNames.PASSWORD, keycloakProperties.getPassword());
        formParameters.add(OAuth2ParameterNames.GRANT_TYPE, keycloakProperties.getGrantType());
        return formParameters;
    }

    private String concatToken(String token) {
        return "Bearer ".concat(token);
    }
}
