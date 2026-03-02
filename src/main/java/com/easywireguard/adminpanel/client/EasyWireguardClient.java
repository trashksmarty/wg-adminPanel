package com.easywireguard.adminpanel.client;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class EasyWireguardClient {

    private final WebClient easyWireguardWebClient;

    public EasyWireguardClient(WebClient easyWireguardWebClient) {
        this.easyWireguardWebClient = easyWireguardWebClient;
    }

    public String createTunnel(String tunnelName) {
        CreateTunnelResponse response = easyWireguardWebClient.post()
                .uri("/api/tunnels")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateTunnelRequest(tunnelName))
                .retrieve()
                .bodyToMono(CreateTunnelResponse.class)
                .block();

        if (response == null || response.id() == null || response.id().isBlank()) {
            throw new IllegalStateException("Easy WireGuard API returned empty tunnel id");
        }

        return response.id();
    }

    public void setTunnelEnabled(String externalTunnelId, boolean enabled) {
        easyWireguardWebClient.patch()
                .uri("/api/tunnels/{id}", externalTunnelId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ToggleTunnelRequest(enabled))
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    private record ToggleTunnelRequest(boolean enabled) {
    }

    private record CreateTunnelRequest(String name) {
    }

    private record CreateTunnelResponse(String id) {
    }
}
