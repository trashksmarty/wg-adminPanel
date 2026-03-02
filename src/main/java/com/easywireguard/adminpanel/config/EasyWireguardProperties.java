package com.easywireguard.adminpanel.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "easy-wireguard")
public record EasyWireguardProperties(String baseUrl, String token) {
}
