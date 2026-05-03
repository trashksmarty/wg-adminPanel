package com.easywireguard.adminpanel.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Конфигурация HTTP-клиента для вызовов easy-wireguard API.
 */
@Configuration
@EnableConfigurationProperties(EasyWireguardProperties.class)
public class WebClientConfig {

    /**
     * Создаёт бин WebClient с базовым URL и Bearer-токеном.
     */
    @Bean
    public WebClient easyWireguardWebClient(EasyWireguardProperties properties) {
        return WebClient.builder()
                .baseUrl(properties.baseUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + properties.token())
                .build();
    }
}
