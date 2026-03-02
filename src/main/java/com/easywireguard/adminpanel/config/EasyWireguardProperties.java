package com.easywireguard.adminpanel.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Конфигурация подключения к удалённому easy-wireguard API.
 *
 * @param baseUrl базовый URL удалённого API
 * @param token токен авторизации для API
 */
@ConfigurationProperties(prefix = "easy-wireguard")
public record EasyWireguardProperties(String baseUrl, String token) {
}
