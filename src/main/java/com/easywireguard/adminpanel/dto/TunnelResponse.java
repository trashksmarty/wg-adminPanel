package com.easywireguard.adminpanel.dto;

import java.time.OffsetDateTime;

public record TunnelResponse(
        Long id,
        String name,
        String externalTunnelId,
        boolean active,
        OffsetDateTime expiresAt
) {
}
