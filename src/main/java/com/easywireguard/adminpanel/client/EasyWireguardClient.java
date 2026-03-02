package com.easywireguard.adminpanel.client;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Тонкий клиент для удалённого easy-wireguard API.
 */
@Component
public class EasyWireguardClient {

    /** WebClient с уже настроенным baseUrl и токеном. */
    private final WebClient easyWireguardWebClient;

    public EasyWireguardClient(WebClient easyWireguardWebClient) {
        this.easyWireguardWebClient = easyWireguardWebClient;
    }

    /**
     * Создаёт туннель в удалённом API.
     *
     * @param tunnelName итоговое имя туннеля (с префиксом)
     * @return сгенерированный удалённой системой ID туннеля
     */
    public String createTunnel(String tunnelName) {
        CreateTunnelResponse response = easyWireguardWebClient.post()
                .uri("/api/tunnels")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateTunnelRequest(tunnelName))
                .retrieve()
                .bodyToMono(CreateTunnelResponse.class)
                .block();

        // Защита от некорректного ответа внешнего API.
        if (response == null || response.id() == null || response.id().isBlank()) {
            throw new IllegalStateException("Easy WireGuard API returned empty tunnel id");
        }

        return response.id();
    }

    /**
     * Включает или выключает существующий удалённый туннель.
     */
    public void setTunnelEnabled(String externalTunnelId, boolean enabled) {
        easyWireguardWebClient.patch()
                .uri("/api/tunnels/{id}", externalTunnelId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ToggleTunnelRequest(enabled))
                .retrieve()
                .toBodilessEntity()
                .block();
    }
    /** DTO body для PATCH enable/disable. */
    private record ToggleTunnelRequest(boolean enabled) {
    }

    /** DTO body для POST create tunnel. */
    private record CreateTunnelRequest(String name) {
    }

    /** DTO response от API создания туннеля. */
    private record CreateTunnelResponse(String id) {
    }
}
