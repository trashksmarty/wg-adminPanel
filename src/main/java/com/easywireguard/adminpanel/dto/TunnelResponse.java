package com.easywireguard.adminpanel.dto;

import java.time.OffsetDateTime;

/**
 * DTO ответа по туннелю для фронтенда.
 */
public record TunnelResponse(
        Long id,
        String name,
        String externalTunnelId,
        boolean active,
        OffsetDateTime expiresAt
) {
}
