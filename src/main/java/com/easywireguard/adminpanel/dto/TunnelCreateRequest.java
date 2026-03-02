package com.easywireguard.adminpanel.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public record TunnelCreateRequest(
        @NotBlank String name,
        @NotBlank String prefix,
        @NotNull @Future OffsetDateTime expiresAt
) {
}
